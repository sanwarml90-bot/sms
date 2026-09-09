package com.messagelock.security

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.media.AudioAttributes
import android.media.AudioFormat
import android.media.AudioTrack
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.messagelock.R
import com.messagelock.SharedPrefsHelper
import kotlin.concurrent.thread
import kotlin.math.PI
import kotlin.math.sin

/** Foreground service that synthesizes a continuous alarm siren without a binary asset. */
class SirenService : Service() {
    private var siren: SynthesizedSiren? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            ACTION_START -> startSiren()
            ACTION_STOP -> stopSiren()
        }
        return START_NOT_STICKY
    }

    private fun startSiren() {
        createChannel()
        startForeground(NOTIFICATION_ID, NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_lock_silent_mode_off)
            .setContentTitle(getString(R.string.siren_notification_title))
            .setContentText(getString(R.string.siren_notification_text))
            .setOngoing(true)
            .build())
        if (siren == null) siren = SynthesizedSiren().also { it.start() }
        SharedPrefsHelper(this).setSirenActive(true)
    }

    private fun stopSiren() {
        siren?.stop()
        siren = null
        SharedPrefsHelper(this).setSirenActive(false)
        stopForeground(STOP_FOREGROUND_REMOVE)
        stopSelf()
    }

    override fun onDestroy() {
        siren?.stop()
        siren = null
        SharedPrefsHelper(this).setSirenActive(false)
        super.onDestroy()
    }

    override fun onBind(intent: Intent?): IBinder? = null

    private fun createChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(CHANNEL_ID, getString(R.string.siren_channel_name), NotificationManager.IMPORTANCE_LOW)
            getSystemService(NotificationManager::class.java).createNotificationChannel(channel)
        }
    }

    companion object {
        private const val CHANNEL_ID = "active_siren"
        private const val NOTIFICATION_ID = 1001
        private const val ACTION_START = "com.messagelock.action.START_SIREN"
        private const val ACTION_STOP = "com.messagelock.action.STOP_SIREN"

        fun start(context: Context) = launch(context, ACTION_START)
        fun stop(context: Context) = launch(context, ACTION_STOP)

        private fun launch(context: Context, action: String) {
            val intent = Intent(context, SirenService::class.java).setAction(action)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) context.startForegroundService(intent) else context.startService(intent)
        }
    }
}

/** Generates a smooth, looping two-tone alarm in memory, so source control stays text-only. */
private class SynthesizedSiren {
    @Volatile private var running = false
    private var audioTrack: AudioTrack? = null

    fun start() {
        if (running) return
        running = true
        thread(name = "message-lock-siren", isDaemon = true) {
            val sampleRate = 44_100
            val framesPerBuffer = 2_048
            val bufferSize = maxOf(
                AudioTrack.getMinBufferSize(sampleRate, AudioFormat.CHANNEL_OUT_MONO, AudioFormat.ENCODING_PCM_16BIT),
                framesPerBuffer * Short.SIZE_BYTES
            )
            val track = AudioTrack.Builder()
                .setAudioAttributes(AudioAttributes.Builder().setUsage(AudioAttributes.USAGE_ALARM).build())
                .setAudioFormat(AudioFormat.Builder().setSampleRate(sampleRate).setChannelMask(AudioFormat.CHANNEL_OUT_MONO).setEncoding(AudioFormat.ENCODING_PCM_16BIT).build())
                .setBufferSizeInBytes(bufferSize)
                .setTransferMode(AudioTrack.MODE_STREAM)
                .build()
            audioTrack = track
            val samples = ShortArray(framesPerBuffer)
            var sample = 0L
            track.play()
            while (running) {
                for (index in samples.indices) {
                    val time = sample++ / sampleRate.toDouble()
                    val frequency = 650.0 + 550.0 * (0.5 - 0.5 * kotlin.math.cos(2.0 * PI * time / 2.0))
                    samples[index] = (Short.MAX_VALUE * 0.92 * sin(2.0 * PI * frequency * time)).toInt().toShort()
                }
                track.write(samples, 0, samples.size)
            }
            track.stop()
            track.flush()
            track.release()
            audioTrack = null
        }
    }

    fun stop() {
        running = false
    }
}
