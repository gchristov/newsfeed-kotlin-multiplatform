import Foundation

public class Theme: ObservableObject {
    @Published public var type: ThemeType
    public lazy var backgrounds: Backgrounds = type.backgrounds()
    public lazy var contentColors: ContentColors = type.contentColors()
    public lazy var typography: Typography = type.typography()
    public lazy var shapes: Shapes = type.shapes()
    
    public init(type: ThemeType) {
        self.type = type
    }
}

public enum ThemeType {
    case dark
    case light
    
    func backgrounds() -> Backgrounds {
        switch self {
        case .dark: return darkBackgrounds()
        case .light: return lightBackgrounds()
        }
    }
    
    func contentColors() -> ContentColors {
        switch self {
        case .dark: return darkContentColors()
        case .light: return lightContentColors()
        }
    }
    
    func typography() -> Typography {
        return CommonSwiftUi.typography()
    }
    
    func shapes() -> Shapes {
        return CommonSwiftUi.shapes()
    }
}
