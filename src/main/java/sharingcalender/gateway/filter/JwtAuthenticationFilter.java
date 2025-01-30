package sharingcalender.gateway.filter;

import io.jsonwebtoken.Jwts;
import java.util.Date;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import sharingcalender.gateway.config.JwtAuthenticationConfig;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter implements GlobalFilter, Ordered {

    private final JwtAuthenticationConfig jwtAuthenticationConfig;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        // msa 다른 서비스 요청 넘기기전에 실행할 로직

        String token = getToken(exchange);

        if (token == null) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        if (token.equals("ANONYMOUS")) {
            return chain.filter(exchange);
        }

        if (!isValidToken(token)) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        if (isExpired(token)) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        return chain.filter (exchange);
    }


    private String getToken(ServerWebExchange exchange) {
        String authorization = exchange.getRequest().getHeaders()
            .getFirst(HttpHeaders.AUTHORIZATION);

        if (authorization != null && authorization.startsWith("Bearer ")) {
            return authorization.substring(7);
        }

        return null;
    }

    private boolean isValidToken(String token) {

        try {
            System.out.println(jwtAuthenticationConfig.getSecretKey());

            Jwts.parser().verifyWith(jwtAuthenticationConfig.getSecretKey()).build()
                .parseSignedClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private boolean isExpired(String token) {

        return Jwts.parser().verifyWith(jwtAuthenticationConfig.getSecretKey()).build()
            .parseSignedClaims(token).getPayload().getExpiration().before(new Date());
    }

    @Override
    public int getOrder() {
        return -1;
    }
}
