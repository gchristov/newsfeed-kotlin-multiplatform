// swift-tools-version: 6.0
import PackageDescription

#if TUIST
import struct ProjectDescription.PackageSettings

let packageSettings = PackageSettings(
    // Customize the product types for specific package product
    // Default is .staticFramework
    productTypes: [
        "Firebase": .framework,
        "FirebaseCore": .framework,
        "FirebaseCoreExtension": .framework,
        "FirebaseCoreInternal": .framework,
        "FirebaseFirestore": .framework,
        "FirebaseFirestoreTarget": .framework,
        // Only included in the main App target, so can remain static. Making dynamic produces linker errors.
        "FirebaseCrashlytics": .staticFramework,
        "GoogleUtilities-Logger": .framework,
        "GoogleUtilities-Environment": .framework,
        "GoogleUtilities-NSData": .framework,
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
