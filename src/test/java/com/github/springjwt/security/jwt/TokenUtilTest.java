package com.github.springjwt.security.jwt;

import io.jsonwebtoken.Clock;
import org.assertj.core.util.DateUtil;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.Mockito.when;

@SpringBootTest
public class TokenUtilTest {
    private final String USERNAME = "testUser";

    @Mock
    private Clock clockMock;
    @InjectMocks
    private TokenUtil tokenUtil;

    @Before
    public void setup(){
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGenerateTokenFromDifferentDates() {
        when(clockMock.now())
                .thenReturn(DateUtil.yesterday())
                .thenReturn(DateUtil.now());

            String token = createToken();
            String tokenLater = createToken();

            assertThat(token).isNotEqualTo(tokenLater);
    }

    private String createToken() {
        return tokenUtil.generateToken(new TestUser(USERNAME));
    }
}
