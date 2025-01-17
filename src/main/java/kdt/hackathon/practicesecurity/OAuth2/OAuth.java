//package kdt.hackathon.practicesecurity.OAuth2;
//
//// [ OAuth? ]
//// - 제 3의 서비스에게 계정 관리를 맡기는 방식
//// ex: 네이버로 로그인하기, 구글로 로그인하기
//
///* [ 용어 정리 ]
//* 1. 리소스 오너 : 클라이언트(사용자)
//* 2. 리소스 서버 : 네이버 혹은 구글 혹은 페이스북
//* 3. 인증 서버 : 서버(토큰 발급 역할)
//* 4. 클라이언트 애플리케이션 : 내가 만들고 있는 서비스 */
//
///* [ 오너(클라이언트) 정보 취득 방법 ]
//* 1. 권한 부여 코드 승인 타입 =============>
//* 2. 암시적 승인 타입
//* 3. 리소스 소유자 암호 자격증명 타입
//* 4. 클라이언트 자격증명 타입 */
//
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.web.DefaultRedirectStrategy;
//import org.springframework.security.web.RedirectStrategy;
//import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
//import org.springframework.stereotype.Component;
//
//import java.io.IOException;
//
///* 권한 요청 : 스프링 부트가 -> 구글 권한 서버에 요청
// { Client_id : 고유 식별자 (내가, 클라이언트에게 할당할)
// { redirect_uri : 로그인 성공 시 이동해야 하는 URI
// { response_type : 클라이언트가 제공받길 원하는 응답 타입
// { scope : 제공받고자 하는 리소스 오너의 정보 목록
//
//
//* */
//@Component
//public class OAuth implements AuthenticationSuccessHandler {
//    private final RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();
//
//    @Override
//    public void onAuthenticationSuccess
//            (HttpServletRequest request,
//             HttpServletResponse response,
//             Authentication authentication)
//            throws IOException, ServletException {
//        redirectStrategy.sendRedirect(request, response, "/service");
//
//    }
//}
