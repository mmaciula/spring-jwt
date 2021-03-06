package com.github.springjwt.security.jwt;

import io.jsonwebtoken.Clock;
import org.assertj.core.util.DateUtil;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Date;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.assertj.core.api.Java6Assertions.within;
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

        ReflectionTestUtils.setField(tokenUtil, "expiration", 1800L);
        ReflectionTestUtils.setField(tokenUtil, "secret", "mySecret");
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

    @Test
    public void testGetUsernameFromToken() {
        when(clockMock.now()).thenReturn(DateUtil.now());

        final String token = createToken();

        assertThat(tokenUtil.getUsernameFromToken(token)).isEqualTo(USERNAME);
    }

    @Test
    public void testGetIssuedDateFromToken() {
        final Date now = DateUtil.now();

        when(clockMock.now()).thenReturn(now);

        final String token = createToken();

        assertThat(tokenUtil.getIssuedDateFromToken(token)).isInSameMinuteWindowAs(now);
    }

    @Test
    public void testGetExpirationDate() {
        final Date currentDate = DateUtil.now();

        when(clockMock.now()).thenReturn(currentDate);

        final String token = createToken();
        final Date expirationDate = tokenUtil.getExpirationDateFromToken(token);

        assertThat(DateUtil.timeDifference(expirationDate, currentDate)).isCloseTo(1800000L, within(1000L));
    }

    private String createToken() {
        String token = tokenUtil.generateToken(new TestUser(USERNAME));
        return token;
    }
}
