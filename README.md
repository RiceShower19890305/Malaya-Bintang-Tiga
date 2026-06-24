# Malaya Bintang Tiga - Production Build

A modular, system-first educational grand strategy game inspired by HOI4.

## What's Included

- **Modular Architecture**: Services for images, audio, achievements, settings, and more (dependency injection via ServiceRegistry)
- **GameEngine**: Core game loop with state management
- **Swing UI**: Simple desktop interface for gameplay
- **Maven Build**: Automatic compilation and packaging

## Quick Start

### Option 1: Download & Run the Prebuilt JAR (Easiest)

1. Go to [Releases](../../releases)
2. Download `malaya-bintang-tiga-1.0-SNAPSHOT.jar`
3. Double-click to run (requires Java 11+)

```bash
java -Xmx2048m -jar malaya-bintang-tiga-1.0-SNAPSHOT.jar
```

### Option 2: Build from Source (Requires Maven & Java 11+)

1. Clone this repo:
   ```bash
   git clone https://github.com/Umakun12/malaya-bintang-tiga.git
   cd malaya-bintang-tiga
   ```

2. Build the JAR:
   ```bash
   mvn clean package
   ```

3. Run it:
   ```bash
   java -Xmx2048m -jar target/malaya-bintang-tiga-1.0-SNAPSHOT.jar
   ```

## Project Structure

```
src/main/java/
├── com/tanmedia/mbt/
│   ├── di/              (Dependency Injection)
│   │   └── ServiceRegistry.java
│   ├── core/            (Game engine & state)
│   │   ├── GameEngine.java
│   │   └── GameState.java
│   ├── assets/          (Image loading)
│   │   ├── ImageService.java (interface)
│   │   └── ImageAssetManager.java
│   ├── audio/           (Audio playback)
│   │   ├── AudioService.java
│   │   └── AudioManager.java
│   ├── achievements/    (Tracking achievements)
│   │   ├── AchievementService.java
│   │   └── AchievementTracker.java
│   ├── settings/        (Configuration)
│   │   ├── SettingsService.java
│   │   └── DefaultSettings.java
│   ├── anim/            (Animations)
│   │   └── AnimationService.java
│   └── access/          (Accessibility)
│       └── AccessibilityService.java
└── MalayaBintangTiga_Production.java  (Entry point)

assets/
├── images/   (Put .png/.jpg images here)
└── audio/    (Put .wav audio files here)
```

## Features (Current)

- ✅ Modular service architecture
- ✅ Game state & month advancement
- ✅ Basic achievement tracking
- ✅ Image asset loading (PNG/JPG)
- ✅ Audio playback (WAV)
- ✅ Swing UI with real-time updates
- 📋 (Upcoming) Full gameplay mechanics, diplomatic AI, production simulation

## Adding Assets

1. **Images**: Place `.png` or `.jpg` files in `assets/images/`
   - Example: `assets/images/malan_command_post.jpg`

2. **Audio**: Place `.wav` files in `assets/audio/`
   - Example: `assets/audio/click.wav`, `assets/audio/background_loop.wav`

The game will automatically load them when it starts.

## Requirements

- Java 11 or later (JDK or JRE)
- Maven 3.6+ (only for building from source)

## License

Educational project. Use freely for learning.

## Contributing

Contributions welcome! Feel free to fork and submit pull requests.
