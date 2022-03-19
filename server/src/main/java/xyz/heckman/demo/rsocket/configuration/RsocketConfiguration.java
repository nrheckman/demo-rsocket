package xyz.heckman.demo.rsocket.configuration;

import io.rsocket.core.Resume;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.rsocket.server.RSocketServerCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.rsocket.RSocketStrategies;
import org.springframework.messaging.rsocket.annotation.support.RSocketMessageHandler;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.rsocket.EnableRSocketSecurity;
import org.springframework.security.config.annotation.rsocket.RSocketSecurity;
import org.springframework.security.messaging.handler.invocation.reactive.AuthenticationPrincipalArgumentResolver;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoders;
import org.springframework.security.rsocket.core.PayloadSocketAcceptorInterceptor;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;
import xyz.heckman.demo.rsocket.configuration.properties.RsocketProperties;
import xyz.heckman.demo.rsocket.dto.ErrorDto;

import java.io.PrintWriter;
import java.io.StringWriter;

@Slf4j
@Configuration
@EnableRSocketSecurity
@EnableConfigurationProperties({ RsocketProperties.class, OAuth2ResourceServerProperties.class })
public class RsocketConfiguration {

	private final RsocketProperties rsocketProperties;
	private final OAuth2ResourceServerProperties oAuth2ResourceServerProperties;

	public RsocketConfiguration(RsocketProperties rsocketProperties, OAuth2ResourceServerProperties oAuth2ResourceServerProperties) {
		this.rsocketProperties = rsocketProperties;
		this.oAuth2ResourceServerProperties = oAuth2ResourceServerProperties;
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
	public RSocketMessageHandler messageHandler(RSocketStrategies strategies) {
		RSocketMessageHandler handler = new RSocketMessageHandler();
		handler.getArgumentResolverConfigurer().addCustomResolver(new AuthenticationPrincipalArgumentResolver());
		handler.setRSocketStrategies(strategies);
		return handler;
	}

	@Bean
	public PayloadSocketAcceptorInterceptor rsocketInterceptor(RSocketSecurity rsocket) {
		rsocket
				.authorizePayload(authorize -> authorize
						.route("demo.*").permitAll()
						.route("users.me").authenticated()
						.route("users").hasRole("ADMIN")
						.anyRequest().authenticated()
						.anyExchange().permitAll())
				.jwt(Customizer.withDefaults());
		return rsocket.build();
	}

	@Bean
	public ReactiveJwtDecoder jwtDecoder() {
		return ReactiveJwtDecoders
				.fromIssuerLocation(oAuth2ResourceServerProperties.getJwt().getIssuerUri());
	}

	@MessageExceptionHandler
	public Mono<ErrorDto> handleException(Exception e) {
		return Mono.fromCallable(() -> {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			return ErrorDto.builder()
					.message(e.getMessage())
					.stackTrace(errors.toString())
					.build();
		});
	}
}
