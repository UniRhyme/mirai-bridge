package red.projekt.paimon.bridge.model

import com.google.gson.annotations.SerializedName

object ResponseModel: IModel {
    @JvmField
    var code: Int = 0
    @JvmField
    var message: String? = null
    @JvmField
    var destination: Int = 0
    @JvmField
    @SerializedName("id")
    var destinationId: String? = null
}