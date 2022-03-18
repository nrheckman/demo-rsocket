package xyz.heckman.demo.rsocket.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.io.Serializable;

@Getter
@Builder
@ToString
public class DemoDataDto implements Serializable {
	private static final long serialVersionUID = 6605059569794491243L;
	private long count;
}
