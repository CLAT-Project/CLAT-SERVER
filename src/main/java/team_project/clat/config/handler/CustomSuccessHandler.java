package team_project.clat.config.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import team_project.clat.dto.CustomOAuth2User;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@Component
public class CustomSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        //OAuth2User
        CustomOAuth2User customUserDetails = (CustomOAuth2User) authentication.getPrincipal();

        String username = customUserDetails.getUsername();
        String email = customUserDetails.getEmail();
        String name = customUserDetails.getName();

        /*Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
        GrantedAuthority auth = iterator.next();
        String role = auth.getAuthority();*/

        // 여기서부터 성공 flag 및 소셜 유저 정보 json 응답 코드 작성
        // 응답 객체 설정
        response.setStatus(HttpServletResponse.SC_OK);  // 200 OK 응답 설정
        response.setContentType("application/json");  // 응답 타입을 JSON으로 설정

        // JSON 형식으로 성공 flag 및 username 반환
        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("success", true);  // 성공 플래그
        responseBody.put("username", username);  // username
        responseBody.put("name", name);
        responseBody.put("email", email);
        response.sendRedirect("https://clat-project.vercel.app/social-login");

        // ObjectMapper를 사용하여 JSON으로 변환 후 응답으로 작성
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonResponse = objectMapper.writeValueAsString(responseBody);

        response.getWriter().write(jsonResponse);  // 응답 전송
    }
}
