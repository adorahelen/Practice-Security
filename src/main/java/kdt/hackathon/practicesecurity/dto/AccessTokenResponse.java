package kdt.hackathon.practicesecurity.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class AccessTokenResponse { // 생성된 엑세스 토큰 (응답용)
    private String accessToken;
}
