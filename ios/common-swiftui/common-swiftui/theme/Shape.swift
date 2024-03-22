import SwiftUI

public struct Shapes {
    public let surface: RoundedCorner
    public let groupStart: RoundedCorner
    public let groupMiddle: RoundedCorner
    public let groupEnd: RoundedCorner
    public let groupSingle: RoundedCorner
}

func shapes() -> Shapes {
    return Shapes(
        surface: RoundedCorner(radius: CornerRadius),
        groupStart: RoundedCorner(
            radius: CornerRadius,
            corners: [.topLeft, .topRight]
        ),
        groupMiddle: RoundedCorner(radius: 0),
        groupEnd: RoundedCorner(
            radius: CornerRadius,
            corners: [.bottomLeft, .bottomRight]
        ),
        groupSingle: RoundedCorner(radius: CornerRadius)
    )
}

public struct RoundedCorner: Shape {
    fileprivate var radius: CGFloat = .infinity
    fileprivate var corners: UIRectCorner = .allCorners
    
    public func path(in rect: CGRect) -> Path {
        let path = UIBezierPath(
            roundedRect: rect,
            byRoundingCorners: corners,
            cornerRadii: CGSize(width: radius, height: radius)
        )
        return Path(path.cgPath)
    }
}

private let CornerRadius = 8.0
