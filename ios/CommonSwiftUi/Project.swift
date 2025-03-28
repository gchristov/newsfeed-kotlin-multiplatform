import ProjectDescription

let baseSettings: SettingsDictionary = [
    "OTHER_LDFLAGS": "$(inherited) -ObjC", // Needed for SDWebImageSwiftUI
    "DEBUG_INFORMATION_FORMAT" : "dwarf-with-dsym"
]

let project = Project(
    name: "CommonSwiftUi",
    settings: .settings(configurations: [
        .debug(name: "Debug", xcconfig: "Configs/Project.xcconfig"),
        .release(name: "Release", xcconfig: "Configs/Project.xcconfig"),
    ]),
    targets: [
        .target(
            name: "CommonSwiftUi",
            destinations: .iOS,
            product: .framework,
            bundleId: "com.gchristov.newsfeed.commonswiftui",
            deploymentTargets: .iOS("15.0"),
            sources: ["Sources/**"],
            dependencies: [
                .project(target: "CommonDesign", path: "../CommonDesign"),
                .external(name: "LetterAvatarKit"),
                .external(name: "SDWebImageSwiftUI"),
                .external(name: "Introspect"),
            ],
            settings: .settings(
                base: baseSettings,
                configurations: [
                    .debug(name: "Debug", xcconfig: "Configs/Target.xcconfig"),
                    .debug(name: "Release", xcconfig: "Configs/Target.xcconfig"),
                ]
            )
        ),
    ]
)
