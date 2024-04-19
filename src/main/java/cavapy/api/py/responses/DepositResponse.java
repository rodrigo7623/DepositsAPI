package cavapy.api.py.responses;

import cavapy.api.py.util.CorrectDeposit;
import cavapy.api.py.util.Deposit;
import cavapy.api.py.util.IncorrectDeposit;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class DepositResponse {

    private Deposit [] correctDeposit;

    private IncorrectDeposit [] incorrectDeposit;
}
