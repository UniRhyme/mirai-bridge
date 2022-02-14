package red.projekt.paimon.bridge.model

object ResponseModel: IModel {
    @JvmField
    var code: Int = 0
    @JvmField
    var message: String? = null
}