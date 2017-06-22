package springboot;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.config.server.EnableConfigServer;
/**
 * Spring boot demo: Non Web Application Hello World!
 *
 */
@EnableConfigServer
@EnableDiscoveryClient
@SpringBootApplication

public class App 
{
    public static void main( String[] args )
    {
        SpringApplication.run(App.class,args);
    }
}