package cavapy.api.py.continental.model;

import lombok.Data;

@Data
public class BuscarRequest {

    String selectedAccount;

    String startDate;

    String endDate;
}
