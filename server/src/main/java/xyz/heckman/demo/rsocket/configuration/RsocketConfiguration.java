package xyz.heckman.demo.rsocket.configuration;

import io.rsocket.core.Resume;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.rsocket.server.RSocketServerCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.util.retry.Retry;
import xyz.heckman.demo.rsocket.configuration.properties.RsocketProperties;

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
}
