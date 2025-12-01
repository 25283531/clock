# GitHub Actions Workflow Documentation

## Overview

This directory contains the CI/CD workflows for the Medicine Reminder Android application.

## Workflows

### android-build.yml

Automated build and release workflow for the Android app.

**Triggers:**
- Push to `master` branch: Builds the app and creates a GitHub release
- Pull requests to `master`: Builds the app for validation (no release)

**Build Outputs:**
- APK: `app/build/outputs/apk/release/app-release.apk`
- AAB: `app/build/outputs/bundle/release/app-release.aab`

**Key Features:**
1. ✅ Proper Gradle configuration written to `gradle.properties`
2. ✅ Robust version extraction using `awk`
3. ✅ Build artifact verification before release
4. ✅ Comprehensive error handling and logging
5. ✅ Gradle caching for faster builds
6. ✅ Detailed comments for maintainability

## Recent Improvements

### 1. Fixed Gradle Configuration (Requirements 1.2, 1.3)
- **Problem**: Configuration was only echoed to console, not written to file
- **Solution**: Use heredoc syntax to append configuration to `gradle.properties`
- **Benefit**: Gradle now uses the CI-specific optimizations

### 2. Improved Version Extraction (Requirements 2.1, 2.2, 2.3)
- **Problem**: Complex `grep` pipeline was fragile and error-prone
- **Solution**: Use `awk` for precise field extraction with validation
- **Benefit**: More reliable version extraction with clear error messages

### 3. Removed Unnecessary Steps (Requirements 1.4)
- **Problem**: `dos2unix` command was unnecessary and could fail
- **Solution**: Removed `dos2unix` calls from build steps
- **Benefit**: Simpler, more reliable workflow

### 4. Optimized Build Commands (Requirements 3.3)
- **Problem**: `--no-daemon` flag prevented Gradle daemon benefits
- **Solution**: Removed `--no-daemon` to enable daemon caching
- **Benefit**: Faster subsequent builds within the same job

### 5. Enhanced Error Handling (Requirements 2.3, 5.3)
- **Problem**: Failures provided minimal debugging information
- **Solution**: Added validation steps with detailed error messages
- **Benefit**: Easier troubleshooting when builds fail

### 6. Improved Documentation (All Requirements)
- **Problem**: Workflow steps lacked clear explanations
- **Solution**: Added comprehensive comments throughout the workflow
- **Benefit**: Easier maintenance and onboarding

## Testing

Property-based tests have been created to verify the correctness of key components:

### Test Scripts

1. **`.github/scripts/test-gradle-config.sh`**
   - Tests: Configuration file completeness (Property 2)
   - Validates: All required Gradle configuration keys are present
   - Run: `bash .github/scripts/test-gradle-config.sh`

2. **`.github/scripts/test-version-extraction.sh`**
   - Tests: Version extraction robustness (Property 1)
   - Validates: Version extraction works with various formats
   - Run: `bash .github/scripts/test-version-extraction.sh`

## Build Requirements

- **JDK**: 17 (Temurin distribution)
- **Android SDK**: Latest (installed via `android-actions/setup-android`)
- **Gradle**: 8.7.2 (via wrapper)
- **Kotlin**: 1.9.23
- **AGP**: 8.7.2

## Configuration

### Gradle Properties (CI-specific)

The workflow appends the following configuration to `gradle.properties`:

```properties
org.gradle.jvmargs=-Xmx4g -XX:+HeapDumpOnOutOfMemoryError -Dfile.encoding=UTF-8 -XX:+UseG1GC
org.gradle.parallel=true
org.gradle.caching=true
org.gradle.configureondemand=false
android.enableBuildCache=true
android.buildCacheDir=$HOME/.android/build-cache
```

### Caching Strategy

The workflow caches the following directories:
- `~/.gradle/caches`: Gradle dependencies and build cache
- `~/.gradle/wrapper`: Gradle wrapper distribution
- `~/.android/build-cache`: Android build cache

Cache key is based on:
- Operating system
- Hash of all Gradle configuration files (`*.gradle*`, `gradle-wrapper.properties`, `libs.versions.toml`)

## Known Limitations

1. **Unsigned Builds**: The workflow generates unsigned release builds. For production releases, signing configuration should be added.

2. **No Test Execution**: Currently, the workflow does not run unit tests or instrumentation tests. This should be added in the future.

3. **Single Platform**: Only builds for Android. No iOS or other platform support.

## Future Enhancements

1. **Add Signing**: Configure release signing using GitHub Secrets
2. **Run Tests**: Add steps to execute unit and instrumentation tests
3. **Code Quality**: Integrate Lint, code coverage, and static analysis
4. **Multi-variant**: Support different build flavors (debug, staging, production)
5. **Notifications**: Add Slack/email notifications for build status

## Troubleshooting

### Build Fails with "Permission Denied"
- The workflow sets execute permissions on `gradlew` in the Gradle Setup step
- If this persists, check that the repository has proper file permissions

### Version Extraction Fails
- Ensure `versionName` is defined in `app/build.gradle.kts`
- Check that the format is: `versionName = "x.y.z"`
- The workflow will show the relevant file content in error messages

### Build Artifacts Not Found
- The workflow verifies artifact existence before creating releases
- Check the build logs for Gradle errors
- Ensure the output paths match the expected locations

### Cache Issues
- Caches are automatically invalidated when configuration files change
- If needed, manually clear caches from the GitHub Actions UI
- The workflow will work without cache, just slower

## Support

For issues or questions about the workflow:
1. Check the workflow run logs in GitHub Actions
2. Review the error messages (they include helpful debugging info)
3. Consult the spec documents in `.kiro/specs/github-workflow-improvements/`
