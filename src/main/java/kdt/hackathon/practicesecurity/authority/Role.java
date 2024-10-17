package kdt.hackathon.practicesecurity.authority;

import lombok.Getter;

@Getter
public enum Role {
    ROLE_ADMIN("admin"),
    ROLE_USER("user");

    private final String description;

    Role(String description) {
        this.description = description;
    }

    public String getAuthority() { // 이게 없어서, 권한을 가져오지 못했다.
        return this.name(); // ROLE_ADMIN, ROLE_USER
       // Role enum에서 권한 문자열을 반환하도록 메서드 추가
    }
}
// 롤의 경우, 디폴트를 유저로 설정하고
// 특정한 인풋이 들어왔을 때 조건문을 사용해서 어드민을 부여하는게 아니라  (X)
// 관리자만 -> 워크브랜치에서 수기로 수정하면 된다.