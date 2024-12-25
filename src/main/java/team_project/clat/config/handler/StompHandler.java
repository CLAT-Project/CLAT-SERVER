package team_project.clat.config.handler;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;
import team_project.clat.exception.AccessTokenInvalidException;
import team_project.clat.jwt.JwtUtil;

@Component
@RequiredArgsConstructor
@Slf4j
public class StompHandler implements ChannelInterceptor {

    private final JwtUtil jwtUtil;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {

        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);

        // STOMP 헤더를 직접 출력
        log.info("STOMP Command: {}", accessor.getCommand());

        // 각 헤더 키와 값을 출력
        log.info("Headers: {}", message.getHeaders());

        log.info("메시지 연결");

        // 모든 헤더 키와 값을 출FUR

        String token = accessor.getFirstNativeHeader("Authorization");

        log.info("토근 값 = {}", token);

        if (StompCommand.CONNECT.equals(accessor.getCommand()) || StompCommand.SEND.equals(accessor.getCommand())) {
            // http에서는 custom 헤더를 사요하고 있지만 stomp는 웹소켓 환경에서 동작하므로 해당 헤더를 알지 못함

            if (token == null || token.isEmpty()) {
                log.info("토큰이 없습니다");
                throw new AccessTokenInvalidException("Access token is required.");
            }

            if (jwtUtil.isExpired(token)) {
                log.info("토큰이 만료되었습니다");
                throw new AccessTokenInvalidException("Access token has expired.");
            }

          String username = jwtUtil.getUsername(token);
            accessor.getSessionAttributes().put("username", username);
            // Optional: Add logging for debugging
            log.info("username = {}", username);
                System.out.println("Valid token received: " + token);
        }
        else {
            log.info("Received command: " + accessor.getCommand()); // 다른 명령어 처리 로깅
        }

        return message;
    }

}
