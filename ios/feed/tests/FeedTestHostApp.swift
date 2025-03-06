import SwiftUI
import CommonSwiftUi
import Feed
import NewsfeedMultiplatform

/*
 App to setup and run the UI tests for this module using a launch environment
 */
@main
struct FeedTestHostApp: App {
    private let feedPages: [DecoratedFeedPage]
    private let feedPageCache: DecoratedFeedPage?
    private let feedRepository: FakeFeedRepository
    private let redecorateSectionedFeedUseCase: RedecorateSectionedFeedUseCase
    private let buildSectionedFeedUseCase: BuildSectionedFeedUseCase
    private let mergeSectionedFeedUseCase: MergeSectionedFeedUseCase
    
    init() {
        DependencyInjector.shared.initialise()
        // Mock/fake necessary constructs based on launch environment
        self.feedPages = FeedType.obtainFromEnvironment()
        self.feedPageCache = FeedCacheType.obtainFromEnvironment()
        self.feedRepository = FakeFeedRepository(
            postRepository: FakePostRepository(
                post: nil,
                usePostForCache: false,
                readingTimeMinutes: 1
            ),
            feedPages: feedPages,
            feedPageCache: feedPageCache
        )
        self.feedRepository.feedResponse = FeedResponseType.obtainFromEnvironment(key: "feedResponse")
        self.feedRepository.feedLoadMoreResponse = FeedResponseType.obtainFromEnvironment(key: "feedLoadMoreResponse")
        self.buildSectionedFeedUseCase = RealBuildSectionedFeedUseCase(
            dispatcher: Dispatcher.shared.Main,
            clock: FakeClock.shared
        )
        self.mergeSectionedFeedUseCase = RealMergeSectionedFeedUseCase(dispatcher: Dispatcher.shared.Main)
        self.redecorateSectionedFeedUseCase = RealRedecorateSectionedFeedUseCase(
            dispatcher: Dispatcher.shared.Main,
            feedRepository: feedRepository,
            flattenSectionedFeedUseCase: RealFlattenSectionedFeedUseCase(dispatcher: Dispatcher.shared.Main),
            buildSectionedFeedUseCase: buildSectionedFeedUseCase
        )
    }
    
    var body: some Scene {
        WindowGroup {
            CustomSwiftUiTestRuleWrapper<FeedScreenContent>(embedWithinNavigation: false) {
                FeedScreenContent(viewModel: FeedViewModel(
                    dispatcher: Dispatcher.shared.Main,
                    feedRepository: feedRepository,
                    redecorateSectionedFeedUseCase: redecorateSectionedFeedUseCase,
                    buildSectionedFeedUseCase: buildSectionedFeedUseCase,
                    mergeSectionedFeedUseCase: mergeSectionedFeedUseCase)
                )
            }
        }
    }
}

private enum FeedType: String {
    case empty = "empty"
    case singlePage = "singlePage"
    case multiPage = "multiPage"
    
    static func obtainFromEnvironment() -> [DecoratedFeedPage] {
        let type = FeedType.init(rawValue: ProcessInfo.processInfo.environment["feedPages"]!)
        switch type {
        case .empty:
            return FeedCreator.shared.emptyFeed()
        case .singlePage:
            return FeedCreator.shared.singlePageFeed()
        default:
            return FeedCreator.shared.multiPageFeed()
        }
    }
}

private enum FeedCacheType: String {
    case singlePage = "singlePage"
    
    static func obtainFromEnvironment() -> DecoratedFeedPage? {
        if let cacheType = ProcessInfo.processInfo.environment["feedPageCache"] {
            let type = FeedType.init(rawValue: cacheType)
            switch type {
            case .singlePage:
                return FeedCreator.shared.singlePageFeed().first
            default:
                return nil
            }
        }
        return nil
    }
}

private enum FeedResponseType: String {
    case error = "error"
    case success = "success"
    case loadForever = "loadForever"
    
    static func obtainFromEnvironment(key: String) -> FakeResponse {
        let type = FeedResponseType.init(rawValue: ProcessInfo.processInfo.environment[key]!)
        switch type {
        case .error:
            return FakeResponse.Error(message: nil)
        case .loadForever:
            return FakeResponse.LoadsForever()
        default:
            return FakeResponse.CompletesNormally()
        }
    }
}
