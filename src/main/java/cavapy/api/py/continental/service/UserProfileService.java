package cavapy.api.py.continental.service;

import cavapy.api.py.continental.enums.SystemProfileType;
import cavapy.api.py.continental.model.ApiResponse;
import cavapy.api.py.continental.model.BankType;
import cavapy.api.py.continental.model.SystemProfile;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class UserProfileService {

    @Value("${cavapy.core.user.profile.url}")
    private String CORE_URL;

    @Autowired
    private RestTemplate restTemplate;

    ObjectMapper objectMapper = new ObjectMapper();

    public boolean hasRequiredProfile(String username) {

        String urlWithParam = CORE_URL.replace("?", username);
        ApiResponse<SystemProfile> response = restTemplate.getForObject(urlWithParam, ApiResponse.class);
        if (response.getCode() == HttpStatus.OK.value()) {

            SystemProfile systemProfile = objectMapper.convertValue(response.getData(), SystemProfile.class);
            if (systemProfile.getIdSystemProfile().equals(SystemProfileType.ADMINISTRACION_Y_FINANZAS_TESORERIA.getCode())
            && systemProfile.getProfileName().equalsIgnoreCase(SystemProfileType.ADMINISTRACION_Y_FINANZAS_TESORERIA.getValue())) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }
}
