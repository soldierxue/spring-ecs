package springboot;

import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.commons.util.InetUtils;
import org.springframework.cloud.config.server.EnableConfigServer;
import org.springframework.cloud.netflix.eureka.EurekaInstanceConfigBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.web.client.RestTemplate;

import com.netflix.appinfo.AmazonInfo;
/**
 * Spring boot demo: Non Web Application Hello World!
 *
 */
@EnableConfigServer
@EnableDiscoveryClient
@SpringBootApplication

public class App 
{
	@Value("${server.port}")
	private String servicePort;	
    @Bean
    @Profile("ecs")
    public EurekaInstanceConfigBean eurekaInstanceConfig(InetUtils inetUtils) {
      EurekaInstanceConfigBean b = new EurekaInstanceConfigBean(inetUtils);
      AmazonInfo info = AmazonInfo.Builder.newBuilder().autoBuild("eureka");
      b.setDataCenterInfo(info);
      int containerPort = b.getNonSecurePort();
      String hostname = b.getHostname();
      String ip = b.getIpAddress();
      System.out.println("ECS host & Port testing,Container Port #"+containerPort+",host#"+hostname+",ip#"+ip);
      System.out.println("ECS host & Port testing server.port#"+this.servicePort);
      String mappingPort = System.getenv("PORT_"+this.servicePort);
      Map<String,String> envs = System.getenv();
      for(String key : envs.keySet()){
    	  System.out.println("key#"+key+" | value#"+envs.get(key));
    	  if(key.equals("PORT_TCP_"+this.servicePort) || key.equals("PORT_"+this.servicePort)){
    		  mappingPort = envs.get(key);
    	  }
      }
      System.out.println(("ECS host & Port testing,mapping port"+mappingPort));
      if(mappingPort != null && mappingPort.length() > 0)
    	  b.setNonSecurePort(Integer.parseInt(mappingPort));
      b.setHostname(info.get(AmazonInfo.MetaDataKey.localHostname));
      b.setIpAddress(info.get(AmazonInfo.MetaDataKey.localIpv4));
      b.setPreferIpAddress(true);
      containerPort = b.getNonSecurePort();
      hostname = b.getHostname();
      ip = b.getIpAddress();
      System.out.println("ECS host & Port testing,Container Port #"+containerPort+",host#"+hostname+",ip#"+ip);    
      
      return b;
    }
    public static void main( String[] args )
    {
        SpringApplication.run(App.class,args);
    }
}
