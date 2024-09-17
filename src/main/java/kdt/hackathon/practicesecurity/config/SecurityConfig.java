package kdt.hackathon.practicesecurity.config;

import kdt.hackathon.practicesecurity.OAuth2.OAuth;
import kdt.hackathon.practicesecurity.handler.LoginFailHandler;
import kdt.hackathon.practicesecurity.service.UserDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

// 로그인 : (누구인지 확인) -> 인증(어센틱)
// 권한 확인 : 인가(어쏘) [관리자는 관리자페이지, 일반사용자는 X]
@RequiredArgsConstructor
@Configuration
// @EnableWebSecurity(debug = true)
public class SecurityConfig {
    private final UserDetailService userService;

    // 1. 기존 스프링 시큐리티 기능 비활성화 (쓸모없는게 많음)
    @Bean
    public WebSecurityCustomizer configure(){
        return (web) -> web.ignoring()
                .requestMatchers("/static/**");
        // 인증 인가 서비스를 위 url 에는 적용하지 않겠다.
    }

    // 2. 특정 요청에 대한 웹 기반 보안 구성
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                // url 권한 메핑
                .authorizeHttpRequests((authorize) -> authorize
                        .requestMatchers("/admin").hasRole("ADMIN")
//                      .requestMatchers("/service").hasAnyRole("USER", "ADMIN")
                        .requestMatchers("/service").authenticated()
                        .anyRequest().permitAll() // 접근 허용
                )
                // 핸들러 처리
                .exceptionHandling()
                .accessDeniedHandler(new CustomAccessDeniedHandler())

                // 로그인
                .and()
                .formLogin()
                .loginPage("/login")
                .failureUrl("/login-error")
                .defaultSuccessUrl("/service")
                .failureHandler(new LoginFailHandler())

                // 로그아웃
                .and()
                .logout()
                .logoutUrl("/logout")
                .invalidateHttpSession(true)
                .clearAuthentication(true)
                .deleteCookies("JSESSIONID")
                .logoutSuccessUrl("/login")

                // Oauth2 (구글)
                .and()
                .oauth2Login()
                .loginPage("/login")  // 로그인 페이지 설정
                .successHandler(new OAuth());

 //               .failureUrl("/login-error")  // 로그인 실패 시 이동할 URL
//                .userInfoEndpoint() // OAuth2 로그인 성공 후 사용자 정보를 가져올 때의 설정
//                .userService(oAuth2UserCustomService); // 사용자 정보를 처리하는 서비스

        // 세션 조정과 동시에 사용할수는 없나?
        //Spring Security에서는 OAuth2 인증이 완료된 후, 해당 인증 정보가 세션에 저장
        // 그러나 세션 관리 방식이 SessionCreationPolicy.ALWAYS나 IF_REQUIRED로 설정되면,
                                    // OAuth2 인증 정보와 세션 관리 방식 간에 충돌이 발생
//                .and()
//                .sessionManagement()
//                .sessionFixation().changeSessionId()
//                .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
//                .invalidSessionUrl("/")
//                .maximumSessions(1)
//                .maxSessionsPreventsLogin(false);

        return http.build();
    }

    // 3. 인증 관리자 관련 설정
    // * 사용자 정보를 가져올 서비스 재정의 , 인증방법 등을 설정 (LDAP / JDBC 기반)
    //      ## 유저디테일서비스(내 서비스단), 유저디테일'스'서비스(시큐리티 인터페이스 : 사용안함)
    @Bean
    public AuthenticationManager authenticationManager
    (HttpSecurity http,
     BCryptPasswordEncoder bCryptPasswordEncoder,
     UserDetailService userDetailService
     ) throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class)
                .userDetailsService(userService) // 사용자 정보 서비스 "설정"
                //  userService <- implements UserDetailsService (서비스단 클래스)
                .passwordEncoder(bCryptPasswordEncoder)
                .and()
                .build();
    }// authenticationManager + builder 를 통해 사용자 로그인 계정을 설정



    // 4. 패스워드 인코더로 사용할 빈 등록(아마 시큐리티 기본제공 pw 암호화)
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }


}
