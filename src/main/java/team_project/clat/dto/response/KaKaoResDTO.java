package team_project.clat.dto.response;

import java.util.Map;

public class KaKaoResDTO implements OAuth2Response{

    private final Map<String, Object> attribute;

    public KaKaoResDTO(Map<String, Object> attribute) {

        this.attribute = attribute;
    }

    @Override
    public String getProvider() {
        return (String) attribute.get("id").toString();
    }

    @Override
    public String getProviderId() {
        return "kakao";
    }

    @Override
    public String getEmail() {
        return null;
    }

    @Override
    public String getName() {
        return (String) attribute.get("profile_nickname");
    }
}
