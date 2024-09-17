package kdt.hackathon.practicesecurity.service;

import jakarta.servlet.http.HttpSession;
import kdt.hackathon.practicesecurity.entity.User;
import kdt.hackathon.practicesecurity.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import jakarta.servlet.http.HttpServletRequest; // HttpServletRequest를 사용하기 위한 import


import java.util.Collections;


// 1. UserDetailsService :
//          * 사용자에 관한 세부 정보, 계약을 구현하는 객체가 관리
//          * 기본 자격 증명에서 사용자 이름은 'user' 이고 기본 암호는 UUID 형식이다.
//          * 암호는 스프링 컨텍스트가 로드될 때 자동으로 생성된다.
@Slf4j
@RequiredArgsConstructor
@Service
public class UserDetailService implements UserDetailsService { // UserDetailsService 핵심, 컨피그는 필터 설정만

    private final UserRepository userRepository;
    private final HttpServletRequest request;  // HttpServletRequest를 주입 받음


    @Override
    public UserDetails loadUserByUsername(String phoneNumber) throws UsernameNotFoundException {
        log.info("loadUserByUsername() 호출됨 - phoneNumber: {}", phoneNumber);
        // 사용자의 정보를 가져오는 UserDetailsService 구현( 여기서, Role:enum 으로부터 권한도 가지고 온다=어떤 롤인지)

        // 세션 ID 출력
        HttpSession httpSession = request.getSession(false); // 세션을 생성하지 않고 존재하는 세션만 가져옴
        if (httpSession != null) {
            log.info("Session ID: {}", httpSession.getId()); // 세션 ID 출력
        } else {
            log.info("Session is null");
        }


        User user = userRepository.findByPhoneNumber(phoneNumber)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with phone number: " + phoneNumber));
// 여기까지가 기존 서비스에서 진행한 (회원가입 후 디비에 저장하는 부분이고), 아래에 리턴문은 인증정보(인가)를 리턴에서 시큐리티에게 전달(필터)한테

        // 09/15 :
        //   UserDetailService가 UserDetails를 반환하도록 해야 하고,  -> 기존에는 유저를 반환했을꺼임(잘못된 코드)
        //   반환된 UserDetails 객체에서 권한을 확인할 수 있어야 했었다 -> Role 이 Enum class 라 가져오는 것도 쉽지 않았다.
        // 사용처는 config/SecurityConfig 클래스 내부, @Bean public AuthenticationManager 를 통해 인증 및 인가 작업에 사용

        // UserDetails 객체 생성 및 반환 *****
        return new org.springframework.security.core.userdetails.User(
                user.getPhoneNumber(),
                user.getPassword(),
                Collections.singletonList(new SimpleGrantedAuthority(user.getRole().getAuthority())) // 권한 설정
                // user.getRole() 하면 되는데, Role 을 스트링이 아닌, Enum 으로 설정해놔서, Enum 안에 메소드 호출을 통해 읽어옴
                // SimpleGrantedAuthority는 String 타입의 권한을 필요로 하기 때문에, Role enum을 String으로 변환하여 사용해야함
        );
    }
}
