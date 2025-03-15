import ProjectDescription

let baseSettings: SettingsDictionary = [
    "DEBUG_INFORMATION_FORMAT" : "dwarf-with-dsym"
]

let project = Project(
    name: "CommonKotlinMultiplatform",
    targets: [
        .target(
            name: "CommonKotlinMultiplatform",
            destinations: .iOS,
            product: .framework,
            bundleId: "com.gchristov.newsfeed.commonkotlinmultiplatform",
            deploymentTargets: .iOS("15.0"),
            scripts: [
                TargetScript.pre(
                    script: """
cd "$SRCROOT/../.."
./gradlew :multiplatform:umbrella:embedAndSignAppleFrameworkForXcode
""",
                    name: "Build Kotlin multiplatform",
                    basedOnDependencyAnalysis: false
                )
            ],
            settings: .settings(
                base: baseSettings
            )
        )
    ]
)
