package cavapy.api.py.continental.responses;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class SimpleApiResponse {

    private Integer statusCode;

    private String message;

}
