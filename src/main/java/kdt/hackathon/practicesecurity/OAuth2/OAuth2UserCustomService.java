//package kdt.hackathon.practicesecurity.OAuth2;
//
//import kdt.hackathon.practicesecurity.authority.Role;
//import kdt.hackathon.practicesecurity.entity.User;
//import kdt.hackathon.practicesecurity.repository.UserRepository;
//import lombok.RequiredArgsConstructor;
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
//
//    @Override
//    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
//        OAuth2User user = super.loadUser(userRequest);
//        saveOrUpdate(user);
//        return user;
//    } // DefaultOAuth2UserService 제공하는 Oauth 서비스에서 제공하는 정보를 기반으로 유저 객체 생성 하는 load()메소드
//    //      -> 이 사용자 객체는 1. 식별자 2. 이름 3. 이메일 4. 프로필 사진 링크 등의 정보를 담고 있다
//
//    private User saveOrUpdate(OAuth2User oAuth2User) {
//        Map<String, Object> attributes = oAuth2User.getAttributes();
//
//        // 구글에서 주는 정보는 한정되어 있다. -> 나머지는 알아서 로직을 구현해 채워야 한다.
//        // Google의 사용자 정보 중 email  기본 로그인 아이디(폰넘버)로 사용
//        String phoneNumber = (String) attributes.get("email"); // 폰 넘버가 아이디니까, 여기에 이메일을 받아 넣는다
//        String name = (String) attributes.get("name");  // Google에서 제공하는 이름 정보
//
//        // 데이터베이스에서 사용자 조회, 없으면 새로 생성
//        User user = userRepository.findByPhoneNumber(phoneNumber)
//                .map(entity -> entity.update(name))  // 네임을 업데이트
//                .orElse(User.builder()
//                        .phoneNumber(phoneNumber)
//                        .name(name)
//                        .birthDate("123456") // 임의 할당
//                        .role(Role.ROLE_USER)  // 기본적으로 일반 사용자로 설정
//                        .password("123456")  // OAuth2 사용자의 경우 비밀번호는 사용하지 않음, 임의 할당
//                        .build());
//
//        return userRepository.save(user);
//    }
//}
