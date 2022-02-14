package red.projekt.paimon.bridge.module

interface IModule {
    fun getName(): String
    fun getChannel(): String
    fun getOrigins(): Int
    fun getDestinations(): Int
}