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
                    script: "${SRCROOT}/../Tuist/.build/checkouts/firebase-ios-sdk/Crashlytics/run",
                    name: "Upload Crashlytics dsym",
                    inputPaths: [
                        "${DWARF_DSYM_FOLDER_PATH}/${DWARF_DSYM_FILE_NAME}",
                        "${DWARF_DSYM_FOLDER_PATH}/${DWARF_DSYM_FILE_NAME}/Contents/Resources/DWARF/${PRODUCT_NAME}",
                        "${DWARF_DSYM_FOLDER_PATH}/${DWARF_DSYM_FILE_NAME}/Contents/Info.plist",
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
