package cavapy.api.py.model;

import lombok.Data;

@Data
public class BuscarRequest {

    String selectedAccount;

    String startDate;

    String endDate;
}
