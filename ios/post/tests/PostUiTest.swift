import XCTest
import PostTestFixtures

class PostUiTest: XCTestCase {
    
    override func setUpWithError() throws {
        // In UI tests it is usually best to stop immediately when a failure occurs.
        continueAfterFailure = false
    }
    
    func testLoadingIndicatorShown() throws {
        // Given
        let response = "loadForever"
        // When
        runTest(postResponse: response) { robot in
            // Then
            robot.assertLoadingExists()
            robot.assertAddToFavouritesButtonDoesNotExist()
            robot.assertRemoveFromFavouritesButtonDoesNotExist()
            robot.assertPostDoesNotExist(
                title: PostTitle,
                author: PostAuthor,
                readingTime: PostReadingTime,
                body: PostBody
            )
            robot.assertBlockingErrorDoesNotExist()
        }
    }
    
    func testCacheShown() throws {
        // Given
        let cache = "notFavourite"
        let response = "loadForever"
        // When
        runTest(
            postCache: cache,
            postResponse: response
        ) { robot in
            robot.assertLoadingExists()
            robot.assertAddToFavouritesButtonExists()
            robot.assertRemoveFromFavouritesButtonDoesNotExist()
            robot.assertPostExists(
                title: PostTitle,
                author: PostAuthor,
                readingTime: PostReadingTime,
                body: PostBody
            )
            robot.assertBlockingErrorDoesNotExist()
        }
    }
    
    func testPostShown() throws {
        runTest { robot in
            robot.assertLoadingDoesNotExist()
            robot.assertAddToFavouritesButtonExists()
            robot.assertRemoveFromFavouritesButtonDoesNotExist()
            robot.assertPostExists(
                title: PostTitle,
                author: PostAuthor,
                readingTime: PostReadingTime,
                body: PostBody
            )
            robot.assertBlockingErrorDoesNotExist()
        }
    }
    
    func testBlockingErrorShown() throws {
        // Given
        let response = "error"
        // When
        runTest(postResponse: response) { robot in
            // Then
            robot.assertLoadingDoesNotExist()
            robot.assertAddToFavouritesButtonDoesNotExist()
            robot.assertRemoveFromFavouritesButtonDoesNotExist()
            robot.assertPostDoesNotExist(
                title: PostTitle,
                author: PostAuthor,
                readingTime: PostReadingTime,
                body: PostBody
            )
            robot.assertBlockingErrorExists()
        }
    }
    
    func testToggleFavouriteAddsToFavourites() throws {
        // Given
        let post = "notFavourite"
        // When
        runTest(post: post) { robot in
            // Then
            robot.assertAddToFavouritesButtonExists()
            robot.assertRemoveFromFavouritesButtonDoesNotExist()
            // When
            robot.clickAddToFavouritesButton()
            // Then
            robot.assertAddToFavouritesButtonDoesNotExist()
            robot.assertRemoveFromFavouritesButtonExists()
        }
    }
    
    func testToggleFavouriteRemovesFromFavourites() throws {
        // Given
        let post = "favourite"
        // When
        runTest(post: post) { robot in
            // Then
            robot.assertAddToFavouritesButtonDoesNotExist()
            robot.assertRemoveFromFavouritesButtonExists()
            // When
            robot.clickRemoveFromFavouritesButton()
            // Then
            robot.assertAddToFavouritesButtonExists()
            robot.assertRemoveFromFavouritesButtonDoesNotExist()
        }
    }
    
    private func runTest(
        post: String = "notFavourite",
        postCache: String? = nil,
        postResponse: String = "success",
        block: (PostRobot) -> Void
    ) {
        let app = XCUIApplication()
        app.launchEnvironment = [
            "post": post,
            "postResponse": postResponse
        ]
        if let postCache = postCache {
            app.launchEnvironment["postCache"] = postCache
        }
        app.launch()
        block(PostRobot(app: app))
    }
}

private let PostTitle = "Post Title"
private let PostAuthor = "Anonymous"
private let PostReadingTime = "1 min read"
private let PostBody = "This is a sample post body"
