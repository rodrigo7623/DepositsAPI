package cavapy.api.py.continental.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BankType {

    private Integer id;


    private String description;


    private String documentNumber;


    private String accountNumber;

    private String currency;
}
