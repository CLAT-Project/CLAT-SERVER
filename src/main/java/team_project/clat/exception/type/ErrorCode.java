package team_project.clat.exception.type;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {

  PAGE_NOT_FOUND(
          HttpStatus.NOT_FOUND,
          4000,
          "해당 페이지를 찾을 수 없습니다."),

  NULL_EMAIL_INPUT(
          HttpStatus.NOT_FOUND,
          4001,
          "답변이 이메일로 전달됩니다. 이메일을 반드시 입력해주세요."),

  WRONG_PASSWORD(
          HttpStatus.NOT_FOUND,
          4002,
          "잘못된 비밀번호입니다."),

  NOT_AUTHENTICATED(
          HttpStatus.NOT_FOUND,
          4003,
          "인증이 완료되지 않았습니다. 인증 완료 후 다시 시도해주세요.");

  private final HttpStatus httpStatus;
  private final Integer errorCode;
  private final String description;

  ErrorCode(HttpStatus httpStatus, Integer errorCode, String description) {
    this.httpStatus = httpStatus;
    this.errorCode = errorCode;
    this.description = description;
  }
}