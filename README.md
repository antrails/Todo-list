# 📝 TodoList App (Android)

一款使用 Kotlin 開發的 Android Todo App，結合 Firebase 與 Room，
實現「離線優先（Offline-first）」的任務管理系統。

支援使用者登入、任務同步與行事曆檢視，提供流暢且可靠的使用體驗。

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

### 🔄 資料同步流程

1. 使用者操作新增任務
2. 資料先寫入 Room（本地）
3. 再同步至 Firebase Firestore（雲端）
4. 確保離線狀態下仍可正常使用（Offline-first）

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

## 👨‍💻 Author

- 洪書文 (antrails)
- GitHub: https://github.com/amtrails
