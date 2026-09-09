#!/bin/sh
# Basic gradlew script for GitHub Actions
exec java -jar gradle/wrapper/gradle-wrapper.jar "$@"
