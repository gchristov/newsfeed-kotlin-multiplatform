import XCTest

/*
 Robot to make assertions on the Post screen.
 */
public class PostRobot {
    private let app: XCUIApplication
    
    public init(app: XCUIApplication) {
        self.app = app
    }
    
    public func assertLoadingExists() {
        XCTAssert(app.activityIndicators[Loading].exists)
    }
    
    public func assertLoadingDoesNotExist() {
        XCTAssert(!app.activityIndicators[Loading].exists)
    }
    
    public func assertPostExists(
        title: String,
        author: String,
        readingTime: String,
        body: String
    ) {
        XCTAssert(app.staticTexts[title].exists)
        XCTAssert(app.staticTexts[author].exists)
        XCTAssert(app.staticTexts[readingTime].exists)
        XCTAssert(app.staticTexts[body].exists)
    }
    
    public func assertPostDoesNotExist(
        title: String,
        author: String,
        readingTime: String,
        body: String
    ) {
        XCTAssert(!app.staticTexts[title].exists)
        XCTAssert(!app.staticTexts[author].exists)
        XCTAssert(!app.staticTexts[readingTime].exists)
        XCTAssert(!app.staticTexts[body].exists)
    }
    
    public func assertAddToFavouritesButtonExists() {
        XCTAssert(app.buttons[AddToFavourite].exists)
    }
    
    public func assertAddToFavouritesButtonDoesNotExist() {
        XCTAssert(!app.buttons[AddToFavourite].exists)
    }
    
    public func assertRemoveFromFavouritesButtonExists() {
        XCTAssert(app.buttons[RemoveFromFavourite].exists)
    }
    
    public func assertRemoveFromFavouritesButtonDoesNotExist() {
        XCTAssert(!app.buttons[RemoveFromFavourite].exists)
    }
    
    public func assertBlockingErrorExists() {
        XCTAssert(app.staticTexts[BlockingErrorTitle].exists)
        XCTAssert(app.staticTexts[BlockingErrorSubtitle].exists)
        XCTAssert(app.buttons[BlockingErrorAction].exists)
    }
    
    public func assertBlockingErrorDoesNotExist() {
        XCTAssert(!app.staticTexts[BlockingErrorTitle].exists)
        XCTAssert(!app.staticTexts[BlockingErrorSubtitle].exists)
        XCTAssert(!app.buttons[BlockingErrorAction].exists)
    }
    
    public func clickAddToFavouritesButton() {
        app.buttons[AddToFavourite].tap()
    }
    
    public func clickRemoveFromFavouritesButton() {
        app.buttons[RemoveFromFavourite].tap()
    }
}

private let Loading = "Loading"

private let AddToFavourite = "Add to favourites"
private let RemoveFromFavourite = "Remove from favourites"

private let BlockingErrorTitle = "Oups!"
private let BlockingErrorSubtitle = "Something has gone wrong. Please try again."
private let BlockingErrorAction = "Try again"
