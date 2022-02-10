import SwiftUI
import LetterAvatarKit

public struct AppAvatar: View {
    private let name: String
    private let size: CGSize
    
    public init(
        name: String,
        size: CGSize
    ) {
        self.name = name
        self.size = size
    }
    
    public var body: some View {
        let avatar = LetterAvatarMaker()
            .setCircle(true)
            .setUsername(name)
            .useSingleLetter(true)
            .setSize(size)
            .build() ?? UIImage()
        
        Image(uiImage: avatar)
    }
}
