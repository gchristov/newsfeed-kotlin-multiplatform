import ProjectDescription

let baseSettings: SettingsDictionary = [
    "OTHER_LDFLAGS": "$(inherited) -lsqlite3", // Needed for Firebase
    "DEBUG_INFORMATION_FORMAT" : "dwarf-with-dsym"
]

let project = Project(
    name: "Post",
    settings: .settings(configurations: [
        .debug(name: "Debug", xcconfig: "Configs/Project.xcconfig"),
        .release(name: "Release", xcconfig: "Configs/Project.xcconfig"),
    ]),
    targets: [
        .target(
            name: "Post",
            destinations: .iOS,
            product: .framework,
            bundleId: "com.gchristov.newsfeed.post",
            deploymentTargets: .iOS("15.0"),
            sources: ["Sources/**"],
            dependencies: [
                .project(target: "CommonSwiftUi", path: "../CommonSwiftUi"),
                .project(target: "CommonKotlinMultiplatform", path: "../CommonKotlinMultiplatform"),
                .project(target: "CommonFirebase", path: "../CommonFirebase"),
            ],
            settings: .settings(
                base: baseSettings,
                configurations: [
                    .debug(name: "Debug", xcconfig: "Configs/Target.xcconfig"),
                    .debug(name: "Release", xcconfig: "Configs/Target.xcconfig"),
                ]
            )
        ),
        .target(
            name: "PostUiTestHost",
            destinations: .iOS,
            product: .app,
            bundleId: "com.gchristov.newsfeed.post.uitesthost",
            deploymentTargets: .iOS("15.0"),
            sources: ["Tests/Host/**"],
            dependencies: [
                .project(target: "Post", path: "../Post"),
            ],
            settings: .settings(
                configurations: [
                    .debug(name: "Debug", xcconfig: "Configs/UiTestHost-Target.xcconfig"),
                    .debug(name: "Release", xcconfig: "Configs/UiTestHost-Target.xcconfig"),
                ]
            )
        ),
        .target(
            name: "PostUiTests",
            destinations: .iOS,
            product: .uiTests,
            bundleId: "com.gchristov.newsfeed.post.uitests",
            deploymentTargets: .iOS("15.0"),
            sources: ["Tests/Sources/**"],
            dependencies: [
                .target(name: "PostUiTestHost"),
            ]
        )
    ]
)
