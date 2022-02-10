import SwiftUI

public struct BlockingError {
    let title: String
    let subtitle: String
    let retry: String
    
    public init(
        title: String = "Oups!",
        subtitle: String = "Something has gone wrong. Please try again.",
        retry: String = "Try again"
    ) {
        self.title = title
        self.subtitle = subtitle
        self.retry = retry
    }
}

public struct NonBlockingError {
    let title: String
    let subtitle: String
    let retry: String
    
    public init(
        title: String = "Oups",
        subtitle: String = "Something went wrong. Please try again.",
        retry: String = "Retry"
    ) {
        self.title = title
        self.subtitle = subtitle
        self.retry = retry
    }
}

public struct AppBlockingErrorView: View {
    @EnvironmentObject var theme: Theme
    private let blockingError: BlockingError
    private let onRetry: () -> ()
    
    public init(
        blockingError: BlockingError,
        onRetry: @escaping () -> ()
    ) {
        self.blockingError = blockingError
        self.onRetry = onRetry
    }
    
    public var body: some View {
        GeometryReader { metrics in
            VStack {
                Spacer()
                HStack {
                    Spacer()
                    VStack(
                        alignment: .center,
                        spacing: 24
                    ) {
                        VStack(spacing: 4) {
                            AppText(
                                text: blockingError.title,
                                font: theme.typography.title
                            )
                            AppText(
                                text: blockingError.subtitle,
                                color: theme.contentColors.secondary,
                                font: theme.typography.subtitle,
                                textAlignment: .center
                            )
                        }
                        AppButton(
                            text: blockingError.retry,
                            onClick: onRetry
                        )
                    }.frame(maxWidth: metrics.size.width * 0.5)
                    Spacer()
                }
                Spacer()
            }
        }
    }
}

public struct AppNonBlockingErrorView: View {
    @EnvironmentObject var theme: Theme
    private let nonBlockingError: NonBlockingError
    private let onRetry: () -> ()
    private let onDismiss: () -> ()
    
    public init(
        nonBlockingError: NonBlockingError,
        onRetry: @escaping () -> (),
        onDismiss: @escaping () -> ()
    ) {
        self.nonBlockingError = nonBlockingError
        self.onRetry = onRetry
        self.onDismiss = onDismiss
    }
    
    public var body: some View {
        VStack {
            Spacer()
            AppSurface {
                HStack(alignment: .top) {
                    AppIcon(
                        imageName: "exclamationmark.triangle.fill",
                        tint: theme.contentColors.warning
                    )
                        .padding(.top, 4)
                        .padding(.trailing, 8)
                    VStack(
                        alignment: .leading,
                        spacing: 2
                    ) {
                        AppText(
                            text: nonBlockingError.title,
                            font: theme.typography.title
                        )
                        AppText(
                            text: nonBlockingError.subtitle,
                            color: theme.contentColors.secondary,
                            font: theme.typography.subtitle
                        )
                        AppTextButton(
                            text: nonBlockingError.retry,
                            onClick: onRetry
                        ).padding(.top, 8)
                    }
                    Spacer()
                    AppIconButton(
                        imageName: "xmark",
                        tint: theme.contentColors.action,
                        contentDescription: "Close"
                    ) {
                        onDismiss()
                    }.padding(.top, 4)
                }
            }
        }.padding(8)
    }
}

struct Error_Previews: PreviewProvider {
    static var previews: some View {
        //        BlockingErrorView(
        //            blockingError: BlockingError(),
        //            onRetry: {}
        //        )
        AppNonBlockingErrorView(
            nonBlockingError: NonBlockingError(),
            onRetry: {},
            onDismiss: {}
        )
    }
}
