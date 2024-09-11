package kdt.hackathon.practicesecurity.service;

import kdt.hackathon.practicesecurity.entity.User;
import kdt.hackathon.practicesecurity.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

// 테스트 코드 작성 (JUnit + Mockito 사용)
//      * UserRepository와 UserDetailService를 검증
//      # @MockBean, Mockito, => 의존성 모킹
//      # 실제 DB를 사용하지 않고, 로직 테스트

@Slf4j
class UserDetailServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserDetailService userDetailService;

    @BeforeEach
    void setUp() { //  테스트 전에 MockitoAnnotations.openMocks(this)를 통해 모킹 설정을 초기화
        MockitoAnnotations.openMocks(this);
        log.info("테스트 시작 - setup 완료");
    }

    @Test
    @DisplayName("JPA: Save & Search(디테일서비스)")
    void PhoneNumberExists() {
        // Given
        String phoneNumber = "010-1234-5678";
        User user = User.builder()
                .phoneNumber(phoneNumber)
                .password("password123")
                .birthDate("1990-01-01")
                .name("John Doe")
                .build();

        when(userRepository.findByPhoneNumber(phoneNumber)).thenReturn(Optional.of(user));
        log.info("Mock User 설정 완료 - phoneNumber: {}", phoneNumber);
        log.info("해쉬화가 진행된 ULID: {}", user.getId());
        // ULID 값 예시 : 01ARZ3NDEKTSV4RRFFQ69G5FAV
        // UUID 값 예시 : 4dfc6b14-7213-3363-8009-b23c56e3a1b1

        // When
        User result = userDetailService.loadUserByUsername(phoneNumber);

        // Then
        assertNotNull(result);
        assertEquals(phoneNumber, result.getPhoneNumber());
        log.info("테스트 성공 - User 반환됨: {}", result);
        verify(userRepository, times(1)).findByPhoneNumber(phoneNumber);
    }

    @Test
    @DisplayName("No exist, Search")
    void PhoneNumberDoesNotExist() {
        // Given
        String phoneNumber = "010-9999-9999";
        log.info("없는 전화번호로 테스트 진행 - phoneNumber: {}", phoneNumber);



        when(userRepository.findByPhoneNumber(phoneNumber)).thenReturn(Optional.empty());

        // When & Then
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            userDetailService.loadUserByUsername(phoneNumber);
        });

        assertEquals("PhoneNumber not found", exception.getMessage());
        log.error("Exception 발생 - {}", exception.getMessage());
        verify(userRepository, times(1)).findByPhoneNumber(phoneNumber);
    }
}