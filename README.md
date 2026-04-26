# Learnio

An Android application designed to help Bangla-speaking students learn English pronunciation through interactive practice with their teachers.

## 📱 Features

### For Teachers
- **Record Example Sentences**: Teachers can record their own pronunciation of English sentences.
- **Provide Audio References**: Students can listen to the teacher's recordings as reference material.
- **Manage Sentences**: Easy interface to browse, add, and categorize sentences.
- **Dual Mode Interface**: Switch between Teacher Mode and Student Mode easily.

### For Students
- **Listen to Teacher Recordings**: Play back teacher's pronunciation of sentences.
- **Practice Recording**: Record their own version of the same sentence.
- **Compare Recordings**: Listen to both teacher and student recordings to compare and improve.
- **Mastery Tracking**: Rate your proficiency on each sentence (1-5 stars).
- **Favorites**: Mark sentences as favorites for quick access.

### 🎮 Gamification & Progress Tracking
- **XP & Leveling System**: Earn XP for practicing and unlock new levels (Beginner to Master).
- **Daily Streaks**: Build consecutive day streaks to earn bonus XP.
- **Achievements**: Unlock over 27 achievements across 6 categories (Practice, Streak, Recordings, Mastery, Level, and Favorites).
- **Daily Goals**: Set and track daily practice goals.
- **Statistics Dashboard**: View detailed stats on your learning journey, including total practice sessions, streaks, and recordings.
- **Quiz Mode**: Test your knowledge with interactive quizzes.

### 💳 Subscription System (NEW!)
- **Robi / Airtel Integration**: Exclusive features for Robi and Airtel users.
- **Secure Authentication**: OTP-based registration and login.
- **Daily Access**: Simple daily subscription for premium lessons and challenges.

## 📝 Sample Sentences
The app includes 40+ common English sentences with Bangla translations, organized by topics:
- **Greetings**: "How are you my friends?" - "তোমরা কেমন আছ বন্ধুরা?"
- **Feelings**: "I am happy" - "আমি খুশি"
- **Conversation**: "What is your name?" - "তোমার নাম কী?"

## 🔧 Technical Details

### Architecture
- **Language**: Java
- **Database**: Room Database for local persistence of sentences, recordings, and user stats.
- **Audio**: MediaRecorder for recording and MediaPlayer for playback.
- **UI**: Material Design components with Support for Dark Mode.
- **Networking**: Retrofit for subscription and OTP services.

### Data Structure (JSON)
```json
{
  "English": "How are you my friends?",
  "Bangla": "তোমরা কেমন আছ বন্ধুরা?",
  "Topic": "greetings_and_checking_in"
}
```

### File Structure
```
app/
├── src/main/java/com/temmahadi/EnglishLearningApp/
│   ├── model/           # Data models (Sentence, StudentRecording, etc.)
│   ├── dao/             # Room Database Access Objects
│   ├── database/        # Room Database configuration
│   ├── repository/      # Data repository layer
│   ├── viewmodel/       # ViewModels for UI state management
│   ├── adapter/         # RecyclerView adapters
│   ├── logIn/           # Subscription and OTP logic
│   ├── utils/           # Utility classes (AudioManager, JsonLoader)
│   └── Activities/      # UI Activities (MainActivity, QuizActivity, etc.)
└── src/main/assets/
    └── sentences.json   # Initial sentence data
```

## 🚀 Getting Started

1. **Clone the repository**: `git clone https://github.com/tem-mahadi/Learnio.git`
2. **Open in Android Studio**: Ensure you have the latest version of Android Studio.
3. **Build the project**: Let Gradle sync and build the application.
4. **Permissions**: The app requires `RECORD_AUDIO` and storage permissions.

## 🤝 Support
- **Teachers**: Use a quiet environment for high-quality reference recordings.
- **Students**: Use headphones for better comparison of recordings.

---
**Happy Learning!**
