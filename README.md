# Learnio

An Android application designed to help Bangla-speaking students learn English pronunciation through interactive practice with their teachers.

## 📱 Features

### For Teachers

- **Record Example Sentences**: Teachers can record their own pronunciation of English sentences
- **Provide Audio References**: Students can listen to the teacher's recordings as reference material
- **Browse Sentences**: Easy interface to browse through categorized sentences

### For Students

- **Listen to Teacher Recordings**: Play back teacher's pronunciation of sentences
- **Practice Recording**: Record their own version of the same sentence
- **Compare Recordings**: Listen to both teacher and student recordings to compare and improve
- **Self-Assessment**: Practice pronunciation independently

### General Features

- **Dual Mode Interface**: Switch between Teacher Mode and Student Mode
- **Bangla-English Sentence Pairs**: Each sentence is displayed in both Bangla and English
- **Topic-Based Organization**: Sentences are organized by topics (greetings, feelings, basic conversation)
- **Audio Playback Controls**: Play and stop functionality for all recordings
- **Persistent Storage**: All recordings are saved for later review

### 🎮 Gamification & Progress Tracking (NEW!)

- **XP & Leveling System**: Earn XP for practicing and unlock new levels
- **Daily Streaks**: Build consecutive day streaks to earn bonus XP
- **27 Achievements**: Unlock achievements for various milestones
- **Daily Goals**: Set and track daily practice goals
- **Progress Statistics**: View detailed stats on your learning journey
- **Mastery Stars**: Rate your proficiency on each sentence (1-5 stars)
- **Favorites**: Mark sentences as favorites for quick access
- **Quiz Mode**: Test your knowledge with interactive quizzes

### 🏆 Achievement System

Unlock achievements across 6 categories:

- **Practice Count**: First Steps, Getting Started, Dedicated Learner, etc.
- **Streak**: On Fire, Week Warrior, Fortnight Hero, etc.
- **Recordings**: Voice Actor, Sound Engineer, etc.
- **Mastered**: Perfect Score, Half Way There, etc.
- **Level**: Rising Star, Scholar, etc.
- **Favorites**: Collector, Curator

### 📊 Statistics Dashboard

- Total practice sessions
- Current and longest streak
- Total recordings made
- Sentences mastered
- Achievements unlocked
- Daily goal progress
- XP and level progress

## 🎯 How to Use

### Teacher Mode

1. **Launch the app** and ensure "Teacher Mode" is selected (button will be highlighted)
2. **Browse sentences** from the list
3. **Select a sentence** you want to record
4. **Tap "Record Teacher"** button to start recording your pronunciation
5. **Speak clearly** the English sentence
6. **Tap "Stop Recording"** when finished
7. **Listen to your recording** by tapping "Play Teacher"
8. **Re-record if needed** by recording again (overwrites previous recording)

### Student Mode

1. **Launch the app** and tap "Student Mode" button to switch modes
2. **Browse available sentences**
3. **Select a sentence** to practice
4. **Tap "Play Teacher"** to hear the teacher's pronunciation (must be recorded first)
5. **Tap "Record Student"** to record your own version
6. **Speak the English sentence** clearly
7. **Tap "Stop Recording"** when finished
8. **Compare recordings**:
   - Tap "Play Teacher" to hear the teacher's version
   - Tap "Play Student" to hear your version
9. **Practice repeatedly** until you're satisfied with your pronunciation

## 📝 Sample Sentences

The app includes 40+ common English sentences with Bangla translations, including:

### Greetings and Checking In

- "How are you my friends?" - "তোমরা কেমন আছ বন্ধুরা?"
- "Good morning" - "শুভ সকাল"
- "Thank you very much" - "অনেক ধন্যবাদ"

### Feelings and Behaviours

- "I am happy" - "আমি খুশি"
- "I am tired" - "আমি ক্লান্ত"
- "I slept well last night" - "গত রাতে আমি ভালো ঘুমিয়েছি"

### Basic Conversation

- "What is your name?" - "তোমার নাম কী?"
- "Where do you live?" - "তুমি কোথায় থাকো?"
- "I don't understand" - "আমি বুঝতে পারছি না"

## 🔧 Technical Details

### Data Structure

Sentences are stored in JSON format:

```json
{
  "English": "How are you my friends?",
  "Bangla": "তোমরা কেমন আছ বন্ধুরা?",
  "Topic": "greetings_and_checking_in"
}
```

### Storage

- **Sentence Data**: Stored in Room Database
- **Audio Recordings**: Saved to device storage
- **Teacher Recordings**: Linked to sentence records
- **Student Recordings**: Separate database table for tracking practice sessions

### Permissions Required

- **RECORD_AUDIO**: Required for recording teacher and student pronunciations
- **READ_EXTERNAL_STORAGE**: For accessing saved recordings
- **WRITE_EXTERNAL_STORAGE**: For saving recordings

## 🎓 Educational Approach

This app follows a **Listen-Practice-Compare** methodology:

1. **Listen**: Students hear correct pronunciation from their teacher
2. **Practice**: Students attempt to pronounce the sentence themselves
3. **Compare**: Students compare their pronunciation with the teacher's
4. **Repeat**: Continuous practice until mastery

## 🌟 Benefits

### For Teachers

- Create personalized learning materials
- Provide consistent pronunciation references
- Support students outside classroom hours
- Track which sentences have been recorded

### For Students

- Learn at their own pace
- Practice anytime, anywhere
- Build confidence in English pronunciation
- Direct comparison with teacher's pronunciation
- Immediate feedback through self-assessment

## 📱 Interface Guide

### Main Screen Elements

- **Title**: "English Learning App"
- **Mode Buttons**: Switch between Teacher and Student modes
- **Mode Indicator**: Shows current mode
- **Sentence List**: Scrollable list of all available sentences

