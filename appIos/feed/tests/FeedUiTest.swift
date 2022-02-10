import XCTest
import FeedTestFixtures

class FeedUiTest: XCTestCase {
    
    override func setUpWithError() throws {
        // In UI tests it is usually best to stop immediately when a failure occurs.
        continueAfterFailure = false
    }
    
    func testLoadingIndicatorShown() throws {
        runTest(feedResponse: "loadForever") { robot in
            robot.assertLoadingExists()
            robot.assertFeedItemDoesNotExist(
                title: Post1Title,
                author: Post1Author,
                body: Post1Body
            )
            robot.assertBlockingErrorDoesNotExist()
            robot.assertNonBlockingErrorDoesNotExist()
        }
    }
    
    func testSinglePageFeedShown() throws {
        runTest { robot in
            robot.assertLoadingDoesNotExist()
            robot.assertFeedItemExists(
                title: Post1Title,
                author: Post1Author,
                body: Post1Body
            )
            robot.assertFeedItemExists(
                title: Post2Title,
                author: Post2Author,
                body: Post2Body
            )
            robot.assertFavouriteItemsShown(favouriteItems: 1)
            robot.assertBlockingErrorDoesNotExist()
            robot.assertNonBlockingErrorDoesNotExist()
        }
    }
    
    func testMultiPageFeedLoadingIndicatorShown() throws {
        runTest(
            feed: "multiPage",
            feedLoadMoreResponse: "loadForever"
        ) { robot in
            robot.assertLoadingExists()
            robot.assertFeedItemExists(
                title: Post1Title,
                author: Post1Author,
                body: Post1Body
            )
            robot.assertBlockingErrorDoesNotExist()
            robot.assertNonBlockingErrorDoesNotExist()
        }
    }
    
    func testMultiPageFeedShown() throws {
        runTest(feed: "multiPage") { robot in
            robot.assertLoadingDoesNotExist()
            robot.assertFeedItemExists(
                title: Post1Title,
                author: Post1Author,
                body: Post1Body
            )
            robot.assertFeedItemExists(
                title: Post2Title,
                author: Post2Author,
                body: Post2Body
            )
            robot.assertFeedItemExists(
                title: Post3Title,
                author: Post3Author,
                body: Post3Body
            )
            robot.assertFavouriteItemsShown(favouriteItems: 2)
            robot.assertBlockingErrorDoesNotExist()
            robot.assertNonBlockingErrorDoesNotExist()
        }
    }

    func testBlockingErrorShown() throws {
        runTest(feedResponse: "error") { robot in
            robot.assertLoadingDoesNotExist()
            robot.assertFeedItemDoesNotExist(
                title: Post1Title,
                author: Post1Author,
                body: Post1Body
            )
            robot.assertBlockingErrorExists()
            robot.assertNonBlockingErrorDoesNotExist()
        }
    }
    
    func testNonBlockingErrorShown() throws {
        runTest(
            feed: "multiPage",
            feedLoadMoreResponse: "error"
        ) { robot in
            robot.assertLoadingDoesNotExist()
            robot.assertFeedItemExists(
                title: Post1Title,
                author: Post1Author,
                body: Post1Body
            )
            robot.assertBlockingErrorDoesNotExist()
            robot.assertNonBlockingErrorExists()
        }
    }

    func testFeedItemClickOpensPost() throws {
        runTest { robot in
            robot.assertAddToFavouritesButtonDoesNotExist()
            robot.clickPost(title: Post1Title)
            robot.assertAddToFavouritesButtonExists()
        }
    }
    
    private func runTest(
        feed: String = "singlePage",
        feedResponse: String = "success",
        feedLoadMoreResponse: String = "success",
        block: (FeedRobot) -> Void
    ) {
        let app = XCUIApplication()
        app.launchEnvironment = [
            "feed": feed,
            "feedResponse": feedResponse,
            "feedLoadMoreResponse": feedLoadMoreResponse
        ]
        app.launch()
        block(FeedRobot(app: app))
    }
}

private let Post1Title = "Post 1 Title"
private let Post1Author = "steve"
private let Post1Body = "This is a sample post 1 body"
private let Post2Title = "Post 2 Title"
private let Post2Author = "amy"
private let Post2Body = "This is a sample post 2 body that may be longer and even go on multiple lines"
private let Post3Title = "Post 3 Title"
private let Post3Author = "sarah"
private let Post3Body = "This is a sample post 3 body"
