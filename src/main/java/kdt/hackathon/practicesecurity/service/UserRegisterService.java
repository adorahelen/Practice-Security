package kdt.hackathon.practicesecurity.service;

import kdt.hackathon.practicesecurity.authority.Role;
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

//    public String save(RegisterUser dto) {
//            User user = User.builder()
//                .phoneNumber(dto.getPhoneNumber())
//                .birthDate(dto.getBirthDate())
//                .name(dto.getName())
//                .role(Role.ROLE_USER)
//                .password(bCryptPasswordEncoder.encode(dto.getPassword())) // 추후 암호화 방식 변경*
//                .build();
//
//           // user.getAuthorities(); 필요없네
//
//        return userRepository.save(user).getId(); // ULID 방식으로 생성한 기본키 반환
//        // 반환 없애고 String -> Void ?
//
//    }

    public String save(RegisterUser dto) {

        // 필요한 값이 없으면 임의로 값을 할당 (예: nickname, profileUrl)
        String nickname = (dto.getNickname() != null) ? dto.getNickname() : "User_" + dto.getPhoneNumber();
        String profileUrl = (dto.getProfileUrl() != null) ? dto.getProfileUrl() : "https://default-profile.url";


        User user = User.builder()


                .email(dto.getEmail()) // Email이 필수는 아니지만, DTO에 포함되면 저장, null 이 들어가겠지>?
                .phoneNumber(dto.getPhoneNumber())
                .birthDate(dto.getBirthDate())
                .name(dto.getName())
                .nickname(nickname) // 임의로 생성된 닉네임 사용
                .role(Role.ROLE_USER) // 기본적으로 일반 사용자 역할 부여
                .password(bCryptPasswordEncoder.encode(dto.getPassword())) // 암호화된 비밀번호 저장
                .profileUrl(profileUrl) // 프로필 URL이 없으면 기본값 사용
                .build();

        return userRepository.save(user).getId(); // ULID 방식의 기본키 반환
    }
}
