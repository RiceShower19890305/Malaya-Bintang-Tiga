import com.weather.ui.WeatherDashboard;
import com.weather.ui.ThemeManager;

import javax.swing.*;

/**
 * Weather Dashboard application entry point.
 */
public class WeatherDashboardApp {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                // Use default look and feel
            }
            
            WeatherDashboard dashboard = new WeatherDashboard();
            dashboard.setVisible(true);
        });
    }
}
