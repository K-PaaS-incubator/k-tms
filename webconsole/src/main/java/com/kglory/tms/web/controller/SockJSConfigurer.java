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

import com.kglory.tms.web.controller.monitor.ApplicationDetectionHandler;
import com.kglory.tms.web.controller.monitor.FileMetaDetectionHandler;
import com.kglory.tms.web.controller.monitor.IntrusionDetectionHandler;
import com.kglory.tms.web.controller.monitor.SensorMonitorHandler;
import com.kglory.tms.web.controller.tools.PingHandler;
import com.kglory.tms.web.controller.tools.TraceRouteHandler;
import com.kglory.tms.web.controller.tools.WhoisHandler;

@Configuration
@EnableWebMvc
@EnableWebSocket
public class SockJSConfigurer extends WebMvcConfigurerAdapter implements WebSocketConfigurer {
	
	@Override
	public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
		registry.addHandler(intrusionDetectionHandler(), "/api/requestDetectionEvent");
		registry.addHandler(intrusionDetectionHandler(), "/api/socketjs/requestDetectionEvent").withSockJS();
		
		registry.addHandler(sensorStatusMonitorHandler(), "/api/requestSensorStatusMonitoring");
		registry.addHandler(sensorStatusMonitorHandler(), "/api/socketjs/requestSensorStatusMonitoring").withSockJS();
		
		registry.addHandler(applicationDetectionHandler(), "/api/requestApplicationDetectionEvent");
		registry.addHandler(applicationDetectionHandler(), "/api/socketjs/requestApplicationDetectionEvent").withSockJS();
		
		registry.addHandler(fileMetaDetectionHandler(), "/api/requestFileMetaDetectionEvent");
		registry.addHandler(fileMetaDetectionHandler(), "/api/socketjs/requestFileMetaDetectionEvent").withSockJS();
		
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
	
	@Bean
	public WebSocketHandler sensorStatusMonitorHandler() {
		return new SensorMonitorHandler();
	}
	
	@Bean
	public WebSocketHandler intrusionDetectionHandler() {
		return new IntrusionDetectionHandler();
	}
	
	@Bean
	public WebSocketHandler applicationDetectionHandler() {
		return new ApplicationDetectionHandler();
	}
	
	@Bean
	public WebSocketHandler fileMetaDetectionHandler() {
		return new FileMetaDetectionHandler();
	}
        	
	@Override
	public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
		configurer.enable();
	}
	
}
