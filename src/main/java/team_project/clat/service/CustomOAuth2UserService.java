package team_project.clat.service;

import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import team_project.clat.dto.CustomOAuth2User;
import team_project.clat.dto.request.OAuthMemberReqDTO;
import team_project.clat.dto.response.GoogleResDTO;
import team_project.clat.dto.response.NaverResDTO;
import team_project.clat.dto.response.OAuth2Response;

public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        OAuth2User oAuth2User = super.loadUser(userRequest);

        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        OAuth2Response oAuth2Response = null;
        if (registrationId.equals("naver")) {

            oAuth2Response = new NaverResDTO(oAuth2User.getAttributes());
        }
        else if (registrationId.equals("google")) {

            oAuth2Response = new GoogleResDTO(oAuth2User.getAttributes());
        }
        else {

            return null;
        }

        //리소스 서버에서 발급 받은 정보로 사용자를 특정할 아이디값을 만듬
        String username = oAuth2Response.getProvider()+" "+oAuth2Response.getProviderId();

        OAuthMemberReqDTO authMemberReqDTO = new OAuthMemberReqDTO();
        authMemberReqDTO.setUsername(username);
        authMemberReqDTO.setName(oAuth2Response.getName());
        authMemberReqDTO.setRole("ROLE_USER");

        return new CustomOAuth2User(authMemberReqDTO);
    }
}
