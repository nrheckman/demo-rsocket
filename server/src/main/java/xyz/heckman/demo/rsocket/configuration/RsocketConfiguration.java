package xyz.heckman.demo.rsocket.configuration;

import io.rsocket.core.Resume;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.rsocket.server.RSocketServerCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.rsocket.RSocketSecurity;
import org.springframework.security.rsocket.core.PayloadSocketAcceptorInterceptor;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;
import xyz.heckman.demo.rsocket.configuration.properties.RsocketProperties;
import xyz.heckman.demo.rsocket.dto.ErrorDto;

@Slf4j
@Configuration
@EnableConfigurationProperties(RsocketProperties.class)
public class RsocketConfiguration {

	private final RsocketProperties rsocketProperties;

	public RsocketConfiguration(RsocketProperties rsocketProperties) {
		this.rsocketProperties = rsocketProperties;
	}

	@Bean
	public RSocketServerCustomizer rSocketResume() {
		Resume resume = new Resume()
				.sessionDuration(rsocketProperties.getResume().getSessionDuration())
				.retry(Retry.fixedDelay(Long.MAX_VALUE, rsocketProperties.getResume().getRetryDelay())
						.doBeforeRetry(s -> log.debug("Disconnected. Trying to resume...")));
		return rSocketServer -> rSocketServer.resume(resume);
	}

	@Bean
	public PayloadSocketAcceptorInterceptor rsocketInterceptor(RSocketSecurity rsocket) {
		rsocket
				.authorizePayload(authorize -> authorize
						.route("demo.*").permitAll()
						.anyRequest().authenticated()
						.anyExchange().permitAll())
				.jwt(Customizer.withDefaults());
		return rsocket.build();
	}

	@MessageExceptionHandler
	public Mono<ErrorDto> handleException(Exception e) {
		return Mono.error(e);
	}
}
