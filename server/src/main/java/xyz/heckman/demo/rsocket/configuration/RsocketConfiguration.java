package xyz.heckman.demo.rsocket.configuration;

import io.rsocket.core.Resume;
import org.springframework.boot.rsocket.server.RSocketServerCustomizer;
import org.springframework.context.annotation.Bean;
import reactor.util.retry.Retry;

import java.time.Duration;

public class RsocketConfiguration {

	@Bean
	public RSocketServerCustomizer rSocketResume() {
		Resume resume =
				new Resume()
						.sessionDuration(Duration.ofMinutes(15))
						.retry(
								Retry.fixedDelay(Long.MAX_VALUE, Duration.ofSeconds(5))
										.doBeforeRetry(s -> log.debug("Disconnected. Trying to resume...")));
		return rSocketServer -> rSocketServer.resume(resume);
	}
}
