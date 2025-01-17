package kdt.hackathon.practicesecurity.OAuth2;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.util.SerializationUtils;

import java.util.Base64;

public class CookieUtil {
    // 요청한 값을 토대로 쿠키를 추가하는 메소드 (이름, 값, 만료기간)
    public static void addCookie(HttpServletResponse response, String name, String value, int maxAge) {
        Cookie cookie = new Cookie(name, value);
        cookie.setMaxAge(maxAge);
        cookie.setPath("/");
        response.addCookie(cookie);
    }

    // 쿠키의 이름을 입력받아 쿠키 삭제를 행하는 메소드
    public static void deleteCookie(HttpServletRequest request, HttpServletResponse response, String name) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            return;
        }
        for (Cookie cookie : cookies) {
            if (name.equals(cookie.getName())) {
                cookie.setMaxAge(0);
                cookie.setPath("/");
                cookie.setValue("");
                response.addCookie(cookie);
            }
        }
    } // 실제로 삭제하는 방법 X, 파라미터로 넘어온 키의 쿠키를 빈 값으로 바꾸고 만료시간을 0으로 설정

    // 객체를 직렬화해 쿠키의 값으로 변환하는 메소드
    public static String serialize(Object obj) {
        return Base64.getUrlEncoder().encodeToString(SerializationUtils.serialize(obj));
    }

    // 쿠키를 역직렬화 -> 객체(자바)로 변환
    public static <T> T deserialize(Cookie cookie, Class<T> cls) {
        return cls.cast(
                SerializationUtils.deserialize(
                        Base64.getUrlDecoder().decode(cookie.getValue()))
        );
    }
}
