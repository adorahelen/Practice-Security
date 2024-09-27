//package kdt.hackathon.practicesecurity.config;
//
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import org.springframework.security.access.AccessDeniedException;
//import org.springframework.security.web.access.AccessDeniedHandler;
//import java.io.IOException;
//
//public class CustomAccessDeniedHandler implements AccessDeniedHandler {
//    @Override
//    public void handle(HttpServletRequest request, HttpServletResponse response,
//                       AccessDeniedException accessDeniedException) throws IOException, ServletException {
//        // 사용자에게 접근 거부 페이지로 리다이렉트하거나 에러 메시지를 반환하는 로직
//        response.sendRedirect("/access-denied");
//    }
//}