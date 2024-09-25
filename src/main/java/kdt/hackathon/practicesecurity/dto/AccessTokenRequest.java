package kdt.hackathon.practicesecurity.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AccessTokenRequest { // 리프레쉬 토큰을 통한. 엑세스 토큰 생성 요청
    private String refreshToken;
}
