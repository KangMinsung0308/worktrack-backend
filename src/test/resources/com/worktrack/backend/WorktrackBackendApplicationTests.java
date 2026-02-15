package com.worktrack.backend;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")  // application-test.yml 매핑
class WorktrackBackendApplicationTests {

	@Test
	void contextLoads() {
	}

}
