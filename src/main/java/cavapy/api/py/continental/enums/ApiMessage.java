package cavapy.api.py.continental.enums;

public enum ApiMessage {

    USER_DOES_NOT_HAVE_REQUIRED_PROFILE(1, "User does not have the required profile");

    ApiMessage(Integer code, String value) {
        this.code = code;
        this.value = value;
    }

    private final Integer code;

    private final String value;

    public Integer getCode() {
        return code;
    }

    public String getValue() {
        return value;
    }
}
