# Globalin - 日本大学留学生コミュニティ

日本の大学に在学中の韓国人留学生のためのコミュニティプラットフォームです。

## 📋 目次

- [プロジェクト概要](#プロジェクト概要)
- [技術スタック](#技術スタック)
- [プロジェクト構造](#プロジェクト構造)
- [ファイル別機能担当](#ファイル別機能担当)
- [始め方](#始め方)
- [API ドキュメント](#api-ドキュメント)
- [詳細API仕様](#詳細api仕様)
- [配置](#配置)

---

## 🎯 プロジェクト概要

### 主な機能

- 👤 ユーザープロフィール管理
- 📝 掲示板システム（人文学、自由掲示板など）
- 🔥 HOT投稿おすすめ
- ⭐ BEST掲示板おすすめ
- 🔍 リアルタイム検索

### デザインコンセプト

**ブランドカラー:**
- 🌿 **ミントグリーン** (#C6E5D1) - ヘッダー背景
- 💜 **パープル** (#937EBF, #433461) - 主要テキストおよび強調
- 🧡 **オレンジ** (#FE9F1A) - アクセントおよびホバー効果

---

## 🛠 技術スタック

### バックエンド
```
言語:         Java 8 (OpenJDK 1.8.0)
フレームワーク: Spring Framework 5.3.31
ビルドツール:   Maven 3.9.11
ORM:          MyBatis 3.5.13
Webサーバー:   Apache Tomcat 7.0.47
データベース:   MariaDB 10.x
JDBC Driver:  mariadb-java-client 2.7.9
```

### フロントエンド
```
言語:          TypeScript 4.9
フレームワーク:  React 18.3.1
スタイリング:    SCSS
ビルドツール:    React Scripts 5.0.1
HTTPクライアント: Fetch API
```

### インフラ & デプロイ
```
コンテナ:      Docker & Docker Compose
Webサーバー:   Nginx (プロダクション)
プロキシ:      Nginx Reverse Proxy
ロギング:      Logback 1.2.11
```

---

## 📁 プロジェクト構造

```
Globalin/
├── 📦 backend/                          # バックエンド Spring アプリケーション
│   ├── src/main/
│   │   ├── java/com/example/Globalin/
│   │   │   ├── 🎮 controller/          # REST API コントローラー
│   │   │   │   ├── HealthCheckController.java      # ヘルスチェック API
│   │   │   │   └── MainPageController.java         # メインページ統合 API
│   │   │   │
│   │   │   ├── 💼 service/             # ビジネスロジック
│   │   │   │   ├── BoardService.java               # 掲示板/投稿サービス
│   │   │   │   └── UserService.java                # ユーザーサービス
│   │   │   │
│   │   │   ├── 📊 model/               # ドメインモデル (Entity)
│   │   │   │   ├── Board.java                      # 掲示板エンティティ
│   │   │   │   ├── Post.java                       # 投稿エンティティ
│   │   │   │   ├── HotPost.java                    # HOT投稿エンティティ
│   │   │   │   └── UserProfile.java                # ユーザープロフィールエンティティ
│   │   │   │
│   │   │   └── 📦 dto/                 # データ転送オブジェクト
│   │   │       └── MainPageDTO.java                # メインページ統合 DTO
│   │   │
│   │   ├── resources/
│   │   │   ├── ⚙️  spring/             # Spring 設定
│   │   │   │   ├── applicationContext.xml          # ルートコンテキスト (Bean, DB)
│   │   │   │   └── dispatcher-servlet.xml          # MVC 設定 (Controller, CORS)
│   │   │   │
│   │   │   ├── 🗄️  mybatis/            # MyBatis 設定
│   │   │   │   ├── mybatis-config.xml              # MyBatis グローバル設定
│   │   │   │   └── mappers/                        # SQL Mapper XML
│   │   │   │
│   │   │   └── 🔧 config/              # アプリケーション設定
│   │   │       └── database.properties             # MariaDB 接続設定
│   │   │
│   │   └── webapp/WEB-INF/
│   │       └── web.xml                             # Webアプリケーション設定
│   │
│   ├── 🐳 Dockerfile                    # バックエンド Docker イメージ
│   ├── .dockerignore
│   └── 📦 pom.xml                       # Maven 依存関係管理
│
├── 🌐 frontend/                         # フロントエンド React アプリケーション
│   ├── public/
│   │   ├── index.html
│   │   ├── favicon.ico
│   │   └── manifest.json
│   │
│   ├── src/
│   │   ├── 🧩 components/
│   │   │   ├── common/                             # 共通コンポーネント
│   │   │   │   ├── Header.tsx                      # サイトヘッダー
│   │   │   │   └── Header.scss                     # ヘッダースタイル
│   │   │   │
│   │   │   └── pages/                              # ページコンポーネント
│   │   │       ├── MainPage.tsx                    # メインページコンテナ
│   │   │       ├── MainPage.scss
│   │   │       └── components/                     # メインページサブコンポーネント
│   │   │           ├── LeftSidebar.tsx             # ユーザープロフィールエリア
│   │   │           ├── LeftSidebar.scss
│   │   │           ├── MainContent.tsx             # 投稿リストエリア
│   │   │           ├── MainContent.scss
│   │   │           ├── RightSidebar.tsx            # HOT/BESTエリア
│   │   │           └── RightSidebar.scss
│   │   │
│   │   ├── 📝 types/                               # TypeScript 型定義
│   │   │   └── index.ts
│   │   │
│   │   ├── App.tsx                                 # ルートコンポーネント
│   │   ├── App.css
│   │   ├── index.tsx                               # エントリーポイント
│   │   └── index.css
│   │
│   ├── 🌍 .env.development              # 開発環境変数
│   ├── 🌍 .env.production               # プロダクション環境変数
│   ├── 🐳 Dockerfile                    # フロントエンド Docker イメージ
│   ├── 🔧 nginx.conf                    # Nginx 設定
│   ├── .dockerignore
│   ├── 📦 package.json                  # npm 依存関係
│   └── tsconfig.json                    # TypeScript 設定
│
├── 🐳 docker-compose.yml                # Docker Compose オーケストレーション
├── 🚀 deploy.sh                         # 自動デプロイスクリプト
├── .gitignore
├── 📖 README.md                         # このファイル
└── 📖 README-DOCKER.md                  # Docker デプロイガイド
```

---

## 🎯 ファイル別機能担当

### バックエンドコンポーネント

#### 🎮 Controllers (コントローラー)
| ファイル | エンドポイント | 機能 |
|------|------|------|
| `HealthCheckController.java` | `GET /api/health` | サーバー状態チェック (ヘルスチェック) |
| `MainPageController.java` | `GET /api/main/dashboard` | メインページ全体データ統合提供 |

#### 💼 Services (サービス - ビジネスロジック)
| ファイル | 担当機能 |
|------|------|
| `BoardService.java` | • 掲示板管理<br>• 最新投稿取得<br>• HOT投稿選定<br>• BEST掲示板選定 |
| `UserService.java` | • ユーザープロフィール取得<br>• ユーザー統計 (投稿/コメント数) |

#### 📊 Models (ドメインモデル)
| ファイル | 担当データ |
|------|------|
| `Board.java` | 掲示板情報 (ID, 名前, 説明, カテゴリ, アイコン) |
| `Post.java` | 投稿情報 (タイトル, 内容, 作成者, 閲覧数, いいね, コメント数) |
| `HotPost.java` | HOT投稿 (人気投稿要約情報) |
| `UserProfile.java` | ユーザープロフィール (ニックネーム, メール, 統計, 登録日) |

#### 📦 DTOs (データ転送オブジェクト)
| ファイル | 担当データ |
|------|------|
| `MainPageDTO.java` | メインページ全体データ統合<br>(ユーザープロフィール + 投稿 + HOT + BEST) |

#### ⚙️ 設定ファイル
| ファイル | 役割 |
|------|------|
| `web.xml` | • DispatcherServlet マッピング (`/*`)<br>• エンコーディングフィルター (UTF-8)<br>• セッションタイムアウト |
| `applicationContext.xml` | • Bean スキャン設定<br>• DataSource (MariaDB 接続)<br>• MyBatis 連携<br>• トランザクション管理 |
| `dispatcher-servlet.xml` | • Controller スキャン<br>• MVC 設定<br>• JSON 変換 (Jackson)<br>• CORS 設定 (localhost:3000) |
| `mybatis-config.xml` | • TypeAlias 設定<br>• Mapper 位置指定 |
| `database.properties` | • MariaDB 接続情報<br>• Connection Pool 設定 |
| `pom.xml` | • Maven 依存関係管理<br>• ビルドプラグイン設定 |

---

### フロントエンドコンポーネント

#### 🧩 共通コンポーネント
| ファイル | 役割 |
|------|------|
| `Header.tsx` | • サイトロゴ表示<br>• サイトタイトル (Globalin)<br>• 検索バー<br>• 上部固定 (sticky) |
| `Header.scss` | • ミントグリーングラデーション背景<br>• パープルテキストスタイル<br>• レスポンシブレイアウト |

#### 📄 ページコンポーネント
| ファイル | 役割 |
|------|------|
| `MainPage.tsx` | • メインページコンテナ<br>• API 呼び出し (`/api/main/dashboard`)<br>• ローディング/エラー状態管理<br>• サブコンポーネント組み合わせ |
| `MainPage.scss` | • 3カラムレイアウトスタイル<br>• ローディング/エラーメッセージスタイル<br>• レスポンシブメディアクエリ |

#### 🧩 メインページサブコンポーネント
| ファイル | 担当エリア |
|------|------|
| `LeftSidebar.tsx` | • ユーザープロフィールカード<br>• アバター画像<br>• 投稿/コメント統計<br>• 登録日表示 |
| `LeftSidebar.scss` | • パープルグラデーションアバター<br>• ミントグリーンボーダー<br>• パープル統計数字 |
| `MainContent.tsx` | • 人文学掲示板最新投稿<br>• 自由掲示板最新投稿<br>• 投稿カード表示 |
| `MainContent.scss` | • パープルセクションボーダー<br>• オレンジホバー効果<br>• カードレイアウトスタイル |
| `RightSidebar.tsx` | • HOT投稿リスト<br>• BEST掲示板リスト<br>• 掲示板アイコン表示 |
| `RightSidebar.scss` | • オレンジホバー効果 (HOT)<br>• ミントグリーンホバー (BEST)<br>• オレンジ統計テキスト |

#### 📝 型定義
| ファイル | 定義型 |
|------|------|
| `types/index.ts` | • UserProfile<br>• Post<br>• HotPost<br>• Board |

#### 🔧 設定ファイル
| ファイル | 役割 |
|------|------|
| `package.json` | • npm 依存関係リスト<br>• ビルド/実行スクリプト |
| `tsconfig.json` | • TypeScript コンパイルオプション<br>• パスエイリアス設定 |
| `.env.development` | • 開発環境 API URL<br>  (http://localhost:8080) |
| `.env.production` | • プロダクション環境 API URL<br>  (nginx プロキシ使用) |
| `nginx.conf` | • 静的ファイルサーブ<br>• API プロキシ (/api/* → backend:8080)<br>• Gzip 圧縮<br>• セキュリティヘッダー |

---

### 🐳 デプロイ関連ファイル
| ファイル | 役割 |
|------|------|
| `docker-compose.yml` | • バックエンド/フロントエンドコンテナ定義<br>• ネットワーク設定<br>• ヘルスチェック設定<br>• 環境変数管理 |
| `backend/Dockerfile` | • マルチステージビルド<br>  (Maven ビルド → Tomcat ランタイム)<br>• WAR ファイルデプロイ |
| `frontend/Dockerfile` | • マルチステージビルド<br>  (Node ビルド → Nginx ランタイム)<br>• 静的ファイル最適化 |
| `deploy.sh` | • 環境チェック<br>• ポート競合確認<br>• Docker ビルド/実行<br>• ヘルスチェック |

---

## 🚀 始め方

### 事前要件

#### ローカル開発
- **Java**: OpenJDK 8 以上
- **Maven**: 3.9 以上
- **Node.js**: 18 以上
- **npm**: 8 以上
- **MariaDB**: 10.x 以上

#### Docker デプロイ
- **Docker**: 20.10 以上
- **Docker Compose**: 2.0 以上

---

### ローカル開発環境設定

#### 1️⃣ MariaDB 設定

```bash
# MariaDB インストール (macOS)
brew install mariadb

# MariaDB 起動
brew services start mariadb

# データベース作成
mysql -u root -p
CREATE DATABASE globalin CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
EXIT;
```

#### 2️⃣ バックエンド実行

```bash
cd backend

# データベース設定確認
cat src/main/resources/config/database.properties

# Maven 依存関係インストール及びサーバー実行
./mvnw tomcat7:run

# サーバー実行確認 (別のターミナルで)
curl http://localhost:8080/api/health
```

#### 3️⃣ フロントエンド実行

```bash
cd frontend

# npm 依存関係インストール
npm install

# 開発サーバー実行
npm start
```

#### 4️⃣ ブラウザ接続

- **フロントエンド**: http://localhost:3000
- **バックエンド API**: http://localhost:8080/api/health

---

## 📡 API ドキュメント

完全なAPI仕様書は **[API.md](./API.md)** を参照してください。

### 主要エンドポイント概要

#### 認証関連
- `POST /api/auth/register` - 会員登録
- `POST /api/auth/login` - ログイン
- `POST /api/auth/logout` - ログアウト
- `GET /api/auth/check` - セッション確認
- `POST /api/auth/reset-password` - パスワードリセット

#### ユーザー関連
- `GET /api/users/{userId}` - ユーザー情報取得
- `PUT /api/users/profile` - プロフィール更新
- `GET /api/users/search` - ユーザー検索

#### 掲示板・投稿関連
- `GET /api/boards` - 全掲示板取得
- `GET /api/posts` - 投稿一覧取得
- `POST /api/posts` - 投稿作成
- `GET /api/posts/{postId}` - 投稿詳細取得
- `PUT /api/posts/{postId}` - 投稿更新
- `DELETE /api/posts/{postId}` - 投稿削除

#### コメント関連
- `GET /api/comments?postId={postId}` - コメント一覧取得
- `POST /api/comments` - コメント作成
- `PUT /api/comments/{commentId}` - コメント更新
- `DELETE /api/comments/{commentId}` - コメント削除

#### いいね関連
- `POST /api/posts/{postId}/like` - 投稿にいいね
- `DELETE /api/posts/{postId}/like` - いいね解除
- `POST /api/comments/{commentId}/like` - コメントにいいね

#### メッセージ・チャット関連
- `POST /api/messages` - メッセージ送信
- `GET /api/messages/received` - 受信メッセージ一覧
- `GET /api/chat/history` - チャット履歴取得
- `GET /api/chat/rooms` - チャットルーム一覧

#### 通知関連
- `GET /api/notifications` - 通知一覧取得
- `PUT /api/notifications/{notificationId}/read` - 通知を既読にする
- `GET /api/notifications/unread/count` - 未読通知数

#### 報告関連
- `POST /api/reports/post/{postId}` - 投稿を報告
- `POST /api/reports/user/{userId}` - ユーザーを報告
- `GET /api/reports/pending` - 保留中の報告取得（管理者用）

#### 時間割関連
- `POST /api/timetables` - 時間割作成
- `GET /api/timetables` - 全時間割取得
- `POST /api/timetables/{timetableId}/courses` - 科目追加
- `GET /api/timetables/{timetableId}/courses` - 科目一覧取得

#### 講義レビュー関連
- `POST /api/lecture-reviews` - レビュー作成
- `GET /api/lecture-reviews/search` - レビュー検索
- `GET /api/lecture-reviews/course` - 講義名+教授名でレビュー取得

#### 画像・その他
- `POST /api/images/upload` - 画像アップロード
- `GET /api/health` - ヘルスチェック
- `GET /api/main/dashboard` - メインページダッシュボード

### 認証方式

セッションベースの認証を使用しています。

- ログイン後、セッションにユーザー情報が保存されます
- セッションタイムアウト: 30分
- CORS設定: `http://localhost:3000` からのアクセスを許可

### エラーレスポンス

```json
{
  "success": false,
  "message": "エラーメッセージ"
}
```

詳細なAPI仕様、リクエスト/レスポンス例は **[API.md](./API.md)** を参照してください。

---

## 📖 詳細API仕様

### 認証 API

#### 会員登録
```http
POST /api/auth/register
```
**リクエスト:**
```json
{
  "username": "user123",
  "password": "password123",
  "email": "user@example.com",
  "nickname": "ユーザー"
}
```

#### ログイン
```http
POST /api/auth/login
```
**リクエスト:**
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
  "userId": 1,
  "username": "user123",
  "nickname": "ユーザー"
}
```

#### セッション確認
```http
GET /api/auth/check
```

#### パスワードリセット
```http
POST /api/auth/reset-password
```
**リクエスト:**
```json
{
  "email": "user@example.com",
  "username": "user123",
  "verificationCode": "123456",
  "newPassword": "newpassword123"
}
```

---

### 投稿 API

#### 投稿作成
```http
POST /api/posts
```
**リクエスト:**
```json
{
  "boardId": 1,
  "title": "投稿タイトル",
  "content": "投稿内容",
  "isAnonymous": false,
  "images": []
}
```

#### 投稿一覧取得
```http
GET /api/posts?boardId={boardId}&page={page}&size={size}
```
**パラメータ:**
- `boardId`: 掲示板ID（省略可）
- `page`: ページ番号（デフォルト: 1）
- `size`: 件数（デフォルト: 20）

#### 投稿詳細取得
```http
GET /api/posts/{postId}
```

#### 投稿更新
```http
PUT /api/posts/{postId}
```

#### 投稿削除
```http
DELETE /api/posts/{postId}
```

#### 投稿検索
```http
GET /api/posts/search?keyword={keyword}
```

---

### コメント API

#### コメント作成
```http
POST /api/comments
```
**リクエスト:**
```json
{
  "postId": 1,
  "content": "コメント内容",
  "isAnonymous": false
}
```

#### コメント一覧取得
```http
GET /api/comments?postId={postId}
```

#### コメント更新
```http
PUT /api/comments/{commentId}
```

#### コメント削除
```http
DELETE /api/comments/{commentId}
```

---

### いいね API

#### 投稿にいいね
```http
POST /api/posts/{postId}/like
```

#### 投稿のいいね解除
```http
DELETE /api/posts/{postId}/like
```

#### コメントにいいね
```http
POST /api/comments/{commentId}/like
```

#### コメントのいいね解除
```http
DELETE /api/comments/{commentId}/like
```

---

### メッセージ API

#### メッセージ送信
```http
POST /api/messages
```
**リクエスト:**
```json
{
  "receiverId": 2,
  "title": "メッセージタイトル",
  "content": "メッセージ内容"
}
```

#### 受信メッセージ一覧
```http
GET /api/messages/received?offset={offset}&limit={limit}
```

#### 送信メッセージ一覧
```http
GET /api/messages/sent?offset={offset}&limit={limit}
```

#### メッセージ詳細取得
```http
GET /api/messages/{messageId}
```

#### 未読メッセージ数
```http
GET /api/messages/unread/count
```

#### メッセージ削除
```http
DELETE /api/messages/{messageId}
```

---

### チャット API

#### チャットメッセージ送信
```http
POST /api/chat/send
```
**リクエスト:**
```json
{
  "receiverId": 2,
  "message": "こんにちは！"
}
```

#### チャット履歴取得
```http
GET /api/chat/history?partnerId={partnerId}&limit={limit}
```

#### チャットルーム一覧
```http
GET /api/chat/rooms
```

#### 未読チャット数
```http
GET /api/chat/unread/count
```

---

### 通知 API

#### 通知一覧取得
```http
GET /api/notifications?limit={limit}&offset={offset}
```

#### 通知を既読にする
```http
PUT /api/notifications/{notificationId}/read
```

#### 全通知を既読にする
```http
PUT /api/notifications/read-all
```

#### 未読通知数
```http
GET /api/notifications/unread/count
```

#### 通知削除
```http
DELETE /api/notifications/{notificationId}
```

---

### 報告 API

#### 投稿を報告
```http
POST /api/reports/post/{postId}
```
**リクエスト:**
```json
{
  "reason": "スパム投稿です"
}
```

#### ユーザーを報告
```http
POST /api/reports/user/{userId}
```

#### 保留中の報告取得（管理者用）
```http
GET /api/reports/pending
```

#### 報告を承認（管理者用）
```http
PUT /api/reports/{reportId}/resolve
```

#### 報告を却下（管理者用）
```http
PUT /api/reports/{reportId}/reject
```

---

### 時間割 API

#### 時間割作成
```http
POST /api/timetables
```
**リクエスト:**
```json
{
  "semester": "2024年春学期",
  "year": 2024,
  "isDefault": true
}
```

#### ユーザーの全時間割取得
```http
GET /api/timetables
```

#### デフォルト時間割取得
```http
GET /api/timetables/default
```

#### 科目追加
```http
POST /api/timetables/{timetableId}/courses
```
**リクエスト:**
```json
{
  "courseName": "日本文学概論",
  "professor": "山田太郎",
  "location": "A棟301",
  "dayOfWeek": 1,
  "startTime": "09:00",
  "endTime": "10:30",
  "color": "#937EBF",
  "credits": 2
}
```

#### 時間割の全科目取得
```http
GET /api/timetables/{timetableId}/courses
```

#### 科目更新
```http
PUT /api/timetables/courses/{courseId}
```

#### 科目削除
```http
DELETE /api/timetables/courses/{courseId}
```

---

### 講義レビュー API

#### レビュー作成
```http
POST /api/lecture-reviews
```
**リクエスト:**
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

#### レビュー検索
```http
GET /api/lecture-reviews/search?keyword={keyword}
```

#### 講義名+教授名でレビュー取得
```http
GET /api/lecture-reviews/course?courseName={courseName}&professor={professor}
```

#### 自分が書いたレビュー一覧
```http
GET /api/lecture-reviews/my-reviews
```

#### レビュー更新
```http
PUT /api/lecture-reviews/{reviewId}
```

#### レビュー削除
```http
DELETE /api/lecture-reviews/{reviewId}
```

---

### 画像アップロード API

#### 画像アップロード
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
  "imageUrl": "/uploads/image.jpg",
  "originalFilename": "photo.jpg",
  "fileSize": 102400
}
```

---

### 管理者 API

#### 全ユーザー取得
```http
GET /api/admin/users
```

#### ユーザー停止
```http
PUT /api/admin/users/{userId}/ban
```

#### ユーザー停止解除
```http
PUT /api/admin/users/{userId}/unban
```

#### 投稿削除（管理者）
```http
DELETE /api/admin/posts/{postId}
```

#### サイト統計取得
```http
GET /api/admin/stats
```

---

## 🐳 デプロイ

### Docker を使用したデプロイ

#### 高速デプロイ (推奨)
```bash
cd /Users/yunsu-in/Downloads/Globalin

# 自動デプロイスクリプト実行
./deploy.sh
```

#### 手動デプロイ
```bash
# Docker Compose でビルド及び実行
docker-compose up -d

# ログ確認
docker-compose logs -f

# 特定サービスのログのみ確認
docker-compose logs -f backend
docker-compose logs -f frontend

# 停止
docker-compose stop

# 完全削除
docker-compose down
```

#### デプロイ後の接続
- **フロントエンド**: http://localhost
- **バックエンド API**: http://localhost:8080/api/health

詳細なデプロイガイドは **[README-DOCKER.md](./README-DOCKER.md)** を参照

---

## 🎨 開発ガイド

### カラーシステム

プロジェクト全体で一貫したブランドカラーを使用してください:

```scss
// 📁 _colors.scss (推奨変数)

// Primary Colors
$mint-green: #C6E5D1;          // ミントグリーン (ヘッダー背景)
$mint-green-dark: #a8d5ba;     // ミントグリーン (濃いトーン)

$purple-light: #937EBF;         // パープル (強調、ボーダー)
$purple-dark: #433461;          // パープル (テキスト、タイトル)

$orange: #FE9F1A;               // オレンジ (アクセント、ホバー)

// Usage Examples
.header {
  background: linear-gradient(135deg, $mint-green 0%, $mint-green-dark 100%);
}

.title {
  color: $purple-dark;
}

.button:hover {
  border-color: $orange;
}

.stat-value {
  color: $purple-light;
}
```

---

### 新機能の追加

#### バックエンド API 追加
1. **Controller 作成**
   ```java
   // src/main/java/com/example/Globalin/controller/
   @RestController
   @RequestMapping("/api/boards")
   public class BoardController {
       @GetMapping("/{id}")
       public ResponseEntity<Board> getBoard(@PathVariable Long id) {
           // ...
       }
   }
   ```

2. **Service ロジック実装**
   ```java
   // src/main/java/com/example/Globalin/service/
   @Service
   public class BoardDetailService {
       public Board getBoardById(Long id) {
           // ...
       }
   }
   ```

#### フロントエンドページ追加
1. **コンポーネント作成**
   ```typescript
   // src/components/pages/BoardDetail/BoardDetail.tsx
   import React from 'react';
   import './BoardDetail.scss';

   const BoardDetail: React.FC = () => {
       return <div className="board-detail">...</div>;
   };
   ```

2. **スタイル追加**
   ```scss
   // src/components/pages/BoardDetail/BoardDetail.scss
   .board-detail {
       max-width: 1200px;
       margin: 0 auto;

       .title {
           color: $purple-dark;
       }
   }
   ```

---

## 🔧 트러블슈팅

### 백엔드

#### 포트 8080 충돌
```bash
# 포트 사용 중인 프로세스 확인
lsof -i:8080

# 프로세스 종료
lsof -ti:8080 | xargs kill -9
```

#### MariaDB 연결 실패
```bash
# MariaDB 상태 확인
brew services list | grep mariadb

# MariaDB 재시작
brew services restart mariadb

# 연결 테스트
mysql -u root -p -e "SHOW DATABASES;"
```

---

### 프론트엔드

#### npm 빌드 실패
```bash
# node_modules 재설치
rm -rf node_modules package-lock.json
npm install
```

#### API 연결 실패
```bash
# .env.development 확인
cat frontend/.env.development
# REACT_APP_API_URL=http://localhost:8080

# 백엔드 서버 실행 확인
curl http://localhost:8080/api/health
```

---

### Docker

#### 컨테이너 빌드 실패
```bash
# 캐시 없이 재빌드
docker-compose build --no-cache

# 모든 컨테이너 및 이미지 제거 후 재시작
docker-compose down -v --rmi all
docker-compose up -d
```

#### 헬스 체크 실패
```bash
# 컨테이너 로그 확인
docker-compose logs backend
docker-compose logs frontend

# 컨테이너 내부 접속
docker exec -it globalin-backend /bin/bash
docker exec -it globalin-frontend /bin/sh
```

---

## 📝 라이센스

이 프로젝트는 교육 목적으로 만들어졌습니다.

---

## 👥 기여

버그 리포트나 기능 제안은 이슈로 등록해주세요.

---

## 📧 연락처

프로젝트 관련 문의: yunsu-in@example.com
# EveryJapan
# EveryJapan
# EveryJapan
