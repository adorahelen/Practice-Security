package kdt.hackathon.practicesecurity.config.jwt;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Setter
@Getter
@Component
// @ConfigurationProperties("jwt") // 자바 클래스 작성되어 있는 프로퍼티 값을 가지고 와서 사용한다. yml
public class JwtProperties {
    private String issuer = "admin";
    private String secretKey = "gradsch.sogang.ac.kr/front/cmsboardlist";
}
