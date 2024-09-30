package com.resilence.config;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;

import io.netty.channel.ChannelOption;
import io.netty.handler.ssl.SslContextBuilder;
import reactor.netty.http.client.HttpClient;
import reactor.netty.tcp.SslProvider;

@Configuration
public class WebClientConfiguration {
	
	@Value("${child.base.url}")
	private String childUrl;
	
	@Bean("webclient")
	public WebClient buildWebClient() {
		
		HttpClient httpClient = HttpClient.create().baseUrl(childUrl)
				  .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)
		          //.option(ChannelOption.SO_KEEPALIVE, true)
		          //.option(EpollChannelOption.TCP_KEEPIDLE, 300)
		          //.option(EpollChannelOption.TCP_KEEPINTVL, 60)
		          //.option(EpollChannelOption.TCP_KEEPCNT, 8)
		          .responseTimeout(Duration.ofSeconds(1))
		          //.doOnConnected(conn -> conn
		          //        .addHandler(new ReadTimeoutHandler(5, TimeUnit.SECONDS))
		          //        .addHandler(new WriteTimeoutHandler(5)))
		          .secure(spec -> spec
		                  .sslContext(SslContextBuilder.forClient())
		                  .defaultConfiguration(SslProvider.DefaultConfigurationType.TCP)
		                  .handshakeTimeout(Duration.ofSeconds(30))
		                  .closeNotifyFlushTimeout(Duration.ofSeconds(10))
		                  .closeNotifyReadTimeout(Duration.ofSeconds(10)));
		
	        return WebClient.builder()
	          .clientConnector(new ReactorClientHttpConnector(httpClient))
	          .build();
	   }
	
	

}
