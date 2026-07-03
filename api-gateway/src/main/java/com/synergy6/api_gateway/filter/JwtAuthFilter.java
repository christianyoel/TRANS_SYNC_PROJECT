package com.synergy6.api_gateway.filter;

import com.synergy6.api_gateway.security.JwtValidator;
import io.jsonwebtoken.Claims;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class JwtAuthFilter implements GlobalFilter, Ordered {

    private final JwtValidator jwtValidator;

    public JwtAuthFilter(JwtValidator jwtValidator) {
        this.jwtValidator = jwtValidator;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String path = request.getURI().getPath();
        HttpMethod method = request.getMethod();

        if (HttpMethod.OPTIONS.equals(method) || isPublic(path, method)) {
            return chain.filter(exchange);
        }

        String authHeader = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        try {
            Claims claims = jwtValidator.validate(authHeader.substring(7));
            String rol = claims.get("rol", String.class);

            if (requiresAdmin(path, method) && !"ADMIN".equals(rol)) {
                exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
                return exchange.getResponse().setComplete();
            }

            ServerHttpRequest mutated = request.mutate()
                    .header("X-User-Id", String.valueOf(claims.get("userId")))
                    .header("X-User-Rol", rol)
                    .header("X-User-Email", claims.getSubject())
                    .build();

            return chain.filter(exchange.mutate().request(mutated).build());
        } catch (Exception e) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }
    }

    private boolean isPublic(String path, HttpMethod method) {
        if (path.startsWith("/actuator/")) {
            return true;
        }
        if ("/api/auth/login".equals(path) && HttpMethod.POST.equals(method)) {
            return true;
        }
        if ("/api/pasajes/buscar".equals(path) && HttpMethod.GET.equals(method)) {
            return true;
        }
        if (path.startsWith("/api/viajes") && HttpMethod.GET.equals(method)) {
            return true;
        }
        // Seguimiento público de encomiendas (sin autenticación)
        if ("/api/encomiendas/seguimiento".equals(path) && HttpMethod.GET.equals(method)) {
            return true;
        }
        return false;
    }

    private boolean requiresAdmin(String path, HttpMethod method) {
        if (path.startsWith("/api/viajes") && (HttpMethod.POST.equals(method) || HttpMethod.PATCH.equals(method))) {
            return true;
        }
        // Encomiendas: COUNTER puede GET y POST (listar y registrar).
        // Solo ADMIN puede modificar (PUT/PATCH) o eliminar (DELETE).
        if (path.startsWith("/api/encomiendas")) {
            if (HttpMethod.GET.equals(method) || HttpMethod.POST.equals(method)) {
                return false; // COUNTER y ADMIN permitidos
            }
            return true; // PUT, PATCH, DELETE solo ADMIN
        }
        if (path.startsWith("/api/usuarios")) {
            if (HttpMethod.GET.equals(method) && "/api/usuarios".equals(path)) {
                return true;
            }
            if (HttpMethod.POST.equals(method) || HttpMethod.PUT.equals(method) || HttpMethod.PATCH.equals(method)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int getOrder() {
        return -1;
    }
}
