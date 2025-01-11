package team_project.clat.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class MyProfileResDTO {

    private String username;
    private String name;
    private String email;
    private String schoolName;
    private String UserType;
    private String password;

}
