package org.example.domain.member.controller;

import javax.validation.Valid;

import org.example.global.result.ResultResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


import org.example.domain.member.service.MemberService;

import static org.example.global.result.ResultCode.*;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {

    private final MemberService memberService;

    @GetMapping(value = "/{username}")
    @ResponseBody
    public String getShadow(@PathVariable("username") String username) {
        return "member";
    }

}