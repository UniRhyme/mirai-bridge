package red.projekt.paimon.bridge

import red.projekt.paimon.bridge.model.ResponseModel
import red.projekt.paimon.bridge.module.IModule
import red.projekt.paimon.bridge.rabbitmq.MQSubscribed

class ModuleRegistry {
    private val modules = mutableListOf<IModule>()

    fun register(module: IModule) {
        modules.add(module)
    }

    fun getModules(): List<IModule> {
        return modules
    }

    @MQSubscribed
    fun onMessage(message: ResponseModel) {

    }
}