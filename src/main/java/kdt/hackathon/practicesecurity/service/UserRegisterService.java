package kdt.hackathon.practicesecurity.service;

import kdt.hackathon.practicesecurity.dto.RegisterUser;
import kdt.hackathon.practicesecurity.entity.User;
import kdt.hackathon.practicesecurity.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserRegisterService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public String save(RegisterUser dto) {
            User user = User.builder()
                .phoneNumber(dto.getPhoneNumber())
                .birthDate(dto.getBirthDate())
                .name(dto.getName())
                .password(bCryptPasswordEncoder.encode(dto.getPassword())) // 추후 암호화 방식 변경*
                .build();

        return userRepository.save(user).getId(); // ULID 방식으로 생성한 기본키 반환
        // 반환 없애고 String -> Void ?

    }
}
