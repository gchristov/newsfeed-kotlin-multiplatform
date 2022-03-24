# Newsfeed
| 🍎 iOS | 🤖 Android |
| --- | --- |
| ![iOS Demo](https://user-images.githubusercontent.com/7644787/159919860-e205484f-c991-4584-af51-2e4ae4f97ff5.mp4) | ![Android Demo](https://user-images.githubusercontent.com/7644787/159919897-6770f6ab-2a4b-4f8b-9675-21df6f346472.mp4) |

## 👀 Overview
Newsfeed app with endless scrolling built for Kotlin Multiplatform (iOS + Android).

### Tech Stack
* **Shared code**: Kotlin Multiplatform, MVVM, Kodein (dependency injection), Ktor (network), SqlDelight (database)
* **Android**: Jetpack Compose, Android Architecture Components (LiveData, ViewModel)
* **iOS**: Swift, SwiftUI

### Setup
The project setup is quite straightforward:

1. Clone or download the repo.
2. Install the latest [Android Studio](https://developer.android.com/studio) - at least version **2020.3.1** is required.
3. Install the latest [Kotlin Multiplatform plugin](https://kotlinlang.org/docs/kmm-plugin-releases.html) - at least version **0.3.0** is required.
4. Install the latest [Xcode](https://developer.apple.com/xcode/) - at least version **13.0.0** is required.
5. Android:
   * Open Android Studio -> `Preferences` -> `Build, Execution, Deployment` -> `Build Tools` -> `Gradle` and set `Gradle JDK` to be `Embedded JDK`.
   * Import the project.
   * Build and run directly from Android Studio using the `appAndroid` configuration.
6. iOS: 
   * Open `appIos.xcworkspace` with Xcode
   * Build and run directly from Xcode using the `appIos` scheme.
7. Shared KMM:
   * The shared multiplatform code cannot be built or run by itself, so no further setup is required.

#### Project Template Setup
The project could also be used as a template for other multiplatform apps, providing a solid foundation to build on:

1. Clone or download the repo
2. Open Terminal and navigate to the repo
3. Run `chmod +x new_app_setup.sh`
4. Run `./new_app_setup.sh` and follow the instructions
5. Once the script finishes you should follow the instructions above to setup the project with Android Studio

#### API Key
Note that you might need to replace the API key for this project if [this link](https://content.guardianapis.com/search?api-key=09658731-cb6d-4a84-9e3c-5f030389de4e) doesn't work. To get a new key, go to [this link](https://bonobo.capi.gutools.co.uk/register/developer) and register a developer account. Once you get the key, replace the `value` for `API_KEY` in `kmm/kmm-common-network/build.gradle.kts`.

## 🏬 Architecture
The project is built using CLEAN multi-module architecture consisting of `feature`, `data` and `test-fixture` modules both for the shared KMM code and the individual client targets. Each module contains classes and resources only related to its functionality and some modules can be reused within other modules. Definitions of the module types can be found under each platform below.

To allow easier differentiation between modules, there are separate root folders for each of the supported platforms:
* `kmm` - shared code and related modules
* `appAndroid` - Android app and related modules
* `appIos` - iOS app and related modules

## 🏗 Build Process
KMM is using Gradle as a build system so the project's build process is setup differently depending on the client target that is being compiled:

### Shared KMM
Ths shared multiplatform code consists of 4 different module types:
* `common` - modules with common setup that are meant to be extended or used within other modules. Some examples include networking, dependency injection, persistence.
* `data` - modules that are responsible for data retrieval and persistence for a specific app feature. We usually have one data module per app feature.
* `feature` - modules that contain the view-models and core business logic for a specific app feature. We usually have one feature module per app feature.
* `test-fixtures` - modules that contain test fake's for a particular feature module. We usually have one test fixtures module per app feature.

To make creating new modules seamless, the KMM setup provides 3 dedicated Kotlin DSL Gradle plugins that should be applied to new modules depending on their type:
* new `common` modules should apply the `id("kmm-module-plugin")` plugin, unless they are meant to be exposed through `kmm-module-plugin` itself, in which case they should apply the `id("kmm-platform-plugin")` plugin to avoid circular dependencies;
* new `data` modules should apply the `id("kmm-data-plugin")` plugin;
* new `feature` modules should apply the `id("kmm-feature-plugin")` plugin;
* all other modules should apply the `id("kmm-module-plugin")` plugin;

#### Example: Adding New KMM Modules
You can create new modules using Android Studio's `File` -> `New Module`. Keep 3 things in mind when adding new KMM modules:
1. The new module has to be defined inside the `kmm` folder.
2. The new module has to be prefixed with `kmm-`.
3. Make sure to update `modules.gradle.kts` and add the new module to the list of modules.

### Android
The Android app is also using Gradle as a build system and consists of 3 different module types:
* `common` - modules with common setup that are meant to be extended or used within other modules. Some examples include Jetpack Compose, the design system, tests.
* `feature` - modules that contain the UI for a specific app feature. We usually have one feature module per app feature.
* `test-fixtures` - modules that contain test robots for a particular feature module. We usually have one test fixtures module and robot per app feature.

Similarly to KMM, to make creating new modules seamless, the Android setup provides 2 dedicated Kotlin DSL Gradle plugins that should be applied to new modules depending on their type:
* new `common` modules should apply the `id("android-module-plugin")` plugin, unless they are meant to be exposed through `android-module-plugin` itself, in which case they should apply the `id("android-library-plugin")` plugin to avoid circular dependencies;
* new `feature` modules should apply the `id("android-feature-plugin")` plugin;
* all other modules should apply the `id("android-module-plugin")` plugin;

#### Example: Adding New Android Modules
You can create new modules using Android Studio's `File` -> `New Module`. Keep 3 things in mind when adding new Android modules:
1. The new module has to be defined inside the `appAndroid` folder.
2. Make sure to update `modules.gradle.kts` and add the new module to the list of modules.
3. To link a KMM module to the new Android module, just add it as a `implementation(projects.MY_KMM_MODULE)` dependency.

### iOS
The iOS app has its own native build system using Xcode and is using the concept of a "workspace" to define a CLEAN multi-module setup consisting of 3 different module types with the exact same definitions as the ones for Android above.

The only real difference is around linking the KMM dependencies which are specified through an iOS `.framework` that has to be linked (or embedded) to Xcode so that it can be accessed correctly from Swift in the final app package. Since this process is a bit more involved, we have provided an overview of the 3 key concepts required to achieve this:
* `embedAndSignAppleFrameworkForXcode`
* `Run Scripts`
* `Framework Search Paths`

#### KMM's [embedAndSignAppleFrameworkForXcode](https://blog.jetbrains.com/kotlin/2021/07/multiplatform-gradle-plugin-improved-for-connecting-kmm-modules/)
This Gradle task is specifically designed to run as part of the Xcode build process and its core purpose is to compile the Kotlin source files into Swift, generate the `.framework` file, link and sign it with Xcode, as the name suggests. It should be invoked from a `Run Script` phase during every Xcode build to generate a `.framework` file for **one** Kotlin dependency module. For example, `./gradlew :kmm-umbrella:embedAndSignAppleFrameworkForXcode` will compile all code in the shared `kmm-umbrella` module only and generate its framework based on the specs in `kmm/kmm-umbrella/build.gradle.kts`. This framework follows [Gradle's rules for exporting dependencies](https://kotlinlang.org/docs/mpp-build-native-binaries.html#export-dependencies-to-binaries) and all `api` dependencies will be visible to Swift. This includes all `api` submodules that `kmm-umbrella` depends on.

A caveat with submodule `api` dependencies is that if a module (`A`) declares a dependency on module (`B`), the generated Swift code will prefix the classes from `B` with its fully qualified module name when they are exposed through `A`. For example, a class `MyClass` defined in `B` but exposed through `A` will be available as `BMyClass` when the `A.framework` is used in Swift. In addition, because the individual modules are exported as separate `.framework`s, they do not share memory and resources, unlike Android, where they are all part of the same app memory model. 

To work around this, the general KMM advice is to export an **umbrella** `.framework` for Xcode containing all modules that should be exposed to iOS and Swift. This method overcomes the two issues above:
* by using the `export()` function, the module's transitive `api` dependencies are correctly exported to Swift with their module-independent names, e.g. in our example above, `MyClass` which is exposed through `A` but defined in `B` will be available as `MyClass` to swift when the `A.framework` is linked; 
* all exported module share the same memory pool which allows them to keep and access the same shared resources; 

#### Run Scripts
`Run Scripts` are custom scripts that can be invoked during an Xcode build during certain stages. In terms of KMM, we require a custom `Run Script` with which to invoke the `embedAndSignAppleFrameworkForXcode` Gradle task to generate and link the `.framework` file. A caveat here is that `embedAndSignAppleFrameworkForXcode` has to ideally be invoked from the main `appIos` target to ensure that the code is signed with the correct signature, otherwise Xcode will throw an error. For simple apps, this should be okay.

In this project, we have a multi-module setup so we have actually linked a `Run Script` phase both for the `appIos` target (to sign the code correctly) and for the relevant feature modules (where the `KmmShared` framework is used). This is because Xcode compiles the code using `Dependency Order` by default which means that the main app files will be compiled last. Therefore, if we do not have the `Run Script`s in each feature module, we will get Swift compile errors related to a missing `KmmShared` module which is indeed the case because the app's `Run Script` will run after all sub-modules have been compiled first. Of course, having two `Run Script` phases means the KMM code will be compiled twice (or more times, depending on how many feature modules we have) when `appIos` is built, but luckily Gradle's cache makes subsequent compilations run almost instantly so there isn't much overhead.

#### Framework Search Paths
Since the generated `KmmShared.framework` isn't directly linked to Xcode, the project needs a way to locate it when referenced from Swift. This is where `Framework Search Paths` have to be used to tell Xcode where the `KmmShared` framework is. An example value for `Framework Search Paths` is:
```  
$(SRCROOT)/../../kmm/kmm-umbrella/build/xcode-frameworks/$(CONFIGURATION)/$(SDK_NAME)  
```

#### Example: Adding New iOS Modules (With KMM Dependency)
For this example, we will assume that we have a new shared KMM module in Kotlin under `kmm/kmm-settings` which is ready to be integrated with Xcode to build a settings feature for the newsfeed app (containing the view-model and all shared code to drive the UI).

1. Since the new KMM module will be accessed from Swift we have to add it to the list of `exportedDependencies` in the umbrella framework under `kmm/kmm-umbrella/build.gradle.kts`, as `projects.kmmSettings`.
2. Open the existing `appIos.xcworkspace`.
3. Create a new `Framework` project called `settings` (without tests for now, more on that in the coming sections) and choose an appropriate `Bundle Identifier`.
4. Link the project with the existing `appIos` group+project so that it becomes part of the same workspace.
5. Open the new project and adjust common settings (iOS version etc).
6. Click on the `settings.framework` target -> `Build Settings`.
7. Set `Framework Search Paths` to `$(SRCROOT)/../../kmm/kmm-umbrella/build/xcode-frameworks/$(CONFIGURATION)/$(SDK_NAME)` and make sure to mark it as `non-recursive`.
8. Go to `Build Phases` -> `+` -> `New Run Script Phase` and paste the following.
```
cd "$SRCROOT/../.."
./gradlew :kmm-umbrella:embedAndSignAppleFrameworkForXcode
```
9. You should now be able to link your newly created Xcode `Settings` module to other modules as `Settings.framework` using the `Frameworks, Libraries and Embedded Content` Xcode option under the relevant target you want to use it from.

## 💉 Dependency Injection
Due to the nature of Kotlin Multiplatform and the interoperability with Native targets, standard dependency injection frameworks like Dagger or Hilt cannot be used within the shared KMM modules. Some reasons are:
* lack of KMM support within the DI frameworks;
* lack of multi-module support (works out-of-the-box on Android but not on Native);
* annotation processing limitations with KMM;
* differences in the Native target build process;
* Kotlin syntactic sugar features are unavailable for Native targets (e.g. `val dependency by inject<Dependency>()` is not valid in Swift);

### Available Libraries
In order to build a multi-module CLEAN architecture with the above restrictions in mind, at the time of writing, we have a few options available to implement the DI pattern in KMM:
* [Koin](https://insert-koin.io/) - requires custom initialisation for each target in order to start the Koin application using `startKoin`; true multi-module setup seems possible only if one module knows about all exposed dependencies *or* if feature modules are linked/unliked dynamically when required, which don't seem to scale well.
* [kotlin-inject](https://github.com/evant/kotlin-inject) - Dagger-like, compile-time, annotation-based dependency injection library.
* [Kodein](https://github.com/Kodein-Framework/Kodein-DI) - official Kotlin dependency injection tool; large community support; *the choice for this project*.

### Approach
The preferred DI framework of choice is `Kodein` as it is in active development, purely Kotlin-based and offers out-of-the-box support for CLEAN multi-module setup.  Below is a summary of the approach chosen for this project:
* The `kmm-common-di` module contains an abstract `DiModule` class which allows code modules to register themselves as dependency providers.
* In order to be eligible for injection, a feature (or data) code module must extend `DiModule` and provide a unique `name` and the dependencies it wishes to expose. Additionally, it can also specify its own upstream dependencies, if any, which will automatically be wired up.
* Clients inject dependencies through custom `inject()` methods which the code modules must specify for each dependency. For example, dependency `A` must have a corresponding `injectA(): A` method within its code module. Although Kodein has dedicated syntax for injecting dependencies for Android and Native targets, having our own `inject` methods allows us to potentially replace the injection library altogether as well as have the same interface when accessing the module's dependencies directly from Kotlin. To make this easier, `DiModule` has a convenience `injector()` method which exposes Kodein's `DirectDI` class which is used to expose the required dependency using Kodein's dedicated `.instance(...)` method family.
* Clients import the relevant modules they need and use their custom injection methods to access dependencies in the same way with pure Kotlin syntax, e.g. `val viewModel = FeedModule.injectFeedViewModel()` (Android) or `FeedModule.shared.injectFeedViewModel()` (iOS).

The chosen approach allows greater flexibility and scalability - replacing the DI framework is just a task of changing how the dependencies are provided through `DiModule`.

### Example: Providing a New Dependency
Following our previous example with the new `kmm-settings` module, lets say we are ready to expose its `SettingsViewModel` to our clients through DI:
1. Create a new file under `kmm-settings/src/commonMain/kotlin/PACKAGE/` called `SettingsModule`.
2. Define the following class:
```
object SettingsModule : DiModule() {  
   override fun name() = "kmm-settings"
      
   override fun build(builder: DI.Builder) {  
      builder.apply {
         bindProvider {
            SettingsViewModel()  
         }
      }  
   }
    
   fun injectSettingsViewModel(): SettingsViewModel = inject()  
}
```
3. `SettingsViewModel` should then be available to clients using `SettingsModule.injectSettingsViewModel()` (Android) or `SettingsModule.shared.injectSettingsViewModel()` (iOS).

## 🔗 Deeplinks
The clients support the following deeplinks:
* Home screen: `news://home`;
* Post details screen: `news://post?postId=b8`;

### Android
Android supports deeplinks out-of-the-box using custom url schemes that Activities can register themselves for in the relevant `AndroidManifest.xml`.

#### Example: Adding a New Android Deeplink
Following our previous example with the new `kmm-settings` module, lets say we are ready to add a new deeplink to the new `Settings` screen on Android:

1. Open the feature module's (`settings`) `AndroidManifest.xml` file and add the following:
```
<application>  
   <activity android:name=".SettingsActivity" android:launchMode="singleTask">  
      <intent-filter> <action android:name="android.intent.action.VIEW" />  
         <category android:name="android.intent.category.DEFAULT" />  
         <category android:name="android.intent.category.BROWSABLE" />  
         <data android:host="settings" android:scheme="@string/deeplink_url_scheme" />  
	  </intent-filter> 
   </activity>
</application>
```
2. Run the app and verify the new screen opens when navigating to:
```  
adb shell am start -d "news://settings"  
```  

### iOS
iOS also supports deeplinks out-of-the-box using custom url schemes handled by SwiftUi. Our common modules provide a handy `View` extension which can be used to register a `struct` as a deeplink receiver.

#### Example: Adding a New iOS Deeplink
Following our previous example with the new `kmm-settings` module, lets say we are ready to add a new deeplink to it on iOS. Depending on where we'd like to launch the Settings screen from we can choose to put our SwiftUI link handler in the relevant place. Our `common-swiftui` package already includes a handy `onDeepLink` extension function that lets us register a rendered `struct` as a handler for a deeplinks. For the purposes of our app, we will just open the settings screen from the feed:

1. Open `FeedScreen.swift`, locate `FeedScreenContent` and a new property for controlling whether the settings screen is visible or not:
```
@State private var settingsOpened: Boolean = false
```
2. Attach the following deep link handler:
```
.onDeepLink(deepLink: "settings") { queryItems in
   settingsOpened = true
}
```
3. Pass `settingsOpened` as a `@Binding` parameter to the `FeedState` struct.
4. Add the actual action to perform:
```
if (settingsOpened) {
   // Fake navigation link to handle programmatic selection of settings
   NavigationLink(
      destination: SettingsScreen(),
      tag: settingsOpened,
      selection: self.$settingsOpened
   ) {
      EmptyView()
   }
}
```
5. Run the app and verify the new screen opens when navigating to:
```  
xcrun simctl openurl booted "news://settings"  
```  

## 🧪 Tests
The project's testing framework includes unit tests for the shared KMM code (covering the view-model code), UI tests for all Android screens and UI tests for iOS (performing combined UI + view-model tests for both).

Additionally, we make use of the [robot testing pattern](https://jakewharton.com/testing-robots/) on both client platforms though dedicated `Robot` classes and `test-fixtures` modules.

### Shared KMM
KMM supports both unit and UI testing through the standard testing framework with the following folder structure:
* `commonTest` - unit tests for the common code;
* `androidTest` - unit tests for any Android-specific logic branched out from `commonMain`;
* `androidAndroidTest` - UI (instrumented) tests for any Android-specific logic branched out from `commonMain`;
* `iosTest` - unit tests for any iOS-specific logic branched out from `commonMain`;

In this project, we have added unit tests for all view-models in `commonMain` in the relevant module's `commonTest` folder which uses `Fake`s rather than `mock`s to verify correct behaviour.

To launch all unit tests (results available under `path_to_your_project/module_name/build/reports/tests/`), run:
```  
./gradlew test  
```
You can also launch the unit tests for a specific module directly from Android Studio by right clicking on its `commonTest` folder and then `Run`.

### Android
Since the KMM code is already covered by unit tests, the Android app only has UI (instrumented) tests. Each feature module is responsible for defining its own UI tests and they should be specified in the standard `androidTest` folder.

To launch all UI tests (results available under `path_to_your_project/module_name/build/reports/androidTests/connected/`), make sure to have an emulator instance running and then run:
```  
./gradlew connectedAndroidTest  
```
You can also launch the UI tests for a specific feature module directly from Android Studio by right clicking on its `androidTest` folder and then `Run`.

### iOS
UI testing on iOS is made up of two components:
* `UI Testing Bundle` - starting point for UI test. Separate testing bundles have to be created for each module we'd like to test;
* `UI Test Host App` - UI tests run within a host app, which can either be the main app target of the project or a "dummy" one just for tests;

In this project, we do not have access to the main `appIos` target from our feature modules, which means that we need to define our own "dummy" target for our unit tests to run in.

Since the KMM code is already covered by unit tests, the iOS app only has UI tests. Each feature module is responsible for defining its own UI tests and to differentiate them from the code we have chosen to put them under `tests` folders for the relevant feature modules. The naming pattern used here is `<Module>UiTests` and `<Module>TestHostApp`.

To launch all UI tests, run:
```
xcodebuild -workspace appIos.xcworkspace -scheme "appIos" -sdk iphonesimulator -destination 'platform=iOS Simulator,name=iPhone 11,OS=15.2' test | xcpretty
```

You can also launch the UI tests for a specific feature module directly from Xcode by following the steps below:
1. Choose the scheme you want to run the tests for.
2. Press and hold on the `Run` button to see the dropdown menu and switch to `Test`.

#### Example: Adding a New iOS UI Test
Following our previous example with the new `kmm-settings` module, lets say we are ready to add a new UI test for the new `settings` feature module on iOS:

1. Create a new `Framework` project called `settings-test-fixtures` and link it with the `appIos` workspace group.
2. Set your required iOS version levels and basic project settings. You can also remove unnecessary files that Xcode creates during this step.
3. Click on your `settings-test-fixtures.framework` target -> `Build Settings` and set the following to allow the framework access to the standard `XCTest` sources:
    - set `Enable Testing Search Paths` to `yes` for `Debug`;
    - add `$(PLATFORM_DIR)/Developer/Library/Frameworks` to `Runpath Search Paths`;
    - add `$(PLATFORM_DIR)/Developer/usr/lib` to `Runpath Search Paths`;
4. Create a new file `SettingsRobot`. You can follow examples from the other robots in the project.
5. Navigate to the `settings` module and create a new `App` target called `SettingsTestHost` - this will be the app that runs the UI tests.
6. Select the new `SettingsTestHost` target and under `Frameworks, Libraries, and Embedded Content` link `CommonSwiftUiTest.framework`, `Settings.framework` and `SettingsTestFixtures.framework`.
7. Navigate to `Build Settings` and:
    - add `$(SRCROOT)/../../kmm/kmm-umbrella/build/xcode-frameworks/$(CONFIGURATION)/$(SDK_NAME)` to `Runpath Search Paths` (to give the app access to the KMM module);
    - set `$(SRCROOT)/../../kmm/kmm-umbrella/build/xcode-frameworks/$(CONFIGURATION)/$(SDK_NAME)` to `Framework Search Paths` (to make the KMM module visible from Swift);
8. Populate `SettingsTestHost.swift` with the UI to be tested. You can use examples from other test host apps.
9. Select the `settings.framework` target and add a new `UI Testing Bundle` making `SettingsTestHost` its host.
10. Populate the test file with some tests. You can use examples from other test files in the project.
11. You should now be able to switch to the `settings` scheme and run the UI tests on the simulator.
12. Optional: if you want your new UI test to run as part of all UI tests:
    - Select the `appIos` scheme -> `Edit Scheme` -> `Test`.
    - Add your new `SettingsUiTest` to the list using the `+`.
