package kdt.hackathon.practicesecurity.service;

import kdt.hackathon.practicesecurity.config.jwt.TokenProvider;
import kdt.hackathon.practicesecurity.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;

@RequiredArgsConstructor
@Service
public class TokenService {

    private final TokenProvider tokenProvider;
    private final RefreshTokenService refreshTokenService;
    private final UserUseService userUseService; // 개별 구현한 메소드 하나 (find)

    public String createNewAccessToken(String refreshToken) {

        // 토큰 유효성 검사에 실패한 케이스
        if (!tokenProvider.validateToken(refreshToken)) {
            throw new IllegalArgumentException("Invalid refresh token or Unexpected tokens");
        }

        Long userId = refreshTokenService.findByRefreshToken(refreshToken).getUserId();
        User user = userUseService.findById(userId);
        // 찾고 찾아서, 아래에서 새 토큰 생성 후 반환

        return tokenProvider.generateToken(user, Duration.ofHours(2));
    }
}
