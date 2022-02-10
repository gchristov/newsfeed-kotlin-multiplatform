import SwiftUI
import CommonSwiftUiTest
import Feed
import KmmShared

/*
 App to setup and run the UI tests for this module using a launch environment
 */
@main
struct FeedTestHostApp: App {
    private let feed: [Feed]
    private let repository: FakeFeedRepository
    
    init() {
        // Mock/fake necessary constructs based on launch environment
        self.feed = FeedType.obtainFromEnvironment()
        self.repository = FakeFeedRepository(
            feed: feed,
            post: nil
        )
        self.repository.feedResponse = FeedResponseType.obtainFromEnvironment(key: "feedResponse")
        self.repository.feedLoadMoreResponse = FeedResponseType.obtainFromEnvironment(key: "feedLoadMoreResponse")
    }
    
    var body: some Scene {
        WindowGroup {
            CustomSwiftUiTestRuleWrapper(embedWithinNavigation: false) {
                FeedScreenContent(viewModel: FeedViewModel(feedRepository: repository))
            }
        }
    }
}

/*
 Obtains a test post from the launch environment to use during tests
 */
private enum FeedType: String {
    case singlePage = "singlePage"
    case multiPage = "multiPage"
    
    static func obtainFromEnvironment() -> [Feed] {
        let type = FeedType.init(rawValue: ProcessInfo.processInfo.environment["feed"]!)
        switch type {
        case .singlePage:
            return FeedCreator.shared.singlePageFeed()
        default:
            return FeedCreator.shared.multiPageFeed()
        }
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
