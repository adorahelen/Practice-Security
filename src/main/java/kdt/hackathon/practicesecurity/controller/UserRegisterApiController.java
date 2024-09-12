package kdt.hackathon.practicesecurity.controller;

import kdt.hackathon.practicesecurity.dto.RegisterUser;
import kdt.hackathon.practicesecurity.service.UserRegisterService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserRegisterApiController {

    private final UserRegisterService userRegisterService;

    @PostMapping("/user")
    public String signUp(@RequestBody RegisterUser DtoRegisterUser) {
        userRegisterService.save(DtoRegisterUser); // 회원가입 메소드 호출한 상황
        return "redirect:/login";
    }
}
// 1. 회원가입이 정상적으로 진행(ULID 발급만 성공)되어도, 현재 role 을 통해 권한의 분배는 구현하지 않은 상태
// 2. 모든 자료형을 String 으로 진행했고, @Validation 등 유효성 검사는 X (null false O, 정규식 X)
// 3. 세션 구현 안되어 있고, Oauth2 (구글,카카오)붙여야 하고, 비밀번호 안전성 고려(해쉬 알고리즘 선택 적용)
// 여기까지가 회원가입에 대한 고려사항

// 로그인 -> 시큐리티 그대로? (비밀번호 찾기 어떻게 만들까?) -> 전화번호를 통해 메일? 문자? 카톡?
