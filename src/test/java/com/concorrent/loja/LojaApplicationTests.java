package com.concorrent.loja;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
class LojaApplicationTests {

	@Test
	void contextLoads() {
		// Teste vazio apenas para verificar carregamento do contexto
	}
}