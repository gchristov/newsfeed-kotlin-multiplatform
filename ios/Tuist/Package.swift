// swift-tools-version: 6.0
import PackageDescription

#if TUIST
import struct ProjectDescription.PackageSettings

let packageSettings = PackageSettings(
    // Customize the product types for specific package product
    // Default is .staticFramework
    productTypes: [
        // The Google Firebase dependencies are mostly static frameworks. Linking them individually in a
        // multi-module iOS setup is practically impossible at the moment as it produces a number of linker
        // issues (like symbol duplicating or missing libraries) which don't have an obvious solution. We
        // therefore link them under a common umbrella static framework which is linked to upstream projects.
        //
        // When adding new dependencies try to have them as dynamic rather than static to avoid symbol
        // duplication issues.
        "FBLPromises": .framework,
        "Firebase": .framework,
        "FirebaseAppCheckInterop": .framework,
        "FirebaseCore": .framework,
        "FirebaseCoreExtension": .framework,
        "FirebaseCoreInternal": .framework,
        "FirebaseFirestore": .framework,
        "FirebaseFirestoreTarget": .framework,
        "FirebaseSessions": .framework,
        "FirebaseCrashlytics": .framework,
        "FirebaseCrashlyticsSwift": .framework,
        "FirebaseInstallations": .framework,
        "FirebaseRemoteConfigInterop": .framework,
        "FirebaseAnalytics": .framework,
        // Having the below as dynamic frameworks produces missing library linker errors and there
        // didn't seem to be an easy workaround, so they are left as static. Doesn't seem to cause
        // issues.
        "FirebaseAnalyticsTarget": .staticFramework,
        "FirebaseAnalyticsWrapper": .staticFramework,
        
        "GoogleUtilities-Logger": .framework,
        "GoogleUtilities-Environment": .framework,
        "GoogleUtilities-NSData": .framework,
        "GoogleUtilities-UserDefaults": .framework,
        // Having the below as dynamic frameworks produces missing library linker errors and there
        // didn't seem to be an easy workaround, so they are left as static. Doesn't seem to cause
        // issues.
        "GoogleUtilities-AppDelegateSwizzler": .staticFramework,
        "GoogleUtilities-MethodSwizzler": .framework,
        "GoogleUtilities-Network": .framework,
        "GoogleUtilities-Reachability": .framework,
        
        "GoogleAppMeasurement": .framework,
        "GoogleAppMeasurementTarget": .framework,
        "GoogleAppMeasurementIdentitySupport": .framework,
        "GoogleDataTransport": .framework,
        
        "Promises": .framework,
        "nanopb": .framework,
    ]
)
#endif

let package = Package(
    name: "Newsfeed",
    dependencies: [
        .package(url: "https://github.com/vpeschenkov/LetterAvatarKit", exact: "1.2.5"),
        .package(url: "https://github.com/SDWebImage/SDWebImageSwiftUI", exact: "3.0.0"),
        .package(url: "https://github.com/siteline/swiftui-introspect", exact: "0.1.3"),
        .package(url: "https://github.com/firebase/firebase-ios-sdk", exact: "11.9.0"),
    ]
)
