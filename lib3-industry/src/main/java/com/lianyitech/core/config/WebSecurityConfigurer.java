package com.lianyitech.core.config;

import com.lianyitech.common.config.Oauth2Configurer;
import com.lianyitech.oauth2.config.IportalClientConfigurer;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.InMemoryTokenStore;

import static com.lianyitech.core.config.ConfigurerConstants.OAUTH2_IP;

/**
 * Created by tangwei on 2016/10/12.
 */
@Configuration
@EnableResourceServer
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true)
public class WebSecurityConfigurer extends IportalClientConfigurer {

    @Autowired
    private Environment environment;

    @Autowired
    private TokenStore tokenStore;

    @Autowired
    private ResourceServerTokenServices tokenService;

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
        resources.resourceId("iportal").stateless(false)
                .tokenServices(tokenService)
                .tokenStore(tokenStore);
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/api/download", "/api/upload", "/uploadfiles/**", "/api/catalog/bookdirectory/**").permitAll()
                .antMatchers("/swagger-ui.html", "/webjars/**", "/swagger-resources/**", "/v2/api-docs", "/configuration/**").permitAll()
                .antMatchers("/druid/**").permitAll()
                .anyRequest().authenticated()
                .and().cors();
    }

    @Override
    @Bean
    public TokenStore redisTokenStore(JedisConnectionFactory redisConnectionFactory) {
        return super.redisTokenStore(redisConnectionFactory);
    }

    @Override
    @Bean
    public ResourceServerTokenServices tokenService(TokenStore redisTokenStore) {
        return super.tokenService(redisTokenStore);
    }
}
