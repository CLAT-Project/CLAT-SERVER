package team_project.clat.dto.response;

public interface OAuth2Response {

    String getProvider(); // 제공자(Ex. naver, google, ...)

    String getProviderId(); //제공자에서 발급해주는 아이디(번호)

    String getEmail();

    String getName();

}
