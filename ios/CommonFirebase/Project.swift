import ProjectDescription

let baseSettings: SettingsDictionary = [
    "DEBUG_INFORMATION_FORMAT" : "dwarf-with-dsym"
]

// The Google Firebase dependencies are mostly static frameworks. Linking them individually in a
// multi-module iOS setup is practically impossible at the moment as it produces a number of linker
// issues (like symbol duplicating or missing libraries) which don't have an obvious solution.
//
// This module is effectively an umbrella for all the Firebase dependencies that upstream projects
// need to workaround the linking issues. Any new future dependency should be linked here and the
// relevant iOS module should link CommonFirebase as a dependency.
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
