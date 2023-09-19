package com.jp.gateway.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.RequestMapping;
import reactor.core.publisher.Mono;

@Configuration
public class GatewayConfig {
    //cambiar el perfil de acuerdo al bean que necesitemos
    @Bean
    @Profile("localhostRouter-noEureka")
    public RouteLocator configLocalNoEureka(RouteLocatorBuilder builder) {
        // Esta configuración es funcional, pero tiene un problema y es que no utiliza Eureka, por lo que
        // si se registran nuevas instancias, no serán incluidas en el enrutamiento.
        // La redirección se hace por la URL, no por el nombre del servicio. Es decir, tenemos quemadas las URL.
        return builder.routes()
                .route(r -> r.path("/api/v1/dragonball/*").uri("http://localhost:8086"))
                .route(r -> r.path("/api/v1/got/*").uri("http://localhost:8083"))
                .build();
    }

    @Bean
    @Profile("localhostRouter-withEureka")
    public RouteLocator configLocalWithEureka(RouteLocatorBuilder builder) {
        // En esta configuración si utilizamos Eureka. Sin embargo, aquí hay un problema debido a que si se cae una
        // de las instancias registradas, ejemplo de JP-CONFIG(Asumiendo que hay mas de una instancia),
        // a pesar de que se elimina de la lista en el servidor de eureka,
        // este gateway no refresca y sigue enrutando hacia una instancia inexistente.
        return builder.routes()
                .route(r -> r.path("/api/v1/dragonball/*").uri("lb://JP-OTHER-MS"))
                .route(r -> r.path("/api/v1/got/*").uri("lb://JP-ANOTHER-MS"))
                .route(r -> r.path("/application-name").uri("lb://JP-CONFIG"))
                .build();
    }

    @Bean
    @Profile("localhostRouter-EurekaWithCB")
    public RouteLocator configLocalWithEurekaAndCircuitBreaker(RouteLocatorBuilder builder) {

        return builder.routes()
                .route(r -> r.path("/api/v1/dragonball/*").uri("lb://JP-OTHER-MS"))
                .route(r -> r.path("/api/v1/got/*").uri("lb://JP-ANOTHER-MS"))

                // Aquí enrutamos a // application-name que està definido en la uri de eureka: lb://JP-CONFIG,
                .route(r -> r.path("/application-name")
                        // colocamos un filtro, y en caso de falla, vamos a hacer un 'forward' hacia
                        // el path '/api/v1/dragonball/names' que está definido en eureka: lb://JP-FAILOVER-MS
                        .filters(f -> {
                            f.circuitBreaker(
                                            c -> c.setName("failoverCB")
                                                    .setFallbackUri("forward:/api/v1/dragonball/names") // en caso de falla, se va por aquí
                                                    .setRouteId("dbFailover"))
                                    .uri("lb://JP-FAILOVER-MS");
                            return f;
                        }).uri("lb://JP-CONFIG"))
                .build();

    }


    @RequestMapping("/fallback")
    public Mono<String> fallback() {
        return Mono.just("fallback");
    }

}
