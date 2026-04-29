# 📝 TodoList App (Android)

這是一個使用 Kotlin 開發的待辦事項 App，支援使用者登入、任務管理，以及本地與雲端（Firebase）資料同步。

---

## 📌 專案特色

- 🔐 使用者登入 / 註冊系統
- 🧠 每個使用者擁有自己的任務資料
- 📅 行事曆查看每日任務
- ✅ 任務新增 / 修改 / 刪除（CRUD）
- ☁️ Firebase Firestore 雲端同步
- 📱 Room 本地資料庫（離線也可使用）

---

## 🧱 技術架構

| 技術 | 用途 |
|------|------|
| Kotlin | Android 開發語言 |
| Room | 本地資料庫 |
| Firebase Firestore | 雲端資料庫 |
| RecyclerView | 顯示任務列表 |
| Fragment | UI 切換 |
| Coroutine | 非同步處理 |

---

## 🗂️ 資料流程

新增任務時：
User 操作
→ 存入 Room（本地）
→ 同步至 Firebase（雲端）

資料結構：
users
└ userAccount
└ missions
└ missionId


---

## 📸 App 畫面
- Login 畫面
<img width="392" height="863" alt="image" src="https://github.com/user-attachments/assets/92118b43-9739-472e-9de4-f7ccc6222161" />
- Home 任務列表
- <img width="388" height="860" alt="image" src="https://github.com/user-attachments/assets/e6307a96-d20c-4d4b-adee-3c5cf4a87206" />
- Calendar 行事曆
- <img width="392" height="865" alt="image" src="https://github.com/user-attachments/assets/fd120b0c-2134-40fc-b2fd-b355b4d28f4e" />


---


---

## 📚 未來改進

- 任務提醒通知
- UI 優化

---

## 👨‍💻 作者

- 洪書文 antrails
