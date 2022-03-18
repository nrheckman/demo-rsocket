package xyz.heckman.demo.rsocket.routing;

import lombok.Builder;
import lombok.Getter;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Flux;

import java.time.Duration;

@Controller
public class DemoRouter {

	@MessageMapping("demoStream")
	public Flux<DemoDataDto> demoStream() {
		return Flux.interval(Duration.ofSeconds(1))
				.map(count -> DemoDataDto.builder().count(count).build());
	}

	@Getter
	@Builder
	public static class DemoDataDto {
		private long count;
	}
}
