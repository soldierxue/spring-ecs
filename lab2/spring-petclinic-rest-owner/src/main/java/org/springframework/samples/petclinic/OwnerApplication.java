/*
 * Copyright 2002-2014 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.samples.petclinic;

import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.commons.util.InetUtils;
import org.springframework.cloud.netflix.eureka.EurekaInstanceConfigBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.web.client.RestTemplate;

import com.netflix.appinfo.AmazonInfo;

/**
 * PetClinic Spring Boot Application.
 * 
 * @author Dave Syer
 *
 */
@EnableDiscoveryClient
@SpringBootApplication
public class OwnerApplication {
	@Bean
	@LoadBalanced
	RestTemplate restTemplate() {
		return new RestTemplate();
	}
	
	@Value("${server.port}")
	private String servicePort;	
	
    public static void main(String[] args) throws Exception {
        SpringApplication.run(OwnerApplication.class, args);
    }
    
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
}

