#!/bin/bash
set -e
# Exports required CI environment secrets to local secrets so that the project can use them.
# Should be invoked from the root of the project as all paths are relative.

# network credentials (the > is intentional to append to a file)
echo GUARDIAN_API_KEY="$GUARDIAN_API_KEY" >> multiplatform/common/network/secrets.properties
echo GUARDIAN_API_URL="$GUARDIAN_API_URL" >> multiplatform/common/network/secrets.properties

# Firebase credentials
echo "$GOOGLE_SERVICES_JSON" > android/app/google-services.json
echo "$GOOGLE_SERVICE_INFO_PLIST" > ios/app/GoogleService-Info.plist
echo "$GOOGLE_SERVICE_INFO_PLIST" > ios/app/Resources/GoogleService-Info.plist
