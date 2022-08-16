package org.example.global.config.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.domain.member.service.CustomUserDetailsService;
import org.example.global.result.ResultCode;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationEntryPointFailureHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.CorsUtils;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    // auth 필요없는 whitelist
    private static final String[] AUTH_WHITELIST = {"/login", "/login/recovery", "/accounts", "/**/without",
            "/accounts/password/email",
            "/accounts/password/reset", "/reissue", "/accounts/email", "/accounts/check", "/logout/only/cookie", "/ws-connection/**"};
    private static final String[] AUTH_WHITELIST_STATIC = {"/static/css/**", "/static/js/**", "*.ico"};


    // service
    private final CustomUserDetailsService customUserDetailsService;
    // provider
    private final CustomAuthenticationProvider customAuthenticationProvider;
    // handler
    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;
    private final CustomAccessDeniedHandler customAccessDeniedHandler;
    private final CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler;
    private final CustomAuthenticationFailureHandler customAuthenticationFailureHandler;
    // filter
    private final CustomExceptionHandleFilter customExceptionHandleFilter;

//    private final ResetPasswordCodeUserDetailService resetPasswordCodeUserDetailService;
//    private final CustomUserDetailsService jwtUserDetailsService;
//    private final EmailCodeService emailCodeService;
//    // Provider
//    private final JwtAuthenticationProvider jwtAuthenticationProvider;
//    private final ReissueAuthenticationProvider reissueAuthenticationProvider;
//    // Handler
//    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;
//    private final CustomAccessDeniedHandler customAccessDeniedHandler;
//    private final CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler;
//    private final CustomAuthenticationFailureHandler customAuthenticationFailureHandler;
//    // Filter
//    private final CustomExceptionHandleFilter customExceptionHandleFilter;

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // client -> daoAuthenticationprovider : 인증 처리 위임
    // daoAuthenticationProvider -> UserDetailService 사용자 정보 가져오게 한다.
    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setPasswordEncoder(bCryptPasswordEncoder());
        daoAuthenticationProvider.setUserDetailsService(customUserDetailsService);
        return daoAuthenticationProvider;
    }

    // password code reset -> 나중에
/*
    @Bean
    public ResetPasswordCodeAuthenticationProvider resetPasswordCodeAuthenticationProvider() {
        return new ResetPasswordCodeAuthenticationProvider(resetPasswordCodeUserDetailService, emailCodeService);
    }
*/

    // 인증이 되지않은 유저가 요청을 했을때 동작된다.
    @Bean
    public AuthenticationEntryPointFailureHandler authenticationEntryPointFailureHandler() {
        return new AuthenticationEntryPointFailureHandler(customAuthenticationEntryPoint);
    }

/*
    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() throws Exception {
        final List<String> skipPaths = new ArrayList<>();
        skipPaths.addAll(Arrays.stream(AUTH_WHITELIST_SWAGGER).collect(Collectors.toList()));
        skipPaths.addAll(Arrays.stream(AUTH_WHITELIST_STATIC).collect(Collectors.toList()));
        skipPaths.addAll(Arrays.stream(AUTH_WHITELIST).collect(Collectors.toList()));
        final RequestMatcher matcher = new CustomRequestMatcher(skipPaths);
        final JwtAuthenticationFilter filter = new JwtAuthenticationFilter(matcher, jwtUtil);

        filter.setAuthenticationManager(super.authenticationManager());
        filter.setAuthenticationFailureHandler(authenticationEntryPointFailureHandler());

        return filter;
    }
*/

    @Bean
    public CustomUsernamePasswordAuthenticationFilter customUsernamePasswordAuthenticationFilter()
            throws Exception {
        final CustomUsernamePasswordAuthenticationFilter filter =
                new CustomUsernamePasswordAuthenticationFilter();
        filter.setAuthenticationManager(super.authenticationManager());
        filter.setAuthenticationSuccessHandler(customAuthenticationSuccessHandler);
        filter.setAuthenticationFailureHandler(customAuthenticationFailureHandler);
        return filter;
    }

    // Reset Password code -> 나중에
