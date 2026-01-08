# Globalin API 仕様書

## 目次

- [認証 API](#認証-api)
- [ユーザー API](#ユーザー-api)
- [掲示板 API](#掲示板-api)
- [投稿 API](#投稿-api)
- [コメント API](#コメント-api)
- [いいね API](#いいね-api)
- [メッセージ API](#メッセージ-api)
- [チャット API](#チャット-api)
- [通知 API](#通知-api)
- [報告 API](#報告-api)
- [時間割 API](#時間割-api)
- [講義レビュー API](#講義レビュー-api)
- [画像アップロード API](#画像アップロード-api)
- [管理者 API](#管理者-api)
- [ヘルスチェック API](#ヘルスチェック-api)

---

## 認証 API

### 会員登録

```http
POST /api/auth/register
```

**リクエストボディ:**
```json
{
  "username": "user123",
  "password": "password123",
  "email": "user@example.com",
  "nickname": "ユーザー"
}
```

**レスポンス:**
```json
{
  "success": true,
  "message": "会員登録が完了しました。"
}
```

---

### ログイン

```http
POST /api/auth/login
```

**リクエストボディ:**
```json
{
  "username": "user123",
  "password": "password123"
}
```

**レスポンス:**
```json
{
  "success": true,
  "message": "ログインしました。",
  "userId": 1,
  "username": "user123",
  "nickname": "ユーザー"
}
```

---

### ログアウト

```http
POST /api/auth/logout
```

**レスポンス:**
```json
{
  "success": true,
  "message": "ログアウトしました。"
}
```

---

### セッション確認

```http
GET /api/auth/check
```

**レスポンス (ログイン済み):**
```json
{
  "success": true,
  "message": "ログイン済みです。",
  "userId": 1,
  "username": "user123",
  "nickname": "ユーザー"
}
```

**レスポンス (未ログイン):**
```json
{
  "success": false,
  "message": "ログインしていません"
}
```

---

### ユーザー名検索

```http
POST /api/auth/find-username
```

**リクエストボディ:**
```json
{
  "email": "user@example.com"
}
```

**レスポンス:**
```json
{
  "success": true,
  "message": "ユーザー名が見つかりました。",
  "username": "user123"
}
```

---

### 認証コード送信 (パスワードリセット用)

```http
POST /api/auth/send-verification-code
```

**リクエストボディ:**
```json
{
  "email": "user@example.com",
  "username": "user123"
}
```

**レスポンス:**
```json
{
  "success": true,
  "message": "認証コードがメールアドレスに送信されました。（開発環境ではコンソールログを確認してください）"
}
```

---

### パスワードリセット

```http
POST /api/auth/reset-password
```

**リクエストボディ:**
```json
{
  "email": "user@example.com",
  "username": "user123",
  "verificationCode": "123456",
  "newPassword": "newpassword123"
}
```

**レスポンス:**
```json
{
  "success": true,
  "message": "パスワードが正常に変更されました。"
}
```

---

### 会員登録用認証コード送信

```http
POST /api/auth/send-register-verification-code
```

**リクエストボディ:**
```json
{
  "email": "newuser@example.com"
}
```

**レスポンス:**
```json
{
  "success": true,
  "message": "認証コードがメールアドレスに送信されました。（開発環境ではコンソールログを確認してください）"
}
```

---

### 認証コード確認後会員登録

```http
POST /api/auth/register-with-verification
```

**リクエストボディ:**
```json
{
  "email": "newuser@example.com",
  "verificationCode": "123456",
  "username": "newuser",
  "password": "password123",
  "nickname": "新規ユーザー"
}
```

**レスポンス:**
```json
{
  "success": true,
  "message": "会員登録が完了しました。"
}
```

---

## ユーザー API

### ユーザー情報取得

```http
GET /api/users/{userId}
```

**レスポンス:**
```json
{
  "id": 1,
  "username": "user123",
  "nickname": "ユーザー",
  "email": "user@example.com",
  "avatar": "/uploads/avatar1.jpg",
  "bio": "こんにちは",
  "joinDate": "2024-01-01T00:00:00",
  "status": "ACTIVE",
  "role": "USER"
}
```

---

### プロフィール更新

```http
PUT /api/users/profile
```

**リクエストボディ:**
```json
{
  "nickname": "新しいニックネーム",
  "bio": "新しい自己紹介",
  "avatar": "/uploads/newavatar.jpg"
}
```

**レスポンス:**
```json
{
  "success": true,
  "message": "プロフィールが更新されました。"
}
```

---

### パスワード変更

```http
PUT /api/users/password
```

**リクエストボディ:**
```json
{
  "currentPassword": "oldpassword",
  "newPassword": "newpassword123"
}
```

**レスポンス:**
```json
{
  "success": true,
  "message": "パスワードが変更されました。"
}
```

---

### ユーザー検索

```http
GET /api/users/search?keyword={keyword}
```

**パラメータ:**
- `keyword`: 検索キーワード（ユーザー名、ニックネーム）

**レスポンス:**
```json
[
  {
    "id": 1,
    "username": "user123",
    "nickname": "ユーザー",
    "avatar": "/uploads/avatar1.jpg"
  }
]
```

---

## 掲示板 API

### 全掲示板取得

```http
GET /api/boards
```

**レスポンス:**
```json
[
  {
    "id": 1,
    "name": "人文学",
    "description": "人文学に関する掲示板です",
    "category": "学問",
    "icon": "📖",
    "postCount": 234
  }
]
```

---

### 掲示板詳細取得

```http
GET /api/boards/{boardId}
```

**レスポンス:**
```json
{
  "id": 1,
  "name": "人文学",
  "description": "人文学に関する掲示板です",
  "category": "学問",
  "icon": "📖",
  "postCount": 234,
  "createdAt": "2024-01-01T00:00:00"
}
```

---

## 投稿 API

### 投稿作成

```http
POST /api/posts
```

**リクエストボディ:**
```json
{
  "boardId": 1,
  "title": "投稿タイトル",
  "content": "投稿内容",
  "isAnonymous": false,
  "images": [
    {
      "imageUrl": "/uploads/image1.jpg",
      "originalFilename": "photo.jpg",
      "fileSize": 102400,
      "displayOrder": 0
    }
  ]
}
```

**レスポンス:**
```json
{
  "success": true,
  "message": "投稿が作成されました。",
  "postId": 123
}
```

---

### 投稿一覧取得

```http
GET /api/posts?boardId={boardId}&page={page}&size={size}
```

**パラメータ:**
- `boardId`: 掲示板ID（省略可）
- `page`: ページ番号（デフォルト: 1）
- `size`: 1ページあたりの件数（デフォルト: 20）

**レスポンス:**
```json
{
  "posts": [
    {
      "id": 1,
      "boardId": 1,
      "boardName": "人文学",
      "title": "投稿タイトル",
      "content": "投稿内容",
      "author": "ユーザー",
      "authorId": 1,
      "isAnonymous": false,
      "viewCount": 100,
      "likeCount": 15,
      "commentCount": 5,
      "createdAt": "2024-01-01T00:00:00",
      "updatedAt": "2024-01-01T00:00:00",
      "images": []
    }
  ],
  "totalCount": 234,
  "currentPage": 1,
  "totalPages": 12
}
```

---

### 投稿詳細取得

```http
GET /api/posts/{postId}
```

**レスポンス:**
```json
{
  "id": 1,
  "boardId": 1,
  "boardName": "人文学",
  "title": "投稿タイトル",
  "content": "投稿内容",
  "author": "ユーザー",
  "authorId": 1,
  "isAnonymous": false,
  "viewCount": 101,
  "likeCount": 15,
  "commentCount": 5,
  "createdAt": "2024-01-01T00:00:00",
  "updatedAt": "2024-01-01T00:00:00",
  "images": [
    {
      "id": 1,
      "imageUrl": "/uploads/image1.jpg",
      "originalFilename": "photo.jpg",
      "displayOrder": 0
    }
  ]
}
```

---

### 投稿更新

```http
PUT /api/posts/{postId}
```

**リクエストボディ:**
```json
{
  "title": "更新されたタイトル",
  "content": "更新された内容"
}
```

**レスポンス:**
```json
{
  "success": true,
  "message": "投稿が更新されました。"
}
```

---

### 投稿削除

```http
DELETE /api/posts/{postId}
```

**レスポンス:**
```json
{
  "success": true,
  "message": "投稿が削除されました。"
}
```

---

### 投稿検索

```http
GET /api/posts/search?keyword={keyword}&boardId={boardId}
```

**パラメータ:**
- `keyword`: 検索キーワード
- `boardId`: 掲示板ID（省略可）

**レスポンス:**
```json
{
  "posts": [],
  "totalCount": 10
}
```

---

## コメント API

### コメント作成

```http
POST /api/comments
```

**リクエストボディ:**
```json
{
  "postId": 1,
  "content": "コメント内容",
  "isAnonymous": false
}
```

**レスポンス:**
```json
{
  "success": true,
  "message": "コメントが作成されました。",
  "commentId": 456
}
```

---

### コメント一覧取得

```http
GET /api/comments?postId={postId}
```

**パラメータ:**
- `postId`: 投稿ID

**レスポンス:**
```json
[
  {
    "id": 1,
    "postId": 1,
    "content": "コメント内容",
    "author": "ユーザー",
    "authorId": 1,
    "isAnonymous": false,
    "likeCount": 3,
    "createdAt": "2024-01-01T00:00:00",
    "updatedAt": "2024-01-01T00:00:00"
  }
]
```

---

### コメント更新

```http
PUT /api/comments/{commentId}
```

**リクエストボディ:**
```json
{
  "content": "更新されたコメント"
}
```

**レスポンス:**
```json
{
  "success": true,
  "message": "コメントが更新されました。"
}
```

---

### コメント削除

```http
DELETE /api/comments/{commentId}
```

**レスポンス:**
```json
{
  "success": true,
  "message": "コメントが削除されました。"
}
```

---

## いいね API

### 投稿にいいね

```http
POST /api/posts/{postId}/like
```

**レスポンス:**
```json
{
  "success": true,
  "message": "いいねしました。",
  "likeCount": 16
}
```

---

### 投稿のいいね解除

```http
DELETE /api/posts/{postId}/like
```

**レスポンス:**
```json
{
  "success": true,
  "message": "いいねを解除しました。",
  "likeCount": 15
}
```

---

### コメントにいいね

```http
POST /api/comments/{commentId}/like
```

**レスポンス:**
```json
{
  "success": true,
  "message": "いいねしました。",
  "likeCount": 4
}
```

---

### コメントのいいね解除

```http
DELETE /api/comments/{commentId}/like
```

**レスポンス:**
```json
{
  "success": true,
  "message": "いいねを解除しました。",
  "likeCount": 3
}
```

---

## メッセージ API

### メッセージ送信

```http
POST /api/messages
```

**リクエストボディ:**
```json
{
  "receiverId": 2,
  "title": "メッセージタイトル",
  "content": "メッセージ内容"
}
```

**レスポンス:**
```json
{
  "success": true,
  "message": "メッセージが送信されました。"
}
```

---

### 受信メッセージ一覧

```http
GET /api/messages/received?offset={offset}&limit={limit}
```

**パラメータ:**
- `offset`: オフセット（デフォルト: 0）
- `limit`: 取得件数（デフォルト: 20）

**レスポンス:**
```json
{
  "success": true,
  "messages": [
    {
      "id": 1,
      "senderId": 2,
      "senderNickname": "送信者",
      "title": "メッセージタイトル",
      "content": "メッセージ内容",
      "isRead": false,
      "createdAt": "2024-01-01T00:00:00"
    }
  ],
  "total": 10
}
```

---

### 送信メッセージ一覧

```http
GET /api/messages/sent?offset={offset}&limit={limit}
```

**レスポンス:**
```json
{
  "success": true,
  "messages": [],
  "total": 5
}
```

---

### メッセージ詳細取得

```http
GET /api/messages/{messageId}
```

**レスポンス:**
```json
{
  "success": true,
  "message": {
    "id": 1,
    "senderId": 2,
    "senderNickname": "送信者",
    "receiverId": 1,
    "receiverNickname": "受信者",
    "title": "メッセージタイトル",
    "content": "メッセージ内容",
    "isRead": true,
    "readAt": "2024-01-01T01:00:00",
    "createdAt": "2024-01-01T00:00:00"
  }
}
```

---

### 未読メッセージ数

```http
GET /api/messages/unread/count
```

**レスポンス:**
```json
{
  "success": true,
  "count": 3
}
```

---

### メッセージ削除

```http
DELETE /api/messages/{messageId}
```

**レスポンス:**
```json
{
  "success": true,
  "message": "メッセージが削除されました。"
}
```

---

## チャット API

### チャットメッセージ送信

```http
POST /api/chat/send
```

**リクエストボディ:**
```json
{
  "receiverId": 2,
  "message": "こんにちは！"
}
```

**レスポンス:**
```json
{
  "success": true,
  "message": "メッセージが送信されました。"
}
```

---

### チャット履歴取得

```http
GET /api/chat/history?partnerId={partnerId}&limit={limit}
```

**パラメータ:**
- `partnerId`: チャット相手のユーザーID
- `limit`: 取得件数（デフォルト: 50）

**レスポンス:**
```json
{
  "success": true,
  "messages": [
    {
      "id": 1,
      "senderId": 1,
      "receiverId": 2,
      "message": "こんにちは！",
      "isRead": true,
      "createdAt": "2024-01-01T00:00:00"
    }
  ]
}
```

---

### チャットルーム一覧

```http
GET /api/chat/rooms
```

**レスポンス:**
```json
{
  "success": true,
  "rooms": [
    {
      "partnerId": 2,
      "partnerNickname": "チャット相手",
      "partnerAvatar": "/uploads/avatar2.jpg",
      "lastMessage": "こんにちは！",
      "lastMessageTime": "2024-01-01T00:00:00",
      "unreadCount": 2
    }
  ]
}
```

---

### 未読チャット数

```http
GET /api/chat/unread/count
```

**レスポンス:**
```json
{
  "success": true,
  "count": 5
}
```

---

## 通知 API

### 通知一覧取得

```http
GET /api/notifications?limit={limit}&offset={offset}
```

**パラメータ:**
- `limit`: 取得件数（デフォルト: 20）
- `offset`: オフセット（デフォルト: 0）

**レスポンス:**
```json
{
  "success": true,
  "notifications": [
    {
      "id": 1,
      "type": "COMMENT",
      "content": "あなたの投稿に新しいコメントがあります",
      "relatedId": 123,
      "isRead": false,
      "createdAt": "2024-01-01T00:00:00"
    }
  ],
  "total": 15
}
```

---

### 通知を既読にする

```http
PUT /api/notifications/{notificationId}/read
```

**レスポンス:**
```json
{
  "success": true,
  "message": "通知を既読にしました。"
}
```

---

### 全通知を既読にする

```http
PUT /api/notifications/read-all
```

**レスポンス:**
```json
{
  "success": true,
  "message": "すべての通知を既読にしました。"
}
```

---

### 未読通知数

```http
GET /api/notifications/unread/count
```

**レスポンス:**
```json
{
  "success": true,
  "count": 8
}
```

---

### 通知削除

```http
DELETE /api/notifications/{notificationId}
```

**レスポンス:**
```json
{
  "success": true,
  "message": "通知が削除されました。"
}
```

---

## 報告 API

### 投稿を報告

```http
POST /api/reports/post/{postId}
```

**リクエストボディ:**
```json
{
  "reason": "スパム投稿です"
}
```

**レスポンス:**
```json
{
  "success": true,
  "message": "報告が受理されました。"
}
```

---

### ユーザーを報告

```http
POST /api/reports/user/{userId}
```

**リクエストボディ:**
```json
{
  "reason": "不適切な行動"
}
```

**レスポンス:**
```json
{
  "success": true,
  "message": "報告が受理されました。"
}
```

---

### 全報告取得（管理者用）

```http
GET /api/reports
```

**レスポンス:**
```json
[
  {
    "id": 1,
    "reporterId": 1,
    "reporterNickname": "報告者",
    "postId": 123,
    "postTitle": "投稿タイトル",
    "userId": null,
    "reportedUserNickname": null,
    "reason": "スパム投稿です",
    "status": "pending",
    "createdAt": "2024-01-01T00:00:00",
    "resolvedAt": null,
    "adminNote": null
  }
]
```

---

### 保留中の報告取得（管理者用）

```http
GET /api/reports/pending
```

**レスポンス:**
```json
[
  {
    "id": 1,
    "reporterId": 1,
    "reason": "スパム投稿です",
    "status": "pending",
    "createdAt": "2024-01-01T00:00:00"
  }
]
```

---

### 保留中の報告数（管理者用）

```http
GET /api/reports/pending/count
```

**レスポンス:**
```json
5
```

---

### 報告を承認（管理者用）

```http
PUT /api/reports/{reportId}/resolve
```

**リクエストボディ:**
```json
{
  "adminNote": "適切に処理しました"
}
```

**レスポンス:**
```json
{
  "success": true,
  "message": "報告が処理されました。"
}
```

---

### 報告を却下（管理者用）

```http
PUT /api/reports/{reportId}/reject
```

**リクエストボディ:**
```json
{
  "adminNote": "問題ないと判断しました"
}
```

**レスポンス:**
```json
{
  "success": true,
  "message": "報告が却下されました。"
}
```

---

### 報告削除（管理者用）

```http
DELETE /api/reports/{reportId}
```

**レスポンス:**
```json
{
  "success": true,
  "message": "報告が削除されました。"
}
```

---

## 時間割 API

### 時間割作成

```http
POST /api/timetables
```

**リクエストボディ:**
```json
{
  "semester": "2024年春学期",
  "year": 2024,
  "isDefault": true
}
```

**レスポンス:**
```json
{
  "id": 1,
  "userId": 1,
  "semester": "2024年春学期",
  "year": 2024,
  "isDefault": true,
  "createdAt": "2024-01-01T00:00:00"
}
```

---

### ユーザーの全時間割取得

```http
GET /api/timetables
```

**レスポンス:**
```json
[
  {
    "id": 1,
    "semester": "2024年春学期",
    "year": 2024,
    "isDefault": true,
    "createdAt": "2024-01-01T00:00:00"
  }
]
```

---

### デフォルト時間割取得

```http
GET /api/timetables/default
```

**レスポンス:**
```json
{
  "id": 1,
  "semester": "2024年春学期",
  "year": 2024,
  "isDefault": true,
  "courses": []
}
```

---

### 時間割更新

```http
PUT /api/timetables/{timetableId}
```

**リクエストボディ:**
```json
{
  "semester": "2024年秋学期",
  "isDefault": false
}
```

**レスポンス:**
```json
{
  "success": true,
  "message": "時間割が更新されました。"
}
```

---

### 時間割削除

```http
DELETE /api/timetables/{timetableId}
```

**レスポンス:**
```json
{
  "success": true,
  "message": "時間割が削除されました。"
}
```

---

### 科目追加

```http
POST /api/timetables/{timetableId}/courses
```

**リクエストボディ:**
```json
{
  "courseName": "日本文学概論",
  "professor": "山田太郎",
  "location": "A棟301",
  "dayOfWeek": 1,
  "startTime": "09:00",
  "endTime": "10:30",
  "color": "#937EBF",
  "credits": 2,
  "memo": "必修科目"
}
```

**レスポンス:**
```json
{
  "id": 1,
  "timetableId": 1,
  "courseName": "日本文学概論",
  "professor": "山田太郎",
  "location": "A棟301",
  "dayOfWeek": 1,
  "startTime": "09:00",
  "endTime": "10:30",
  "color": "#937EBF",
  "credits": 2,
  "memo": "必修科目"
}
```

---

### 時間割の全科目取得

```http
GET /api/timetables/{timetableId}/courses
```

**レスポンス:**
```json
[
  {
    "id": 1,
    "courseName": "日本文学概論",
    "professor": "山田太郎",
    "location": "A棟301",
    "dayOfWeek": 1,
    "startTime": "09:00",
    "endTime": "10:30",
    "color": "#937EBF",
    "credits": 2
  }
]
```

---

### 科目更新

```http
PUT /api/timetables/courses/{courseId}
```

**リクエストボディ:**
```json
{
  "location": "B棟201",
  "memo": "教室変更"
}
```

**レスポンス:**
```json
{
  "success": true,
  "message": "科目が更新されました。"
}
```

---

### 科目削除

```http
DELETE /api/timetables/courses/{courseId}
```

**レスポンス:**
```json
{
  "success": true,
  "message": "科目が削除されました。"
}
```

---

## 講義レビュー API

### レビュー作成

```http
POST /api/lecture-reviews
```

**リクエストボディ:**
```json
{
  "courseName": "日本文学概論",
  "professor": "山田太郎",
  "semester": "2024年春学期",
  "year": 2024,
  "rating": 4.5,
  "difficulty": 3,
  "workload": 2,
  "content": "とても良い講義でした。",
  "isAnonymous": false
}
```

**レスポンス:**
```json
{
  "success": true,
  "message": "レビューが作成されました。",
  "review": {
    "id": 1,
    "courseName": "日本文学概論",
    "professor": "山田太郎",
    "rating": 4.5,
    "content": "とても良い講義でした。"
  }
}
```

---

### レビュー取得

```http
GET /api/lecture-reviews/{reviewId}
```

**レスポンス:**
```json
{
  "id": 1,
  "userId": 1,
  "author": "ユーザー",
  "courseName": "日本文学概論",
  "professor": "山田太郎",
  "semester": "2024年春学期",
  "year": 2024,
  "rating": 4.5,
  "difficulty": 3,
  "workload": 2,
  "content": "とても良い講義でした。",
  "isAnonymous": false,
  "likeCount": 5,
  "createdAt": "2024-01-01T00:00:00"
}
```

---

### レビュー更新

```http
PUT /api/lecture-reviews/{reviewId}
```

**リクエストボディ:**
```json
{
  "rating": 5.0,
  "content": "更新された内容"
}
```

**レスポンス:**
```json
{
  "success": true,
  "message": "レビューが更新されました。"
}
```

---

### レビュー削除

```http
DELETE /api/lecture-reviews/{reviewId}
```

**レスポンス:**
```json
{
  "success": true,
  "message": "レビューが削除されました。"
}
```

---

### レビュー検索

```http
GET /api/lecture-reviews/search?keyword={keyword}
```

**パラメータ:**
- `keyword`: 検索キーワード（講義名または教授名）

**レスポンス:**
```json
[
  {
    "id": 1,
    "courseName": "日本文学概論",
    "professor": "山田太郎",
    "rating": 4.5,
    "author": "ユーザー",
    "createdAt": "2024-01-01T00:00:00"
  }
]
```

---

### 講義名+教授名でレビュー取得

```http
GET /api/lecture-reviews/course?courseName={courseName}&professor={professor}
```

**パラメータ:**
- `courseName`: 講義名
- `professor`: 教授名

**レスポンス:**
```json
{
  "courseName": "日本文学概論",
  "professor": "山田太郎",
  "averageRating": 4.5,
  "totalReviews": 10,
  "reviews": []
}
```

---

### 自分が書いたレビュー一覧

```http
GET /api/lecture-reviews/my-reviews
```

**レスポンス:**
```json
[
  {
    "id": 1,
    "courseName": "日本文学概論",
    "professor": "山田太郎",
    "rating": 4.5,
    "createdAt": "2024-01-01T00:00:00"
  }
]
```

---

### 全レビュー取得（ページング）

```http
GET /api/lecture-reviews?limit={limit}&offset={offset}
```

**パラメータ:**
- `limit`: 取得件数（デフォルト: 20）
- `offset`: オフセット（デフォルト: 0）

**レスポンス:**
```json
{
  "reviews": [],
  "totalCount": 100
}
```

---

## 画像アップロード API

### 画像アップロード

```http
POST /api/images/upload
```

**リクエスト:**
- Content-Type: `multipart/form-data`
- Body: `file` (画像ファイル)

**レスポンス:**
```json
{
  "success": true,
  "imageUrl": "/uploads/20240101_123456_image.jpg",
  "originalFilename": "photo.jpg",
  "fileSize": 102400
}
```

---

## 管理者 API

### 全ユーザー取得

```http
GET /api/admin/users
```

**レスポンス:**
```json
[
  {
    "id": 1,
    "username": "user123",
    "nickname": "ユーザー",
    "email": "user@example.com",
    "status": "ACTIVE",
    "role": "USER",
    "joinDate": "2024-01-01T00:00:00"
  }
]
```

---

### ユーザー停止

```http
PUT /api/admin/users/{userId}/ban
```

**レスポンス:**
```json
{
  "success": true,
  "message": "ユーザーが停止されました。"
}
```

---

### ユーザー停止解除

```http
PUT /api/admin/users/{userId}/unban
```

**レスポンス:**
```json
{
  "success": true,
  "message": "ユーザーの停止が解除されました。"
}
```

---

### 投稿削除（管理者）

```http
DELETE /api/admin/posts/{postId}
```

**レスポンス:**
```json
{
  "success": true,
  "message": "投稿が削除されました。"
}
```

---

### サイト統計取得

```http
GET /api/admin/stats
```

**レスポンス:**
```json
{
  "totalUsers": 1000,
  "totalPosts": 5000,
  "totalComments": 15000,
  "activeUsers": 500,
  "pendingReports": 5
}
```

---

## ヘルスチェック API

### ヘルスチェック

```http
GET /api/health
```

**レスポンス:**
```json
{
  "status": "OK",
  "message": "Globalin API is running",
  "timestamp": 1704067200000
}
```

---

### メインページダッシュボード

```http
GET /api/main/dashboard
```

**レスポンス:**
```json
{
  "userProfile": {
    "nickname": "ユーザー",
    "postCount": 15,
    "commentCount": 42
  },
  "humanitiesPosts": [],
  "freePosts": [],
  "hotPosts": [],
  "bestBoards": [],
  "allBoards": []
}
```

---

## エラーレスポンス

全APIで共通のエラーレスポンス形式：

```json
{
  "success": false,
  "message": "エラーメッセージ"
}
```

### HTTPステータスコード

- `200 OK`: 成功
- `201 Created`: リソース作成成功
- `400 Bad Request`: リクエストエラー
- `401 Unauthorized`: 未認証
- `403 Forbidden`: 権限なし
- `404 Not Found`: リソースが見つからない
- `500 Internal Server Error`: サーバーエラー

---

## 認証について

セッションベースの認証を使用しています。

- ログイン後、セッションにユーザー情報が保存されます
- セッションタイムアウト: 30分
- CORS設定: `http://localhost:3000` からのアクセスを許可

---

## レート制限

現在、レート制限は実装されていません。

---

## バージョン

API Version: 1.0

最終更新日: 2024-01-01
