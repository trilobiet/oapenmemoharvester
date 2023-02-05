package org.oapen.memoproject.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(
	basePackages = "org.oapen.memoproject.dataingestion"
	//, enableDefaultTransactions = false
)
public class JpaConfig {}
