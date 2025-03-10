import SwiftUI
import CommonDesign

public struct ContentColors {
    public let action: Color
    public let primary: Color
    public let secondary: Color
    public let warning: Color
}

func lightContentColors() -> ContentColors {
    return ContentColors(
        action: Color.appRed,
        primary: Color.appBlack,
        secondary: Color.appBlack3,
        warning: Color.appYellow
    )
}

func darkContentColors() -> ContentColors {
    return ContentColors(
        action: Color.appRed,
        primary: Color.appWhite,
        secondary: Color.appGray3,
        warning: Color.appYellow
    )
}
