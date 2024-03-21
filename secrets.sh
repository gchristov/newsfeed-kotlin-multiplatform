#!/bin/bash
set -e
# Exports required CI environment secrets to local secrets so that the project can use them

# network credentials (the > is intentional to support concurrent writes)
echo GUARDIAN_API_KEY="$GUARDIAN_API_KEY" > ./multiplatform/common/network/secrets.properties
echo GUARDIAN_API_URL="$GUARDIAN_API_URL" >> ./multiplatform/common/network/secrets.properties
