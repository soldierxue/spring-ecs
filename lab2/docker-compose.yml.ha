version: "2"
services:
  eureka1:  
    image: ${docker_registry_host}/spring-netflix-eureka-ha:0.0.1-SNAPSHOT
    ports:
      - "8761:8761"
    environment:
      - spring.profiles.active=eureka1
  eureka2:
    image: ${docker_registry_host}/spring-netflix-eureka-ha:0.0.1-SNAPSHOT
    hostname: eureka2
    ports:
      - "8762:8762"
    environment:
      - spring.profiles.active=eureka2
