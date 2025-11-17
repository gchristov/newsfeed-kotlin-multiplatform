import ProjectDescription

let baseSettings: SettingsDictionary = [
    "DEBUG_INFORMATION_FORMAT" : "dwarf-with-dsym"
]

// Umrella module for Kotlin Multiplatform to ensure the code is compiled only once
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
