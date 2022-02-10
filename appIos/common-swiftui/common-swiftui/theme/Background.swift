import SwiftUI
import CommonDesign

public struct Backgrounds {
    public let primary: Color
    public let surface: Color
}

func lightBackgrounds() -> Backgrounds {
    return Backgrounds(
        primary: Color.appWhite,
        surface: Color.appWhite
    )
}

func darkBackgrounds() -> Backgrounds {
    return Backgrounds(
        primary: Color.appBlack,
        surface: Color.appBlack2
    )
}
