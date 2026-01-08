package com.example.Globalin.service;

import com.example.Globalin.dao.UserDao;
import com.example.Globalin.dto.request.LoginRequest;
import com.example.Globalin.dto.request.RegisterRequest;
import com.example.Globalin.dto.response.LoginResponse;
import com.example.Globalin.dto.response.RegisterResponse;
import com.example.Globalin.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

    @Autowired
    private UserDao userDao;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Transactional
    public RegisterResponse register(RegisterRequest request) {
        try {
            // 1. バリデーション
            if (request.getUsername() == null || request.getUsername().trim().isEmpty()) {
                return new RegisterResponse(false, "ユーザーIDを入力してください");
            }
            if (request.getPassword() == null || request.getPassword().trim().isEmpty()) {
                return new RegisterResponse(false, "パスワードを入力してください");
            }
            if (request.getEmail() == null || request.getEmail().trim().isEmpty()) {
                return new RegisterResponse(false, "メールアドレスを入力してください");
            }
            if (request.getNickname() == null || request.getNickname().trim().isEmpty()) {
                return new RegisterResponse(false, "ニックネームを入力してください");
            }

            // 2. ユーザーID長バリデーション（4-20文字）
            if (request.getUsername().length() < 4 || request.getUsername().length() > 20) {
                return new RegisterResponse(false, "ユーザーIDは4～20文字である必要があります");
            }

            // 3. パスワード長バリデーション（8文字以上）
            if (request.getPassword().length() < 8) {
                return new RegisterResponse(false, "パスワードは最低8文字以上である必要があります");
            }

            // 4. メールアドレス形式バリデーション
            if (!isValidEmail(request.getEmail())) {
                return new RegisterResponse(false, "正しいメールアドレス形式ではありません");
            }

            // 5. 重複チェック - ユーザーID
            if (userDao.countByUsername(request.getUsername()) > 0) {
                return new RegisterResponse(false, "既に使用中のユーザーIDです");
            }

            // 6. 重複チェック - メール
            if (userDao.countByEmail(request.getEmail()) > 0) {
                return new RegisterResponse(false, "既に使用中のメールアドレスです");
            }

            // 7. ユーザー作成
            User user = new User();
            user.setUsername(request.getUsername());
            user.setPassword(passwordEncoder.encode(request.getPassword())); // パスワード暗号化
            user.setEmail(request.getEmail());
            user.setNickname(request.getNickname());
            user.setStatus("ACTIVE");

            userDao.insertUser(user);

            return new RegisterResponse(true, "会員登録が完了しました", user.getId());

        } catch (Exception e) {
            e.printStackTrace();
            return new RegisterResponse(false, "会員登録中にエラーが発生しました: " + e.getMessage());
        }
    }

    public LoginResponse login(LoginRequest request) {
        try {
            // 1. バリデーション
            if (request.getUsername() == null || request.getUsername().trim().isEmpty()) {
                return new LoginResponse(false, "ユーザーIDを入力してください");
            }
            if (request.getPassword() == null || request.getPassword().trim().isEmpty()) {
                return new LoginResponse(false, "パスワードを入力してください");
            }

            // 2. ユーザー取得
            User user = userDao.findByUsername(request.getUsername());
            if (user == null) {
                return new LoginResponse(false, "ユーザーIDまたはパスワードが一致しません");
            }

            // 3. パスワード確認
            if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
                return new LoginResponse(false, "ユーザーIDまたはパスワードが一致しません");
            }

            // 4. アカウント状態確認
            if (!"ACTIVE".equals(user.getStatus())) {
                return new LoginResponse(false, "無効化されたアカウントです");
            }

            // 5. 最後のログイン時間更新
            userDao.updateLastLoginDate(user.getId());

            // 6. ログイン成功
            LoginResponse response = new LoginResponse(true, "ログイン成功");
            response.setUserId(user.getId());
            response.setUsername(user.getUsername());
            response.setNickname(user.getNickname());

            return response;

        } catch (Exception e) {
            e.printStackTrace();
            return new LoginResponse(false, "ログイン中にエラーが発生しました: " + e.getMessage());
        }
    }

    // メールアドレス形式バリデーション
    private boolean isValidEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
        return email.matches(emailRegex);
    }

    // メールアドレスからユーザーID取得
    public String findUsernameByEmail(String email) {
        try {
            User user = userDao.findByEmail(email);
            if (user != null) {
                return user.getUsername();
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // メールアドレスとユーザーIDの一致確認
    public boolean verifyEmailAndUsername(String email, String username) {
        try {
            User user = userDao.findByEmail(email);
            return user != null && user.getUsername().equals(username);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // パスワード再設定
    @Transactional
    public boolean resetPassword(String email, String username, String newPassword) {
        try {
            // メールアドレスとユーザーIDが一致するユーザーを検索
            User user = userDao.findByEmail(email);
            if (user == null || !user.getUsername().equals(username)) {
                return false;
            }

            // パスワード暗号化後に更新
            String encryptedPassword = passwordEncoder.encode(newPassword);
            userDao.updatePassword(user.getId(), encryptedPassword);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // メールアドレス重複確認
    public boolean isEmailExists(String email) {
        try {
            return userDao.countByEmail(email) > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
