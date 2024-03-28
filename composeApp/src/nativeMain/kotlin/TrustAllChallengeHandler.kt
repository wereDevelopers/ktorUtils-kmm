
import io.github.aakira.napier.Napier
import io.ktor.client.engine.darwin.ChallengeHandler
import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.NSURLAuthenticationChallenge
import platform.Foundation.NSURLAuthenticationMethodServerTrust
import platform.Foundation.NSURLCredential
import platform.Foundation.NSURLSession
import platform.Foundation.NSURLSessionAuthChallengeCancelAuthenticationChallenge
import platform.Foundation.NSURLSessionAuthChallengeDisposition
import platform.Foundation.NSURLSessionAuthChallengePerformDefaultHandling
import platform.Foundation.NSURLSessionAuthChallengeUseCredential
import platform.Foundation.NSURLSessionTask
import platform.Foundation.credentialForTrust
import platform.Foundation.serverTrust
import platform.Security.SecTrustCopyCertificateChain
import platform.Security.SecTrustRef
import platform.Security.SecTrustSetAnchorCertificates


/**
 * Challenge handler which trusts whatever certificate the server presents
 * This needs to be used in combination with plist additions:
 * <code>
 *     <key>NSAppTransportSecurity</key>
 *     <dict>
 *         <key>NSExceptionDomains</key>
 *         <dict>
 *             <key>example.com</key>
 *             <dict>
 *                 <key>NSExceptionAllowsInsecureHTTPLoads</key>
 *                 <true/>
 *             </dict>
 *         </dict>
 *     </dict>
 * </code>
 * Supporting links:
 * - https://developer.apple.com/documentation/bundleresources/information_property_list/nsexceptionallowsinsecurehttploads
 * - https://developer.apple.com/documentation/foundation/url_loading_system/handling_an_authentication_challenge/performing_manual_server_trust_authentication
 */

internal class TrustAllChallengeHandler : ChallengeHandler {


    @OptIn(ExperimentalForeignApi::class)
    override fun invoke(
        session: NSURLSession,
        task: NSURLSessionTask,
        challenge: NSURLAuthenticationChallenge,
        completionHandler: (NSURLSessionAuthChallengeDisposition, NSURLCredential?) -> Unit,
    ) {
        Napier.d { "TrustAllChallengeHandler start" }
        // Check that we want to handle this kind of challenge
        val protectionSpace = challenge.protectionSpace
        if (protectionSpace.authenticationMethod != NSURLAuthenticationMethodServerTrust) {
            // Not a 'NSURLAuthenticationMethodServerTrust', default handling...
            completionHandler(NSURLSessionAuthChallengePerformDefaultHandling, null)
            Napier.d { "TrustAllChallengeHandler protectionSpace.authenticationMethod != NSURLAuthenticationMethodServerTrust" }

        }

        val serverTrust = challenge.protectionSpace.serverTrust
        if (serverTrust == null) {
            Napier.d { "TrustAllChallengeHandler serverTrust" }

            // Server trust is null, default handling...
            completionHandler(NSURLSessionAuthChallengePerformDefaultHandling, null)
        }

        // Get the servers certs
        val certChain = SecTrustCopyCertificateChain(serverTrust)
        // Set those certs as trusted anchors
        SecTrustSetAnchorCertificates(serverTrust, certChain)

        if (serverTrust?.trustIsValid() == true) {
            Napier.d { "TrustAllChallengeHandler serverTrust?.trustIsValid() == true" }

            // ✔ Server trust is valid, continue...
            val credential = NSURLCredential.credentialForTrust(serverTrust)
            completionHandler(NSURLSessionAuthChallengeUseCredential, credential)
        } else {
            Napier.d { "TrustAllChallengeHandler else" }

            // ✖ Server trust not valid, cancel challenge...
            completionHandler(NSURLSessionAuthChallengeCancelAuthenticationChallenge, null)
        }
    }
}


//
//            handleChallenge { session, task, challenge, completionHandler ->
//                // Check that we want to handle this kind of challenge
//                val protectionSpace = challenge.protectionSpace
//                if (protectionSpace.authenticationMethod != NSURLAuthenticationMethodServerTrust) {
//                    // Not a 'NSURLAuthenticationMethodServerTrust', default handling...
//                    completionHandler(NSURLSessionAuthChallengeDisposition, null)
//                   // completionHandler(NSURLSessionAuthChallengePerformDefaultHandling, null)
//                }
//                else
//                {
//                    val serverTrust = challenge.protectionSpace.serverTrust
//                    if (serverTrust == null) {
//                        // Server trust is null, default handling...
//                        completionHandler(NSURLSessionAuthChallengePerformDefaultHandling, null)
//                    }
//                    else
//                    {
//
//                        // Get the servers certs
//                        val certChain = SecTrustCopyCertificateChain(serverTrust)
//                        // Set those certs as trusted anchors
//                        SecTrustSetAnchorCertificates(serverTrust, certChain)
//
//                        if (serverTrust.trustIsValid()) {
//                            // ✔ Server trust is valid, continue...
//                            val credential = NSURLCredential.credentialForTrust(serverTrust)
//                            completionHandler(NSURLSessionAuthChallengeUseCredential, credential)
//                        } else {
//                            // ✖ Server trust not valid, cancel challenge...
//                            completionHandler(NSURLSessionAuthChallengeCancelAuthenticationChallenge, null)
//                        }
//                    }
//                }
//            }
//        }
//    }
//}
//
/**
 * Evaluates trust for the specified certificate and policies.
 */
@OptIn(ExperimentalForeignApi::class)
public fun SecTrustRef.trustIsValid(): Boolean {
    var isValid = true
//
//    val version = cValue<NSOperatingSystemVersion> {
//        majorVersion = 12
//        minorVersion = 0
//        patchVersion = 0
//    }
//    if (NSProcessInfo().isOperatingSystemAtLeastVersion(version)) {
//        memScoped {
//            val result = alloc<CFErrorRefVar>()
//            // https://developer.apple.com/documentation/security/2980705-sectrustevaluatewitherror
//            isValid = SecTrustEvaluateWithError(this@trustIsValid, result.ptr)
//        }
//    } else {
//        // https://developer.apple.com/documentation/security/1394363-sectrustevaluate
//        memScoped {
//            val result = alloc<SecTrustResultTypeVar>()
//            result.value = platform.Security.kSecTrustResultInvalid
//            val status = SecTrustEvaluate(this@trustIsValid, result.ptr)
//            if (status == platform.Security.errSecSuccess) {
//                isValid = result.value == platform.Security.kSecTrustResultUnspecified ||
//                        result.value == platform.Security.kSecTrustResultProceed
//            }
//        }
//    }

    return isValid
}