package red.projekt.paimon.bridge.module

interface IModule {
    /**
     * Returns the name of the module.
     */
    fun getName(): String

    /**
     * Returns the channel name will be used for the module.
     */
    fun getChannel(): String

    /**
     * Returns the origins that the module will be listening to.
     */
    fun getOrigins(): Int

    /**
     * Returns the destinations that the module will be sending to.
     */
    fun getDestinations(): Int
}