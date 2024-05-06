package cavapy.api.py.continental.util;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Messages {

    JSON_PROCESSING_ERROR("JSON error", "Error processing JSON request"),

    GATEWAY_TIME_OUT("Internal Server Error", "Gateway Time-out"),

    BANK_ACCOUNT_EMPTY("Bank Account Empty", "The Bank Account List is empty"),

    MOVEMENTS_EMPTY("Movements Empty", "The Movements List is empty");

    private final String error;

    private final String errorDescription;
}
