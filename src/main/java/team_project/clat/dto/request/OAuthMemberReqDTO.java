package team_project.clat.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OAuthMemberReqDTO {

    private String role;
    private String name;
    private String username;
}
