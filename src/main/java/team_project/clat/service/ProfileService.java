package team_project.clat.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team_project.clat.domain.Member;
import team_project.clat.dto.request.UpdateProfileReqDTO;
import team_project.clat.exception.GlobalException;
import team_project.clat.exception.type.ErrorCode;
import team_project.clat.repository.MemberRepository;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class ProfileService {
  private final MemberRepository memberRepository;
  private final PasswordEncoder passwordEncoder;

  @Transactional
  public boolean authenticationPassword(Member member, String password) {
    return passwordEncoder.matches(password, member.getPassword());
  }

  public Member updateProfile(Member member, UpdateProfileReqDTO updateProfileDTO, boolean emailVerified) {
      if (updateProfileDTO.getName() != null) {
        member.setName(updateProfileDTO.getName());
      }

      if (updateProfileDTO.getPassword() != null) {
        member.setPassword(passwordEncoder.encode(updateProfileDTO.getPassword()));
      }

      if (updateProfileDTO.getEmail() != null) {
        if (!emailVerified) {
          throw new GlobalException(ErrorCode.NOT_AUTHENTICATED);
        }
        member.setEmail(updateProfileDTO.getEmail());
      }

      return memberRepository.save(member);
    }
}