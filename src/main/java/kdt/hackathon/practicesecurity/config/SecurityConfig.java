package kdt.hackathon.practicesecurity.config;

// 인증을 위한 엔티티 -> 리포 -> 서비스, done(테스트도 성공, ULID로 기본키 생성)
// 실제 인증 처리를 진행하는 설정파일 작성 ****

import kdt.hackathon.practicesecurity.service.UserDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@RequiredArgsConstructor
@Configuration
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
        return http
                .authorizeRequests()
                .requestMatchers("/login", "/signup", "/user").permitAll()
                // 누구나 접근이 가능하도록, 설정한 url (인증+인가) 없이도
                .anyRequest().authenticated() // 그 외는 인가 없어도 인증은 있어야 된다는 설정
                .and()
                .formLogin().loginPage("/login")
                .defaultSuccessUrl("/service") // 임의로 만든 서비스 페이지
                .and()
                .logout().logoutSuccessUrl("/login") // 로그아웃시 이동할 경로 설정
                .invalidateHttpSession(true)// 로그아웃 이후에 세션을 전체 삭제할지 여부 설정
                .and().csrf().disable().build(); // csrf 비활성화
    } // 세션이 적용이 된건가???????????

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
    }



    // 4. 패스워드 인코더로 사용할 빈 등록(아마 시큐리티 기본제공 pw 암호화)
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
  /* 비밀번호 암호화 전략
    - 단방향 해시 함수 ( One-Way Hash Function )
         8d969eef6ecad3c29a3a629280e686cf0c3f5d5a86aff3ca12020c923adc6c92
         (SHA-256 을 통해 123456 을 해싱한 값, 다이제스트라고 하며 입력값에 따라 동일한 결과)
      # 위 다이제스트는 항상 같은 값, -> 레인보우 테이블에 전부 등록되어 있음
      # 무차별 대입공격 (브루트포스) 취약, 해쉬충돌도 고려해야함(두개의 입력이 동일한 값을 가지는 현상) -> 바뀌치기 가

   - 솔트 (Salt)
        * 해시함수를 돌리기 전에 원문에 임의의 문자열을 덧붙이는 것
        password + salt -> Hash -> Digest

        ex:
        사용자1과 사용자2가 123456 이라는 같은 password 를 사용
        사용자1은 솔트 값이 sffs13osz043opq9dsfdkj32 이고, 사용자2는 osela31dif3298hcwaw8s301
        (SHA-256 적용)
        343099b2867417f1b60462a8c70aa9dc33f4b1cec287fdb22e9fcf9b999ee3ce : 사용자1의 암호
        725c8c66c181855dd578961d90b2a051a250b232ede85a7ab5da5d0d4586d135 : 사용자2의 암호

  - 개발 프로세스
        1. HASH 알고리즘 선택(SHA-256) import java.security.MessageDigest;
        2. 난수생성기로 난수 생성하기, import java.security.SecureRandom;
     */