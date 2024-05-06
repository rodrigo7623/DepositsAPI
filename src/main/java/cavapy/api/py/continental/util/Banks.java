package cavapy.api.py.continental.util;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Banks {

    CONTINENTAL(1, "BANCO CONTINENTAL SAECA");

    private final Integer idBank;

    private final String bankName;

}
