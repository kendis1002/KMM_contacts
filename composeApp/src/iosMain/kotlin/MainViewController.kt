import androidx.compose.ui.window.ComposeUIViewController
import di.AppModule
import platform.UIKit.UIScreen
import platform.UIKit.UIUserInterfaceStyle

fun MainViewController() = ComposeUIViewController {
    val isDarkTheme =
        UIScreen.mainScreen.traitCollection.userInterfaceStyle == UIUserInterfaceStyle.UIUserInterfaceStyleDark
    App(darkTheme = isDarkTheme, dynamicColor = false, appModule = AppModule())
}
