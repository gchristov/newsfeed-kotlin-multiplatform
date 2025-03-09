import ProjectDescription

let project = Project(
    name: "common-swiftui-tuist",
    settings: .settings(configurations: [
        .debug(name: "Debug", xcconfig: "Common-SwiftUI-Project.xcconfig"),
        .release(name: "Release", xcconfig: "Common-SwiftUI-Project.xcconfig"),
    ]),
    targets: [
        .target(
            name: "CommonSwiftUi",
            destinations: .iOS,
            product: .framework,
            bundleId: "com.gchristov.newsfeed.commonswiftui",
            sources: ["common-swiftui/**"],
            dependencies: [
                .project(target: "CommonDesign", path: "../common-design"),
                .external(name: "LetterAvatarKit"),
                .external(name: "SDWebImageSwiftUI"),
                .external(name: "Introspect"),
            ],
            settings: .settings(
                base: ["OTHER_LDFLAGS": "$(inherited) -ObjC"], // Needed for SDWebImageSwiftUI
                configurations: [
                    .debug(name: "Debug", xcconfig: "Common-SwiftUI-Target.xcconfig"),
                    .debug(name: "Release", xcconfig: "Common-SwiftUI-Target.xcconfig"),
                ])
        ),
    ]
)
