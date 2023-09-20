# Getting Started
Start zipkin service to collect logs:
>docker run -d -p 9411:9411 openzipkin/zipkin

and the URL of the service is:
> http://localhost:9411/

We start the local service in docker
>docker run -d -p 9090:9090 prom/prometheus

and it runs in 
> http://localhost:9090/

### Reference Documentation
For further reference, please consider the following sections:

* [Official Gradle documentation](https://docs.gradle.org)
* [Spring Boot Gradle Plugin Reference Guide](https://docs.spring.io/spring-boot/docs/3.1.3/gradle-plugin/reference/html/)
* [Create an OCI image](https://docs.spring.io/spring-boot/docs/3.1.3/gradle-plugin/reference/html/#build-image)
* [Gateway](https://docs.spring.io/spring-cloud-gateway/docs/current/reference/html/)
* [Eureka Discovery Client](https://docs.spring.io/spring-cloud-netflix/docs/current/reference/html/#service-discovery-eureka-clients)
* [Resilience4J](https://docs.spring.io/spring-cloud-circuitbreaker/docs/current/reference/html/#configuring-resilience4j-circuit-breakers)
* [Spring Boot DevTools](https://docs.spring.io/spring-boot/docs/3.1.3/reference/htmlsingle/index.html#using.devtools)

### Guides
The following guides illustrate how to use some features concretely:

* [Using Spring Cloud Gateway](https://github.com/spring-cloud-samples/spring-cloud-gateway-sample)
* [Service Registration and Discovery with Eureka and Spring Cloud](https://spring.io/guides/gs/service-registration-and-discovery/)

### Additional Links
These additional references should also help you:

* [Gradle Build Scans – insights for your project's build](https://scans.gradle.com#gradle)

