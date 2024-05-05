package com.poshtarenko.codeforge.integration.controller.websocket.util;

import com.poshtarenko.codeforge.entity.user.User;
import com.poshtarenko.codeforge.security.dto.SignInRequest;
import com.poshtarenko.codeforge.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.jetbrains.annotations.NotNull;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHttpHeaders;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.Transport;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;

import java.util.List;

import static java.util.concurrent.TimeUnit.SECONDS;

@Component
@RequiredArgsConstructor
public class WebSocketClient {

    private final UserService userService;

    @SneakyThrows
    public StompSession connect(String url, User user) {
        List<Transport> transports = List.of(new WebSocketTransport(new StandardWebSocketClient()));
        WebSocketStompClient stompClient = new WebSocketStompClient(new SockJsClient(transports));
        stompClient.setMessageConverter(new MappingJackson2MessageConverter());

        WebSocketHttpHeaders headers = new WebSocketHttpHeaders();
        String jwtToken = userService.auth(new SignInRequest(
                user.getEmail(),
                "password"
        )).getToken();
//        headers.add("Authorization", "Bearer " + jwtToken);

        return stompClient.connectAsync(url, headers, new StompSessionHandlerAdapter() {
            @Override
            public void afterConnected(@NotNull StompSession session, @NotNull StompHeaders connectedHeaders) {
                System.out.println("WS CLIENT: Connected to " + url);
            }
        }).get(10, SECONDS);
    }

}
