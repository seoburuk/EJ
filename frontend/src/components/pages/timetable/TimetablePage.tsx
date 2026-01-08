import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { timetableApi, Timetable, Course } from '../../../api/timetable';
import './TimetablePage.scss';

const TimetablePage: React.FC = () => {
  const navigate = useNavigate();
  const [currentTimetable, setCurrentTimetable] = useState<Timetable | null>(null);
  const [courses, setCourses] = useState<Course[]>([]);
  const [loading, setLoading] = useState(true);
  const [showModal, setShowModal] = useState(false);
  const [editingCourse, setEditingCourse] = useState<Course | null>(null);

  const [formData, setFormData] = useState<Course>({
    courseName: '',
    professor: '',
    location: '',
    dayOfWeek: 0,
    startTime: '09:00',
    endTime: '10:00',
    color: '#937EBF',
    credits: 3,
    memo: '',
  });

  const currentUserId = localStorage.getItem('userId');

  const days = ['月', '火', '水', '木', '金'];
  const hours = Array.from({ length: 13 }, (_, i) => i + 9); // 9時から21時まで
  const colors = ['#937EBF', '#FE9F1A', '#4CAF50', '#2196F3', '#FF5722', '#9C27B0', '#00BCD4'];

  useEffect(() => {
    if (!currentUserId) {
      alert('ログインが必要です。');
      navigate('/login');
      return;
    }
    loadTimetable();
  }, []);

  const loadTimetable = async () => {
    try {
      const timetable = await timetableApi.getDefaultTimetable();
      if (timetable) {
        setCurrentTimetable(timetable);
        const coursesData = await timetableApi.getCourses(timetable.id!);
        setCourses(coursesData);
      } else {
        // デフォルト時間割がない場合は作成
        const newTimetable = await timetableApi.createTimetable({
          semester: '1学期',
          year: 2025,
          name: '私の時間割',
          isDefault: true,
        });
        setCurrentTimetable(newTimetable);
      }
    } catch (err) {
      console.error('時間割読み込み失敗:', err);
    } finally {
      setLoading(false);
    }
  };

  const handleAddCourse = () => {
    setEditingCourse(null);
    setFormData({
      courseName: '',
      professor: '',
      location: '',
      dayOfWeek: 0,
      startTime: '09:00',
      endTime: '10:00',
      color: colors[Math.floor(Math.random() * colors.length)],
      credits: 3,
      memo: '',
    });
    setShowModal(true);
  };

  const handleEditCourse = (course: Course) => {
    setEditingCourse(course);
    setFormData(course);
    setShowModal(true);
  };

  const handleSaveCourse = async () => {
    if (!currentTimetable) return;
    if (!formData.courseName.trim()) {
      alert('科目名を入力してください。');
      return;
    }

    try {
      if (editingCourse) {
        await timetableApi.updateCourse(editingCourse.id!, formData);
      } else {
        await timetableApi.addCourse(currentTimetable.id!, formData);
      }
      setShowModal(false);
      loadTimetable();
    } catch (err: any) {
      alert(err.message || '保存中にエラーが発生しました。');
    }
  };

  const handleDeleteCourse = async (id: number) => {
    if (!window.confirm('この科目を削除しますか？')) return;

    try {
      await timetableApi.deleteCourse(id);
      loadTimetable();
    } catch (err: any) {
      alert(err.message || '削除中にエラーが発生しました。');
    }
  };

  const timeToMinutes = (time: string) => {
    const [hours, minutes] = time.split(':').map(Number);
    return hours * 60 + minutes;
  };

  const getCoursesForSlot = (day: number, hour: number) => {
    const slotStart = hour * 60;
    const slotEnd = (hour + 1) * 60;

    return courses.filter((course) => {
      if (course.dayOfWeek !== day) return false;
      const courseStart = timeToMinutes(course.startTime);
      const courseEnd = timeToMinutes(course.endTime);
      return courseStart < slotEnd && courseEnd > slotStart;
    });
  };

  const getCourseStyle = (course: Course, hour: number) => {
    const slotStart = hour * 60;
    const courseStart = timeToMinutes(course.startTime);
    const courseEnd = timeToMinutes(course.endTime);
    const duration = courseEnd - courseStart;
    const height = (duration / 60) * 100;
    const top = courseStart <= slotStart ? 0 : ((courseStart - slotStart) / 60) * 100;

    return {
      height: `${height}%`,
      top: `${top}%`,
      backgroundColor: course.color,
    };
  };

  if (loading) return <div className="loading">読み込み中...</div>;

  return (
    <div className="timetable-page">
      <div className="timetable-container">
        <div className="timetable-header">
          <h1>私の時間割</h1>
          <button className="add-course-btn" onClick={handleAddCourse}>
            + 科目追加
          </button>
        </div>

        <div className="timetable-grid">
          <div className="time-column">
            <div className="time-header"></div>
            {hours.map((hour) => (
              <div key={hour} className="time-slot">
                {hour}:00
              </div>
            ))}
          </div>

          {days.map((day, dayIndex) => (
            <div key={dayIndex} className="day-column">
              <div className="day-header">{day}</div>
              {hours.map((hour) => (
                <div key={hour} className="course-slot">
                  {getCoursesForSlot(dayIndex, hour).map((course) => {
                    const isFirstSlot = timeToMinutes(course.startTime) >= hour * 60 &&
                                       timeToMinutes(course.startTime) < (hour + 1) * 60;
                    if (!isFirstSlot) return null;

                    return (
                      <div
                        key={course.id}
                        className="course-block"
                        style={getCourseStyle(course, hour)}
                        onClick={() => handleEditCourse(course)}
                      >
                        <div className="course-name">{course.courseName}</div>
                        <div className="course-location">{course.location}</div>
                      </div>
                    );
                  })}
                </div>
              ))}
            </div>
          ))}
        </div>
      </div>

      {showModal && (
        <div className="modal-overlay" onClick={() => setShowModal(false)}>
          <div className="modal-content" onClick={(e) => e.stopPropagation()}>
            <h2>{editingCourse ? '科目編集' : '科目追加'}</h2>

            <div className="form-group">
              <label>科目名 *</label>
              <input
                type="text"
                value={formData.courseName}
                onChange={(e) => setFormData({ ...formData, courseName: e.target.value })}
                placeholder="例：Javaプログラミング"
              />
            </div>

            <div className="form-group">
              <label>教授</label>
              <input
                type="text"
                value={formData.professor || ''}
                onChange={(e) => setFormData({ ...formData, professor: e.target.value })}
                placeholder="例：金教授"
              />
            </div>

            <div className="form-group">
              <label>教室</label>
              <input
                type="text"
                value={formData.location || ''}
                onChange={(e) => setFormData({ ...formData, location: e.target.value })}
                placeholder="例：工学館301"
              />
            </div>

            <div className="form-row">
              <div className="form-group">
                <label>曜日</label>
                <select
                  value={formData.dayOfWeek}
                  onChange={(e) => setFormData({ ...formData, dayOfWeek: Number(e.target.value) })}
                >
                  {days.map((day, idx) => (
                    <option key={idx} value={idx}>{day}</option>
                  ))}
                </select>
              </div>

              <div className="form-group">
                <label>開始時間</label>
                <input
                  type="time"
                  value={formData.startTime}
                  onChange={(e) => setFormData({ ...formData, startTime: e.target.value })}
                />
              </div>

              <div className="form-group">
                <label>終了時間</label>
                <input
                  type="time"
                  value={formData.endTime}
                  onChange={(e) => setFormData({ ...formData, endTime: e.target.value })}
                />
              </div>
            </div>

            <div className="form-row">
              <div className="form-group">
                <label>単位</label>
                <input
                  type="number"
                  step="0.5"
                  value={formData.credits || ''}
                  onChange={(e) => setFormData({ ...formData, credits: Number(e.target.value) })}
                />
              </div>

              <div className="form-group">
                <label>色</label>
                <div className="color-picker">
                  {colors.map((color) => (
                    <div
                      key={color}
                      className={`color-option ${formData.color === color ? 'selected' : ''}`}
                      style={{ backgroundColor: color }}
                      onClick={() => setFormData({ ...formData, color })}
                    />
                  ))}
                </div>
              </div>
            </div>

            <div className="modal-buttons">
              {editingCourse && (
                <button
                  className="delete-button"
                  onClick={() => {
                    setShowModal(false);
                    handleDeleteCourse(editingCourse.id!);
                  }}
                >
                  削除
                </button>
              )}
              <button className="save-button" onClick={handleSaveCourse}>
                保存
              </button>
              <button className="cancel-button" onClick={() => setShowModal(false)}>
                キャンセル
              </button>
            </div>
          </div>
        </div>
      )}
    </div>
  );
};

export default TimetablePage;
