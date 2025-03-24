import ProjectDescription

let baseSettings: SettingsDictionary = [
    "DEBUG_INFORMATION_FORMAT" : "dwarf-with-dsym"
]

let project = Project(
    name: "Feed",
    settings: .settings(configurations: [
        .debug(name: "Debug", xcconfig: "Configs/Project.xcconfig"),
        .release(name: "Release", xcconfig: "Configs/Project.xcconfig"),
    ]),
    targets: [
        .target(
            name: "Feed",
            destinations: .iOS,
            product: .framework,
            bundleId: "com.gchristov.newsfeed.feed",
            deploymentTargets: .iOS("15.0"),
            sources: ["Sources/**"],
            dependencies: [
                .project(target: "CommonSwiftUi", path: "../CommonSwiftUi"),
                .project(target: "CommonKotlinMultiplatform", path: "../CommonKotlinMultiplatform"),
                .project(target: "CommonFirebase", path: "../CommonFirebase"),
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
        .target(
            name: "FeedUiTestHost",
            destinations: .iOS,
            product: .app,
            bundleId: "com.gchristov.newsfeed.feed.uitesthost",
            deploymentTargets: .iOS("15.0"),
            sources: ["Tests/Host/**"],
            dependencies: [
                .project(target: "Feed", path: "../Feed"),
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
            name: "FeedUiTests",
            destinations: .iOS,
            product: .uiTests,
            bundleId: "com.gchristov.newsfeed.feed.uitests",
            deploymentTargets: .iOS("15.0"),
            sources: ["Tests/Sources/**"],
            dependencies: [
                .target(name: "FeedUiTestHost"),
            ]
        )
    ]
)
