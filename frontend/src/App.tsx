import React from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import Header from './components/common/Header';
import MainPage from './components/pages/MainPage';
import RegisterPage from './components/pages/auth/RegisterPage';
import LoginPage from './components/pages/auth/LoginPage';
import FindUsernamePage from './components/pages/auth/FindUsernamePage';
import FindPasswordPage from './components/pages/auth/FindPasswordPage';
import PostCreatePage from './components/pages/post/PostCreatePage';
import PostDetailPage from './components/pages/post/PostDetailPage';
import PostListPage from './components/pages/board/PostListPage';
import ProfilePage from './components/pages/profile/ProfilePage';
import ProfileEditPage from './components/pages/profile/ProfileEditPage';
import SearchPage from './components/pages/search/SearchPage';
import AdminPage from './components/pages/admin/AdminPage';
import MessagesPage from './components/pages/messages/MessagesPage';
import TimetablePage from './components/pages/timetable/TimetablePage';
import LectureReviewListPage from './components/pages/lectureReview/LectureReviewListPage';
import LectureReviewDetailPage from './components/pages/lectureReview/LectureReviewDetailPage';
import LectureReviewWritePage from './components/pages/lectureReview/LectureReviewWritePage';
import './App.css';

function App() {
  return (
    <Router>
      <div className="App">
        <Header />
        <Routes>
          <Route path="/" element={<MainPage />} />
          <Route path="/register" element={<RegisterPage />} />
          <Route path="/login" element={<LoginPage />} />
          <Route path="/find-username" element={<FindUsernamePage />} />
          <Route path="/find-password" element={<FindPasswordPage />} />
          <Route path="/board/:boardId" element={<PostListPage />} />
          <Route path="/posts/create" element={<PostCreatePage />} />
          <Route path="/posts/:id" element={<PostDetailPage />} />
          <Route path="/profile/:userId" element={<ProfilePage />} />
          <Route path="/profile/:userId/edit" element={<ProfileEditPage />} />
          <Route path="/search" element={<SearchPage />} />
          <Route path="/admin" element={<AdminPage />} />
          <Route path="/messages" element={<MessagesPage />} />
          <Route path="/timetable" element={<TimetablePage />} />
          <Route path="/lecture-reviews" element={<LectureReviewListPage />} />
          <Route path="/lecture-reviews/write" element={<LectureReviewWritePage />} />
          <Route path="/lecture-reviews/:courseName/:professor" element={<LectureReviewDetailPage />} />
        </Routes>
      </div>
    </Router>
  );
}

export default App;
