package team_project.clat.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import team_project.clat.domain.Member;
import team_project.clat.dto.CustomOAuth2User;
import team_project.clat.dto.request.OAuthMemberReqDTO;
import team_project.clat.dto.response.GoogleResDTO;
import team_project.clat.dto.response.KaKaoResDTO;
import team_project.clat.dto.response.NaverResDTO;
import team_project.clat.dto.response.OAuth2Response;
import team_project.clat.repository.MemberRepository;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final MemberRepository memberRepository;

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
        else if (registrationId.equals("kakao")){

            oAuth2Response = new KaKaoResDTO(oAuth2User.getAttributes());
        }else {
            return null;
        }

        //리소스 서버에서 발급 받은 정보로 사용자를 특정할 아이디값을 만듬
        String username = oAuth2Response.getProvider()+" "+oAuth2Response.getProviderId();
        log.info("OAuth2 getProvider : {}", oAuth2Response.getProvider());
        log.info("OAuth2 getProviderId : {}", oAuth2Response.getProviderId());
        log.info("OAuth2 username : {}", username);
        Member existMember = memberRepository.findByUsername(username);

        if(existMember == null){
            /*Member member = Member.builder()
                    .username(username)
                    .email(oAuth2Response.getEmail())
                    .name(oAuth2Response.getName()).build();
            memberRepository.save(member);*/

            OAuthMemberReqDTO authMemberReqDTO = new OAuthMemberReqDTO();
            authMemberReqDTO.setUsername(username);
            authMemberReqDTO.setName(oAuth2Response.getName());
            if(oAuth2Response.getEmail()!=null) authMemberReqDTO.setEmail(oAuth2Response.getEmail());
            authMemberReqDTO.setExistFlag("no");

            log.info("authMemberReqDTO.getUsername() {}", authMemberReqDTO.getUsername());

            return new CustomOAuth2User(authMemberReqDTO);
        }else {

            existMember.memberNameSet(oAuth2Response.getName());
            existMember.memberEmailSet(oAuth2Response.getEmail());

            memberRepository.save(existMember);

            OAuthMemberReqDTO authMemberReqDTO = new OAuthMemberReqDTO();
            authMemberReqDTO.setUsername(existMember.getUsername());
            authMemberReqDTO.setName(oAuth2Response.getName());
            authMemberReqDTO.setEmail(oAuth2Response.getEmail());
            authMemberReqDTO.setExistFlag("yes");

            return new CustomOAuth2User(authMemberReqDTO);

        }
    }
}
