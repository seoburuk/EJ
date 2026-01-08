package com.example.Globalin.controller;

import com.example.Globalin.dto.request.UpdateProfileRequest;
import com.example.Globalin.dto.response.UserProfileDTO;
import com.example.Globalin.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * ユーザーコントローラー
 * ユーザープロフィール照会および管理API
 */
@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    /**
     * ユーザープロフィール照会
     * GET /api/users/{userId}/profile
     */
    @GetMapping("/{userId}/profile")
    public ResponseEntity<UserProfileDTO> getUserProfile(@PathVariable Long userId) {
        logger.info("ユーザープロフィール照会リクエスト - userId: {}", userId);

        try {
            UserProfileDTO profile = userService.getUserProfile(userId);
            return ResponseEntity.ok(profile);

        } catch (RuntimeException e) {
            logger.error("プロフィール照会失敗 - userId: {}, error: {}", userId, e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);

        } catch (Exception e) {
            logger.error("プロフィール照会中のエラー - userId: {}, error: {}", userId, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    /**
     * 現在ログイン中のユーザープロフィール照会
     * GET /api/users/me/profile
     */
    @GetMapping("/me/profile")
    public ResponseEntity<UserProfileDTO> getMyProfile(@RequestParam(required = false) Long userId) {
        logger.info("マイプロフィール照会リクエスト - userId: {}", userId);

        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        return getUserProfile(userId);
    }

    /**
     * プロフィールアップデート
     * PUT /api/users/{userId}/profile
     */
    @PutMapping("/{userId}/profile")
    public ResponseEntity<UserProfileDTO> updateProfile(
            @PathVariable Long userId,
            @RequestBody UpdateProfileRequest request) {
        logger.info("プロフィールアップデートリクエスト - userId: {}", userId);

        try {
            UserProfileDTO updatedProfile = userService.updateProfile(userId, request);
            return ResponseEntity.ok(updatedProfile);

        } catch (RuntimeException e) {
            logger.error("プロフィールアップデート失敗 - userId: {}, error: {}", userId, e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);

        } catch (Exception e) {
            logger.error("プロフィールアップデート中のエラー発生 - userId: {}", userId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}
