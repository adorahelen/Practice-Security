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

import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class JwtTest {
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
        String phoneNumber = "111-1561-1214"; // 전번 계속 바꿔주거나 디비에서 날려야함 (중복X)
        User user = User.builder()
                .phoneNumber(phoneNumber)
                .password("password123")
                .birthDate("1990-01-01")
                .name("John Doe")
                .role(Role.ROLE_ADMIN)
                .build();


        User testUser = userRepository.save(user);
        // 이거 문제가 생길수도? : ㅇㅇ 생겼네요

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
}
