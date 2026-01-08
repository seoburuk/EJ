package com.example.Globalin.controller;

import com.example.Globalin.model.Course;
import com.example.Globalin.model.Timetable;
import com.example.Globalin.service.TimetableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/timetables")
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
public class TimetableController {

    @Autowired
    private TimetableService timetableService;

    // 시간표 생성
    @PostMapping
    public ResponseEntity<Timetable> createTimetable(@RequestBody Timetable timetable, HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            return ResponseEntity.status(401).build();
        }

        timetable.setUserId(userId);
        Timetable created = timetableService.createTimetable(timetable);
        return ResponseEntity.ok(created);
    }

    // 사용자의 모든 시간표 조회
    @GetMapping
    public ResponseEntity<List<Timetable>> getUserTimetables(HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            return ResponseEntity.status(401).build();
        }

        List<Timetable> timetables = timetableService.getUserTimetables(userId);
        return ResponseEntity.ok(timetables);
    }

    // 기본 시간표 조회
    @GetMapping("/default")
    public ResponseEntity<Timetable> getDefaultTimetable(HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            return ResponseEntity.status(401).build();
        }

        Timetable timetable = timetableService.getDefaultTimetable(userId);
        return ResponseEntity.ok(timetable);
    }

    // 시간표 수정
    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> updateTimetable(
            @PathVariable Long id,
            @RequestBody Timetable timetable,
            HttpSession session) {

        Map<String, Object> response = new HashMap<>();
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            response.put("success", false);
            response.put("message", "로그인이 필요합니다.");
            return ResponseEntity.ok(response);
        }

        try {
            timetableService.updateTimetable(id, timetable, userId);
            response.put("success", true);
            response.put("message", "시간표가 수정되었습니다.");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.ok(response);
        }
    }

    // 시간표 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deleteTimetable(@PathVariable Long id, HttpSession session) {
        Map<String, Object> response = new HashMap<>();
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            response.put("success", false);
            response.put("message", "로그인이 필요합니다.");
            return ResponseEntity.ok(response);
        }

        try {
            timetableService.deleteTimetable(id, userId);
            response.put("success", true);
            response.put("message", "시간표가 삭제되었습니다.");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.ok(response);
        }
    }

    // 과목 추가
    @PostMapping("/{timetableId}/courses")
    public ResponseEntity<Course> addCourse(
            @PathVariable Long timetableId,
            @RequestBody Course course,
            HttpSession session) {

        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            return ResponseEntity.status(401).build();
        }

        course.setTimetableId(timetableId);
        Course created = timetableService.addCourse(course, userId);
        return ResponseEntity.ok(created);
    }

    // 시간표의 모든 과목 조회
    @GetMapping("/{timetableId}/courses")
    public ResponseEntity<List<Course>> getTimetableCourses(@PathVariable Long timetableId, HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            return ResponseEntity.status(401).build();
        }

        List<Course> courses = timetableService.getTimetableCourses(timetableId, userId);
        return ResponseEntity.ok(courses);
    }

    // 과목 수정
    @PutMapping("/courses/{id}")
    public ResponseEntity<Map<String, Object>> updateCourse(
            @PathVariable Long id,
            @RequestBody Course course,
            HttpSession session) {

        Map<String, Object> response = new HashMap<>();
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            response.put("success", false);
            response.put("message", "로그인이 필요합니다.");
            return ResponseEntity.ok(response);
        }

        try {
            timetableService.updateCourse(id, course, userId);
            response.put("success", true);
            response.put("message", "과목이 수정되었습니다.");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.ok(response);
        }
    }

    // 과목 삭제
    @DeleteMapping("/courses/{id}")
    public ResponseEntity<Map<String, Object>> deleteCourse(@PathVariable Long id, HttpSession session) {
        Map<String, Object> response = new HashMap<>();
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            response.put("success", false);
            response.put("message", "로그인이 필요합니다.");
            return ResponseEntity.ok(response);
        }

        try {
            timetableService.deleteCourse(id, userId);
            response.put("success", true);
            response.put("message", "과목이 삭제되었습니다.");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.ok(response);
        }
    }
}
