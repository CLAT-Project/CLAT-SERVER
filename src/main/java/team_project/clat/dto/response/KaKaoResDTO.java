package team_project.clat.dto.response;

import java.util.Map;

public class KaKaoResDTO implements OAuth2Response{

    private final Map<String, Object> attribute;
    private final Map<String, Object> account;
    private final Map<String, Object> profile;
    private final String email;

    public KaKaoResDTO(Map<String, Object> attribute) {

        this.attribute = attribute;
        account = (Map<String, Object>) attribute.get("kakao_account");
        profile = (Map<String, Object>) account.get("profile");
        email = (String) account.get("email");
    }

    @Override
    public String getProvider() {
        return "kakao";
    }

    @Override
    public String getProviderId() {
        return String.valueOf(attribute.get("id"));
    }

    @Override
    public String getEmail() {
        return email;
    }

    @Override
    public String getName() {
        return String.valueOf(profile.get("nickname"));
    }
}
