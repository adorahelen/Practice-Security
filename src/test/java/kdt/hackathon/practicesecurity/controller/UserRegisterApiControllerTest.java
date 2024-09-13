package kdt.hackathon.practicesecurity.controller;

import kdt.hackathon.practicesecurity.dto.RegisterUser;
import kdt.hackathon.practicesecurity.service.UserRegisterService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import com.fasterxml.jackson.databind.ObjectMapper;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@WebMvcTest(UserRegisterApiController.class)
public class UserRegisterApiControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserRegisterService userRegisterService;

    private RegisterUser registerUser;

    @BeforeEach
    public void setUp() {
        // 회원가입에 필요한 테스트 데이터를 설정
        registerUser = RegisterUser.builder()
                .phoneNumber("01012345678")
                .password("password123")
                .birthDate("1990-01-01")
                .name("TestUser")
                .build();
    }

    @Test
    @WithMockUser // 모킹된 사용자로 인증된 요청을 보냄
    public void testSignUpSuccess() throws Exception {
        // userRegisterService의 save 메서드가 호출되었을 때, 아무것도 반환하지 않도록 설정
        Mockito.doReturn("ULID1234").when(userRegisterService).save(Mockito.any(RegisterUser.class));

        // 회원가입 API에 대해 MockMvc를 사용하여 요청을 보냄
        mockMvc.perform(MockMvcRequestBuilders.post("/user")
                        .with(SecurityMockMvcRequestPostProcessors.csrf()) // CSRF 토큰을 추가
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(registerUser))) // request body를 JSON으로 변환
//                .andExpect(status().isOk()) // 200 OK 응답을 기대
//                .andExpect(content().string("redirect:/login")); // 응답 메시지 "success" 기대
//    }
}