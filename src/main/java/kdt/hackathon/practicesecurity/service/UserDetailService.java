package kdt.hackathon.practicesecurity.service;

import kdt.hackathon.practicesecurity.entity.User;
import kdt.hackathon.practicesecurity.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;


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
    public User loadUserByUsername(String phoneNumber) {
        log.info("loadUserByUsername() 호출됨 - phoneNumber: {}", phoneNumber);

        return userRepository.findByPhoneNumber(phoneNumber)
                .orElseThrow(()->new IllegalArgumentException("PhoneNumber not found"));

    }
}
