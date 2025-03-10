import ProjectDescription

let project = Project(
    name: "Post-Tuist",
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
                .external(name: "FirebaseAnalytics"),
                .external(name: "FirebaseFirestore"),
            ],
            settings: .settings(configurations: [
                .debug(name: "Debug", xcconfig: "Configs/Target.xcconfig"),
                .debug(name: "Release", xcconfig: "Configs/Target.xcconfig"),
            ])
        ),
    ]
)
