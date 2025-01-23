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
            .route("calender-service", r -> r.path("/test/**")
                .uri("lb://calender-service"))
            .build();

    }


}