/*
    @Bean
    public ResetPasswordCodeAuthenticationFilter resetPasswordCodeAuthenticationFilter() throws Exception {
        final ResetPasswordCodeAuthenticationFilter filter = new ResetPasswordCodeAuthenticationFilter();
        filter.setAuthenticationManager(super.authenticationManager());
        filter.setAuthenticationSuccessHandler(customAuthenticationSuccessHandler);
        filter.setAuthenticationFailureHandler(customAuthenticationFailureHandler);
        return filter;
    }
*/

    // 왜 Reissue? -> 나중에
/*
    @Bean
    public ReissueAuthenticationFilter reissueAuthenticationFilter() throws Exception {
        final ReissueAuthenticationFilter filter = new ReissueAuthenticationFilter();
        filter.setAuthenticationManager(super.authenticationManager());
        filter.setAuthenticationSuccessHandler(customAuthenticationSuccessHandler);
        filter.setAuthenticationFailureHandler(customAuthenticationFailureHandler);
        return filter;
    }
*/

    // 인증처리 적용
    // AutenticationManagerBuilder에 UserDetailsService 적용
    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(customAuthenticationProvider)
//                .authenticationProvider(resetPasswordCodeAuthenticationProvider())
//                .authenticationProvider(reissueAuthenticationProvider)
                .authenticationProvider(daoAuthenticationProvider());
    }

    // cors 설정 넣고 테스트후에 잘되면 빼고 테스트하기
    @Bean
    public CorsConfigurationSource configurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        configuration.addAllowedOriginPattern("*");
        configuration.addAllowedHeader("*");
        configuration.addAllowedMethod("*");
        configuration.setAllowCredentials(true);
        configuration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }

    // WebSecurity
    // auth 필요없는 path들 모아서 ignoring 시키기
    // H2 도 넣자
    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers(AUTH_WHITELIST_STATIC);
//        web.ignoring().antMatchers(AUTH_WHITELIST_SWAGGER);
    }

    // HttpSecurity
    // 인증 이벤트 처리
    // -
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        configureCustomBeans();

        http.exceptionHandling() // auth 실패할때 handling
                .authenticationEntryPoint(customAuthenticationEntryPoint)
                .accessDeniedHandler(customAccessDeniedHandler);
//        http.sessionManagement()
//                .sessionCreationPolicy(SessionCreationPolicy.STATELESS); // 이건 jwt 써서 그런거니까 빼야지

        http.logout().disable() // logout, formLogin().disable 한다? httpBasic을 disable?
                .formLogin().disable()
                .httpBasic().disable();

        // cors
        http.cors()
                .configurationSource(configurationSource())
                .and()
                .csrf()
                .disable()
                .authorizeRequests()
                .requestMatchers(CorsUtils::isPreFlightRequest)
                .permitAll()
                .antMatchers(AUTH_WHITELIST)
                .permitAll()
                .anyRequest().hasAuthority("ROLE_USER");

        http.addFilterBefore(customAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
        http.addFilterBefore(customExceptionHandleFilter, CustomAuthenticationFilter.class);
        http.addFilterBefore(customUsernamePasswordAuthenticationFilter(), CustomAuthenticationFilter.class);
//        http.addFilterBefore(resetPasswordCodeAuthenticationFilter(), JwtAuthenticationFilter.class);
//        http.addFilterBefore(reissueAuthenticationFilter(), JwtAuthenticationFilter.class);
    }

    // resultcode map
    private void configureCustomBeans() {
        final Map<String, ResultCode> map = new HashMap<>();
        map.put("/login", ResultCode.LOGIN_SUCCESS);
        map.put("/reissue", ResultCode.REISSUE_SUCCESS);
        map.put("/login/recovery", ResultCode.LOGIN_WITH_CODE_SUCCESS);
        customAuthenticationSuccessHandler.setResultCodeMap(map);
    }

}