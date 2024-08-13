package cavapy.api.py.continental.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
public class ApiResponse <T>{

    private boolean success;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String message;

    private int code;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private T data;
}
