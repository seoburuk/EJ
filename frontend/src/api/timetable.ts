const API_BASE_URL = 'http://localhost:8080/api';

export interface Timetable {
  id?: number;
  userId?: number;
  semester: string;
  year: number;
  name: string;
  isDefault?: boolean;
  createdAt?: string;
  updatedAt?: string;
}

export interface Course {
  id?: number;
  timetableId?: number;
  courseName: string;
  professor?: string;
  location?: string;
  dayOfWeek: number; // 0=月, 1=火, 2=水, 3=木, 4=金
  startTime: string; // "09:00"
  endTime: string; // "10:30"
  color?: string;
  credits?: number;
  memo?: string;
  createdAt?: string;
  updatedAt?: string;
}

export interface ApiResponse {
  success: boolean;
  message: string;
}

export const timetableApi = {
  // 時間割作成
  createTimetable: async (timetable: Timetable): Promise<Timetable> => {
    const response = await fetch(`${API_BASE_URL}/timetables`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      credentials: 'include',
      body: JSON.stringify(timetable),
    });

    if (!response.ok) {
      throw new Error('時間割作成に失敗しました。');
    }

    return response.json();
  },

  // ユーザーのすべての時間割を取得
  getTimetables: async (): Promise<Timetable[]> => {
    const response = await fetch(`${API_BASE_URL}/timetables`, {
      credentials: 'include',
    });

    if (!response.ok) {
      throw new Error('時間割リスト取得に失敗しました。');
    }

    return response.json();
  },

  // デフォルト時間割を取得
  getDefaultTimetable: async (): Promise<Timetable> => {
    const response = await fetch(`${API_BASE_URL}/timetables/default`, {
      credentials: 'include',
    });

    if (!response.ok) {
      throw new Error('デフォルト時間割取得に失敗しました。');
    }

    return response.json();
  },

  // 時間割更新
  updateTimetable: async (id: number, timetable: Timetable): Promise<ApiResponse> => {
    const response = await fetch(`${API_BASE_URL}/timetables/${id}`, {
      method: 'PUT',
      headers: {
        'Content-Type': 'application/json',
      },
      credentials: 'include',
      body: JSON.stringify(timetable),
    });

    if (!response.ok) {
      throw new Error('時間割更新に失敗しました。');
    }

    return response.json();
  },

  // 時間割削除
  deleteTimetable: async (id: number): Promise<ApiResponse> => {
    const response = await fetch(`${API_BASE_URL}/timetables/${id}`, {
      method: 'DELETE',
      credentials: 'include',
    });

    if (!response.ok) {
      throw new Error('時間割削除に失敗しました。');
    }

    return response.json();
  },

  // 科目追加
  addCourse: async (timetableId: number, course: Course): Promise<Course> => {
    const response = await fetch(`${API_BASE_URL}/timetables/${timetableId}/courses`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      credentials: 'include',
      body: JSON.stringify(course),
    });

    if (!response.ok) {
      throw new Error('科目追加に失敗しました。');
    }

    return response.json();
  },

  // 時間割のすべての科目を取得
  getCourses: async (timetableId: number): Promise<Course[]> => {
    const response = await fetch(`${API_BASE_URL}/timetables/${timetableId}/courses`, {
      credentials: 'include',
    });

    if (!response.ok) {
      throw new Error('科目リスト取得に失敗しました。');
    }

    return response.json();
  },

  // 科目更新
  updateCourse: async (id: number, course: Course): Promise<ApiResponse> => {
    const response = await fetch(`${API_BASE_URL}/timetables/courses/${id}`, {
      method: 'PUT',
      headers: {
        'Content-Type': 'application/json',
      },
      credentials: 'include',
      body: JSON.stringify(course),
    });

    if (!response.ok) {
      throw new Error('科目更新に失敗しました。');
    }

    return response.json();
  },

  // 科目削除
  deleteCourse: async (id: number): Promise<ApiResponse> => {
    const response = await fetch(`${API_BASE_URL}/timetables/courses/${id}`, {
      method: 'DELETE',
      credentials: 'include',
    });

    if (!response.ok) {
      throw new Error('科目削除に失敗しました。');
    }

    return response.json();
  },
};
