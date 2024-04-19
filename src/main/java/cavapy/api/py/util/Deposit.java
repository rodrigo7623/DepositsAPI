package cavapy.api.py.util;

import lombok.Data;

@Data
public class Deposit {

    private String ruc;

    private String currency;

    private String accountNumber;

    private String operationAmount;
}
