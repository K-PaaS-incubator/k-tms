package com.kglory.tms.web.controller;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

import com.kglory.tms.web.controller.tools.PingHandler;
import com.kglory.tms.web.controller.tools.TraceRouteHandler;
import com.kglory.tms.web.controller.tools.WhoisHandler;

@Configuration
@EnableWebMvc
@EnableWebSocket
public class SockJSConfigurer extends WebMvcConfigurerAdapter implements WebSocketConfigurer {
	
	@Override
	public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {

		registry.addHandler(requestWhois(), "/api/requestWhois");
		registry.addHandler(requestWhois(), "/api/socketjs/requestWhois").withSockJS();
		
		registry.addHandler(requestTraceRoute(), "/api/requestTraceRoute");
		registry.addHandler(requestTraceRoute(), "/api/socketjs/requestTraceRoute").withSockJS();
		
		registry.addHandler(requestPing(), "/api/requestPing");
		registry.addHandler(requestPing(), "/api/socketjs/requestPing").withSockJS();
		
		registry.addHandler(requestPresence(), "/api/requestPresenceMonitoring");
		registry.addHandler(requestPresence(), "/api/socketjs/requestPresenceMonitoring").withSockJS();
	}
	
	@Bean
	public WebSocketHandler requestPresence() {
		return new PresenceHandler();
	}

	@Bean
	public WebSocketHandler requestPing() {
		return new PingHandler();
	}
	
	@Bean
	public WebSocketHandler requestTraceRoute() {
		return new TraceRouteHandler();
	}
	
	@Bean
	public WebSocketHandler requestWhois() {
		return new WhoisHandler();
	}
	

	@Override
	public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
		configurer.enable();
	}
	
}
