package team_project.clat.service;

import jakarta.mail.MessagingException;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import team_project.clat.domain.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team_project.clat.domain.Report;
import team_project.clat.dto.request.ReportReqDTO;
import team_project.clat.exception.GlobalException;
import team_project.clat.repository.ReportRepository;
import team_project.clat.exception.type.ErrorCode;


@Service
@RequiredArgsConstructor
@Slf4j
@EnableGlobalMethodSecurity(prePostEnabled = true)
@Transactional(readOnly = true)
public class ReportService {
  @Autowired
  private final ReportRepository reportRepository;

  @Autowired
  private EmailService emailService;

  @Transactional
  public ReportReqDTO createReport(ReportReqDTO reportDTO, Member member) throws MessagingException{

    String email = member.getEmail();

    // 만약 입력한 이메일이 있다면 그 이메일이 우선
    if (reportDTO.getEmail() != null && !reportDTO.getEmail().isEmpty()) {
      email = reportDTO.getEmail();
    }

    // 만약 멤버 정보에도 이메일이 없고, 입력 받은 이메일도 없다면 예외 발생
    if (email == null || email.isEmpty()) {
      throw new GlobalException(ErrorCode.NULL_EMAIL_INPUT);
    }

    Report report = new Report(
            null,
            email,
            reportDTO.getDescription(),
            member,
            reportDTO.getFilepath()
    );

    report = reportRepository.save(report);

    // 이메일에 적힐 내용
    String title = "고객센터 문의입니다.";

    String body = "\n";

    body += "member id: " + member.getId() + "\n";

    body +=
            "답변받을 이메일: " + report.getEmail() + "\n\n" +
            "문의 내용: " + report.getDescription() + "\n";

    // 첨부파일 미구현 상태
    String attachmentPath = report.getFilepath();

    emailService.sendEmailWithAttachment(title, body, attachmentPath);

    return convertToDTO(report);
  }

  @Transactional
  public ReportReqDTO convertToDTO(Report report) {
    ReportReqDTO dto = new ReportReqDTO();
    dto.setId(report.getId());
    dto.setEmail(report.getEmail());
    dto.setDescription(report.getDescription());
    dto.setFilepath(report.getFilepath());
    if (report.getMember() != null) {
      dto.setMemberId(report.getMember().getId());
    }
    return dto;
  }
}
