package com.example.jpashop.redis;

import com.example.jpashop.config.CacheConfig;
import com.example.jpashop.config.RedisConfig;
import com.example.jpashop.config.SecurityConfig;
import com.example.jpashop.jwt.JWTUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@Import({SecurityConfig.class, RedisConfig.class, CacheConfig.class})// 테스트용 보안 설정 적용
public class RedisConfigTest {

    @MockBean
    private JWTUtil jwtUtil; // JWTUtil을 Mocking

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Test
    public void testRedisConnection() {
        // Given
        String key = "testKey";
        String value = "testValue";

        // When
        ValueOperations<String, Object> ops = redisTemplate.opsForValue();
        ops.set(key, value);

        // Then
        Object result = ops.get(key);
        assertThat(result).isEqualTo(value);
    }
}