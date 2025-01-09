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
  private final EmailService emailService;

  public boolean authenticationPassword(Long memberId, String password) {
    Member member = memberRepository.findById(memberId)
            .orElseThrow(() -> new GlobalException(ErrorCode.PAGE_NOT_FOUND));
    return passwordEncoder.matches(password, member.getPassword());
  }

  public Member updateProfile(Long memberId, UpdateProfileReqDTO updateProfileDTO, boolean emailVerified) {
      Member member = memberRepository.findById(memberId)
              .orElseThrow(() -> new RuntimeException("User not found"));

      if (updateProfileDTO.getName() != null) {
        member.setUsername(updateProfileDTO.getName());
      }

      if (updateProfileDTO.getPassword() != null) {
        member.setPassword(passwordEncoder.encode(updateProfileDTO.getPassword()));
      }

      if (updateProfileDTO.getEmail() != null) {
        if (!emailVerified) {
          throw new RuntimeException("Email verification required before changing email.");
        }
        member.setEmail(updateProfileDTO.getEmail());
      }

      return memberRepository.save(member);
    }
}