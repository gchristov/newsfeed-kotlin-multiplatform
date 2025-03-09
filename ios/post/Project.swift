import ProjectDescription

let project = Project(
    name: "post-tuist",
    settings: .settings(configurations: [
        .debug(name: "Debug", xcconfig: "Post-Project.xcconfig"),
        .release(name: "Release", xcconfig: "Post-Project.xcconfig"),
    ]),
    targets: [
        .target(
            name: "Post",
            destinations: .iOS,
            product: .framework,
            bundleId: "com.gchristov.newsfeed.post",
            sources: ["post/**"],
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
                .external(name: "FirebaseAnalytics"),
                .external(name: "FirebaseFirestore"),
            ],
            settings: .settings(configurations: [
                .debug(name: "Debug", xcconfig: "Post-Target.xcconfig"),
                .debug(name: "Release", xcconfig: "Post-Target.xcconfig"),
            ])
        ),
    ]
)
