# WorldT2 🏏

**WorldT2** is a cricket match simulation Android app built with a focus on clean architecture and extensibility. The simulation logic is structured to be easily upgradable without affecting the UI or flow, allowing future enhancements like over-based gameplay, detailed scorecards, or player performance tracking.

---

## 🛠 Tech Stack

- **Language**: Kotlin  
- **Architecture**: MVVM  
- **Async Handling**: Kotlin Coroutines  
- **UI**: ViewBinding, LiveData  
- **Networking & Media**: Retrofit, Glide  

---

## 📚 Libraries Used

- [Kotlinx Coroutines](https://github.com/Kotlin/kotlinx.coroutines) – For background operations  
- [Retrofit](https://square.github.io/retrofit/) – For REST API consumption  
- [Glide](https://github.com/bumptech/glide) – For image loading and caching  
- [AndroidX Lifecycle](https://developer.android.com/jetpack/androidx/releases/lifecycle) – LiveData and ViewModel

---

## ✨ Features

- Simulates a match between two cricket teams  
- Displays match results after simulation  
- Lightweight and performant with separation of concerns  
- Clean and readable code for future enhancements  

---

## 📦 Project Structure

- ui/ # Activity and layout handling
- viewmodel/ # UI state management using ViewModel
- model/ # Match, team, and player data classes
- simulation/ # Core match simulation logic


---

## 🚀 Getting Started

1. Clone the repo  
2. Open in Android Studio  
3. Build and run on emulator or device (min SDK 21)

