package com.example.Globalin.service;

import com.example.Globalin.dao.CourseDao;
import com.example.Globalin.dao.TimetableDao;
import com.example.Globalin.model.Course;
import com.example.Globalin.model.Timetable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TimetableService {

    @Autowired
    private TimetableDao timetableDao;

    @Autowired
    private CourseDao courseDao;

    /**
     * 時間割作成
     */
    @Transactional
    public Timetable createTimetable(Timetable timetable) {
        // 既定の時間割として設定する場合、他の時間割の既定設定を解除
        if (timetable.getIsDefault() != null && timetable.getIsDefault()) {
            timetableDao.clearDefaultByUserId(timetable.getUserId());
        }

        timetableDao.insertTimetable(timetable);
        return timetable;
    }

    /**
     * 時間割取得
     */
    public Timetable getTimetable(Long id) {
        return timetableDao.findById(id);
    }

    /**
     * ユーザーのすべての時間割取得
     */
    public List<Timetable> getUserTimetables(Long userId) {
        return timetableDao.findByUserId(userId);
    }

    /**
     * 既定の時間割取得
     */
    public Timetable getDefaultTimetable(Long userId) {
        return timetableDao.findDefaultByUserId(userId);
    }

    /**
     * 時間割更新
     */
    @Transactional
    public void updateTimetable(Long id, Timetable timetable, Long userId) {
        Timetable existing = timetableDao.findById(id);
        if (existing == null || !existing.getUserId().equals(userId)) {
            throw new RuntimeException("時間割を編集する権限がありません");
        }

        // 既定の時間割として設定する場合
        if (timetable.getIsDefault() != null && timetable.getIsDefault()) {
            timetableDao.clearDefaultByUserId(userId);
        }

        timetable.setId(id);
        timetable.setUserId(userId);
        timetableDao.updateTimetable(timetable);
    }

    /**
     * 時間割削除
     */
    @Transactional
    public void deleteTimetable(Long id, Long userId) {
        Timetable existing = timetableDao.findById(id);
        if (existing == null || !existing.getUserId().equals(userId)) {
            throw new RuntimeException("時間割を削除する権限がありません");
        }

        // 時間割のすべての科目も一緒に削除
        courseDao.deleteByTimetableId(id);
        timetableDao.deleteTimetable(id);
    }

    /**
     * 科目追加
     */
    @Transactional
    public Course addCourse(Course course, Long userId) {
        // 時間割の所有権確認
        Timetable timetable = timetableDao.findById(course.getTimetableId());
        if (timetable == null || !timetable.getUserId().equals(userId)) {
            throw new RuntimeException("科目を追加する権限がありません");
        }

        courseDao.insertCourse(course);
        return course;
    }

    /**
     * 時間割のすべての科目取得
     */
    public List<Course> getTimetableCourses(Long timetableId, Long userId) {
        // 時間割の所有権確認
        Timetable timetable = timetableDao.findById(timetableId);
        if (timetable == null || !timetable.getUserId().equals(userId)) {
            throw new RuntimeException("科目を取得する権限がありません");
        }

        return courseDao.findByTimetableId(timetableId);
    }

    /**
     * 科目更新
     */
    @Transactional
    public void updateCourse(Long id, Course course, Long userId) {
        Course existing = courseDao.findById(id);
        if (existing == null) {
            throw new RuntimeException("科目が見つかりません");
        }

        // 時間割の所有権確認
        Timetable timetable = timetableDao.findById(existing.getTimetableId());
        if (timetable == null || !timetable.getUserId().equals(userId)) {
            throw new RuntimeException("科目を編集する権限がありません");
        }

        course.setId(id);
        course.setTimetableId(existing.getTimetableId());
        courseDao.updateCourse(course);
    }

    /**
     * 科目削除
     */
    @Transactional
    public void deleteCourse(Long id, Long userId) {
        Course existing = courseDao.findById(id);
        if (existing == null) {
            throw new RuntimeException("科目が見つかりません");
        }

        // 時間割の所有権確認
        Timetable timetable = timetableDao.findById(existing.getTimetableId());
        if (timetable == null || !timetable.getUserId().equals(userId)) {
            throw new RuntimeException("科目を削除する権限がありません");
        }

        courseDao.deleteCourse(id);
    }
}
