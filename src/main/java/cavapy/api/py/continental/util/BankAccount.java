package cavapy.api.py.continental.util;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class BankAccount {

    @JsonProperty("numero_Cuenta")
    private String numeroCuenta;

    @JsonProperty("denominacion")
    private String denominacion;

    @JsonProperty("moneda")
    private String moneda;

    @JsonProperty("tipo")
    private String tipo;

    @JsonProperty("hash")
    private String hash;
}
