package kdt.hackathon.practicesecurity.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import kdt.hackathon.practicesecurity.config.JwtFactoryForTest;
import kdt.hackathon.practicesecurity.config.jwt.JwtProperties;
import kdt.hackathon.practicesecurity.dto.AccessTokenRequest;
import kdt.hackathon.practicesecurity.entity.RefreshToken;
import kdt.hackathon.practicesecurity.entity.User;
import kdt.hackathon.practicesecurity.repository.RefreshTokenRepository;
import kdt.hackathon.practicesecurity.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class TokenApiControllerTest {

    @Autowired
    protected MockMvc mockMvc;
    @Autowired
    protected ObjectMapper objectMapper;
    @Autowired
    private WebApplicationContext context;
    @Autowired
    JwtProperties jwtProperties;
    @Autowired
    UserRepository userRepository;
    @Autowired
    RefreshTokenRepository refreshTokenRepository;

    @BeforeEach
    public void mockMvcSetUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context).build();

        userRepository.deleteAll();
    }


    @DisplayName("Access Token POST")
    @Test
    public void createToken() throws Exception {

        //given
        final String url = "/api/token";



        User testUser = userRepository.save(User.builder()
                .phoneNumber("010-9855-2311")
                .password("password123")
                .birthDate("0000-01-01")
                .name("TestUser")
                .build());


        String refreshToken = JwtFactoryForTest.builder()
                .claims(Map.of("id", testUser.getId())).build().createToken(jwtProperties);


        refreshTokenRepository.save(new RefreshToken(testUser.getId(), refreshToken));
        // 'RefreshToken(java.lang.Long, java.lang.String)' in 'kdt.hackathon.practicesecurity.entity.RefreshToken'
        // cannot be applied to '(java.lang.String, java.lang.String)'
        // 스트링으로 가져온 값을 인자로 보내, 다시 롱으로 변환시켜 토큰을 생성한다.

        AccessTokenRequest request = new AccessTokenRequest();
        request.setRefreshToken(refreshToken);

        final String requestBody = objectMapper.writeValueAsString(request);

        //when
        ResultActions resultActions = mockMvc
                .perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(requestBody));
        // then
        resultActions
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.accessToken").isNotEmpty());


    }
}

// refreshToken 엔티티의 생성자에서 Long 타입으로 처리해야 할 값을 String 타입으로 전달하려고 시도했기 때문에 생긴 오류
// For input string: "01J8P2EG3NC93MHEB04T98JH10"
//java.lang.NumberFormatException: For input string: "01J8P2EG3NC93MHEB04T98JH10"
//	at java.base/java.lang.NumberFormatException.forInputString(NumberFormatException.java:67)