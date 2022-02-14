package red.projekt.paimon.bridge;

import com.google.gson.annotations.SerializedName;

public class PaimonModule {
    /**
     * The origin of the message.
     */
    @SerializedName("origins")
    public int messageOrigins;

    /**
     * The destination of the message. (Group ID or null when sending directly to the bot)
     */
    @SerializedName("destinations")
    public int messageDestinations;

    /**
     * The name of the module.
     */
    @SerializedName("name")
    public String moduleName;

    /**
     * The channel will be used by the module.
     */
    @SerializedName("channel")
    public String moduleChannel;
}
