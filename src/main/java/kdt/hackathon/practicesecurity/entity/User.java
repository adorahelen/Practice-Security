package kdt.hackathon.practicesecurity.entity;

import com.github.f4b6a3.ulid.Ulid; // ULID 사용
import jakarta.persistence.*;
import kdt.hackathon.practicesecurity.authority.Role;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Table(name = "users")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class User implements UserDetails {
    // @Validadtion 어떻게 하지?
    // private String role; // 관리자 아니면 전부다 일반
    // private String email; // 안쓰기로 했지만, 혹시 몰라서 필드로 남겨둠

    @Id
    @Column(name = "id")
    private String id; // ULID 를 기본키로 설정한다.
    // *전화번호(아이디)가 기본키가 되면, 식별은 가능하지만, 노출될 위험이 있기 때문에 -> UUID or ULID 사용
    // => 비밀번호 찾기는 필요하지만, 아이디 찾기는 불필요하다.
    @Column(name = "phoneNumber", unique = true, nullable = false)
    private String phoneNumber; // 로그인 아이디
    @Column(name = "password", nullable = false)
    private String password; // 로그인 패스워드
    @Column(name = "birthDate", nullable = false)
    private String birthDate;
    @Column(name = "name", nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Builder // @Builder는 빌더 패턴을 적용해 객체 생성을 유연하게
    public User(String phoneNumber,
                String password,
                String birthDate,
                String name,
                Role role)
    {
        this.id = Ulid.fast().toString();
        // 각 User 객체가 생성될 때마다 고유한 ULID가 기본키로 자동 생성

        this.phoneNumber = phoneNumber;
        this.password = password;
        this.birthDate = birthDate;
        this.name = name;
        this.role = role;
    }

    @Override // 권한 반환
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_USER"));
        // 이부분 권한 지정, 어르신들은 ROLE_USER <-> 개발자는 ROLE_ADMIN(워크브랜치에서 개별 수정)
        // 디폴트는 모든 계정이 회원가입 시도할때 "유저"로 권한을 부여한다.
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