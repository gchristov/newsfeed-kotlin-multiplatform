import SwiftUI

public extension Color {
    static let appRed = Color("red", bundle: bundle)
    static let appRed2 = Color("red_2", bundle: bundle)
    static let appRed3 = Color("red_3", bundle: bundle)
    
    static let appWhite = Color("white", bundle: bundle)
    
    static let appYellow = Color("yellow", bundle: bundle)
    static let appYellow2 = Color("yellow_2", bundle: bundle)
    static let appYellow3 = Color("yellow_3", bundle: bundle)
    static let appYellow4 = Color("yellow_4", bundle: bundle)
    static let appYellow5 = Color("yellow_5", bundle: bundle)
    
    static let appBlack = Color("black", bundle: bundle)
    static let appBlack2 = Color("black_2", bundle: bundle)
    static let appBlack3 = Color("black_3", bundle: bundle)
    
    static let appGray = Color("gray", bundle: bundle)
    static let appGray2 = Color("gray_2", bundle: bundle)
    static let appGray3 = Color("gray_3", bundle: bundle)
    
    static let appBlue = Color("blue", bundle: bundle)
    static let appBlue2 = Color("blue_2", bundle: bundle)
    static let appBlue3 = Color("blue_3", bundle: bundle)
    static let appBlue4 = Color("blue_4", bundle: bundle)
    static let appBlue5 = Color("blue_5", bundle: bundle)
    static let appBlue6 = Color("blue_6", bundle: bundle)
    
    static let appGreen = Color("green", bundle: bundle)
    static let appGreen2 = Color("green_2", bundle: bundle)
    static let appGreen3 = Color("green_3", bundle: bundle)
    static let appGreen4 = Color("green_4", bundle: bundle)
    
    static let appBrown = Color("brown", bundle: bundle)
    
    private static var bundle: Bundle {
        class __ { }
        return Bundle(for: __.self)
    }
}
