import XCTest
import PostTestFixtures

class PostUiTest: XCTestCase {
    
    override func setUpWithError() throws {
        // In UI tests it is usually best to stop immediately when a failure occurs.
        continueAfterFailure = false
    }
    
    func testLoadingIndicatorShown() throws {
        runTest(postResponse: "loadForever") { robot in
            robot.assertLoadingExists()
            robot.assertPostDoesNotExist(
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
        runTest(postResponse: "error") { robot in
            robot.assertLoadingDoesNotExist()
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
        runTest(post: "notFavourite") { robot in
            robot.assertAddToFavouritesButtonExists()
            robot.assertRemoveFromFavouritesButtonDoesNotExist()
            robot.clickAddToFavouritesButton()
            robot.assertAddToFavouritesButtonDoesNotExist()
            robot.assertRemoveFromFavouritesButtonExists()
        }
    }
    
    func testToggleFavouriteRemovesFromFavourites() throws {
        runTest(post: "favourite") { robot in
            robot.assertAddToFavouritesButtonDoesNotExist()
            robot.assertRemoveFromFavouritesButtonExists()
            robot.clickRemoveFromFavouritesButton()
            robot.assertAddToFavouritesButtonExists()
            robot.assertRemoveFromFavouritesButtonDoesNotExist()
        }
    }
    
    private func runTest(
        post: String = "notFavourite",
        postResponse: String = "success",
        block: (PostRobot) -> Void
    ) {
        let app = XCUIApplication()
        app.launchEnvironment = [
            "post": post,
            "postResponse": postResponse
        ]
        app.launch()
        block(PostRobot(app: app))
    }
}

private let PostTitle = "Post Title"
private let PostAuthor = "steve"
private let PostReadingTime = "1 min read"
private let PostBody = "This is a sample post body"
