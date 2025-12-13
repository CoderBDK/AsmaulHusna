# 🌙 Asmaul Husna – 99 Names of Allah
A modern, multi-screen Jetpack Compose Android app that **displays and explores Allah’s 99 beautiful names** with Arabic script, meanings, pronunciation audio, and detailed explanation.

## 🚀 Features

### 📌 **Core Features**
- 🕋 **99 Names of Allah** with:
  - Arabic Name
  - Meaning
  - Transliteration
  - Pronunciation Audio
  - Detailed Explanation Page
- ⭐ **Favorite Names** – Save your favorite Asmaul Husna.
- 🎙 **Audio Playback** – Listen to correct pronunciation of each name.
- 🌗 **Dark/Light Theme**
- 🎨 **Modern, Clean UI**
  - Jetpack Compose Material 3
  - Smooth animations
  - Elegant layout for displaying content
  - Optimized for readability & user experience
- 📱 **Multi-Screen Navigation**
  - Type-safe sealed class routes
  - Clean Navigation Graph
- 🌍 **Full Multi-Language Support**
  - The **entire app UI** adapts to the selected language (Bangla, English,Hindi, Urdu, etc.)
  - Easily extendable to new languages

---

## 📱 Screens Included

- **Splash Screen** – App launch/loading screen
- **Onboarding Screen** – Single screen with multiple steps:
  - Welcome
  - Language Selection
  - Feature Highlights
  - Final “Get Started”
- **Home Screen** – Grid/List of all 99 Names
- **Details Screen** – Arabic, meanings, audio & tafsir
- **AudioPlayback Screen** - Audio Player for playback
- **Favorites Screen** - List of user’s favorite names
- **Settings Screen** – Theme, Language, About

## ✅ Feature Checklist

- [x] Display all 99 Names with Arabic
  - [x] Meaning
  - [x] Transliteration
  - [x] Pronunciation Audio
  - [ ] Detailed Explanation Page
- [x] Favorites functionality
- [x] Modern UI with Jetpack Compose
- [x] Multi-Screen Navigation
- [x] Dark/Light Theme
- [x] Audio Playback for each name
- [x] Search Feature
- [x] Settings (Theme, Language, About, etc)

## 🌐 Language Support

- [ ] Arabic (العربية)
- [ ] Bangla (বাংলা)
- [x] English (English)
- [ ] Hindi (हिन्दी)
- [ ] Urdu (اردو)
- [ ] Other Languages

## 🛠️ Tech Stack

- **Kotlin** – Core programming language
- **Jetpack Compose** – Modern UI toolkit for Android
- **Material 3** – Design components and theming
- **Navigation Compose** – Type-safe navigation
- **Kotlin Serialization** – JSON parsing and data serialization
- **ViewModel + StateFlow** – State management
- **Datastore** – Persistent key-value storage
- **Room Database** – Local database for storing app data
- **Ktor Client** – Network requests & remote data fetching
- **MediaPlayer** – Audio playback
- **Dagger & Hilt** – Dependency Injection

## 📂 Project Architecture
```plaintext
📦 asmaul-husna-app
┣ 📂 audio
┣ 📂 data
┃ ┣ 📂 local
┃ ┃ ┣ 📂 db
┃ ┃ ┗ 📂 prefs
┃ ┣ 📂 model
┃ ┣ 📂 remote
┃ ┣ 📂 util
┃ ┗ 📂 repository
┣ 📂 domain
┃ ┣ 📂 usecase
┃ ┗ 📂 model
┣ 📂 di
┣ 📂 service
┣ 📂 ui
┃ ┣ 📂 audio
┃ ┣ 📂 components
┃ ┣ 📂 details
┃ ┣ 📂 home
┃ ┣ 📂 settings
┃ ┣ 📂 splash
┃ ┣ 📂 favorite
┃ ┣ 📂 navigation
┃ ┗ 📂 theme
┣ MainActivity.kt
┗ README.md
```

## Installation

### 1. Clone the repository
```bash
git clone https://github.com/CoderBDK/AsmaulHusna.git
```

## License

This project is licensed under the MIT License. See the [LICENSE](LICENSE) file for details.
