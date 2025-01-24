package team_project.clat.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import team_project.clat.domain.Enum.UserType;

@Getter
@Setter
public class SocialJoinReqDTO {

    private String username;

    @NotBlank(message = "학교/기관은 필수 입력값입니다.")
    private String schoolName;

    private UserType userType;

}
