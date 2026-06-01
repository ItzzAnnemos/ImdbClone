package mk.ukim.finki.imdbclone.config.security;

import mk.ukim.finki.imdbclone.security.CustomUsernamePasswordAuthenticationProvider;
import mk.ukim.finki.imdbclone.web.filters.JwtFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@Profile("jwt")
@EnableWebSecurity
public class JwtSecurityWebConfig {

    private final CustomUsernamePasswordAuthenticationProvider authenticationProvider;
    private final JwtFilter jwtFilter;
    private final List<String> allowedOrigins;

    public JwtSecurityWebConfig(CustomUsernamePasswordAuthenticationProvider authenticationProvider,
                                JwtFilter jwtFilter,
                                @Value("${app.cors.allowed-origins:http://localhost:3000,http://127.0.0.1:3000}")
                                List<String> allowedOrigins) {
        this.authenticationProvider = authenticationProvider;
        this.jwtFilter = jwtFilter;
        this.allowedOrigins = allowedOrigins;
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.setAllowedOriginPatterns(allowedOrigins);
        corsConfiguration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        corsConfiguration.setAllowedHeaders(List.of("*"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration);
        return source;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(corsCustomizer -> corsCustomizer.configurationSource(corsConfigurationSource()))
                .authorizeHttpRequests(authorizeHttpRequestsCustomizer -> authorizeHttpRequestsCustomizer
                        .requestMatchers(
                                "/swagger-ui/**",
                                "/v3/api-docs/**",
                                "/api/user/register",
                                "/api/user/login",
                                // ----- Movies: public browse endpoints -----
                                "/api/movies",
                                "/api/movies/recent",
                                "/api/movies/top-rated",
                                "/api/movies/top-250",
                                "/api/movies/most-popular",
                                "/api/movies/genre-ranked/**",
                                "/api/movies/{id}",
                                "/api/movies/{id}/similar",
                                "/api/movies/genre/**",
                                "/api/movies/year/**",
                                "/api/movies/year-range",
                                "/api/movies/director",
                                // ----- TV series: public browse endpoints -----
                                "/api/tv-series",
                                "/api/tv-series/recent",
                                "/api/tv-series/top-rated",
                                "/api/tv-series/top-250",
                                "/api/tv-series/most-popular",
                                "/api/tv-series/genre-ranked/**",
                                "/api/tv-series/{id}",
                                "/api/tv-series/{id}/similar",
                                "/api/tv-series/status",
                                // ----- Global search (media + people + year) -----
                                "/api/search",
                                "/api/genres",
                                "/api/persons",
                                "/api/persons/{id}",
                                "/api/persons/search",
                                "/api/persons/born-today",
                                "/api/persons/most-popular",
                                "/api/ratings/media/**",
                                "/api/reviews/media/**")
                        .permitAll()
                        .anyRequest()
                        .authenticated())
                .sessionManagement(sessionManagementConfigurer -> sessionManagementConfigurer
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}
