package xyz.heckman.demo.rsocket.router;

import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import xyz.heckman.demo.rsocket.dto.DemoDataDto;
import xyz.heckman.demo.rsocket.dto.ErrorDto;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.Duration;

@Slf4j
@Controller
public class DemoRouter {

	@MessageMapping("demo.stream")
	public Flux<DemoDataDto> demoStream(Mono<DemoDataDto> dataDtoMono) {
		return dataDtoMono
				.doOnNext(dataDto -> log.info("Stream request {}", dataDto))
				.flatMapMany(dataDto -> Flux.interval(Duration.ofSeconds(1))
				.map(count -> count + dataDto.getCount())
				.switchMap(count -> count < 10
						? Mono.just(DemoDataDto.builder().count(count).build())
						: Mono.error(new Exception("Count Exception"))));
	}

	@MessageMapping("demo.requestResponse")
	public Mono<DemoDataDto> demoRequestResponse(Mono<DemoDataDto> dataDtoMono) {
		return dataDtoMono
				.doOnNext(dataDto -> log.info("Response request {}", dataDto))
				.map(dataDto -> DemoDataDto.builder().count(dataDto.getCount()).build());
	}

	@MessageMapping("demo.fireForget")
	public Mono<Void> demoFireForget(Mono<DemoDataDto> dataDtoMono) {
		return dataDtoMono
				.doOnNext(dataDto -> log.info("Fire and Forget request {}", dataDto))
				.then();
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
