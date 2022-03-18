package xyz.heckman.demo.rsocket.router;

import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import xyz.heckman.demo.rsocket.dto.UserDto;

@Slf4j
@Controller
public class UserRouter {

	@MessageMapping("users.me")
	public Mono<UserDto> currentUserRequest() {
		return Mono.just(UserDto.builder().build());
	}

	@MessageMapping("users")
	public Flux<UserDto> userRequest() {
		return Flux.just(UserDto.builder().build());
	}
}
