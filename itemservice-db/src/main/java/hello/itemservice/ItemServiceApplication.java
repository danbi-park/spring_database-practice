package hello.itemservice;

import hello.itemservice.config.*;
import hello.itemservice.repository.ItemRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;


@Import(MemoryConfig.class) // MemoryConfig 설정
@SpringBootApplication(scanBasePackages = "hello.itemservice.web") // 지정을 안하면 현재 위치 기준 하위에 있는 전부 컴포넌트 스캔 대상이 됨, 여기서는 컨트롤러만 스캔 대상으로 설정 나머지는 수동으로
public class ItemServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ItemServiceApplication.class, args);
	}

	// 데이터 초기화를 하기 위해 아래 데이터 빈 등록
	@Bean
	@Profile("local")
	public TestDataInit testDataInit(ItemRepository itemRepository) {
		return new TestDataInit(itemRepository);
	}

}
