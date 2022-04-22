package xyz.heckman.demo.rsocket;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class RsocketApplicationTests {

	@Value("${local.rsocket.server.port}")
	private int rSocketPort;

	@Test
	void contextLoads() {
		System.out.println(String.format("RSocket server running on port: %d", rSocketPort));
	}

}
