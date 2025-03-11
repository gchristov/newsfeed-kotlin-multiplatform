import ProjectDescription

let project = Project(
    name: "App-Tuist",
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
            infoPlist: InfoPlist.extendingDefault(with: [
                "CFBundleDisplayName": "Newsfeed",
                "UIMainStoryboardFile": "",
                "UILaunchStoryboardName": "LaunchScreen"
            ]),
            sources: ["Sources/**"],
            resources: ["Resources/**"],
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
                .project(target: "Feed", path: "../feed"),
                .project(target: "Post", path: "../post"),
            ],
            settings: .settings(configurations: [
                .debug(name: "Debug", xcconfig: "Configs/Target.xcconfig"),
                .debug(name: "Release", xcconfig: "Configs/Target.xcconfig"),
            ])
        ),
    ]
)
