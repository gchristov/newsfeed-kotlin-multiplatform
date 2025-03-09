import ProjectDescription

let project = Project(
    name: "feed-tuist",
    settings: .settings(configurations: [
        .debug(name: "Debug", xcconfig: "Feed-Project.xcconfig"),
        .release(name: "Release", xcconfig: "Feed-Project.xcconfig"),
    ]),
    targets: [
        .target(
            name: "Feed",
            destinations: .iOS,
            product: .framework,
            bundleId: "com.gchristov.newsfeed.feed",
            sources: ["feed/**"],
            scripts: [
                TargetScript.pre(script: """
# Shared KMM compilation needed here in addition to the main app because Xcode compiles 
# projects based on dependencies so this module might be compiled before anything else, 
# in which case we might get Xcode errors about missing KMM modules
echo "Building shared KMM module for target $TARGET_NAME"
cd "$SRCROOT/../.."
./gradlew :multiplatform:umbrella:embedAndSignAppleFrameworkForXcode
""", name: "Build multiplatform")
            ],
            dependencies: [
                .project(target: "CommonSwiftUi", path: "../common-swiftui"),
                .project(target: "Post", path: "../post"),
            ],
            settings: .settings(
                configurations: [
                    .debug(name: "Debug", xcconfig: "Feed-Target.xcconfig"),
                    .debug(name: "Release", xcconfig: "Feed-Target.xcconfig"),
                ])
        ),
    ]
)
