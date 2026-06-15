package com.example.demo;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.*;


@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {
    private final LiveSocketHandler handler;

    public WebSocketConfig(LiveSocketHandler handler) {
        this. handler=handler;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(handler,"/live").setAllowedOriginPatterns("*");
    }

}
