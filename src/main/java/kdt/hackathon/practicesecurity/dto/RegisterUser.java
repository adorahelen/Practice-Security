package kdt.hackathon.practicesecurity.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class RegisterUser {
    private String phoneNumber;
    private String password;
    private String birthDate;
    private String name;
}