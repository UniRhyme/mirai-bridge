package red.projekt.paimon.bridge.model

import com.google.gson.annotations.SerializedName

object MessageModel {
    @JvmField
    var origin = 0
    @JvmField
    var mentioned = false
    @JvmField
    @SerializedName("id")
    var identify: String? = null
    @JvmField
    var message: String? = null
}