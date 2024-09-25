package kdt.hackathon.practicesecurity.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class RefreshToken { // 엔티티 이유 : 리프레쉬 토큰은 데이터베이스에 저장하는 정보임!
    // 필드(컬럼)는 총 3개

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false)
    private Long id; // 리프레쉬 토큰 고유 아이디 (기본키)

    @Column(name = "user_id", nullable = false)
    private Long userId;
    // Long => String

    @Column(name = "refresh_token", nullable = false)
    private String refreshToken;

    // 생성자
    public RefreshToken(Long userId, String refreshToken) {
        this.userId = userId;
        this.refreshToken = refreshToken;
    }
    // update method

    public RefreshToken updateToken(String newRefreshToken) {
        this.refreshToken = newRefreshToken;
        return this;
    }
}
