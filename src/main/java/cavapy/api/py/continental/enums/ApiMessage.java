package cavapy.api.py.continental.enums;

public enum ApiMessage {

    ERR_USER_DOES_NOT_HAVE_REQUIRED_PROFILE(-1000, "User does not have the required profile"),
    INF_LOGIN_SUCCESSFUL(2000, "Login successful"),

    ERR_LOGIN_FAILED(-1001, "Invalid username or password");

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
