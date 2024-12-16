package team_project.clat.dto.request;

import lombok.Data;

@Data
public class FindMemberPasswordReqDTO {
    private String username;
    private String email;
}
