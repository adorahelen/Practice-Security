//package kdt.hackathon.practicesecurity.service;
//
//import kdt.hackathon.practicesecurity.authority.Role;
//import kdt.hackathon.practicesecurity.entity.User;
//import kdt.hackathon.practicesecurity.repository.UserRepository;
//import lombok.RequiredArgsConstructor;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
//import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
//import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
//import org.springframework.security.oauth2.core.user.OAuth2User;
//import org.springframework.stereotype.Service;
//
//import java.util.Map;
//
//@RequiredArgsConstructor
//@Service
//public class OAuth2UserCustomService extends DefaultOAuth2UserService {
//
//    private final UserRepository userRepository;
//    private final BCryptPasswordEncoder bCryptPasswordEncoder;
//
//
//    @Override
//    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws
//            OAuth2AuthenticationException {
//        OAuth2User user = super.loadUser(userRequest);
//        saveOrUpdate(user);
//        return user;
//    } // DefaultOAuth2UserService 제공하는 Oauth 서비스에서 제공하는 정보를 기반으로 유저 객체 생성 하는 load()메소드
//    //      -> 이 사용자 객체는 1. 식별자 2. 이름 3. 이메일 4. 프로필 사진 링크 등의 정보를 담고 있다
//
//
//    // ==============================================================
//    // 유저가 있으면 유저 업데이트,  없으면 유저 생성
//    private User saveOrUpdate(OAuth2User oAuth2User) {
//        Map<String, Object> attributes = oAuth2User.getAttributes();
//
//        // 구글에서 주는 정보는 한정되어 있다. -> 나머지는 알아서 로직을 구현해 채워야 한다.
//        // Google의 사용자 정보 중 email  기본 로그인 아이디(폰넘버)로 사용
//
//
//        // 데이터베이스에서 사용자 조회, 없으면 새로 생성, 근데 이건 오스 유저를 디비에 넣는거니까
//        // 기본 (일반) 회원가입 유저들과는 다르게 디비에 담기게 될것
//        String email = (String) attributes.getOrDefault("email", generateRandomEmail());
//        String name = (String) attributes.getOrDefault("name", "NoName_" + System.currentTimeMillis());
//        String randomPhoneNumber = "000-0000-" + ((int) (Math.random() * 9000) + 1000); // 임의의 전화번호 생성
//
//        User user = userRepository.findByEmail(email)
//                .map(entity -> entity.update(name))
//                .orElse(User.builder()
//                        .email(email)
//                        .nickname(name)  // 닉네임이 없으면 이름으로 설정
//                        .phoneNumber(randomPhoneNumber) // 임의의 전화번호
//                        .role(Role.ROLE_USER)
//                        .password(bCryptPasswordEncoder.encode("defaultPassword")) // OAuth 로그인에는 비번이 필요없지만 기본값 설정
//                        .build());
//
//        return userRepository.save(user);
//        // 이 코드는 그러면, 디비에 사용자 이름이 있으면 업데이트(뭐를?)
//        // 없으면 이메일과 이름만을(닉네임)을 가지고 회원을 생성한 다음 디비에 저장한다.
//
//
//
//        // 즉 디비에는 두가지 회원 양상이 나타나게 될 것임
//        // 1. 회원가입을 통해 모든 정보를 가지고 있는 회원
//        // 2. 구글 혹은 카카오에서 보내주는 정보만 가지고 있는 회원
//        // => 추후 다른게 정상적으로 진행된다면, 구글이나 카카오에서 제공해주는 정보를 늘리고. 이 부분 빌더 패턴 수정하면 될듯
//
//    }
//
//    private String generateRandomEmail() {
//        return "user" + System.currentTimeMillis() + "@example.com"; // 임의의 이메일 생성
//    }
//}
//
//
//
//
//
//
//
//
//
