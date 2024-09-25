package kdt.hackathon.practicesecurity.controller;

import kdt.hackathon.practicesecurity.dto.AccessTokenRequest;
import kdt.hackathon.practicesecurity.dto.AccessTokenResponse;
import kdt.hackathon.practicesecurity.service.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class TokenApiController {

    private final TokenService tokenService;

    @PostMapping("/api/token")
    public ResponseEntity<AccessTokenResponse>
            createNewAccessToken(@RequestBody AccessTokenRequest accessRequest) {
        String newAccessToken =
                tokenService.createNewAccessToken(accessRequest.getRefreshToken());

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new AccessTokenResponse(newAccessToken));
    }

}
