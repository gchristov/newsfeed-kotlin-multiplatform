import ProjectDescription

let baseSettings: SettingsDictionary = [
    "OTHER_LDFLAGS": "$(inherited) -ObjC", // Needed for Firebase
]

let project = Project(
    name: "CommonFirebase",
    targets: [
        .target(
            name: "CommonFirebase",
            destinations: .iOS,
            product: .staticFramework,
            bundleId: "com.gchristov.newsfeed.commonfirebase",
            deploymentTargets: .iOS("15.0"),
            dependencies: [
                .external(name: "FirebaseAnalytics"),
                .external(name: "FirebaseCrashlytics"),
                .external(name: "FirebaseFirestore"),
            ],
            settings: .settings(
                base: baseSettings
            )
        )
    ]
)
