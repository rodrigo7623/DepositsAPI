package cavapy.api.py.responses;

import lombok.*;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ApiResponse {

    private Integer statusCode;

    private String message;

}
