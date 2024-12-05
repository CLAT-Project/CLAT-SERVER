package team_project.clat.jwt;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.util.StreamUtils;
import team_project.clat.domain.Enum.UserStatus;
import team_project.clat.domain.Member;
import team_project.clat.domain.Token;
import team_project.clat.dto.response.CommonResultResDTO;
import team_project.clat.dto.request.LoginReqDTO;
import team_project.clat.exception.UnAuthorizationException;
import team_project.clat.repository.MemberRepository;
import team_project.clat.repository.TokenRepository;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Iterator;

@RequiredArgsConstructor
@Slf4j
public class LoginFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final ObjectMapper objectMapper;
    private final TokenRepository tokenRepository;
    private final MemberRepository memberRepository;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        //String username = obtainUsername(request);
        //String password = obtainPassword(request);

        LoginReqDTO loginReqDTO = new LoginReqDTO();

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            ServletInputStream inputStream = request.getInputStream();
            String messageBody = StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);
            loginReqDTO = objectMapper.readValue(messageBody, LoginReqDTO.class);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        String username = loginReqDTO.getUsername();
        String password = loginReqDTO.getPassword();

        Member loginMember = memberRepository.findByUsername(username);
        if(loginMember!=null && loginMember.getUserStatus()== UserStatus.INACTIVE){
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 401 Unauthorized
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");

            CommonResultResDTO responseDto = new CommonResultResDTO("401 Unauthorized", "이미 탈퇴한 사용자입니다.");
            try {
                // 응답에 JSON 형태로 반환
                String jsonResponse = objectMapper.writeValueAsString(responseDto);
                response.getWriter().write(jsonResponse);
            } catch (IOException ex) {
                throw new RuntimeException("로그인 응답 작성 실패", ex);
            }

            // 다음 필터로 넘어가지 못하도록 필터 종료
            return null;
        }

        log.info("username : {}", username);

        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(username, password, null);

        return authenticationManager.authenticate(authToken);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) throws IOException, ServletException {
        //유저 정보
        String username = authentication.getName();

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
        GrantedAuthority auth = iterator.next();
        String role = auth.getAuthority();


            //토큰 생성
        String access = jwtUtil.createJwt("access", username, role, 600000L);
        String refresh = jwtUtil.createJwt("refresh", username, role, 86400000L);

        Token token = new Token(username, refresh, 86400000L);
        tokenRepository.save(token);


            //응답 설정
        response.setHeader("access", access);
        //response.addCookie(createCookie("refresh", refresh));
        response.setHeader(HttpHeaders.SET_COOKIE, createCookie("refresh", refresh).toString());
        response.setStatus(HttpStatus.OK.value());

        chain.doFilter(request, response);
        }

        @Override
        protected void unsuccessfulAuthentication (HttpServletRequest request, HttpServletResponse
        response, AuthenticationException failed) throws IOException, ServletException {
            response.setStatus(409);
            response.setContentType("application/json"); // 응답 형식 설정
            response.setCharacterEncoding("UTF-8");

            CommonResultResDTO commonResultResDTO = new CommonResultResDTO("409 Conflict", "올바르지않은 아이디 또는 비밀번호 입니다.");

            // CommonResult 객체를 JSON으로 직렬화
            String jsonResponse = objectMapper.writeValueAsString(commonResultResDTO);

            // 응답 본문에 JSON 작성
            response.getWriter().write(jsonResponse);

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

            /*Cookie cookie = new Cookie(key, value);
            cookie.setMaxAge(24 * 60 * 60);
            cookie.setSecure(true); //https로 진행할 시
            cookie.setPath("/"); //쿠키가 적용될 범위
            cookie.setHttpOnly(true);

            return cookie;*/
        }
    }
