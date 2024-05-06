package cavapy.api.py.continental.responses;

import cavapy.api.py.continental.util.Deposit;
import cavapy.api.py.continental.util.IncorrectDeposit;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class DepositResponse {

    private Deposit[] correctDeposit;

    private IncorrectDeposit [] incorrectDeposit;
}
