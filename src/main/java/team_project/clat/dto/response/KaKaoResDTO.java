package team_project.clat.dto.response;

import java.util.Map;

public class KaKaoResDTO implements OAuth2Response{

    private final Map<String, Object> attribute;

    public KaKaoResDTO(Map<String, Object> attribute) {

        this.attribute = attribute;
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
        return (String) attribute.get("email");
    }

    @Override
    public String getName() {
        return (String) attribute.get("profile_nickname");
    }
}
