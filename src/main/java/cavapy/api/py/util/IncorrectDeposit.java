package cavapy.api.py.util;

import lombok.Data;

@Data
public class IncorrectDeposit extends Deposit {

    private String errorMessage;

}
