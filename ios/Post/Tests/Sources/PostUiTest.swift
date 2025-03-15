import XCTest

class PostUiTest: XCTestCase {
    
    override func setUpWithError() throws {
        // In UI tests it is usually best to stop immediately when a failure occurs.
        continueAfterFailure = false
    }
    
    func testLoadingIndicatorShown() throws {
        let response = "loadForever"
        
        runTest(postResponse: response) { robot in
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
        let response = "loadForever"
        
        runTest(
            usePostForCache: true,
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
        let response = "error"
        
        runTest(postResponse: response) { robot in
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
    
    func testToggleFavouriteTogglesFavourite() throws {
        runTest { robot in
            robot.assertAddToFavouritesButtonExists()
            robot.assertRemoveFromFavouritesButtonDoesNotExist()
            
            robot.clickAddToFavouritesButton()
            
            robot.assertAddToFavouritesButtonDoesNotExist()
            robot.assertRemoveFromFavouritesButtonExists()
            
            robot.clickRemoveFromFavouritesButton()
            
            robot.assertAddToFavouritesButtonExists()
            robot.assertRemoveFromFavouritesButtonDoesNotExist()
        }
    }
    
    private func runTest(
        usePostForCache: Bool = false,
        postResponse: String = "success",
        block: (PostRobot) -> Void
    ) {
        let app = XCUIApplication()
        app.launchEnvironment = [
            "postResponse": postResponse,
            "usePostForCache": usePostForCache.description
        ]
        app.launch()
        block(PostRobot(app: app))
    }
}

private let PostTitle = "Post Title"
private let PostAuthor = "Anonymous"
private let PostReadingTime = "1 min read"
private let PostBody = "This is a sample post body"
