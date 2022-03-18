package xyz.heckman.demo.rsocket.configuration.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;

@Getter
@Setter
@ConfigurationProperties("rsocket")
public class RsocketProperties {
	private Resume resume;

	@Getter
	@Setter
	public static class Resume {
		private Duration sessionDuration = Duration.ofMinutes(15);
		private Duration retryDelay = Duration.ofSeconds(5);
	}
}
