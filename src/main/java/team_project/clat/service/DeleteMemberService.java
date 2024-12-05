package team_project.clat.service;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team_project.clat.domain.Enum.UserStatus;
import team_project.clat.domain.Member;
import team_project.clat.dto.request.DeleteMemberReqDTO;
import team_project.clat.dto.response.CommonResultResDTO;
import team_project.clat.dto.response.DeleteMemberResDTO;
import team_project.clat.exception.RefreshTokenNotFound;
import team_project.clat.exception.UnAuthorizationException;
import team_project.clat.repository.MemberRepository;
import team_project.clat.repository.TokenRepository;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class DeleteMemberService {

    private final AuthenticationManager authenticationManager;
    private final MemberRepository memberRepository;
    private final TokenRepository tokenRepository;

    public DeleteMemberResDTO deleteUser(DeleteMemberReqDTO deleteMemberReqDTO, String username){

        //입력한 username(id)가 일치하는지 확인
        if(deleteMemberReqDTO.getUsername().equals(username)){

            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(username, deleteMemberReqDTO.getPassword(), null);
            try {
                Authentication authenticate = authenticationManager.authenticate(authToken);
                //id, password 인증
                if(authenticate.isAuthenticated()){
                    Member deleteMember = memberRepository.findByUsername(username);
                    deleteMember.memberUserStatusSet(UserStatus.INACTIVE);
                    memberRepository.save(deleteMember);

                    boolean isExist = tokenRepository.existsById(username);
                    if (!isExist) {
                        throw new RefreshTokenNotFound("Refresh 토큰이 존재하지 않습니다.");
                    }
                    //Refresh 토큰 DB에서 제거
                    tokenRepository.deleteById(username);

                    //회원 탈퇴 완료
                    CommonResultResDTO commonResultResDTO = new CommonResultResDTO("200 OK", "회원 탈퇴가 완료되었습니다.");
                    return new DeleteMemberResDTO(commonResultResDTO, null, null, HttpStatus.OK);

                }else {
                    //id, password 일치하지 않음
                    throw new UnAuthorizationException("아이디, 비밀번호가 일치하지 않습니다.");
                }
            }catch (BadCredentialsException e){
                throw new UnAuthorizationException("아이디 또는 비밀번호가 일치하지 않습니다.");
            }
        }else throw new UnAuthorizationException("아이디, 비밀번호가 일치하지 않습니다.");
    }
}
