import SwiftUI

public struct AppButton: View {
    @EnvironmentObject var theme: Theme
    private let text: String
    private let onClick: () -> ()
    
    public init(
        text: String,
        onClick: @escaping () -> ()
    ) {
        self.text = text
        self.onClick = onClick
    }
    
    public var body: some View {
        Button {
            onClick()
        } label: {
            AppText(
                text: text,
                color: Color.appWhite
            )
        }
        .padding(.top, 8)
        .padding(.bottom, 8)
        .padding(.leading, 16)
        .padding(.trailing, 16)
        .background(theme.contentColors.action)
        .cornerRadius(3)
        .shadow(radius: 2, x: 0.2, y: 0.5)
    }
}

public struct AppIconButton: View {
    @EnvironmentObject var theme: Theme
    private let imageName: String
    private let tint: Color?
    private let contentDescription: String
    private let onClick: () -> ()
    
    public init(
        imageName: String,
        tint: Color? = nil,
        contentDescription: String,
        onClick: @escaping () -> ()
    ) {
        self.imageName = imageName
        self.tint = tint
        self.contentDescription = contentDescription
        self.onClick = onClick
    }
    
    public var body: some View {
        Button {
            onClick()
        } label: {
            AppIcon(
                imageName: imageName,
                tint: tint
            )
        }
        .accessibilityLabel(contentDescription)
    }
}

public struct AppTextButton: View {
    @EnvironmentObject var theme: Theme
    private let text: String
    private let onClick: () -> ()
    
    public init(
        text: String,
        onClick: @escaping () -> ()
    ) {
        self.text = text
        self.onClick = onClick
    }
    
    public var body: some View {
        Button {
            onClick()
        } label: {
            AppText(
                text: text,
                color: theme.contentColors.action
            )
        }
        .padding(.top, 8)
        .padding(.bottom, 8)
    }
}
