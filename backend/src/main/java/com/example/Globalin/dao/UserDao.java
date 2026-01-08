package com.example.Globalin.dao;

import com.example.Globalin.model.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserDao {

    // 사용자 생성
    void insertUser(User user);

    // 사용자 조회 (ID)
    User findById(@Param("id") Long id);

    // 사용자 조회 (username)
    User findByUsername(@Param("username") String username);

    // 사용자 조회 (email)
    User findByEmail(@Param("email") String email);

    // 사용자 업데이트
    void updateUser(User user);

    // 마지막 로그인 시간 업데이트
    void updateLastLoginDate(@Param("id") Long id);

    // 사용자 삭제
    void deleteUser(@Param("id") Long id);

    // username 중복 체크
    int countByUsername(@Param("username") String username);

    // email 중복 체크
    int countByEmail(@Param("email") String email);

    // 전체 사용자 수 조회
    int countAllUsers();

    // 전체 사용자 목록 조회
    java.util.List<User> findAllUsers(@Param("offset") int offset, @Param("limit") int limit);

    // 사용자 상태 변경
    void updateUserStatus(@Param("id") Long id, @Param("status") String status);

    // 비밀번호 변경
    void updatePassword(@Param("id") Long id, @Param("password") String password);
}
