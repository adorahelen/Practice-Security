package kdt.hackathon.practicesecurity.repository;

import kdt.hackathon.practicesecurity.entity.RefreshToken;
import kdt.hackathon.practicesecurity.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByPhoneNumber(String PhoneNumber); // 유니크 붙은, 전화번호로 사용자 정보 가져옴
    // 전화번호로 사용자를 식별 == 사용자 이름 (ULID는 기본키)
    // JPA는 메서드 규칙에 맞춰 메서드를 선언하면 이름을 분석해 자동으로 쿼리를 생성
    Optional<User> findById(String id); // Long userId -> String userId

}
