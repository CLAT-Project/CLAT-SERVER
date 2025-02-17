package team_project.clat.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import team_project.clat.domain.Member;
import team_project.clat.domain.Token;
import team_project.clat.dto.request.LoginReqDTO;
import team_project.clat.dto.response.JoinResultResDTO;
import team_project.clat.dto.response.SocialLoginResDTO;
import team_project.clat.jwt.JwtUtil;
import team_project.clat.repository.MemberRepository;
import team_project.clat.repository.TokenRepository;

@Service
@RequiredArgsConstructor
public class LoginService {

    private final MemberRepository memberRepository;
    private final TokenRepository tokenRepository;
    private final JwtUtil jwtUtil;

    public SocialLoginResDTO socialLogin(LoginReqDTO loginReqDTO){
        String username = loginReqDTO.getUsername();
        Member byUsername = memberRepository.findByUsername(username);

        //토큰 생성
        String access = jwtUtil.createJwt("access", username, byUsername.getUserType().getDescription(), 600000L);
        String refresh = jwtUtil.createJwt("refresh", username, byUsername.getUserType().getDescription(), 86400000L);

        Token token = new Token(username, refresh, 86400000L);
        tokenRepository.save(token);

        return new SocialLoginResDTO(access, refresh);
    }
}
