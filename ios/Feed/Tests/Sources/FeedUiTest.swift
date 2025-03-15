import XCTest

class FeedUiTest: XCTestCase {
    
    override func setUpWithError() throws {
        // In UI tests it is usually best to stop immediately when a failure occurs.
        continueAfterFailure = false
    }
    
    func testEmptyStateShown() throws {
        let feed = "empty"

        runTest(feedPages: feed) { robot in
            robot.assertLoadingDoesNotExist()
            robot.assertEmptyStateExists()
            robot.assertSectionDoesNotExist(header: Post1Section)
            robot.assertFeedItemDoesNotExist(
                title: Post1Title,
                date: Post1Date
            )
            robot.assertBlockingErrorDoesNotExist()
            robot.assertNonBlockingErrorDoesNotExist()
        }
    }
    
    func testCacheShown() throws {
        let cache = "singlePage"
        let response = "loadForever"

        runTest(
            feedPageCache: cache,
            feedResponse: response
        ) { robot in
            robot.assertLoadingExists()
            robot.assertEmptyStateDoesNotExist()
            robot.assertSectionExists(header: Post1Section)
            robot.assertFeedItemExists(
                title: Post1Title,
                date: Post1Date
            )
            robot.assertSectionExists(header: Post2Section)
            robot.assertFeedItemExists(
                title: Post2Title,
                date: Post2Date
            )
            robot.assertSectionExists(header: Post3Section)
            robot.assertFeedItemExists(
                title: Post3Title,
                date: Post3Date
            )
            robot.assertSectionExists(header: Post4Section)
            robot.assertFeedItemExists(
                title: Post4Title,
                date: Post4Date
            )
            robot.assertFavouriteItemsShown(favouriteItems: 2)
            robot.assertBlockingErrorDoesNotExist()
            robot.assertNonBlockingErrorDoesNotExist()
        }
    }
    
    func testSinglePageFeedLoadingIndicatorShown() throws {
        let response = "loadForever"

        runTest(feedResponse: response) { robot in
            robot.assertLoadingExists()
            robot.assertEmptyStateDoesNotExist()
            robot.assertSectionDoesNotExist(header: Post1Section)
            robot.assertFeedItemDoesNotExist(
                title: Post1Title,
                date: Post1Date
            )
            robot.assertBlockingErrorDoesNotExist()
            robot.assertNonBlockingErrorDoesNotExist()
        }
    }
    
    func testSinglePageFeedShown() throws {
        runTest { robot in
            robot.assertLoadingDoesNotExist()
            robot.assertEmptyStateDoesNotExist()
            robot.assertSectionExists(header: Post1Section)
            robot.assertFeedItemExists(
                title: Post1Title,
                date: Post1Date
            )
            robot.assertSectionExists(header: Post2Section)
            robot.assertFeedItemExists(
                title: Post2Title,
                date: Post2Date
            )
            robot.assertSectionExists(header: Post3Section)
            robot.assertFeedItemExists(
                title: Post3Title,
                date: Post3Date
            )
            robot.assertSectionExists(header: Post4Section)
            robot.assertFeedItemExists(
                title: Post4Title,
                date: Post4Date
            )
            robot.assertFavouriteItemsShown(favouriteItems: 2)
            robot.assertBlockingErrorDoesNotExist()
            robot.assertNonBlockingErrorDoesNotExist()
        }
    }
    
    func testMultiPageFeedLoadingIndicatorShown() throws {
        let feed = "multiPage"
        let response = "loadForever"

        runTest(
            feedPages: feed,
            feedLoadMoreResponse: response
        ) { robot in
            robot.assertLoadingExists()
            robot.assertEmptyStateDoesNotExist()
            robot.assertSectionExists(header: Post1Section)
            robot.assertFeedItemExists(
                title: Post1Title,
                date: Post1Date
            )
            robot.assertBlockingErrorDoesNotExist()
            robot.assertNonBlockingErrorDoesNotExist()
        }
    }
    
    func testMultiPageFeedShown() throws {
        let feed = "multiPage"

        runTest(feedPages: feed) { robot in
            robot.assertLoadingDoesNotExist()
            robot.assertEmptyStateDoesNotExist()
            robot.assertSectionExists(header: Post1Section)
            robot.assertFeedItemExists(
                title: Post1Title,
                date: Post1Date
            )
            robot.assertSectionExists(header: Post2Section)
            robot.assertFeedItemExists(
                title: Post2Title,
                date: Post2Date
            )
            robot.assertSectionExists(header: Post3Section)
            robot.assertFeedItemExists(
                title: Post3Title,
                date: Post3Date
            )
            robot.assertSectionExists(header: Post4Section)
            robot.assertFeedItemExists(
                title: Post4Title,
                date: Post4Date
            )
            robot.assertFavouriteItemsShown(favouriteItems: 2)
            robot.assertBlockingErrorDoesNotExist()
            robot.assertNonBlockingErrorDoesNotExist()
        }
    }

    func testBlockingErrorShown() throws {
        let response = "error"

        runTest(feedResponse: response) { robot in
            robot.assertLoadingDoesNotExist()
            robot.assertEmptyStateDoesNotExist()
            robot.assertSectionDoesNotExist(header: Post1Section)
            robot.assertFeedItemDoesNotExist(
                title: Post1Title,
                date: Post1Date
            )
            robot.assertBlockingErrorExists()
            robot.assertNonBlockingErrorDoesNotExist()
        }
    }
    
    func testNonBlockingErrorShown() throws {
        let feed = "multiPage"
        let response = "error"

        runTest(
            feedPages: feed,
            feedLoadMoreResponse: response
        ) { robot in
            robot.assertLoadingDoesNotExist()
            robot.assertEmptyStateDoesNotExist()
            robot.assertSectionExists(header: Post1Section)
            robot.assertFeedItemExists(
                title: Post1Title,
                date: Post1Date
            )
            robot.assertBlockingErrorDoesNotExist()
            robot.assertNonBlockingErrorExists()
        }
    }

    func testFeedItemClickOpensPost() throws {
        throw XCTSkip("Skipped because it throws an error about a missing GoogleService-Info.plist error when the post opens. This is likely because PostScreen appears and its darepositoryta isn't mocked.")
        
        let post = Post1Title

        runTest { robot in
            robot.assertBackButtonDoesNotExist()
            robot.clickPost(title: post)
            robot.assertBackButtonExists()
        }
    }
    
    private func runTest(
        feedPages: String = "singlePage",
        feedPageCache: String? = nil,
        feedResponse: String = "success",
        feedLoadMoreResponse: String = "success",
        block: (FeedRobot) -> Void
    ) {
        let app = XCUIApplication()
        app.launchEnvironment = [
            "feedPages": feedPages,
            "feedResponse": feedResponse,
            "feedLoadMoreResponse": feedLoadMoreResponse
        ]
        if let feedPageCache = feedPageCache {
            app.launchEnvironment["feedPageCache"] = feedPageCache
        }
        app.launch()
        block(FeedRobot(app: app))
    }
}

private let Post1Section = "THIS WEEK"
private let Post1Title = "Post 1 Title"
private let Post1Date = "21/02/2022"
private let Post2Section = "LAST WEEK"
private let Post2Title = "This is a sample post 2 title that may be longer and even go on multiple lines"
private let Post2Date = "13/02/2022"
private let Post3Section = "THIS MONTH"
private let Post3Title = "Post 3 Title"
private let Post3Date = "01/02/2022"
private let Post4Section = "JAN, 2022"
private let Post4Title = "Post 4 Title"
private let Post4Date = "01/01/2022"
