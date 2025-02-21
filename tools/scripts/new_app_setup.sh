#!/bin/bash

TARGET_COMPANY_NAME="gchristov"
TARGET_APP_PACKAGE="newsfeed"
TARGET_APP_CLASS_NAME="Newsfeed"
NEEDS_CLEANUP=0

# Needed for byte sequence error in ascii to utf conversion on OSX
export LC_CTYPE=C;
export LANG=C;

# Read required properties
read -p "Enter company name (spaces will be trimmed): " cn
read -p "Enter app name (spaces will be trimmed): " an
if [ -z "$cn" ] || [ -z "$an" ]; then
  echo "Invalid input. Please enter app and company name."
  exit 1
fi
companyName=$(echo $cn | tr -d '[:space:]' | tr '[:upper:]' '[:lower:]') # Trim and convert to lowercase
appPackageName=$(echo $an | tr -d '[:space:]' | tr '[:upper:]' '[:lower:]') # Trim and convert to lowercase
appClassName=$(echo $an | tr -d '[:space:]') # Trim

# Check if 'brew' tool is installed
if ! command -v brew &> /dev/null
then
    echo "Homebrew not setup. Please follow instructions here to set it up"
    echo "https://brew.sh"
    exit 1
fi

# Check if 'rename' tool is installed
if ! command -v rename &> /dev/null
then
    echo "Setting up environment. This may take a while..."
    brew update -q
    brew install rename -q
    NEEDS_CLEANUP=1
    echo "Setting up environment. Done"
fi

# Define replace function
function replace() {
    # sed -i "" is needed by the osx version of sed (instead of sed -i)
    find . -type f -exec sed -i "" "s|${1}|${2}|g" {} +
    find . * | rename -f "s|${1}|${2}|" &> /dev/null
}

# Replace relevant items in the template
echo "Configuring company..."
replace $TARGET_COMPANY_NAME $companyName
echo "Configuring company. Done"

echo "Configuring app package..."
replace $TARGET_APP_PACKAGE $appPackageName
echo "Configuring app package. Done"

echo "Configuring app name..."
replace $TARGET_APP_CLASS_NAME $appClassName
echo "Configuring app name. Done"

# Cleanup in case the user didn't have the 'rename' tool installed previously
if NEEDS_CLEANUP==1
then
    echo "Cleaning up..."
    brew uninstall rename -q
    echo "Cleaning up. Done"
fi

exit 0
