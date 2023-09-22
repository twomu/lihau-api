package com.lhj.apigateway.config;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author lihua
 */
@Configuration
public class GatewayConfig {
    @Bean
    public RouteLocator myRoutes(RouteLocatorBuilder builder) {
        return builder.routes()
                // Add a simple re-route from: /get to: http://httpbin.org:80
                // Add a simple "Hello:World" HTTP Header
                .route("name-interface",p -> p
                        .path("/api/**") // intercept calls to the /get path
                        .filters(f -> f.addRequestHeader("Hello", "World")) // add header
                        .uri("http://127.0.0.1:9091"))
                .route("random-interface",p -> p
                        .path("/api/random/**") // intercept calls to the /get path
                        .filters(f -> f.addRequestHeader("Hello", "World")) // add header
                        .uri("http://127.0.0.1:9091")) // forward to httpbin
                .route("day-interface",p -> p
                        .path("/api/day/**") // intercept calls to the /get path
                        .filters(f -> f.addRequestHeader("Hello", "World")) // add header
                        .uri("http://127.0.0.1:9091")) // forward to httpbin
                .build();
    }
}
