package kdt.hackathon.practicesecurity.service;

import kdt.hackathon.practicesecurity.entity.User;
import kdt.hackathon.practicesecurity.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;


// 1. UserDetailsService :
//          * 사용자에 관한 세부 정보, 계약을 구현하는 객체가 관리
//          * 기본 자격 증명에서 사용자 이름은 'user' 이고 기본 암호는 UUID 형식이다.
//          * 암호는 스프링 컨텍스트가 로드될 때 자동으로 생성된다.
@Slf4j
@RequiredArgsConstructor
@Service
public class UserDetailService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String phoneNumber) throws UsernameNotFoundException {
        log.info("loadUserByUsername() 호출됨 - phoneNumber: {}", phoneNumber);
        // 사용자의 정보를 가져오는 UserDetailsService 구현( 여기서, Role:enum 으로부터 권한도 가지고 온다=어떤 롤인지)
        User user = userRepository.findByPhoneNumber(phoneNumber)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with phone number: " + phoneNumber));
// 여기까지가 기존 서비스에서 진행한 (회원가입 후 디비에 저장하는 부분이고), 아래에 리턴문은 인증정보(인가)를 리턴에서 시큐리티에게 전달(필터)한테

        return new org.springframework.security.core.userdetails.User(
                user.getPhoneNumber(),
                user.getPassword(),
                Collections.singletonList(new SimpleGrantedAuthority(user.getRole().getAuthority())) // 권한 설정
        );
    }
}
