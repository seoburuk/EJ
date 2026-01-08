package com.example.Globalin.controller;

import com.example.Globalin.dto.request.LoginRequest;
import com.example.Globalin.dto.request.RegisterRequest;
import com.example.Globalin.dto.response.LoginResponse;
import com.example.Globalin.dto.response.RegisterResponse;
import com.example.Globalin.service.AuthService;
import com.example.Globalin.service.EmailVerificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private EmailVerificationService emailVerificationService;

    // Helper method to create Map for Java 8 compatibility
    private java.util.Map<String, Object> createMap(String key1, Object value1, String key2, Object value2) {
        java.util.Map<String, Object> map = new java.util.HashMap<>();
        map.put(key1, value1);
        map.put(key2, value2);
        return map;
    }

    private java.util.Map<String, Object> createMap(String key1, Object value1, String key2, Object value2, String key3, Object value3) {
        java.util.Map<String, Object> map = new java.util.HashMap<>();
        map.put(key1, value1);
        map.put(key2, value2);
        map.put(key3, value3);
        return map;
    }

    private java.util.Map<String, Object> createMap(String key1, Object value1, String key2, Object value2, String key3, Object value3, String key4, Object value4) {
        java.util.Map<String, Object> map = new java.util.HashMap<>();
        map.put(key1, value1);
        map.put(key2, value2);
        map.put(key3, value3);
        map.put(key4, value4);
        return map;
    }

    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> register(@RequestBody RegisterRequest request) {
        RegisterResponse response = authService.register(request);

        if (response.isSuccess()) {
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request, HttpSession session) {
        LoginResponse response = authService.login(request);

        if (response.isSuccess()) {
            // セッションにユーザー情報を保存
            System.out.println("Login - Session ID: " + session.getId());
            System.out.println("Login - Setting userId: " + response.getUserId());
            session.setAttribute("userId", response.getUserId());
            session.setAttribute("username", response.getUsername());
            session.setAttribute("nickname", response.getNickname());
            System.out.println("Login - userId after set: " + session.getAttribute("userId"));
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpSession session) {
        session.invalidate();
        return ResponseEntity.ok().body("{\"success\": true, \"message\": \"ログアウトしました。\"}");
    }

    @GetMapping("/check")
    public ResponseEntity<?> checkSession(HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId != null) {
            LoginResponse response = new LoginResponse(true, "ログイン済みです。");
            response.setUserId(userId);
            response.setUsername((String) session.getAttribute("username"));
            response.setNickname((String) session.getAttribute("nickname"));
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.ok(new LoginResponse(false, "ログインしていません"));
        }
    }

    @PostMapping("/find-username")
    public ResponseEntity<?> findUsername(@RequestBody java.util.Map<String, String> request) {
        try {
            String email = request.get("email");
            if (email == null || email.trim().isEmpty()) {
                return ResponseEntity.badRequest().body(
                    createMap("success", false, "message", "メールアドレスを入力してください。")
                );
            }

            String username = authService.findUsernameByEmail(email);
            if (username != null) {
                return ResponseEntity.ok(createMap(
                    "success", true,
                    "message", "ユーザー名が見つかりました。",
                    "username", username
                ));
            } else {
                return ResponseEntity.ok(createMap(
                    "success", false,
                    "message", "このメールアドレスで登録されたアカウントがありません。"
                ));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                createMap("success", false, "message", "ユーザー名検索中にエラーが発生しました。")
            );
        }
    }

    @PostMapping("/send-verification-code")
    public ResponseEntity<?> sendVerificationCode(@RequestBody java.util.Map<String, String> request) {
        try {
            String email = request.get("email");
            String username = request.get("username");

            if (email == null || email.trim().isEmpty()) {
                return ResponseEntity.badRequest().body(
                    createMap("success", false, "message", "メールアドレスを入力してください。")
                );
            }

            if (username == null || username.trim().isEmpty()) {
                return ResponseEntity.badRequest().body(
                    createMap("success", false, "message", "ユーザー名を入力してください。")
                );
            }

            // メールアドレスとユーザー名が一致するか確認
            boolean isValid = authService.verifyEmailAndUsername(email, username);
            if (!isValid) {
                return ResponseEntity.ok(createMap(
                    "success", false,
                    "message", "メールアドレスとユーザー名が一致しません。"
                ));
            }

            // 認証コード生成および送信
            emailVerificationService.generateVerificationCode(email);

            return ResponseEntity.ok(createMap(
                "success", true,
                "message", "認証コードがメールアドレスに送信されました。（開発環境ではコンソールログを確認してください）"
            ));

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                createMap("success", false, "message", "認証コード送信中にエラーが発生しました。")
            );
        }
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody java.util.Map<String, String> request) {
        try {
            String email = request.get("email");
            String username = request.get("username");
            String verificationCode = request.get("verificationCode");
            String newPassword = request.get("newPassword");

            if (email == null || email.trim().isEmpty() ||
                username == null || username.trim().isEmpty() ||
                verificationCode == null || verificationCode.trim().isEmpty() ||
                newPassword == null || newPassword.trim().isEmpty()) {
                return ResponseEntity.badRequest().body(
                    createMap("success", false, "message", "すべての項目を入力してください。")
                );
            }

            if (newPassword.length() < 6) {
                return ResponseEntity.badRequest().body(
                    createMap("success", false, "message", "パスワードは6文字以上である必要があります。")
                );
            }

            // 認証コード検証
            boolean isCodeValid = emailVerificationService.verifyCode(email, verificationCode);
            if (!isCodeValid) {
                return ResponseEntity.ok(createMap(
                    "success", false,
                    "message", "認証コードが一致しないか、有効期限が切れています。"
                ));
            }

            // パスワード再設定
            boolean result = authService.resetPassword(email, username, newPassword);
            if (result) {
                return ResponseEntity.ok(createMap(
                    "success", true,
                    "message", "パスワードが正常に変更されました。"
                ));
            } else {
                return ResponseEntity.ok(createMap(
                    "success", false,
                    "message", "メールアドレスとユーザー名が一致しません。"
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                createMap("success", false, "message", "パスワード再設定中にエラーが発生しました。")
            );
        }
    }

    @PostMapping("/send-register-verification-code")
    public ResponseEntity<?> sendRegisterVerificationCode(@RequestBody java.util.Map<String, String> request) {
        try {
            String email = request.get("email");

            if (email == null || email.trim().isEmpty()) {
                java.util.Map<String, Object> errorMap = new java.util.HashMap<>();
                errorMap.put("success", false);
                errorMap.put("message", "メールアドレスを入力してください。");
                return ResponseEntity.badRequest().body(errorMap);
            }

            // メールアドレス重複チェック
            if (authService.isEmailExists(email)) {
                java.util.Map<String, Object> errorMap = new java.util.HashMap<>();
                errorMap.put("success", false);
                errorMap.put("message", "既に使用中のメールアドレスです。");
                return ResponseEntity.ok(errorMap);
            }

            // 認証コード生成および送信
            emailVerificationService.generateVerificationCode(email);

            java.util.Map<String, Object> successMap = new java.util.HashMap<>();
            successMap.put("success", true);
            successMap.put("message", "認証コードがメールアドレスに送信されました。（開発環境ではコンソールログを確認してください）");
            return ResponseEntity.ok(successMap);

        } catch (Exception e) {
            e.printStackTrace();
            java.util.Map<String, Object> errorMap = new java.util.HashMap<>();
            errorMap.put("success", false);
            errorMap.put("message", "認証コード送信中にエラーが発生しました。");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMap);
        }
    }

    @PostMapping("/register-with-verification")
    public ResponseEntity<?> registerWithVerification(@RequestBody java.util.Map<String, String> request) {
        try {
            String email = request.get("email");
            String verificationCode = request.get("verificationCode");
            String username = request.get("username");
            String password = request.get("password");
            String nickname = request.get("nickname");

            if (email == null || email.trim().isEmpty() ||
                verificationCode == null || verificationCode.trim().isEmpty() ||
                username == null || username.trim().isEmpty() ||
                password == null || password.trim().isEmpty() ||
                nickname == null || nickname.trim().isEmpty()) {
                java.util.Map<String, Object> errorMap = new java.util.HashMap<>();
                errorMap.put("success", false);
                errorMap.put("message", "すべての項目を入力してください。");
                return ResponseEntity.badRequest().body(errorMap);
            }

            // 認証コード検証
            boolean isCodeValid = emailVerificationService.verifyCode(email, verificationCode);
            if (!isCodeValid) {
                java.util.Map<String, Object> errorMap = new java.util.HashMap<>();
                errorMap.put("success", false);
                errorMap.put("message", "認証コードが一致しないか、有効期限が切れています。");
                return ResponseEntity.ok(errorMap);
            }

            // 会員登録処理
            RegisterRequest registerRequest = new RegisterRequest();
            registerRequest.setEmail(email);
            registerRequest.setUsername(username);
            registerRequest.setPassword(password);
            registerRequest.setNickname(nickname);

            RegisterResponse response = authService.register(registerRequest);

            if (response.isSuccess()) {
                return ResponseEntity.status(HttpStatus.CREATED).body(response);
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }
        } catch (Exception e) {
            e.printStackTrace();
            java.util.Map<String, Object> errorMap = new java.util.HashMap<>();
            errorMap.put("success", false);
            errorMap.put("message", "会員登録中にエラーが発生しました。");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMap);
        }
    }
}
