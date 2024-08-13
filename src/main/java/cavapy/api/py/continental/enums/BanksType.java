package cavapy.api.py.continental.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum BanksType {

    CONTINENTAL(1, "BANCO CONTINENTAL SAECA");

    private final Integer idBank;

    private final String bankName;

}
