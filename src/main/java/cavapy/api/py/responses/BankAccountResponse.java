package cavapy.api.py.responses;

import cavapy.api.py.util.BankAccount;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class BankAccountResponse {

    private List<BankAccount> bankAccountList;

}
