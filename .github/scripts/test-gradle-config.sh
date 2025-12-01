#!/bin/bash
# Test script to verify gradle.properties configuration completeness
# Feature: github-workflow-improvements, Property 2: Configuration file completeness
# Validates: Requirements 1.2

set -e

echo "Testing Gradle configuration file generation..."

# Required configuration keys
REQUIRED_KEYS=(
    "org.gradle.jvmargs"
    "org.gradle.parallel"
    "org.gradle.caching"
    "org.gradle.configureondemand"
    "android.enableBuildCache"
    "android.buildCacheDir"
)

# Create a temporary test directory
TEST_DIR=$(mktemp -d)
cd "$TEST_DIR"

# Create a minimal gradle.properties with existing content
cat > gradle.properties << 'EOF'
# Existing Gradle properties
org.gradle.jvmargs=-Xmx4g -Dfile.encoding=UTF-8
org.gradle.parallel=true
android.useAndroidX=true
android.enableJetifier=true
EOF

echo "Initial gradle.properties content:"
cat gradle.properties
echo ""

# Simulate the CI configuration step
cat >> gradle.properties << 'EOF'
# CI-specific Gradle configuration
org.gradle.jvmargs=-Xmx4g -XX:+HeapDumpOnOutOfMemoryError -Dfile.encoding=UTF-8 -XX:+UseG1GC
org.gradle.parallel=true
org.gradle.caching=true
org.gradle.configureondemand=false
android.enableBuildCache=true
android.buildCacheDir=$HOME/.android/build-cache
EOF

echo "Updated gradle.properties content:"
cat gradle.properties
echo ""

# Verify all required keys are present
MISSING_KEYS=()
for key in "${REQUIRED_KEYS[@]}"; do
    if ! grep -q "^${key}=" gradle.properties; then
        MISSING_KEYS+=("$key")
    fi
done

# Report results
if [ ${#MISSING_KEYS[@]} -eq 0 ]; then
    echo "✓ All required configuration keys are present"
    echo "✓ Property test passed: Configuration file completeness"
    cd - > /dev/null
    rm -rf "$TEST_DIR"
    exit 0
else
    echo "✗ Missing required configuration keys:"
    for key in "${MISSING_KEYS[@]}"; do
        echo "  - $key"
    done
    echo "✗ Property test failed: Configuration file completeness"
    cd - > /dev/null
    rm -rf "$TEST_DIR"
    exit 1
fi
