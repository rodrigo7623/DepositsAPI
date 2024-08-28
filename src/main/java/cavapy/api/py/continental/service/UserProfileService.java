package cavapy.api.py.continental.service;

import cavapy.api.py.continental.enums.PermittedProfiles;
import cavapy.api.py.continental.model.UserProfile;
import cavapy.api.py.continental.util.ApiResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.logging.Logger;

@Service
public class UserProfileService {

    Logger logger = Logger.getLogger(UserProfileService.class.getName());

    @Value("${cavapy.core.user.profile.url}")
    private String USER_PROFILE_URL;

    private final RestTemplate restTemplate;

    private final ObjectMapper objectMapper = new ObjectMapper();

    public UserProfileService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }


    public boolean hasRequiredProfile(String userName) {
        String urlWithParams = USER_PROFILE_URL.replace("?", userName);
        ApiResponse<?> response = restTemplate.getForObject(urlWithParams, ApiResponse.class);
        boolean hasRequiredProfile = false;
        if (response != null && response.isSuccess()) {
            UserProfile userProfile = objectMapper.convertValue(response.getData(), UserProfile.class);
            hasRequiredProfile = verifyProfile(userProfile.getIdSystemProfile(), userProfile.getProfileName(), userProfile.getDescription());
        } else {
            logger.info(response.getMessage());
        }
        return hasRequiredProfile;
    }

    private boolean verifyProfile(Integer idSystemProfile, String profileName, String description) {
        boolean response = false;
        for (PermittedProfiles pf : PermittedProfiles.values()) {
            if (isValidPk(pf.getIdSystemProfilePk(), idSystemProfile) && isValidUserName(pf.getDescription(), profileName, description)) {
                response = true;
                break;
            }
        }
        return response;
    }

    private boolean isValidUserName(String profileDescription, String profileName, String description) {
        return profileDescription.equalsIgnoreCase(profileName) || profileDescription.equalsIgnoreCase(description);
    }

    private boolean isValidPk(Integer pk, Integer idSystemProfile) {
        return pk.equals(idSystemProfile);
    }

}
