package hello.springtx.exception;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
public class RollbackTest {

    @Autowired RollbackService service;

    @Test
    void runtimeException() {
        Assertions.assertThatThrownBy(() -> service.runtimeException())
                .isInstanceOf(RuntimeException.class);
        //Rolling back JPA transaction on EntityManager
    }

    @Test
    void checkedException() {
        Assertions.assertThatThrownBy(() -> service.checkedException())
                .isInstanceOf(MyException.class);
        //Committing JPA transaction on EntityManager
    }

    @Test
    void rollbackFor() {
        Assertions.assertThatThrownBy(() -> service.rollbackFor())
                .isInstanceOf(MyException.class);
        //Rolling back JPA transaction on EntityManager
    }

    @TestConfiguration
    static class RollbackTestConfig {
        @Bean
        RollbackService rollbackService() {
            return new RollbackService();
        }
    }

    @Slf4j
    static class RollbackService {

        //런타임 예외 발생: 롤백, 언체크
        @Transactional
        public void runtimeException() {
            log.info("call runtimeException");
            throw new RuntimeException(); //런타임의 자식들은 다 롤백됨
        }

        //체크 예외 발생: 커밋
        @Transactional
        public void checkedException() throws MyException { //잡거나 던지거나!
            log.info("call checkedException");
            throw new MyException();
            //예외를 던졌는데 왜 커밋하지?

        }

        //체크 예외 rollbackFor 지정: 롤백
        //checkedException() 이랑 똑같은데 옵션만 다름
        @Transactional(rollbackFor = MyException.class) // 옵션 주기
        public void rollbackFor() throws MyException {
            log.info("call rollbackFor");
            throw new MyException();
        }
    }

    static class MyException extends Exception {
    }

}
