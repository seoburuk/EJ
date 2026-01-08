package com.example.Globalin.dao;

import com.example.Globalin.model.Timetable;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface TimetableDao {

    // 시간표 생성
    void insertTimetable(Timetable timetable);

    // 시간표 조회
    Timetable findById(@Param("id") Long id);

    // 사용자의 모든 시간표 조회
    List<Timetable> findByUserId(@Param("userId") Long userId);

    // 기본 시간표 조회
    Timetable findDefaultByUserId(@Param("userId") Long userId);

    // 시간표 수정
    void updateTimetable(Timetable timetable);

    // 기본 시간표 설정 (다른 시간표의 isDefault를 false로)
    void clearDefaultByUserId(@Param("userId") Long userId);

    // 시간표 삭제
    void deleteTimetable(@Param("id") Long id);
}
