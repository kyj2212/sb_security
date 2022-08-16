package org.example.domain.member.controller;

import static org.example.global.result.ResultCode.*;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.domain.member.service.MemberAuthService;
import org.example.global.result.ResultResponse;
import org.hibernate.validator.constraints.Length;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.Pattern;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
public class MemberAuthController {

    private final MemberAuthService memberAuthService;

    @GetMapping(value = "/Member/check")
    public ResponseEntity<ResultResponse> checkUsername(
            @RequestParam
            @Length(min = 4, max = 12, message = "Id는 4문자 이상 12문자 이하여야 합니다")
            @Pattern(regexp = "^[0-9a-zA-Z]+$", message = "username엔 대소문자, 숫자만 사용할 수 있습니다.")
            String username) {
        final boolean check = memberAuthService.checkUsername(username);
        if (check) {
            return ResponseEntity.ok(ResultResponse.of(CHECK_USERNAME_GOOD, true));
        } else {
            return ResponseEntity.ok(ResultResponse.of(CHECK_USERNAME_BAD, false));
        }
    }
}
