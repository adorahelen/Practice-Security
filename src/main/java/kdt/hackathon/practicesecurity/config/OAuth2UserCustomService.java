package kdt.hackathon.practicesecurity.config;

import kdt.hackathon.practicesecurity.authority.Role;
import kdt.hackathon.practicesecurity.entity.User;
import kdt.hackathon.practicesecurity.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class OAuth2UserCustomService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;
   // private final BCryptPasswordEncoder bCryptPasswordEncoder;


    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User user = super.loadUser(userRequest);
        saveOrUpdate(user);
        return user;
    }

    private User saveOrUpdate(OAuth2User oAuth2User) {
        Map<String, Object> attributes = oAuth2User.getAttributes();
        String email = (String) attributes.getOrDefault("email", generateRandomEmail());
        String name = (String) attributes.getOrDefault("name", "NoName_" + System.currentTimeMillis());
        String randomPhoneNumber = "000-0000-" + ((int) (Math.random() * 9000) + 1000);

        User user = userRepository.findByEmail(email)
                .map(entity -> entity.update(name))
                .orElse(User.builder()
                        .email(email)
                        .nickname(name)
                        .phoneNumber(randomPhoneNumber)
                        .role(Role.ROLE_USER)
                        .password("123456")
                       // .password(bCryptPasswordEncoder.encode("123456")) // 암호화된 비밀번호 저장

                        .build());

        return userRepository.save(user);
    }

    private String generateRandomEmail() {
        return "user" + System.currentTimeMillis() + "@example.com";
    }


}