package sharingcalender.gateway.config;

import lombok.RequiredArgsConstructor;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class RouteConfig {


    @Bean
    public RouteLocator routeLocator(RouteLocatorBuilder routeBuilder) {

        return routeBuilder.routes()
            .route("calendar-service", r -> r.path("/api/calendar/**")
                .uri("lb://calendar-service"))
            .route("auth-service",r -> r.path("/api/auth/**")
                .uri("lb://auth-service"))
            .build();

    }


}
