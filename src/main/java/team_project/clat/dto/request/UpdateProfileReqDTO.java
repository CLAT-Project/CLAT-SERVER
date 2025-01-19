package team_project.clat.dto.request;

import lombok.Getter;

@Getter
public class UpdateProfileReqDTO {
  private String name;
  private String password;
  private String email;
}
