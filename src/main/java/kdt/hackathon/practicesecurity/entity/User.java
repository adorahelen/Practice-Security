package kdt.hackathon.practicesecurity.entity;

import com.github.f4b6a3.ulid.Ulid; // ULID 사용
import jakarta.persistence.*;
import kdt.hackathon.practicesecurity.authority.Role;
import lombok.*;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;


import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;


import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Table(name = "users")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Getter
@Entity
public class User implements UserDetails {
    // @Validadtion 어떻게 하지?
    // private String role; // 관리자 아니면 전부다 일반
    // private String email; // 안쓰기로 했지만, 혹시 몰라서 필드로 남겨둠

    @Id
    @Column(name = "id")
    private String id; // ULID 를 기본키로 설정한다.

    @Column(name = "email")
    private String email;

    @Column(name = "password", nullable = false)
    private String password; // 로그인 패스워드

    @Column(name = "phoneNumber", unique = true, nullable = false)
    @NotNull
    @Pattern(regexp = "\\d{3}-\\d{4}-\\d{4}", message = "전화번호 형식이 맞지 않습니다.")
    private String phoneNumber;

    @Column(name = "birthDate", nullable = false)
    private String birthDate;

    @Column(name = "name", nullable = false)
    private String name;

    // 오스투에서 내려받는 정보를 위해? 추가
    @Column(name = "nickname", unique = true)
    private String nickname;

    @Enumerated(EnumType.STRING)
    private Status status;

    @Enumerated(EnumType.STRING)
    private Role role;

    @CreatedDate // save when Entity Created
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @LastModifiedDate // save when Entity Modified
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Lob
    @Column(name = "profile_image")
    private byte[] profileImage;

    // - 추후 운용 / OAuth2유저를 위한 VARCHAR / string 타입의 url 속성도 추가
    @Column(name = "profile_url")
    private String profileUrl;



//    @Builder
//    public User(String email,
//                String nickname)
//    {
//        this.email = email;
//        this.nickname = nickname;
//    }

    //  빌더를 , 생성자를 개별 적으로 만드는게 아니라 어노테이션 붙이고 생성하는 대로 처리하도록
    @Builder // @Builder는 빌더 패턴을 적용해 객체 생성을 유연하게
    public User(String phoneNumber,
                String password,
                String birthDate,
                String name,
                Role role
          )
    {
        this.id = Ulid.fast().toString();
        // 각 User 객체가 생성될 때마다 고유한 ULID가 기본키로 자동 생성

        this.phoneNumber = phoneNumber;
        this.password = password;
        this.birthDate = birthDate;
        this.name = name;
        this.role = role;

    }

//     사용자 정보를 조회하여, 유저 테이블에 사용자 정보가 있다면, 리소스 서버에서 제공해주는 이름을 업데이트(구글)
//     없다면 유저 테이블에서 새 사용자를 생성해 데이터베이스에 저장

//
//    public User update(String name) {
//        this.name = name;
//        return this;
//    }
public User update(String name) {
    this.name = name; // Update the user's name
    this.updatedAt = LocalDateTime.now(); // Update the `updatedAt` timestamp
    return this; // Return the updated user
}

    @Override // 권한 반환
    public Collection<? extends GrantedAuthority> getAuthorities() { // 권한 지정 X, 권한 "반환"
        return null; // 와 이부분 전혀 안쓰이고 있었다. (오버라이드니까, 커스텀 해서 쓰라고 해논건데)
        //          * 전혀 커스텀 안하고 그냥 만들어서 방치해두고 있었다. null 해도 아무 문제 없음

    /*        return user.getRoles().stream()
            .map(role -> new SimpleGrantedAuthority(role.getDescription()))  // 'ROLE_USER'와 같은 권한
            .collect(Collectors.toList());
    } 기존에 작성되어 있던 방식은 스프링 시큐리티가 인식할 수 있는 방식이 아니었다. 혹은 유저로 명시해서 (유저만)
        ( return List.of(new SimpleGrantedAuthority("ROLE_USER")); )
        아무튼, 권한 반환이 정상적으로 진행되지 못했고, 이를 해결하기 위해 서비스단에 아래와 같은 코드를 작성해 해결

                return new org.springframework.security.core.userdetails.User(
                user.getPhoneNumber(),
                user.getPassword(),
                Collections.singletonList(new SimpleGrantedAuthority(user.getRole().getAuthority()))
    * */
    }

    @Override // 사용자의 고유한 아이디를 반환 -> 반드시 고유. 유니크 속성 적용
    public String getUsername() {
        return phoneNumber;
    }

    @Override // 암화화 꼭 해놓기
    public String getPassword() {
        return password;
    }

    //계정 만료, 잠금 만료, 비번 만료 등
    @Override
    public boolean isAccountNonExpired() {
        return true;
        // 참 = 만료되지 않았음
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
        // 참 = 잠금되지 않았음
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
        // 패스워드는 만료되지 않았음
    }

    @Override
    public boolean isEnabled() {
        return true;
        // 계정은 사용 가능함
    }
}

// 1. UserDetailsService :
//          * 사용자에 관한 세부 정보, 계약을 구현하는 객체가 관리
//          * 기본 자격 증명에서 사용자 이름은 'user' 이고 기본 암호는 UUID 형식이다.
//          * 암호는 스프링 컨텍스트가 로드될 때 자동으로 생성된다.

// 2. PasswordEncoder :
//      * 암호를 인코딩한다.
//      * 암호가 기존 인코딩과 일치하는지 확인한다.

// 3. AuthenticationProvider : (인증 구현)
//  3-1. AuthenticationManger

// 1-1. UserDetails
//          * 인터페이스는 스프링 시큐리티에서 사용자를 기술하는 데 이용하는 계약
// 1-2. UserDetailsService
//          * 인터페이스는 애플리케이션이 사용자 세부 정보를 얻는 방법을 설명하기 위해, 구현해야 하는 계약
// 1-3. UserDetailsManger
//          * 인터페이스는 위에 유저디테일서비스를 확장하고 사용자 [생성/변경/삭제]관련 동작을 추가한다.

// ### UUID(범용 고유 식별자)처럼 단순한 값을 토큰으로 이용 할 수 있지만, JWT (JSON 웹 토큰)를 더 많이 사용