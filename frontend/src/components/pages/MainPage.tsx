// MainPage.tsx
import React, { useState, useEffect } from 'react';
import LeftSidebar from './components/LeftSidebar';
import MainContent from './components/MainContent';
import RightSidebar from './components/RightSidebar';
import { UserProfile, Post, HotPost, Board } from '../../types';
import './MainPage.scss';

interface BoardWithPosts {
  id: number;
  name: string;
  description: string;
  category: string;
  icon: string;
  postCount: number;
  recentPosts: Post[];
}

interface MainPageData {
  userProfile: UserProfile;
  humanitiesPosts: Post[];
  freePosts: Post[];
  hotPosts: HotPost[];
  bestBoards: Board[];
  allBoards: BoardWithPosts[];
}

const MainPage: React.FC = () => {
  const [data, setData] = useState<MainPageData | null>(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const [userProfile, setUserProfile] = useState<UserProfile | null>(null);

  useEffect(() => {
    fetchUserProfile();
    fetchMainPageData();
  }, []);

  const fetchUserProfile = async () => {
    // localStorageからログインユーザーID確認
    const userId = localStorage.getItem('userId');

    if (!userId) {
      setUserProfile(null);
      return;
    }

    try {
      const apiUrl = process.env.REACT_APP_API_URL || 'http://localhost:8080';
      const response = await fetch(`${apiUrl}/api/users/${userId}/profile`);

      if (response.ok) {
        const profileData = await response.json();
        setUserProfile(profileData);
      } else {
        console.error('プロフィール取得失敗');
        setUserProfile(null);
      }
    } catch (error) {
      console.error('プロフィール取得エラー:', error);
      setUserProfile(null);
    }
  };

  const fetchMainPageData = async () => {
    try {
      // Use relative URL for production (proxied by nginx) or absolute URL for development
      const apiUrl = process.env.REACT_APP_API_URL || 'http://localhost:8080';
      const response = await fetch(`${apiUrl}/api/main/dashboard`, {
        credentials: 'include'
      });

      if (!response.ok) {
        throw new Error('データの読み込みに失敗しました。');
      }

      const result = await response.json();
      setData(result);
    } catch (error) {
      console.error('メインページ読み込み失敗:', error);
      setError(error instanceof Error ? error.message : '不明なエラーが発生しました。');
    } finally {
      setLoading(false);
    }
  };

  if (loading) {
    return (
      <div className="main-page-container loading">
        <div className="loading-message">読み込み中...</div>
      </div>
    );
  }

  if (error) {
    return (
      <div className="main-page-container error">
        <div className="error-message">
          {error}
        </div>
      </div>
    );
  }

  // データがなくても空配列でレンダリング
  const safeData = data || {
    userProfile: null,
    humanitiesPosts: [],
    freePosts: [],
    hotPosts: [],
    bestBoards: [],
    allBoards: []
  };

  return (
    <div className="main-page-container">
      <LeftSidebar userProfile={userProfile} />
      <MainContent
        humanitiesPosts={safeData.humanitiesPosts}
        freePosts={safeData.freePosts}
        allBoards={safeData.allBoards}
      />
      <RightSidebar
        hotPosts={safeData.hotPosts}
      />
    </div>
  );
};

export default MainPage;