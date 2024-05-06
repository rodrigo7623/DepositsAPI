package cavapy.api.py.continental.responses;

import cavapy.api.py.continental.util.BankAccount;
import lombok.Data;

import java.util.List;

@Data
public class BankAccountResponse {

    private List<BankAccount> bankAccountList;

}
