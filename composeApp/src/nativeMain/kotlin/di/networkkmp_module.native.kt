package di

import TrustAllChallengeHandler
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.darwin.ChallengeHandler
import io.ktor.client.engine.darwin.Darwin

actual fun configureEngineForPlatform(disableSSLCheks: Boolean): HttpClientEngine {
    return Darwin.create(){
        //this as DarwinClientEngineConfig
        if (disableSSLCheks) {
            handleChallenge(TrustAllChallengeHandler() as ChallengeHandler)
        }
    }
}