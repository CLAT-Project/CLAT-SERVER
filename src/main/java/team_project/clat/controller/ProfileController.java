package team_project.clat.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import team_project.clat.domain.Member;
import team_project.clat.dto.request.PasswordAuthReqDTO;
import team_project.clat.dto.request.UpdateProfileReqDTO;
import team_project.clat.dto.response.MyProfileResDTO;
import team_project.clat.exception.GlobalException;
import team_project.clat.exception.type.ErrorCode;
import team_project.clat.jwt.JwtUtil;
import team_project.clat.repository.MemberRepository;
import team_project.clat.service.MemberService;
import team_project.clat.service.ProfileService;

@Slf4j
@Controller
@ResponseBody
@RequestMapping("/my-profile")
@RequiredArgsConstructor
public class ProfileController {
  private final MemberService memberService;
  private final ProfileService profileService;
  private final JwtUtil jwtUtil;
  private final MemberRepository memberRepository;

  @GetMapping
  public ResponseEntity<MyProfileResDTO> profileMember(HttpServletRequest request){
    String accessToken = request.getHeader("access");
    String username = jwtUtil.getUsername(accessToken);
    String userType = jwtUtil.getUserType(accessToken);

    Member findMember = memberRepository.findByUsername(username);

    MyProfileResDTO myProfileResDTO = new MyProfileResDTO(username, findMember.getName(), findMember.getSchoolName(), userType);

    return new ResponseEntity<>(myProfileResDTO, HttpStatus.OK);
  }

  // 마이페이지 수정 전 패스워드를 인증하는 api
  @PostMapping("/auth")
  public String authenticatePassword(HttpServletRequest request,
                                     @RequestBody PasswordAuthReqDTO passwordAuthReqDTO) {
    String accessToken = request.getHeader("access");
    Member member = memberRepository.findByUsername(jwtUtil.getUsername(accessToken));

    boolean isAuthenticated = profileService.authenticationPassword(
            member,
            passwordAuthReqDTO.getPassword()
    );

    if (!isAuthenticated) {
      throw new GlobalException(ErrorCode.WRONG_PASSWORD);
    }

    return "인증이 완료되었습니다.";
  }

  @PutMapping("/modify")
  public ResponseEntity<Member> updateProfile(HttpServletRequest request,
                                              @RequestBody UpdateProfileReqDTO updateProfileReqDTO,
                                              @RequestParam boolean emailVerified) {
    String accessToken = request.getHeader("access");
    Member member = memberRepository.findByUsername(jwtUtil.getUsername(accessToken));

    return ResponseEntity.ok(profileService.updateProfile(member, updateProfileReqDTO, emailVerified));
  }

}