# News App - Android Task

A modern Android News application built with Kotlin following MVVM architecture pattern. This app fetches news articles from NewsAPI and provides a clean, user-friendly interface with favorites functionality and dark mode support.

## 🎯 Bonus Points Achieved

- ✅ **Favorite Screen**: Complete favorites management
- ✅ **Dark Mode Support**: Full theme switching
- ✅ **Pull-to-Refresh**: Smooth content refresh
- ✅ **Additional Features**: Onboarding + Modern android built-in splash screen

## 📱 Features

### Core Features

- **News Listing**: Browse through latest news articles with images, titles, dates, and descriptions
- **Article Details**: View full article content with author information
- **Favorites System**: Mark/unmark articles as favorites with local storage
- **Profile Screen**: Static user profile with logout option
- **Smooth Navigation**: Seamless transitions between screens

### Bonus Features Implemented ✨

- **Favorites Screen**: Dedicated screen to view all favorited articles
- **Dark Mode Support**: Toggle between light and dark themes
- **Pull-to-Refresh**: Refresh news content with swipe gesture
- **Onboarding Screen**: Welcome screen for first-time users
- **Android Built-in Splash Screen**: Modern splash screen implementation

## 🏗️ Architecture

This project follows the **MVVM (Model-View-ViewModel)** architecture pattern with Repository pattern for data management.

### Project Structure

**Architecture Philosophy**: This modular structure has been successfully utilized in team environments and promotes maintainability with clear separation of concerns. While I'm experienced with this approach, I remain flexible and adaptable to alternative architectural patterns that align with team standards and project requirements.

```
com/silverkey/task/
├── base/                           # Base components used across features
│   ├── BaseActivity
│   ├── BaseFragment
│   ├── BaseViewModel
│   └── ...
├── db/                             # Room database components
│   ├── dao/
│   └── database/
├── model/                          # Data models for each feature
│   ├── home/
│   └── onboardind/
├── network/
│   ├── api_helper/                 # API interface definitions
│   │   ├── home_api_helper/
│   │   └── ...
│   ├── api_helper_impl/            # API implementation
│   │   ├── home_api_helper/
│   │   └── ...
│   ├── ApiService
│   ├── AppModule
│   └── ...
├── repos/                          # Repository layer for each feature
│   └── home/
├── ui/                             # UI components for each feature
│   ├── components/
│   ├── onboarding/
│   ├── home/
│   └── routing/
├── viewmodels/                     # ViewModels for each feature
│   └── home/
└── utils/                          # Utility classes and helpers
    ├── Constants
    ├── Extensions
    ├── NetworkUtils
    └── ...
```

## 🛠️ Technical Stack

### Core Technologies

- **Language**: Kotlin
- **Architecture**: MVVM + Repository Pattern
- **UI**: XML Layouts with ViewBinding
- **Dependency Injection**: Hilt
- **Database**: Room
- **Networking**: Retrofit + OkHttp
- **Image Loading**: Glide
- **Navigation**: Navigation Component

## 📱 Download APK

You can download the APK from Google Drive:
**[Download News App APK](https://drive.google.com/drive/folders/1W3k6v4loTaJAxoN8rPRd4AgCZbERHXDp?usp=sharing)**

## 🎨 UI/UX Design

The app follows the provided Figma design specifications with:

- Clean and modern interface
- Consistent spacing and typography
- Intuitive navigation patterns
- Responsive design for different screen sizes
- Dark mode support with proper color schemes

## 🔄 App Flow

1. **Splash Screen** → Native Android 12+ splash screen
2. **Onboarding** → First-time user welcome (bonus feature)
3. **Home Screen** → News articles listing with favorites toggle
4. **Article Details** → Full article view with favorite option
5. **Favorites Screen** → User's saved articles (bonus feature)
6. **Profile Screen** → User information and settings

## 🌟 Key Features Implementation

### News Loading

- Fetches articles from NewsAPI
- Handles loading states with progress indicators
- Error handling with retry options
- Pull-to-refresh functionality

### Favorites System

- Local storage using Room database
- Real-time favorites sync across screens
- Persistent favorites across app sessions

### Dark Mode

- System-wide dark mode support
- Smooth theme transitions
- Consistent dark theme across all screens

### Performance Optimizations

- Image loading with Glide caching
- Efficient RecyclerView implementation
- Proper lifecycle management
- Memory leak prevention
