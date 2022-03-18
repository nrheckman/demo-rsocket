package xyz.heckman.demo.rsocket.routing;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.Serializable;
import java.time.Duration;

@Slf4j
@Controller
public class DemoRouter {

	@MessageMapping("demo.stream")
	public Flux<DemoDataDto> demoStream(Mono<DemoDataDto> dataDtoMono) {
		return dataDtoMono.flatMapMany(dataDto -> Flux.interval(Duration.ofSeconds(1))
				.map(count -> count + dataDto.getCount())
				.switchMap(count -> count < 10
						? Mono.just(DemoDataDto.builder().count(count).build())
						: Mono.error(new Exception("Count Exception"))));
	}

	@MessageMapping("demo.requestResponse")
	public Mono<DemoDataDto> demoRequestResponse(Mono<DemoDataDto> dataDtoMono) {
		return dataDtoMono
				.map(dataDto -> DemoDataDto.builder().count(dataDto.getCount()).build());
	}

	@MessageMapping("demo.fireForget")
	public Mono<Void> demoFireForget(Mono<DemoDataDto> dataDtoMono) {
		return dataDtoMono
				.doOnNext(dataDto -> log.info("Received {}", dataDto))
				.then();
	}

	@Getter
	@Builder
	@ToString
	public static class DemoDataDto implements Serializable {
		private static final long serialVersionUID = 6605059569794491243L;
		private long count;
	}
}
