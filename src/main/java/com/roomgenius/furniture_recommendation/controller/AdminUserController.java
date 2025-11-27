package com.roomgenius.furniture_recommendation.controller;

import com.roomgenius.furniture_recommendation.entity.UserDTO;
import com.roomgenius.furniture_recommendation.service.AdminUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/users")
@RequiredArgsConstructor
public class AdminUserController {

    private final AdminUserService adminUserService;

    /** 전체 회원 조회 + 검색 + 필터 */
    @GetMapping("")
    public List<UserDTO> getUsers(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String role,
            @RequestParam(defaultValue = "created_desc") String sort
    ) {
        return adminUserService.getUsers(keyword, role, sort);
    }


    /** 권한 변경 (USER → ADMIN) */
    @PutMapping("/{id}/role")
    public ResponseEntity<String> updateUserRole(
            @PathVariable Integer id,
            @RequestBody UserDTO dto
    ) {
        adminUserService.updateUserRole(id, dto.getRole());
        return ResponseEntity.ok("updated");
    }
}
