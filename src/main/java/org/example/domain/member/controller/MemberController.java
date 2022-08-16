package org.example.domain.member.controller;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.example.domain.member.MemberCreateForm;
import org.example.domain.member.service.MemberService;


@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {

    private final MemberService memberService;

    @GetMapping(value = "/{username}")
    public ResponseEntity<ResultResponse> getShadow(@PathVariable("username") String username) {
        final ShadowResponse shadowResponse = memberService.getShadow(username);

        return ResponseEntity.ok(ResultResponse.of(GET_USERPROFILE_SUCCESS, shadowResponse));
    }

}