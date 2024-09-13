package kdt.hackathon.practicesecurity.auth;

import lombok.Getter;

@Getter

public enum Role {
    ROLE_ADMIN("admin"), ROLE_USER("user");
    private final String description;
    Role(String description) {
        this.description = description;
    }
}
// 롤의 경우, 디폴트를 유저로 설정하고
// 특정한 인풋이 들어왔을 때 조건문을 사용해서 어드민을 부여하는게 아니라  (X)
// 관리자만 -> 워크브랜치에서 수기로 수정하면 된다.