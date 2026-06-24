import com.tanmedia.mbt.di.ServiceRegistry;
import com.tanmedia.mbt.assets.ImageAssetManager;
import com.tanmedia.mbt.audio.AudioManager;
import com.tanmedia.mbt.achievements.AchievementTracker;
import com.tanmedia.mbt.settings.DefaultSettings;
import com.tanmedia.mbt.core.GameEngine;

/**
 * Production main launcher (simple). Main class is in default package to match older builds.
 */
public class MalayaBintangTiga_Production {
    public static void main(String[] args) {
        // register default implementations
        ServiceRegistry.register(new ImageAssetManager());
        ServiceRegistry.register(new AudioManager());
        ServiceRegistry.register(new AchievementTracker());
        ServiceRegistry.register(new DefaultSettings());

        // create engine and show UI
        GameEngine engine = new GameEngine();
        engine.startUI();

        // optionally play background music if assets present
        try {
            if (ServiceRegistry.audio() != null) {
                ServiceRegistry.audio().playBackground("assets/audio/background_loop.wav");
            }
        } catch (Exception e) {
            System.out.println("Background music not started: " + e.getMessage());
        }
    }
}
