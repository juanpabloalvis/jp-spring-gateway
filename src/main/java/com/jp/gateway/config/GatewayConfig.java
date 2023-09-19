package com.jp.gateway.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

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

}
