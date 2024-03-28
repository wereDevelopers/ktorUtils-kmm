import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier
import kotlinx.coroutines.delay
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import utils.Test


@Composable
fun App() {
    Napier.base(DebugAntilog())

    MaterialTheme {
        DemoContent()
    }
}

@OptIn(ExperimentalResourceApi::class)
@Composable
fun DemoContent(){
    var showContent by remember { mutableStateOf(false) }
    val greeting = remember { Greeting().greet() }
    var txt1 by remember { mutableStateOf("loading...") }
    Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
        Button(onClick = { showContent = !showContent }) {
            Text("Click me!")
        }
        AnimatedVisibility(showContent) {
            Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
                Image(painterResource("compose-multiplatform.xml"), null, modifier = Modifier.size(100.dp))
                Text("Compose: $greeting")
                Spacer(modifier = Modifier.padding(8.dp))
                Text("result: $txt1")
            }

            LaunchedEffect(null){
                delay(2000)
                txt1 = Test.remoteString("https://jsonplaceholder.typicode.com/todos/1")
            }
        }
    }
}