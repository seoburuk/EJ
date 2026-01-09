# Globalin - 日本大学留学生コミュニティ

日本の大学に在学中の韓国人留学生のためのコミュニティプラットフォームです。

## 📋 目次


---

## 🎯 プロジェクト概要

### 主な機能

- 👤 ユーザープロフィール管理
- 📝 掲示板システム（人文学、自由掲示板など）
- 🔥 HOT投稿おすすめ
- ⭐ BEST掲示板おすすめ
- 🔍 リアルタイム検索

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
1. 대학교 도메인
2. 이미지 크기 축소

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

