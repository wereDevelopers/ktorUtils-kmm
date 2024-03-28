package di

import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier
import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.plugins.api.Send
import io.ktor.client.plugins.api.createClientPlugin
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import org.koin.dsl.module
import utils.DeviceHeaderParams
import utils.NetworkPreferences

@OptIn(ExperimentalSerializationApi::class)
fun networkKMPModule(networkPreferences: NetworkPreferences?, deviceHeaderParams: DeviceHeaderParams?) = module {
    //network
    single {
        //Client.http
        HttpClient(configureEngineForPlatform(networkPreferences?.disableSSLCheks ?: true)) { // CIO error in ios device tls sessions are not supported on native platform
            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                    useAlternativeNames = true
                    isLenient = true
                    prettyPrint = true
                    explicitNulls = false
                    encodeDefaults = true
                })
            }
            install(createClientPlugin("fix") {
                on(Send) { request ->
                    request.headers.remove("Accept-Charset")
                    this.proceed(request)
                }
            })
            install(Logging){
                logger = DebugKtorLogger()
                level = networkPreferences?.logLevel?.toLogLevel() ?: LogLevel.NONE
            }.also { Napier.base(DebugAntilog()) }
        }
    }

    single<DeviceHeaderParams> {
        deviceHeaderParams ?: DeviceHeaderParams()
    }
}

expect fun configureEngineForPlatform(disableSSLCheks: Boolean): HttpClientEngine

class DebugKtorLogger : Logger {
    override fun log(message: String) {
        Napier.i("Ktor: $message")
    }
}

