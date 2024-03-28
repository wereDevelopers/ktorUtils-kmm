package utils


import io.ktor.client.plugins.logging.LogLevel
import org.koin.core.component.KoinComponent
import kotlin.native.concurrent.ThreadLocal

@ThreadLocal
object AppManager: KoinComponent {
    val libraryVersion = "1.0.0"
}

data class DeviceHeaderParams(
    val bundleID : String="", // package
    val deviceID : String = "", // androidID
    val platform : String = "", //iOS o Android
    val OSVersion : String = "", // android 14, iOS 17
    val model : String = "",  //Samsung xxx
)

public enum class LogLevelRequest {
    ALL,
    HEADERS,
    BODY,
    INFO,
    NONE;

    fun toLogLevel(): LogLevel {
        return when(this) {
            ALL -> LogLevel.ALL
            HEADERS -> LogLevel.HEADERS
            BODY -> LogLevel.BODY
            INFO -> LogLevel.INFO
            NONE -> LogLevel.NONE
        }
    }
}

data class NetworkPreferences(
    val logLevel: LogLevelRequest = LogLevelRequest.ALL,
    val disableSSLCheks: Boolean = false
)