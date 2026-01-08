package com.example.Globalin.dao;

import com.example.Globalin.model.Course;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CourseDao {

    // 과목 추가
    void insertCourse(Course course);

    // 과목 조회
    Course findById(@Param("id") Long id);

    // 시간표의 모든 과목 조회
    List<Course> findByTimetableId(@Param("timetableId") Long timetableId);

    // 과목 수정
    void updateCourse(Course course);

    // 과목 삭제
    void deleteCourse(@Param("id") Long id);

    // 시간표의 모든 과목 삭제
    void deleteByTimetableId(@Param("timetableId") Long timetableId);
}
