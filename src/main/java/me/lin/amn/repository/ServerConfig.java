package me.lin.amn.repository;

import me.lin.amn.repository.controller.ControllerConfig;
import me.lin.amn.repository.dao.RepositoryConfig;
import me.lin.amn.repository.model.ModelConfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * config java file, mainly for spring mvc.
 * Created by Lin on 6/20/16.
 */
@Configuration
//do NOT use @ComponentScan here
@Import({RepositoryConfig.class, ModelConfig.class, ControllerConfig.class})
public class ServerConfig {
}