### Sentence Card Elements

- **Topic Badge**: Shows the category (e.g., greetings_and_checking_in)
- **Bangla Text**: Main display (larger, bold, blue)
- **English Text**: Translation (smaller, gray)
- **Record Button**: Record pronunciation (Teacher or Student based on mode)
- **Play Teacher Button**: Play teacher's recording
- **Play Student Button**: Play student's recording (visible only in Student Mode)

### Button States

- **Record**: Purple button - ready to record
- **Stop Recording**: Red button - currently recording
- **Play**: Teal button - ready to play
- **Stop Playing**: Red button - currently playing
- **Disabled**: Grayed out - no recording available

## 🚀 Getting Started

1. **Install the app** on your Android device
2. **Grant audio recording permission** when prompted
3. **Teachers**: Switch to Teacher Mode and start recording sentences
4. **Students**: Use Student Mode to listen and practice
5. **Practice regularly** for best results!

## 🔄 Workflow Example

**Teacher Workflow:**

1. Open app → Teacher Mode
2. Select "Good morning"
3. Record → Say "Good morning" clearly → Stop
4. Listen to verify → Re-record if needed
5. Move to next sentence

**Student Workflow:**

1. Open app → Student Mode
2. Select "Good morning"
3. Play Teacher → Listen carefully
4. Record Student → Say "Good morning" → Stop
5. Compare: Play Teacher, then Play Student
6. Practice again if pronunciation needs improvement
7. Move to next sentence when satisfied

## 💡 Tips for Best Results

### For Teachers

- Speak clearly and at moderate speed
- Record in a quiet environment
- Use natural pronunciation (not overly slow)
- Re-record if you make a mistake

### For Students

- Listen to teacher's recording multiple times before attempting
- Record in a quiet place
- Try to match the teacher's rhythm and intonation
- Don't be discouraged - practice makes perfect!
- Record multiple times to track improvement

## 📊 Progress Tracking

While the app doesn't have built-in analytics, students can:

- Keep practicing sentences until confident
- Re-record to hear their improvement over time
- Work through topics systematically

## 🔮 Future Enhancements (Potential)

- Add more sentence categories
- Include pronunciation scoring
- Add waveform visualization
- Support for custom sentence addition
- Progress tracking and achievements
- Offline mode improvements

## 📄 License

This app is designed for educational purposes to help Bangla speakers learn English pronunciation.

## 🤝 Support

For teachers and students using this app:

- Practice regularly for best results
- Use headphones for better audio quality
- Ensure good microphone positioning when recording

---

**Happy Learning! শুভ শিক্ষা!**

A mobile application designed for language learning that allows teachers to record sentences and students to practice them by comparing their recordings with the teacher's versions.

## Features

### Teacher Mode

- View a list of Tiwi language sentences loaded from a JSON file
- Record pronunciations for each sentence
- Play back previously recorded teacher pronunciations

### Student Mode

- View the same list of sentences
- Record their own pronunciations of sentences
- Play back both teacher and student recordings for comparison
- Practice pronunciation by comparing with teacher recordings

## Technical Features

- **Local Database**: Uses Room database to store sentences and recordings
- **Audio Recording**: Records audio using MediaRecorder
- **Audio Playback**: Plays recordings using MediaPlayer
- **JSON Data Loading**: Loads initial sentence data from assets
- **Dual Mode Interface**: Switch between teacher and student modes

## Data Structure

The app uses Tiwi language data with the following structure:

- **English**: English translation of the sentence
- **Tiwi**: Native Tiwi language sentence
- **Topic**: Category/topic of the sentence

## Setup Instructions

1. Ensure you have Android Studio installed
2. Open the project in Android Studio
3. Build and run the app on a device or emulator
4. Grant audio recording permissions when prompted

## Usage

### First Time Setup

1. Launch the app
2. The app will automatically load sentences from the JSON file into the local database
3. Grant audio recording permissions

### Teacher Mode

1. Select "Teacher Mode"
2. Browse through the list of sentences
3. Tap "Record" button to record your pronunciation
4. Tap "Play Teacher" to hear your recorded pronunciation

### Student Mode

1. Select "Student Mode"
2. Browse through the sentences
3. Tap "Play Teacher" to hear the teacher's pronunciation
4. Tap "Record" to record your own pronunciation
5. Tap "Play Student" to hear your recorded pronunciation
6. Compare the two recordings to improve pronunciation

## File Structure

```
app/
├── src/main/java/com/temmahadi/tiwilanguageapp/
│   ├── model/           # Data models (Sentence, StudentRecording)
│   ├── dao/             # Database access objects
│   ├── database/        # Room database configuration
│   ├── repository/      # Data repository layer
│   ├── viewmodel/       # ViewModels for UI
│   ├── adapter/         # RecyclerView adapters
│   ├── utils/           # Utility classes (AudioManager, JsonLoader)
│   └── MainActivity.java
├── src/main/res/
│   ├── layout/          # XML layouts
│   ├── values/          # Colors, strings, themes
│   └── ...
└── src/main/assets/
    └── tiwi_sentences.json
```

## Dependencies

- AndroidX libraries (Room, RecyclerView, Lifecycle)
- Gson for JSON parsing
- Material Design components

## Permissions

The app requires the following permissions:

- `RECORD_AUDIO`: For recording audio
- `WRITE_EXTERNAL_STORAGE`: For saving recordings
- `READ_EXTERNAL_STORAGE`: For reading recordings

## Future Enhancements

- Student name management
- Recording quality indicators
- Progress tracking
- Export/import of recordings
- Cloud synchronization
- Multiple language support
- Gamification features
