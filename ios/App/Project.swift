import ProjectDescription

let baseSettings: SettingsDictionary = [
    "DEBUG_INFORMATION_FORMAT" : "dwarf-with-dsym"
]

let project = Project(
    name: "App",
    settings: .settings(configurations: [
        .debug(name: "Debug", xcconfig: "Configs/Project.xcconfig"),
        .release(name: "Release", xcconfig: "Configs/Project.xcconfig"),
    ]),
    targets: [
        .target(
            name: "App",
            destinations: .iOS,
            product: .app,
            bundleId: "com.gchristov.newsfeed",
            deploymentTargets: .iOS("15.0"),
            infoPlist: InfoPlist.extendingDefault(with: [
                "CFBundleDisplayName": "Newsfeed",
                "UIMainStoryboardFile": "",
                "UILaunchStoryboardName": "LaunchScreen",
                "NSApplicationCrashOnExceptions": "Yes"
            ]),
            sources: ["Sources/**"],
            resources: ["Resources/**"],
            scripts: [
                TargetScript.post(
                    script: """
DWARF_DSYM_FILES=$(find "$DWARF_DSYM_FOLDER_PATH" -type d -name "*.framework.dSYM" -o -name "*.app.dSYM")

if [ -n "$DWARF_DSYM_FILES" ]; then
    for dsym_file in $DWARF_DSYM_FILES; do
        echo "Uploading dSYM: $dsym_file"

        # Construct the Crashlytics/run command
        "${SRCROOT}/../Tuist/.build/checkouts/firebase-ios-sdk/Crashlytics/run" \
            -dsp "$dsym_file"

        if [ $? -ne 0 ]; then
            echo "Error uploading dSYM: $dsym_file"
        fi
    done
else
    echo "No dSYM files found."
fi
""",
                    name: "Upload dSYMs to Crashlytics",
                    inputPaths: [
                        "$(DWARF_DSYM_FOLDER_PATH)",
                        "$(TARGET_BUILD_DIR)/$(UNLOCALIZED_RESOURCES_FOLDER_PATH)/GoogleService-Info.plist",
                        "$(TARGET_BUILD_DIR)/$(EXECUTABLE_PATH)"
                    ]
                )
            ],
            dependencies: [
                .project(target: "CommonFirebase", path: "../CommonFirebase"),
                .project(target: "CommonKotlinMultiplatform", path: "../CommonKotlinMultiplatform"),
                .project(target: "Feed", path: "../Feed"),
                .project(target: "Post", path: "../Post"),
            ],
            settings: .settings(
                base: baseSettings,
                configurations: [
                    .debug(name: "Debug", xcconfig: "Configs/Target.xcconfig"),
                    .debug(name: "Release", xcconfig: "Configs/Target.xcconfig"),
                ]
            )
        ),
    ]
)
