import SwiftUI
import CommonSwiftUiTest
import Post
import KmmShared

/*
 App to setup and run the UI tests for this module using a launch environment
 */
@main
struct PostTestHostApp: App {
    private let post: DecoratedPost
    private let repository: FakeFeedRepository
    
    init() {
        // Mock/fake necessary constructs based on launch environment
        self.post = PostType.obtainFromEnvironment()
        self.repository = FakeFeedRepository(
            feed: nil,
            post: post
        )
        self.repository.postResponse = PostResponseType.obtainFromEnvironment()
    }
    
    var body: some Scene {
        WindowGroup {
            CustomSwiftUiTestRuleWrapper(embedWithinNavigation: true) {
                PostScreenContent(viewModel: PostViewModel(
                    postId: PostId,
                    feedRepository: repository)
                )
            }
        }
    }
}

/*
 Obtains a test post from the launch environment to use during tests
 */
private enum PostType: String {
    case notFavourite = "notFavourite"
    case favourite = "favourite"
    
    static func obtainFromEnvironment() -> DecoratedPost {
        let type = PostType.init(rawValue: ProcessInfo.processInfo.environment["post"]!)
        switch type {
        case .favourite:
            return PostCreator.shared.post(favouriteTimestamp: 123)
        default:
            return PostCreator.shared.post(favouriteTimestamp: nil)
        }
    }
}

/*
 Obtains a test response type from the launch environment to use during tests
 */
private enum PostResponseType: String {
    case error = "error"
    case success = "success"
    case loadForever = "loadForever"
    
    static func obtainFromEnvironment() -> FakeResponse {
        let type = PostResponseType.init(rawValue: ProcessInfo.processInfo.environment["postResponse"]!)
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

private let PostId = "post_123"
