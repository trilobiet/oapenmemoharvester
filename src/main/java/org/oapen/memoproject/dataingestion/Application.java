package org.oapen.memoproject.dataingestion;

import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication(scanBasePackages = { "org.oapen.memoproject" })
public class Application {

	public static void main(String[] args) {
		new SpringApplicationBuilder(Application.class)
         .web(WebApplicationType.NONE) // Console app, no need for web server
         .run(args);
	}

}