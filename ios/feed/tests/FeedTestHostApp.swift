import SwiftUI
import CommonSwiftUi
import Feed
import KmmShared

/*
 App to setup and run the UI tests for this module using a launch environment
 */
@main
struct FeedTestHostApp: App {
    private let feedPages: [DecoratedFeedPage]
    private let feedPageCache: DecoratedFeedPage?
    private let repository: FakeFeedRepository
    private let getSectionedFeedUseCase: GetSectionedFeedUseCase
    private let redecorateSectionedFeedUseCase: RedecorateSectionedFeedUseCase
    
    init() {
        DependencyInjector.shared.initialise()
        // Mock/fake necessary constructs based on launch environment
        self.feedPages = FeedType.obtainFromEnvironment()
        self.feedPageCache = FeedCacheType.obtainFromEnvironment()
        self.repository = FakeFeedRepository(
            postRepository: FakePostRepository(
                post: nil,
                postCache: nil
            ),
            feedPages: feedPages,
            feedPageCache: feedPageCache
        )
        self.repository.feedResponse = FeedResponseType.obtainFromEnvironment(key: "feedResponse")
        self.repository.feedLoadMoreResponse = FeedResponseType.obtainFromEnvironment(key: "feedLoadMoreResponse")
        let buildSectionedFeedUseCase = BuildSectionedFeedUseCase(
            dispatcher: Dispatchers.shared.Main,
            clock: FakeClock.shared
        )
        self.getSectionedFeedUseCase = GetSectionedFeedUseCase(
            feedRepository: repository,
            buildSectionedFeedUseCase: buildSectionedFeedUseCase,
            mergeSectionedFeedUseCase: MergeSectionedFeedUseCase(dispatcher: Dispatchers.shared.Main)
        )
        self.redecorateSectionedFeedUseCase = RedecorateSectionedFeedUseCase(
            feedRepository: repository,
            flattenSectionedFeedUseCase: FlattenSectionedFeedUseCase(dispatcher: Dispatchers.shared.Main),
            buildSectionedFeedUseCase: buildSectionedFeedUseCase
        )
    }
    
    var body: some Scene {
        WindowGroup {
            CustomSwiftUiTestRuleWrapper<FeedScreenContent>(embedWithinNavigation: false) {
                FeedScreenContent(viewModel: FeedViewModel(
                    dispatcher: Dispatchers.shared.Main,
                    feedRepository: repository,
                    getSectionedFeedUseCase: getSectionedFeedUseCase,
                    redecorateSectionedFeedUseCase: redecorateSectionedFeedUseCase)
                )
            }
        }
    }
}

/*
 Obtains a test post from the launch environment to use during tests
 */
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

/*
 Obtains a test cache from the launch environment to use during tests
 */
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

/*
 Obtains a test response type from the launch environment to use during tests
 */
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
