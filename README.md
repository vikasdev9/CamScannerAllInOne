# 🚀 ScanFlow: Modern Document Scanning Ecosystem

[![Kotlin](https://img.shields.io/badge/Kotlin-2.2.10-blue.svg?style=flat&logo=kotlin)](https://kotlinlang.org)
[![Compose](https://img.shields.io/badge/Jetpack_Compose-2026.02.01-green.svg?style=flat&logo=android)](https://developer.android.com/jetpack/compose)
[![Architecture](https://img.shields.io/badge/Architecture-Clean_MVVM-orange.svg?style=flat)](https://developer.android.com/topic/architecture)
[![License](https://img.shields.io/badge/License-MIT-brightgreen.svg?style=flat)](LICENSE)
[![Platform](https://img.shields.io/badge/Platform-Android-3DDC84.svg?style=flat&logo=android)](https://www.android.com)

**ScanFlow** is an enterprise-grade document scanning application for Android, engineered with the latest mobile technologies. It transforms your smartphone into a high-performance portable scanner, enabling users to capture, enhance, and organize documents with industrial-strength precision. Built on **Clean Architecture** and **Jetpack Compose**, ScanFlow offers a premium, lag-free experience for document management.

---

## 📸 Screenshots

| Home & Files | Camera Scanner | PDF Preview |
|:---:|:---:|:---:|
| ![Home](https://via.placeholder.com/300x600?text=Home+Screen) | ![Scanner](https://via.placeholder.com/300x600?text=CameraX+Scanner) | ![PDF](https://via.placeholder.com/300x600?text=PDF+Preview) |

| OCR Extraction | Folder Management | Edit & Filters |
|:---:|:---:|:---:|
| ![OCR](https://via.placeholder.com/300x600?text=OCR+Extraction) | ![Folders](https://via.placeholder.com/300x600?text=Folder+Management) | ![Edit](https://via.placeholder.com/300x600?text=Image+Enhancement) |

---

## ✨ Features

### 📑 Document Capture
- **Multi-page Scanning:** Capture dozens of pages in a single session with seamless background saving.
- **CameraX Integration:** Real-time camera preview with smart flashlight and zoom controls.
- **Gallery Import:** Batch import existing images to convert them into organized PDF projects.

### 🧠 Intelligent Processing
- **OpenCV Smart Crop:** Automatic edge detection and perspective correction to straighten skewed scans.
- **AI Enhancement:** \"Magic Color\" algorithms to boost text legibility and remove shadows.
- **Professional Filters:** Grayscale, Black & White, Sharpen, and High Contrast modes.

### 📄 PDF & OCR
- **High-Fidelity PDF:** Generate industrial-standard PDFs with intelligent image compression.
- **ML Kit OCR:** Extract text from images with high accuracy using Google's Machine Learning.
- **Built-in Preview:** Native PDF renderer for viewing documents instantly without third-party apps.

### 📂 Organization & Security
- **Folder Management:** Create custom folders, rename, and move PDFs for logical categorization.
- **Local Persistence:** Offline-first architecture using Room database for metadata and secure internal storage for files.
- **Search & Discovery:** Instant full-text search across document names and metadata.

---

## 🛠 Tech Stack

| Component | Technology |
|:--- |:--- |
| **Language** | [Kotlin](https://kotlinlang.org/) - 100% Type-safe & Coroutine-driven |
| **UI Framework** | [Jetpack Compose](https://developer.android.com/jetpack/compose) - Modern Declarative UI |
| **Architecture** | Clean Architecture + MVVM + Repository Pattern |
| **Dependency Injection** | [Hilt](https://developer.android.com/training/dependency-injection/hilt-android) - Dagger-based DI |
| **Database** | [Room](https://developer.android.com/training/data-storage/room) - SQLite abstraction layer |
| **Camera** | [CameraX](https://developer.android.com/training/camerax) - Lifecycle-aware camera library |
| **Image Processing** | [OpenCV Android](https://opencv.org/android/) - Computer Vision for cropping/filters |
| **Machine Learning** | [ML Kit](https://developers.google.com/ml-kit) - On-device Text Recognition (OCR) |
| **Async Logic** | [Coroutines](https://kotlinlang.org/docs/coroutines-overview.html) & [Flow](https://kotlinlang.org/docs/flow.html) |
| **PDF Tools** | [PdfBox-Android](https://github.com/TomRoush/PdfBox-Android) / Native PdfDocument |

---

## 🏛 Architecture

ScanFlow follows the **SOLID** principles of **Clean Architecture** to ensure the codebase is testable, scalable, and maintainable.

- **`data/`:** Implementation of repositories, Room database (DAOs/Entities), and API integrations.
- **`domain/`:** Pure business logic. Contains Models, Repository Interfaces, and UseCases (Interactable units).
- **`presentation/`:** UI layer containing Jetpack Compose screens, ViewModels (state management), and Navigation graphs.

### 📦 Folder Structure

```
app/
├── data/
│   ├── local/            # Room Database, DAOs, Entities
│   ├── mapper/           # Data to Domain transformations
│   ├── repository/       # Repository Implementations
│   └── util/             # ImageProcessor, PDFManager, OCRManager
├── domain/
│   ├── model/            # Business Models (Document, Page, Folder)
│   ├── repository/       # Abstract Repository Contracts
│   └── usecase/          # Individual business logic units
├── presentation/
│   ├── navigation/       # Compose Navigation setup
│   ├── screens/          # Feature-based Compose screens
│   ├── theme/            # Material 3 Color, Type, Shape
│   └── components/       # Reusable UI widgets
├── di/                   # Hilt Modules
└── util/                 # App-wide utility classes
```

---

## 🚀 Installation & Setup

### Prerequisites
- Android Studio Ladybug (2024.2.1) or newer
- JDK 17
- Android SDK 35+

### Steps
1. **Clone the repository:**
   ```bash
   git clone https://github.com/yourusername/ScanFlow.git
   ```
2. **Open in Android Studio:**
   File > Open > Select the ScanFlow project folder.
3. **Gradle Sync:**
   Wait for the project to download all dependencies and sync.
4. **Run:**
   Select a physical device or emulator and click the **Run** icon.

---

## 🛡 Permissions

ScanFlow requests the following permissions to ensure full functionality:
- `CAMERA`: For document capturing.
- `READ_MEDIA_IMAGES`: For importing documents from the gallery.
- `POST_NOTIFICATIONS`: For scan status and export alerts.
- `INTERNET`: For Firebase Analytics (optional) and crash reporting.

---

## ⚡ Performance Optimization

- **Background Processing:** Large PDF generations and OCR tasks run on `Dispatchers.IO` to keep the UI thread responsive.
- **Memory Management:** Bitmaps are intelligently sampled and recycled to prevent `OutOfMemoryError`.
- **Scoped Storage:** Fully compliant with Android 11+ storage requirements for secure and private file handling.
- **Thumbnail Caching:** Uses Coil for high-performance image loading and caching.

---

## 🗺 Roadmap
- [ ] Google Drive / Dropbox Cloud Sync
- [ ] Biometric App Lock (Fingerprint/FaceID)
- [ ] Digital Signature & Watermarking
- [ ] Handwriting Recognition support
- [ ] Multi-language OCR support

---

## 🤝 Contributing

Contributions make the open-source community an amazing place to learn, inspire, and create.
1. Fork the Project
2. Create your Feature Branch (`git checkout -b feature/AmazingFeature`)
3. Commit your Changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the Branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

---

## 📄 License

Distributed under the MIT License. See `LICENSE` for more information.

---

## 📧 Contact

**Vikas** - Lead Architect  
Project Link: [https://github.com/yourusername/ScanFlow](https://github.com/yourusername/ScanFlow)  
Email: `vikas@example.com`

---
*Built with ❤️ using Jetpack Compose & Clean Architecture*
