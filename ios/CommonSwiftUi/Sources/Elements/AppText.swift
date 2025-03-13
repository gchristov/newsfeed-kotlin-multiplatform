import SwiftUI

public struct AppText: View {
    @EnvironmentObject var theme: Theme
    private let text: String
    private let color: Color?
    private let font: Font?
    private let lineLimit: Int?
    private let textAlignment: TextAlignment
    
    public init(
        text: String,
        color: Color? = nil,
        font: Font? = nil,
        lineLimit: Int? = nil,
        textAlignment: TextAlignment = .leading
    ) {
        self.text = text
        self.color = color
        self.font = font
        self.lineLimit = lineLimit
        self.textAlignment = textAlignment
    }
    
    public var body: some View {
        Text(text)
            .font(font ?? theme.typography.body)
            .foregroundColor(color ?? theme.contentColors.primary)
            .lineLimit(lineLimit)
            .multilineTextAlignment(textAlignment)
    }
}
