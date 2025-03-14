# CLAT

<img src="https://github.com/user-attachments/assets/748da446-ddc8-4ac8-a308-50cf4b59bb4f" width="600">



## 👨‍👨‍👧 Team 소개

|                                      령원                                       |                                      웅애요                                      |                                    도그                                         |
|:------------------------------------------------------------------------------:|:------------------------------------------------------------------------------:|:------------------------------------------------------------------------------:|
|                                   DEVELOPER                                    |                                   DEVELOPER                                    |                                   DEVELOPER                                    |
| <img src="https://github.com/user-attachments/assets/d2045545-b489-4df6-ac3b-c36361d7f831" width="250" /> | <img src="https://github.com/user-attachments/assets/c5ef6f57-f083-49ec-a7cd-8c1074b66a31" width="250" /> | <img src="https://github.com/user-attachments/assets/d8e14666-f0a3-413a-b880-39649563567a" width="250" /> |
|                     [kcsc2217](https://github.com/kcsc2217)                    |        [ensalada-de-pollo](https://github.com/ensalada-de-pollo)           |                     [AKKDevMachine](https://github.com/AKKDevMachine)          |            |

<br><br>

## 📚CLAT 서비스

> **CLASS + CHATTING = CLAT 서비스** <br><br>
> 교수와 학생간의 실시간 문의 및 채팅 서비스. <br>
> STOMP 실시간 소켓 통신을 기반으로한 채팅 서비스 제공 <br>
> 시큐리티/Json Web Token 기반의 로그인 서비스 제공 <br><br>
> Q&A 및 고객센터 게시판 서비스, 메일로 모든 문의에 대한 답변 제공<br>
> **다양한 기술에 도전해보는 것과, 지속적인 리팩토링을 통한 코드 개선에 집중**했습니다.

<br>

```
├── 회원
│   ├── 로그인
│   ├── 회원가입
│   ├── SMTP 기반 이메일 인증
│   └── 로그아웃
│
│
├── 홈 
│   ├── 회원 수강 목록
│   ├── 회원 메시지 목록
│   └──  회원 북마크 목록
│   
│
├── 실시간 채팅
│   ├── STOMP 기반의 실시간 소켓 통신
│   ├── S3 연동을 통한 채팅방 파일 업로드, 다운로드
│   ├── 교수의 답글 기능
│   └── 메모 및 좋아요 기능
│
│
├── 고객센터
│   ├── Q&A 게시판
│   ├── 문의하기 게시판, 메일을 통한 답변
│   └── 
```

<br><br>

## 🛠 기술스택

### Languages
![Java](https://img.shields.io/badge/Java-007396?style=flat-square&logo=java&logoColor=white)

### Frameworks
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-6DB33F?style=flat-square&logo=spring-boot&logoColor=white)
![Spring Security](https://img.shields.io/badge/Spring%20Security-6DB33F?style=flat-square&logo=spring-security&logoColor=white)
![Spring Data JPA](https://img.shields.io/badge/Spring%20Data%20JPA-6DB33F?style=flat-square&logo=spring&logoColor=white)
![QueryDSL](https://img.shields.io/badge/QueryDSL-5C7F4E?style=flat-square&logo=querydsl&logoColor=white)

### Testing
![JUnit](https://img.shields.io/badge/JUnit-25A162?style=flat-square&logo=junit&logoColor=white)

### Databases
![MySQL](https://img.shields.io/badge/MySQL-4479A1?style=flat-square&logo=mysql&logoColor=white)
![Redis](https://img.shields.io/badge/Redis-DC382D?style=flat-square&logo=redis&logoColor=white)

### Tools
![IntelliJ IDEA](https://img.shields.io/badge/IntelliJ%20IDEA-000000?style=flat-square&logo=intellij-idea&logoColor=white)
![Gradle](https://img.shields.io/badge/Gradle-02303A?style=flat-square&logo=gradle&logoColor=white)
![Postman](https://img.shields.io/badge/Postman-FF6C37?style=flat-square&logo=postman&logoColor=white)
![Notion](https://img.shields.io/badge/Notion-000000?style=flat-square&logo=notion&logoColor=white)

### Infrastructure
![AWS EC2](https://img.shields.io/badge/AWS%20EC2-FF9900?style=flat-square&logo=amazonec2&logoColor=white)
![AWS RDS](https://img.shields.io/badge/AWS%20RDS-527FFF?style=flat-square&logo=amazonrds&logoColor=white)
![AWS S3](https://img.shields.io/badge/AWS%20S3-569A31?style=flat-square&logo=amazons3&logoColor=white)
![nGrinder](https://img.shields.io/badge/nGrinder-2F6EB0?style=flat-square&logo=ngrinder&logoColor=white)
![NGINX](https://img.shields.io/badge/NGINX-009639?style=flat-square&logo=nginx&logoColor=white)
![JWT](https://img.shields.io/badge/JWT-000000?style=flat-square&logo=json-web-tokens&logoColor=white)


<br><br>

## 🗂️ 프로젝트 구조

프로젝트의 전체 구조를 빠르게 파악 가능한 계층형 구조를 사용
- [module-application](https://github.com/prgrms-be-devcourse/BE-04-JTOON/tree/develop/module-application)
- [module-core](https://github.com/prgrms-be-devcourse/BE-04-JTOON/tree/develop/module-core)
- [module-domain](https://github.com/prgrms-be-devcourse/BE-04-JTOON/tree/develop/module-domain)
- [module-internal](https://github.com/prgrms-be-devcourse/BE-04-JTOON/tree/develop/module-internal)

```.
.
└── clat
    ├── ClatApplication.java
    ├── config
    │   ├── CorsMvcConfig.java   
    │   │         .
    │   │         .
    │   │         .
    │   ├── StompConfig.java
    │   └── handler
    │       └── StompHandler.java
    ├── controller
    │   ├── BookMarkController.java
    │   │         .
    │   │         .
    │   │         .
    │   ├── ReIssueController.java
    │   └── ReportController.java
    ├── domain
    │   ├── Answer.java
    │   │         .
    │   │         .
    │   │         .
    │   ├── Course.java
    │   ├── Enum
    │   │   ├── Emoticon.java
    │   │   └── UserType.java
    │   ├── FAQItem.java
    │   ├── File
    │   │   └── ValidationFileSize.java
    │   ├── Image.java
    │   │         .
    │   │         .
    │   │         .
    │   └── Token.java
    ├── dto
    │   ├── CustomUserDetails.java
    │   ├── request
    │   │   ├── ChatRoomCreateReqDTO.java
    │   │         .
    │   │         .
    │   │         .
    │   │   ├── ReportReqDTO.java
    │   │   └── RoomKeyReqDTO.java
    │   └── response
    │       ├── BookMarkSaveDTO.java
    │       │         .
    │       │         .
    │       │         .
    │       ├── RestResponse.java
    │       └── RoomKeyResDTO.java
    ├── exception
    │   ├── AccessTokenInvalidException.java
    │   │         .
    │   │         .
    │   │         .
    │   ├── UsernameDataIntegrityViolationException.java
    │   ├── exhandler
    │   │   └── advice
    │   │       └── ExControllerAdvice.java
    │   └── type
    │       └── ErrorCode.java
    ├── jwt
    │   ├── CustomLogoutFilter.java
    │   ├── JwtFilter.java
    │   ├── JwtUtil.java
    │   └── LoginFilter.java
    ├── repository
    │   ├── AnswerRepository.java
    │   │         .
    │   │         .
    │   │         .
    │   ├── TokenRepository.java
    │   └── VerificationCodeRepository.java
    └── service
        ├── AnswerService.java
        │         .
        │         .
        │         .
        ├── TokenService.java
        └── VerificationCode.java
```

<br><br>

## 📂 ERD

<img width="1693" alt="image" src="https://github.com/user-attachments/assets/f0482b99-ab9c-451a-8e0a-a637fac0e93a">

<br><br>

## 🌈 협업

<img width="1060" alt="image" src="https://github.com/prgrms-be-devcourse/BE-04-JTOON/assets/87688023/db950edb-df7a-455f-9980-d95ded1d5b4e">
