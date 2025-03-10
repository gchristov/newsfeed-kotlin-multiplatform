import ProjectDescription

let project = Project(
    name: "Feed-Tuist",
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
            sources: ["Sources/**"],
            scripts: [
                TargetScript.pre(
                    script: """
# Shared KMM compilation needed here in addition to the main app because Xcode compiles 
# projects based on dependencies so this module might be compiled before anything else, 
# in which case we might get Xcode errors about missing KMM modules
echo "Building shared KMM module for target $TARGET_NAME"
cd "$SRCROOT/../.."
./gradlew :multiplatform:umbrella:embedAndSignAppleFrameworkForXcode
""",
                    name: "Build Kotlin multiplatform")
            ],
            dependencies: [
                .project(target: "CommonSwiftUi", path: "../common-swiftui"),
                .project(target: "Post", path: "../post"),
            ],
            settings: .settings(
                configurations: [
                    .debug(name: "Debug", xcconfig: "Configs/Target.xcconfig"),
                    .debug(name: "Release", xcconfig: "Configs/Target.xcconfig"),
                ])
        ),
        .target(
            name: "FeedUiTestHost",
            destinations: .iOS,
            product: .app,
            bundleId: "com.gchristov.newsfeed.feed.uitesthost",
            sources: ["tests/Host/**"],
            dependencies: [
                .project(target: "Feed", path: "../feed"),
                .project(target: "Post", path: "../post"),
            ],
            settings: .settings(configurations: [
                .debug(name: "Debug", xcconfig: "Configs/UiTestHost-Target.xcconfig"),
                .debug(name: "Release", xcconfig: "Configs/UiTestHost-Target.xcconfig"),
            ])
        ),
        .target(
            name: "FeedUiTests",
            destinations: .iOS,
            product: .uiTests,
            bundleId: "com.gchristov.newsfeed.feed.uitests",
            sources: ["tests/Sources/**"],
            dependencies: [
                .target(name: "FeedUiTestHost"),
            ]
        )
    ]
)
