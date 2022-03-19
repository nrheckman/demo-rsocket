package xyz.heckman.demo.rsocket.dto;

import lombok.Builder;
import lombok.Getter;

import java.io.Serializable;

@Getter
@Builder
public class ErrorDto implements Serializable {
	private static final long serialVersionUID = -4069741151315247036L;

	private String message;
	private String stackTrace;
}
