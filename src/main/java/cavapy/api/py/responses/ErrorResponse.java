package cavapy.api.py.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class ErrorResponse {

    private String error;

    @JsonProperty("error_description")
    private String errorDescription;
}
