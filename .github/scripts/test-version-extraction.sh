#!/bin/bash
# Test script to verify version extraction robustness
# Feature: github-workflow-improvements, Property 1: Version extraction robustness
# Validates: Requirements 2.1

set -e

echo "Testing version extraction robustness..."

# Test cases with different build.gradle.kts formats
TEST_CASES=(
    # Standard format
    'versionName = "1.0"'
    # With spaces
    'versionName  =  "2.0.1"'
    # With extra whitespace
    '    versionName = "3.5.2"    '
    # Multiple lines context
    'minSdk = 26
        versionName = "1.2.3"
        targetSdk = 34'
    # With comments after
    'versionName = "4.0" // Version comment'
)

EXPECTED_VERSIONS=(
    "1.0"
    "2.0.1"
    "3.5.2"
    "1.2.3"
    "4.0"
)

# Create a temporary test directory
TEST_DIR=$(mktemp -d)
cd "$TEST_DIR"

# Test each case
PASSED=0
FAILED=0

for i in "${!TEST_CASES[@]}"; do
    TEST_CASE="${TEST_CASES[$i]}"
    EXPECTED="${EXPECTED_VERSIONS[$i]}"
    
    # Create test build.gradle.kts
    cat > build.gradle.kts << EOF
plugins {
    id("com.android.application")
}

android {
    namespace = "com.test"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.test"
        minSdk = 26
        ${TEST_CASE}
        targetSdk = 34
    }
}
EOF

    # Extract version using the same logic as the workflow
    VERSION_NAME=$(awk -F'"' '/versionName/ {print $2; exit}' build.gradle.kts)
    
    # Verify extraction
    if [ "$VERSION_NAME" = "$EXPECTED" ]; then
        echo "✓ Test case $((i+1)): Extracted '$VERSION_NAME' (expected '$EXPECTED')"
        PASSED=$((PASSED + 1))
    else
        echo "✗ Test case $((i+1)): Extracted '$VERSION_NAME' but expected '$EXPECTED'"
        FAILED=$((FAILED + 1))
    fi
done

# Test error case: missing versionName
echo ""
echo "Testing error handling for missing versionName..."
cat > build.gradle.kts << 'EOF'
plugins {
    id("com.android.application")
}

android {
    namespace = "com.test"
    compileSdk = 34
}
EOF

VERSION_NAME=$(awk -F'"' '/versionName/ {print $2; exit}' build.gradle.kts)

if [ -z "$VERSION_NAME" ]; then
    echo "✓ Correctly handled missing versionName (empty result)"
    PASSED=$((PASSED + 1))
else
    echo "✗ Should return empty for missing versionName, got: '$VERSION_NAME'"
    FAILED=$((FAILED + 1))
fi

# Report results
echo ""
echo "========================================="
echo "Test Results:"
echo "  Passed: $PASSED"
echo "  Failed: $FAILED"
echo "========================================="

# Cleanup
cd - > /dev/null
rm -rf "$TEST_DIR"

if [ $FAILED -eq 0 ]; then
    echo "✓ Property test passed: Version extraction robustness"
    exit 0
else
    echo "✗ Property test failed: Version extraction robustness"
    exit 1
fi
