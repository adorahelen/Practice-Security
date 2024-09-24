package kdt.hackathon.practicesecurity.config;

import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import kdt.hackathon.practicesecurity.config.jwt.JwtProperties;
import lombok.Builder;
import lombok.Getter;

import java.time.Duration;
import java.util.Date;
import java.util.Map;

import static java.util.Collections.emptyMap;

@Getter
public class JwtFactoryForTest { // = JWT 토큰 서비스를 테스트 하기위해 사용 할 "모킹" 객체 : 따라서 이름이 팩토리, 즉 공장이다.

    private String subject = "010-1256-9876"; // 이부분이 이메일을 사용하지 않기 때문에(엔티티), 바꿔야 했다!
    private Date issuedAt = new Date();
    private Date expiration = new Date(new Date().getTime() + Duration.ofDays(14).toMillis());

    private Map<String, Object> claims = emptyMap();

    // 빌더 패턴을 사용해, 설정이 요구되는 데이터만 선택 결정
    @Builder
    public JwtFactoryForTest(String subject,
                             Date issuedAt,
                             Date expiration,
                             Map<String, Object> claims) {
        this.subject = subject != null ? subject : this.subject;
        this.issuedAt = issuedAt != null ? issuedAt : this.issuedAt;
        this.expiration = expiration != null ? expiration : this.expiration;
        this.claims = claims != null ? claims : this.claims;
    }

    // 빌드패턴으로 생성후 반환
    public static JwtFactoryForTest jwtFactoryTestWithDefaultValues() {
        return JwtFactoryForTest.builder().build();
    }

    // jwt 라이브러리를 이용해서 JWT 토큰 생성
    public String createToken(JwtProperties properties) {
        return Jwts.builder()
                .setSubject(subject)
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
                .setIssuer(properties.getIssuer())
                .setIssuedAt(issuedAt)
                .setExpiration(expiration)
                .addClaims(claims)
                .signWith(SignatureAlgorithm.HS256,properties.getSecretKey())
                .compact();
    }

}
