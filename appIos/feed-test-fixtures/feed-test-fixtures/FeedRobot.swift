import XCTest

/*
 Robot to make assertions on the Feed screen.
 */
public class FeedRobot {
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
    
    public func assertEmptyStateExists() {
        XCTAssert(app.staticTexts["No results found. Please\ntry another search term"].exists)
    }
    
    public func assertEmptyStateDoesNotExist() {
        XCTAssert(!app.staticTexts["No results found. Please\ntry another search term"].exists)
    }
    
    public func assertSectionExists(header: String) {
        XCTAssert(app.staticTexts[header].exists)
    }
    
    public func assertSectionDoesNotExist(header: String) {
        XCTAssert(!app.staticTexts[header].exists)
    }
    
    public func assertFeedItemExists(
        title: String,
        date: String
    ) {
        XCTAssert(app.staticTexts[title].exists)
        XCTAssert(app.staticTexts[date].exists)
    }
    
    public func assertFeedItemDoesNotExist(
        title: String,
        date: String
    ) {
        XCTAssert(!app.staticTexts[title].exists)
        XCTAssert(!app.staticTexts[date].exists)
    }
    
    public func assertFavouriteItemsShown(favouriteItems: Int) {
        XCTAssertEqual(app.images.matching(identifier: AddedToFavourites).count, favouriteItems)
    }
    
    public func assertBackButtonExists() {
        XCTAssert(app.buttons["Newsfeed"].exists)
    }
    
    public func assertBackButtonDoesNotExist() {
        XCTAssert(!app.buttons["Newsfeed"].exists)
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
    
    public func assertNonBlockingErrorExists() {
        XCTAssert(app.staticTexts[NonBlockingErrorTitle].exists)
        XCTAssert(app.staticTexts[NonBlockingErrorSubtitle].exists)
        XCTAssert(app.buttons[NonBlockingErrorAction].exists)
    }
    
    public func assertNonBlockingErrorDoesNotExist() {
        XCTAssert(!app.staticTexts[NonBlockingErrorTitle].exists)
        XCTAssert(!app.staticTexts[NonBlockingErrorSubtitle].exists)
        XCTAssert(!app.buttons[NonBlockingErrorAction].exists)
    }
    
    public func clickPost(title: String) {
        app.staticTexts[title].tap()
    }
}

private let Loading = "Loading"

private let AddedToFavourites = "Added to favourites"

private let BlockingErrorTitle = "Oups!"
private let BlockingErrorSubtitle = "Something has gone wrong. Please try again."
private let BlockingErrorAction = "Try again"
private let NonBlockingErrorTitle = "Oups"
private let NonBlockingErrorSubtitle = "Something went wrong. Please try again."
private let NonBlockingErrorAction = "Retry"
