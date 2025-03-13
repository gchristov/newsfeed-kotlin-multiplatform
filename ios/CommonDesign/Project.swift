import ProjectDescription

let project = Project(
    name: "CommonDesign",
    settings: .settings(configurations: [
        .debug(name: "Debug", xcconfig: "Configs/Project.xcconfig"),
        .release(name: "Release", xcconfig: "Configs/Project.xcconfig"),
    ]),
    targets: [
        .target(
            name: "CommonDesign",
            destinations: .iOS,
            product: .framework,
            bundleId: "com.gchristov.newsfeed.commondesign",
            sources: ["Sources/**"],
            resources: ["Resources/**"],
            settings: .settings(configurations: [
                .debug(name: "Debug", xcconfig: "Configs/Target.xcconfig"),
                .debug(name: "Release", xcconfig: "Configs/Target.xcconfig"),
            ])
        ),
    ]
)
