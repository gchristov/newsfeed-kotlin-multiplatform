import ProjectDescription

let project = Project(
    name: "app-tuist",
    settings: .settings(configurations: [
        .debug(name: "Debug", xcconfig: "App-Project.xcconfig"),
        .release(name: "Release", xcconfig: "App-Project.xcconfig"),
    ]),
    targets: [
        .target(
            name: "App",
            destinations: .iOS,
            product: .app,
            bundleId: "com.gchristov.newsfeed",
            infoPlist: InfoPlist.extendingDefault(with: [
                "UIMainStoryboardFile": "",
                "UILaunchStoryboardName": "LaunchScreen"
            ]),
            sources: ["app/**"],
            resources: ["resources/**"],
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
                .project(target: "Feed", path: "../feed"),
                .project(target: "Post", path: "../post"),
            ],
            settings: .settings(configurations: [
                .debug(name: "Debug", xcconfig: "App-Target.xcconfig"),
                .debug(name: "Release", xcconfig: "App-Target.xcconfig"),
            ])
        ),
    ]
)
