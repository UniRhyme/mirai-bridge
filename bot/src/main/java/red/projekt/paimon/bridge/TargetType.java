package red.projekt.paimon.bridge;

public enum TargetType {
    NONE(0),
    USER(1),
    GROUP(1 << 1),
    USER_AND_GROUP(USER.type | GROUP.type);

    public int type;

    TargetType(int value) {
        this.type = value;
    }
}
