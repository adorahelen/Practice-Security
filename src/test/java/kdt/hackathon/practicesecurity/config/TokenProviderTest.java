package kdt.hackathon.practicesecurity.config;

import io.jsonwebtoken.Jwts;
import kdt.hackathon.practicesecurity.authority.Role;
import kdt.hackathon.practicesecurity.config.jwt.JwtProperties;
import kdt.hackathon.practicesecurity.config.jwt.TokenProvider;
import kdt.hackathon.practicesecurity.entity.User;
import kdt.hackathon.practicesecurity.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.Duration;
import java.util.Date;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class TokenProviderTest {
    @Autowired     // 프로세스 흐름 config > TokenProvider.Class
    private TokenProvider tokenProvider; // 토큰 생성을 위해 의존성 주입
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JwtProperties jwtProperties;

    // 1. 토큰 생성 테스트 2. 토큰 검증 테스트 3. 토큰 안에 인증정보 담겼는지 4. 토큰으로 조회가 되는지

    @Test
    void generateToken() {
        // given
        String phoneNumber = "333-3333-1214"; // 전번 계속 바꿔주거나 디비에서 날려야함 (중복X)
        User user = User.builder()
                .phoneNumber(phoneNumber)
                .password("password123")
                .birthDate("1990-01-01")
                .name("John Doe")
                .role(Role.ROLE_ADMIN)
                .build();


        User testUser = userRepository.save(user);

        // when
        String token = tokenProvider.generateToken(testUser, Duration.ofDays(14));
        // then
        // 여기가 문제인게 1. 엔티티를 수정함(개수 늘림, 이메일 대신에 전화번호를 아이디로 사용함)
        //              2. 기본키가 ULID 라서, 엄청 긴 문자열임 ;;
        // 해결은 했는데 ,, 앞으로 비슷한 문제를 조심
        String userId = Jwts.parser()
                .setSigningKey(jwtProperties.getSecretKey())
                .parseClaimsJws(token)
                .getBody()
                .get("id", String.class);

        assertThat(userId).isEqualTo(testUser.getId().toString());
    }

    @Test
    void invalidateToken() { // 발급되지 않은 토큰 false 로 나오는지 테스트
        String token = JwtFactoryForTest.builder()
                .expiration(new Date(new Date().getTime() - Duration.ofDays(7).toMillis())).build()
                .createToken(jwtProperties);
        // when
        boolean result = tokenProvider.validateToken(token);

        // then
        assertThat(result).isFalse(); // 토큰 없는 경우
    }

    @Test
    void validateToken() { // 발급된 토큰이 true 로 잘 나오는지 테스트
        // given
        String token = JwtFactoryForTest.jwtFactoryTestWithDefaultValues()
                .createToken(jwtProperties);
        // when
        boolean result = tokenProvider.validateToken(token);
        // then
        assertThat(result).isTrue();
    }

    @Test
    void getAuthentication() { // 토큰을 전달받아, 인증 정보를 담은 객체를 반환한다
        // given
        String userPhoneNumber = "111-1561-1214";
        String token = JwtFactoryForTest.builder()
                .subject(userPhoneNumber)
                .build()
                .createToken(jwtProperties);

        // when
        Authentication authentication = tokenProvider.getAuthentication(token);
        // then
        assertThat(((UserDetails) authentication.getPrincipal()).getUsername()).isEqualTo(userPhoneNumber);

    }

    @Test
    void getUserId() { // 토큰 기반으로 유저 아이디를 가져오는게 되는지 테스트
        // given
        String userId = "1231311"; // 클레임으로 사용할 임의 아이디
        String token = JwtFactoryForTest.builder()
                .claims(Map.of("id", userId))
                .build()
                .createToken(jwtProperties);
        // when
        String userIdByToken = tokenProvider.getUserIdStringToken(token);
                // 복호화 시키는 부분은 getUserIdFromToken 메소드 안에있는 getClaims 메소드가 복호화 진행

        // then
        assertThat(userIdByToken).isEqualTo(userId);
    }
}
