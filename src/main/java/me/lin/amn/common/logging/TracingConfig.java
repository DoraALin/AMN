package me.lin.amn.common.logging;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * Created by Lin on 6/17/16.
 */
@Configuration
@EnableAspectJAutoProxy
@ComponentScan
public class TracingConfig {

    @Bean
    public Tracing tracing() {
        return new Tracing();
    }
}
