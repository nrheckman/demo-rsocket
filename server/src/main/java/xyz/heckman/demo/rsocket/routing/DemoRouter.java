package xyz.heckman.demo.rsocket.routing;

import lombok.Builder;
import lombok.Getter;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.Serializable;
import java.time.Duration;

@Controller
public class DemoRouter {

	@MessageMapping("demoStream")
	public Flux<DemoDataDto> demoStream() {
		return Flux.interval(Duration.ofSeconds(1))
				.switchMap(count -> count < 10
						? Mono.just(DemoDataDto.builder().count(count).build())
						: Mono.error(new Exception("Count Exception")));
	}

	@Getter
	@Builder
	public static class DemoDataDto implements Serializable {
		private static final long serialVersionUID = 6605059569794491243L;
		private long count;
	}
}
