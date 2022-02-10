import SwiftUI

public struct Typography {
    public let title: Font
    public let subtitle: Font
    public let body: Font
    public let caption: Font
}

func typography() -> Typography {
    return Typography(
        title: Font.system(size: 24),
        subtitle: Font.system(size: 15),
        body: Font.system(size: 17),
        caption: Font.system(size: 12)
    )
}
