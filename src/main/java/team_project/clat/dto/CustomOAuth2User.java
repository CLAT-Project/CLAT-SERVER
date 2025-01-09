package team_project.clat.dto;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;
import team_project.clat.dto.request.OAuthMemberReqDTO;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

public class CustomOAuth2User implements OAuth2User {

    private final OAuthMemberReqDTO oAuthMemberReqDTO;

    public CustomOAuth2User(OAuthMemberReqDTO oAuthMemberReqDTO) {
        this.oAuthMemberReqDTO = oAuthMemberReqDTO;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return null;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> collection = new ArrayList<>();

        collection.add(new GrantedAuthority() {

            @Override
            public String getAuthority() {

                return oAuthMemberReqDTO.getRole();
            }
        });

        return collection;
    }

    @Override
    public String getName() {
        return oAuthMemberReqDTO.getName();
    }

    public String getUsername() {

        return oAuthMemberReqDTO.getUsername();
    }
}
