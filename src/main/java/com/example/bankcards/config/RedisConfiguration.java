package com.example.bankcards.config;

import com.example.bankcards.entity.RefreshToken;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisKeyValueAdapter;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.redis.core.convert.KeyspaceConfiguration;

import java.time.Duration;
import java.util.Collections;

@Configuration
@EnableRedisRepositories(
        keyspaceConfiguration = RedisConfiguration.RefreshTokenKeyspaceConfiguration.class,
        enableKeyspaceEvents = RedisKeyValueAdapter.EnableKeyspaceEvents.OFF
)
public class RedisConfiguration {

    @Value("${app.jwt.refreshTokenExpiration}")
    private Duration refreshTokenExpiration;

    @Value("${spring.data.redis.host}")
    private String redisHost;

    @Value("${spring.data.redis.port}")
    private int redisPort;

    @Bean
    public LettuceConnectionFactory redisConnectionFactory() {
        RedisStandaloneConfiguration redisConfig = new RedisStandaloneConfiguration(redisHost, redisPort);
        return new LettuceConnectionFactory(redisConfig);
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate(LettuceConnectionFactory connectionFactory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);
        return template;
    }

    public class RefreshTokenKeyspaceConfiguration extends KeyspaceConfiguration {

        private static final String REFRESH_TOKEN_KEYSPACE = "refresh_token";

        @Override
        protected Iterable<KeyspaceSettings> initialConfiguration() {
            KeyspaceSettings settings = new KeyspaceSettings(RefreshToken.class, REFRESH_TOKEN_KEYSPACE);
            settings.setTimeToLive(refreshTokenExpiration.getSeconds());
            return Collections.singleton(settings);
        }
    }
}
