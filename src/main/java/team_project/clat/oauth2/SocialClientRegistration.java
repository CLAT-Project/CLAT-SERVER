package team_project.clat.oauth2;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.oidc.IdTokenClaimNames;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class SocialClientRegistration {

    @Value("${oauth2.google.client-secret}")
    String googleSecret;

    @Value("${oauth2.naver.client-secret}")
    String naverSecret;

    @Value("${oauth2.kakao.client-secret}")
    String kakaoSecret;

    public ClientRegistration naverClientRegistration() {

        return ClientRegistration.withRegistrationId("naver")
                .clientId("nTYJzjk224aAD0gGHBn9")
                .clientSecret(naverSecret)
                .redirectUri("https://clat.duckdns.org/login/oauth2/code/naver")
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .scope("name", "email")
                .authorizationUri("https://nid.naver.com/oauth2.0/authorize")
                .tokenUri("https://nid.naver.com/oauth2.0/token")
                .userInfoUri("https://openapi.naver.com/v1/nid/me")
                .userNameAttributeName("response")
                .build();
    }

    public ClientRegistration googleClientRegistration() {

        log.info("clientSecret : {}", googleSecret);

        return ClientRegistration.withRegistrationId("google")
                .clientId("52474534592-p74ookn70s1m2l1qjqvf66u63bslcglu.apps.googleusercontent.com")
                .clientSecret(googleSecret)
                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_POST)
                .redirectUri("https://clat.duckdns.org/login/oauth2/code/google")
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .scope("profile", "email")
                .authorizationUri("https://accounts.google.com/o/oauth2/v2/auth")
                .tokenUri("https://www.googleapis.com/oauth2/v4/token")
                .jwkSetUri("https://www.googleapis.com/oauth2/v3/certs")
                .issuerUri("https://accounts.google.com")
                .userInfoUri("https://www.googleapis.com/oauth2/v3/userinfo")
                .userNameAttributeName(IdTokenClaimNames.SUB)
                .build();
    }

    public ClientRegistration kakaoClientRegistration(){

        log.info("clientSecret : {}", kakaoSecret);

        return ClientRegistration.withRegistrationId("kakao")
                .clientId("5699f32eb8a13842ed375907835277d7")
                .clientSecret(kakaoSecret)
                .redirectUri("https://clat.duckdns.org/login/oauth2/code/kakao")
                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_POST)
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .scope("profile_nickname")
                .authorizationUri("https://kauth.kakao.com/oauth/authorize")
                .tokenUri("https://kauth.kakao.com/oauth/token")
                .userInfoUri("https://kapi.kakao.com/v2/user/me")
                .userNameAttributeName(IdTokenClaimNames.SUB)
                .build();
    }
}
