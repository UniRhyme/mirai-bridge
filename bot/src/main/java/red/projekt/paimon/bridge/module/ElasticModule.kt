package red.projekt.paimon.bridge.module

import org.jetbrains.annotations.Nullable
import red.projekt.paimon.bridge.TargetType

sealed class ElasticModule(name: String, @Nullable channel: String?, @Nullable origins: Int?, @Nullable destinations: Int?): IModule {
    private var name: String
    private var channel: String
    private var origins: Int
    private var destinations: Int

    init {
        this.name = name
        this.channel = channel ?: "paimon"
        this.origins = origins ?: TargetType.NONE.type
        this.destinations = destinations ?: TargetType.NONE.type
    }

    override fun getName(): String {
        return name
    }

    override fun getChannel(): String {
        return channel
    }

    override fun getOrigins(): Int {
        return origins
    }

    override fun getDestinations(): Int {
        return destinations
    }

}
