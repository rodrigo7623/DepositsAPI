package cavapy.api.py.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class AccessTokenResponse {

    @JsonProperty("access_token")
    private String accessToken;

    @JsonProperty("expires_In")
    private Integer expiresIn;

    @JsonProperty("refresh_Expires_In")
    private Integer refreshExpiresIn;

    @JsonProperty("refresh_Token")
    private String refreshToken;

    @JsonProperty("token_Type")
    private String tokenType;

    @JsonProperty("id_Token")
    private String idToken;

    @JsonProperty("not_Before_Policy")
    private Integer notBeforePolicy;

    @JsonProperty("session_State")
    private String sessionState;

    @JsonProperty("scope")
    private String scope;
}