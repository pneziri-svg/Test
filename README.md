# Kastrati TV

Android TV project countdown, Android 6.0+ (API 23), version 1.1.
Uses the original supplied project photograph and Kastrati logo.

## Install and configure

Install Kastrati-TV.apk (release mode, signed with a test certificate), or
Kastrati-TV-debug.apk. Both have the same application ID; install only one.
Open Kastrati TV from the TV launcher. Press OK/Enter or Menu to configure the
project name (up to 48 characters), date and time using the TV's timezone.
Confirm both date and time to save. Back/Cancel leaves existing settings intact.
The initial 100-day example deadline is saved once; replace it with your real date.

The absolute deadline and project name are synchronously persisted in private
SharedPreferences. Each screen refresh uses the current wall clock, so time
spent powered off is deducted automatically. Keep the TV's automatic date/time
enabled. Uninstalling or clearing app data removes settings.

## TV behavior

Fullscreen landscape, keep-screen-on, remote control support and TV launcher
banner. The 1920x1080 composition scales to 720p, 1080p and 4K, with 5% safe
insets and high contrast countdown text. The photograph fills the screen with
center cropping. Its original 1536x766 resolution and the supplied small logo
limit image sharpness on large TVs; screen size in inches does not alter layout.

Select Kastrati TV in Android TV Settings > Screensaver to use the DreamService.
The screensaver shares the saved deadline. The app requests launch after boot
and package update; Android/vendor restrictions can prevent background launches.
Open the app once after installation. Auto-start is best effort, not guaranteed.

## Build and signing

JDK 17, Gradle 8.13, Android SDK 35:

```
gradle testDebugUnitTest lintDebug assembleDebug assembleRelease
```

GitHub Actions builds both APKs and uploads Kastrati-TV-APK, including SHA-256
checksums and signature verification. Validation reports are uploaded separately.
Release mode is non-debuggable but uses the Android debug/test signing identity.
This is a sideload build, not a production/store signing setup. Fresh CI builds
can use a different test key; an update may then require uninstalling (which
deletes settings). Keep a private stable signing key for future production updates.
No private signing key is committed.

## Validation

Unit tests cover downtime, deadline expiration, subsecond rounding and clock
correction. CI also runs Android lint and verifies both APK signatures.
Physical TV boot behavior and remote date/time controls must be checked on the
target TV; successful compilation alone does not verify those device behaviors.
