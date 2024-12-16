package team_project.clat.exception.exhandler.advice;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import team_project.clat.dto.response.RestResponse;
import team_project.clat.dto.response.CommonResultResDTO;
import team_project.clat.exception.*;

@Slf4j
@RestControllerAdvice
public class ExControllerAdvice {

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler
    public CommonResultResDTO exHandle(MailVerifyException e){
        String errorText = HttpStatus.CONFLICT.value() +" "+ HttpStatus.CONFLICT.getReasonPhrase();
        return new CommonResultResDTO(errorText, e.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class) // 타입 매핑이 안맞을때
    public RestResponse validationException(MethodArgumentNotValidException ex){
        log.error("[exceptionHandle] ex", ex);

        return new RestResponse("BAD", ex.getMessage());

    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NotFoundException.class) //해당 강의를 찾을수 없을떄
    public RestResponse courseNotFoundException(NotFoundException ex){
        log.error("[exceptionHandle] ex", ex);

        return new RestResponse("NOT_FOUND", ex.getMessage());
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(DuplicateCourseChatRoomException.class)
    public RestResponse duplicateCourseChatRoomException(DuplicateCourseChatRoomException ex){
        log.error("[exceptionHandle] ex", ex);
        return new RestResponse("CONFLICT", ex.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(FileSizeLimitException.class)
    public RestResponse fileException(FileSizeLimitException ex){
        log.error("[exceptionHandle] ex", ex);
        return new RestResponse("Bad", ex.getMessage());
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(AccessTokenInvalidException.class)
    public RestResponse tokenException(AccessTokenInvalidException ex){
        log.error("[exceptionHandle] ex", ex);
        return new RestResponse("UNAUTHORIZED", ex.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public String MaxUploadSIze(MaxUploadSizeExceededException ex){
        log.error("[exceptionHandle] ex", ex);
        return "file size  exceeds the maximum allowed limit!";
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler
    public CommonResultResDTO exImageDownloadHandle(ImageDownloadFailedException e){
        String errorText = HttpStatus.BAD_REQUEST.value() +" "+ HttpStatus.BAD_REQUEST.getReasonPhrase();
        return new CommonResultResDTO(errorText, e.getMessage());
    }

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(ProfessorAuthorizationException.class)
    public RestResponse forBiden(ProfessorAuthorizationException ex){
        log.error("[exceptionHandle] ex", ex);
        return new RestResponse("FORBIDDEN", ex.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(DuplicateException.class)
    public RestResponse forBiden(DuplicateException ex){
        log.error("[exceptionHandle] ex", ex);

        return new RestResponse("BAD", ex.getMessage());
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(MemberNotAccessException.class)
    public RestResponse forBiden(MemberNotAccessException ex){
        log.error("[exceptionHandle] ex", ex);
        return new RestResponse("UNAUTHORIZED", ex.getMessage());
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(UnAuthorizationException.class)
    public RestResponse forBiden(UnAuthorizationException ex){
        log.error("[exceptionHandle] ex", ex);
        return new RestResponse("UNAUTHORIZED", ex.getMessage());
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(GlobalException.class)
    public RestResponse handleGlobalException(GlobalException ex) {
        log.error("[exceptionHandle] ex", ex);

        return new RestResponse(ex.getErrorCode().toString(), ex.getErrorCode().getDescription());
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler
    public CommonResultResDTO usernameJoinExHandle(UsernameDataIntegrityViolationException e){
        String errorText = HttpStatus.CONFLICT.value() +" "+ HttpStatus.CONFLICT.getReasonPhrase();
        return new CommonResultResDTO(errorText, e.getMessage());
    }
  
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(RefreshTokenNotFound.class)
    public CommonResultResDTO refreshTokenNotFoundExHandle(RefreshTokenNotFound e){
        String errorText = HttpStatus.UNAUTHORIZED.value() +" "+ HttpStatus.UNAUTHORIZED.getReasonPhrase();
        return new CommonResultResDTO(errorText, e.getMessage());
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(UserNotFoundException.class)
    public CommonResultResDTO userNotFoundExceptionExHandle(UserNotFoundException e){
        String errorText = HttpStatus.NOT_FOUND.value() +" "+ HttpStatus.NOT_FOUND.getReasonPhrase();
        return new CommonResultResDTO(errorText, e.getMessage());
    }
}
