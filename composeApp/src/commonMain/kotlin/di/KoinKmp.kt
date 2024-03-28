package di


import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.dsl.KoinAppDeclaration
import utils.AppManager
import utils.DeviceHeaderParams
import utils.NetworkPreferences

fun initKoin(
    deviceHeaderParams: DeviceHeaderParams? = null,
    networkPreferences: NetworkPreferences? = null,
    modulesFromNative: List<Module> = listOf(),
    appDeclaration: KoinAppDeclaration = {}): String {

    val libraryVersion = AppManager.libraryVersion

    startKoin {
        appDeclaration()

        val allModule: MutableList<Module> = mutableListOf()
        allModule.addAll(modulesFromNative)
        allModule.addAll(getKMPModules(deviceHeaderParams, networkPreferences))
        modules(allModule.map { it })
    }

    return libraryVersion
}

private fun getKMPModules(
    deviceHeaderParams: DeviceHeaderParams? = null,
    networkPreferences: NetworkPreferences? = null,
): List<Module> {
    return listOf(networkKMPModule(networkPreferences, deviceHeaderParams))
}

// called by iOS etc
fun initializeKoin(deviceHeaderParams: DeviceHeaderParams? = null,
                   networkPreferences: NetworkPreferences = NetworkPreferences()) = initKoin(deviceHeaderParams, networkPreferences)
