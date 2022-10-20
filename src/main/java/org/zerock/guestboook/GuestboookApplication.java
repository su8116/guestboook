package org.zerock.guestboook;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing //JPA의 컨텍스트 변경 사항을 감시하는 역할
public class GuestboookApplication {

	public static void main(String[] args) {
		SpringApplication.run(GuestboookApplication.class, args);
	}

}
