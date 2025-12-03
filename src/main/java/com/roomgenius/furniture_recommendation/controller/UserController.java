package com.roomgenius.furniture_recommendation.controller;

import com.roomgenius.furniture_recommendation.entity.UserDTO;
import com.roomgenius.furniture_recommendation.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Validated
public class UserController {

    private final UserService userService;

    /** 회원가입 **/
    @PostMapping("/signup")
    public ResponseEntity<Map<String, Object>> signup(@Valid @RequestBody UserDTO dto) {
        UserDTO response = userService.signup(dto);

        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("message", "회원가입이 완료되었습니다");
        result.put("data", response);

        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    /** 로그인 **/
    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody UserDTO dto) {
        UserDTO response = userService.login(dto);

        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("message", response.getMessage());
        result.put("data", response);

        return ResponseEntity.ok(result);
    }

    /** 회원 단건 조회 **/
    @GetMapping("/{userId}")
    public ResponseEntity<Map<String, Object>> getUser(@PathVariable Integer userId) {
        UserDTO response = userService.getUserById(userId);

        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("data", response);

        return ResponseEntity.ok(result);
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<Map<String, Object>> getUserByEmail(@PathVariable String email) {
        UserDTO response = userService.getUserByEmail(email);

        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("data", response);

        return ResponseEntity.ok(result);
    }

    @PutMapping("/update")
    public ResponseEntity<Map<String, Object>> updateUser(@RequestBody UserDTO dto) {
        UserDTO updated = userService.updateUser(dto);

        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("data", updated);

        return ResponseEntity.ok(result);
    }

    // 비밀번호 변경
    @PutMapping("/password")
    public ResponseEntity<Map<String, Object>> changePassword(@RequestBody Map<String, String> req) {
        String email = req.get("email");
        String currentPw = req.get("currentPw");
        String newPw = req.get("newPw");

        userService.changePassword(email, currentPw, newPw);

        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("message", "비밀번호 변경 완료");

        return ResponseEntity.ok(result);
    }


}