import ProjectDescription

let project = Project(
    name: "common-design-tuist",
    settings: .settings(configurations: [
        .debug(name: "Debug", xcconfig: "Common-Design-Project.xcconfig"),
        .release(name: "Release", xcconfig: "Common-Design-Project.xcconfig"),
    ]),
    targets: [
        .target(
            name: "CommonDesign",
            destinations: .iOS,
            product: .framework,
            bundleId: "com.gchristov.newsfeed.commondesign",
            sources: ["common-design/**"],
            resources: ["resources/**"],
            settings: .settings(configurations: [
                .debug(name: "Debug", xcconfig: "Common-Design-Target.xcconfig"),
                .debug(name: "Release", xcconfig: "Common-Design-Target.xcconfig"),
            ])
        ),
    ]
)
