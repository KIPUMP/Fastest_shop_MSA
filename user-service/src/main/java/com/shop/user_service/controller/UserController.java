package com.shop.user_service.controller;

import com.shop.user_service.dto.LoginRequestDto;
import com.shop.user_service.dto.SignupRequestDto;
import com.shop.user_service.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import java.util.List;

@Slf4j
@RestController // @Controller 대신 @RestController를 사용하여 JSON 응답을 반환
@RequiredArgsConstructor
@RequestMapping("/user-service")
public class UserController {
    private final UserService userService;
    @GetMapping("/user/login-page")
    public ResponseEntity<String> loginPage() {
        // 로그인이 필요 없으므로 이 엔드포인트는 비어 있습니다
        return ResponseEntity.ok("Login page is accessible.");
    }

    @GetMapping("/user/signup")
    public ResponseEntity<String> signupPage() {
        // 회원 가입 페이지는 더 이상 필요 없으므로 비어 있습니다
        return ResponseEntity.ok("Signup page is accessible.");
    }

    @PostMapping("/user/signup")
    public String signup(@Valid @RequestBody SignupRequestDto requestDto, BindingResult bindingResult) {
        // Validation 예외처리
        List<FieldError> fieldErrors = bindingResult.getFieldErrors();
        if(fieldErrors.size() > 0) {
            for (FieldError fieldError : bindingResult.getFieldErrors()) {
                log.error(fieldError.getField() + " 필드 : " + fieldError.getDefaultMessage());
            }
            return "redirect:/api/user/signup";
        }

        userService.signup(requestDto);

        return "redirect:/api/user/login-page";
    }

    @PostMapping("/user/login")
    public ResponseEntity<String> loginPage(@RequestBody LoginRequestDto requestDto) {
        String token = userService.login(requestDto);
        return ResponseEntity.ok("Login success" + "Token : " + token);
    }
}
