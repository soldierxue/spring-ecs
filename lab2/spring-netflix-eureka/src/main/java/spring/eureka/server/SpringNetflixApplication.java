package spring.eureka.server;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;
//import org.springframework.cloud.netflix.zuul.EnableZuulServer;

@EnableEurekaServer
@SpringBootApplication
public class SpringNetflixApplication {

	public static void main(String[] args) {
		new SpringApplicationBuilder(SpringNetflixApplication.class).web(true).run(args);
		// SpringApplication.run(SpringNetflixApplication.class, args);
	}
}
