package team_project.clat.config.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;
import team_project.clat.domain.Token;
import team_project.clat.dto.CustomOAuth2User;
import team_project.clat.jwt.JwtUtil;
import team_project.clat.repository.TokenRepository;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class CustomSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtUtil jwtUtil;
    private final TokenRepository tokenRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        //OAuth2User
        CustomOAuth2User customUserDetails = (CustomOAuth2User) authentication.getPrincipal();
        log.info("getPrincipal : {}", authentication.getPrincipal().toString());

        String username = customUserDetails.getUsername();
        String email = customUserDetails.getEmail();
        String name = customUserDetails.getName();
        String existFlag = customUserDetails.getExistFlag();

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
        GrantedAuthority auth = iterator.next();
        String role = auth.getAuthority();

        log.info("existFlag!!!!!!! : {}", existFlag);

        if(existFlag.equals("no")) {
            String encodedName = URLEncoder.encode(name, StandardCharsets.UTF_8);

            MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
            queryParams.add("success", "true");
            queryParams.add("username", username);
            queryParams.add("name", encodedName);
            queryParams.add("email", email);

            String uri = UriComponentsBuilder
                    .newInstance()
                    .scheme("https")
                    .host("clat-project.vercel.app")
                    .path("/social-login")
                    .queryParams(queryParams)
                    .build()
                    .toString();

            getRedirectStrategy().sendRedirect(request, response, uri);
        }else {

            /*String access = jwtUtil.createJwt("access", username, role, 600000L);
            String refresh = jwtUtil.createJwt("refresh", username, role, 86400000L);

            Token token = new Token(username, refresh, 86400000L);
            tokenRepository.save(token);


            //응답 설정
            //response.setHeader("access", access);
            response.addCookie(createCookie1("refresh", refresh));
            //response.setHeader(HttpHeaders.SET_COOKIE, createCookie("refresh", refresh).toString());
            response.setStatus(HttpStatus.OK.value());*/
            //response.sendRedirect("https://clat-project.vercel.app/social-redirect");

            String encodedName = URLEncoder.encode(name, StandardCharsets.UTF_8);

            MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
            queryParams.add("username", username);
            queryParams.add("name", encodedName);
            queryParams.add("email", email);

            String uri = UriComponentsBuilder
                    .newInstance()
                    .scheme("https")
                    .host("clat-project.vercel.app")
                    .path("/social-redirect")
                    .queryParams(queryParams)
                    .build()
                    .toString();

            getRedirectStrategy().sendRedirect(request, response, uri);
        }
    }

    private Cookie createCookie1 (String key, String value){

        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(24*60*60);
        cookie.setPath("/");
        cookie.setSecure(true);
        cookie.setHttpOnly(true);
        cookie.setDomain("clat-project.vercel.app");

        return cookie;
    }

    private ResponseCookie createCookie (String key, String value){

        return ResponseCookie
                .from(key, value)
                .path("/")
                .secure(true)
                .httpOnly(true)
                .maxAge(24*60*60)
                .sameSite("None")
                .build();
    }
}
