import SwiftUI
import CommonDesign

public struct Backgrounds {
    public let primary: Color
    public let surface: Color
}

func lightBackgrounds() -> Backgrounds {
    return Backgrounds(
        primary: Color.appGray4,
        surface: Color.appWhite
    )
}

func darkBackgrounds() -> Backgrounds {
    return Backgrounds(
        primary: Color.appBlack,
        surface: Color.appBlack2
    )
}
