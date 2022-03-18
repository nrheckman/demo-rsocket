package xyz.heckman.demo.rsocket.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.io.Serializable;

@Getter
@Builder
@ToString
public class UserDto implements Serializable {
	private static final long serialVersionUID = 698883988892654979L;

	private String username;
}
