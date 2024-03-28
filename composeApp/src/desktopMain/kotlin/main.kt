import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.runtime.Composable
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import di.initKoin

fun main() = application {
    Window(onCloseRequest = ::exitApplication, title = "AppDemo") {
        //window.minimumSize = Dimension(800, 700)
        initKoin()
        App()
    }
}

@Preview
@Composable
fun AppDesktopPreview() {
    App()
}