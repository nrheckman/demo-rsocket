package xyz.heckman.demo.rsocket.router;

import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import xyz.heckman.demo.rsocket.dto.UserDto;

@Slf4j
@Controller
public class UserRouter {

	@MessageMapping("users.me")
	public Mono<UserDto> currentUserRequest(@AuthenticationPrincipal Object principal) {
		return Mono.just(UserDto.builder().username(usernameFromPrincipal(principal)).build());
	}

	@MessageMapping("users")
	public Flux<UserDto> userRequest(@AuthenticationPrincipal Jwt principal) {
		return Flux.just(UserDto.builder().username(usernameFromPrincipal(principal)).build());
	}

	private String usernameFromPrincipal(Object principal) {
		if (principal instanceof Jwt) {
			Jwt jwt = (Jwt)principal;
			return jwt.getClaimAsString("preferred_username");
		}
		return null;
	}
}
