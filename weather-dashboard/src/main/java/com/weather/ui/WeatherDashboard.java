package com.weather.ui;

import com.weather.api.OpenWeatherMapClient;
import com.weather.model.Weather;
import com.weather.model.Forecast;
import com.weather.service.*;
import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * Main weather dashboard application window.
 */
public class WeatherDashboard extends JFrame {
    private final ConfigService configService = new ConfigService();
    private final OpenWeatherMapClient apiClient = new OpenWeatherMapClient();
    private final WeatherService weatherService = new CachedWeatherService(apiClient);
    private final FavoritesService favoritesService = new FavoritesService(configService);
    private final AlertService alertService = new AlertService(configService);
    private final HistoryService historyService = new HistoryService();
    private final ThemeManager themeManager = new ThemeManager();
    
    private JTextField searchField;
    private JButton searchButton;
    private JButton refreshButton;
    private JButton addFavoriteButton;
    private JTabbedPane tabbedPane;
    private WeatherPanel currentWeatherPanel;
    private ForecastPanel forecastPanel;
    private FavoritesPanel favoritesPanel;
    private AlertsPanel alertsPanel;
    private HistoryPanel historyPanel;
    private JLabel statusLabel;
    private String currentCity = "London";

    public WeatherDashboard() {
        setTitle("Weather Dashboard");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 800);
        setLocationRelativeTo(null);
        setResizable(true);
        
        loadConfig();
        applyTheme();
        initializeUI();
        loadWeather(currentCity);
    }

    private void initializeUI() {
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        topPanel.setBackground(themeManager.getAlternateBackgroundColor());
        
        topPanel.add(new JLabel("City:"));
        searchField = new JTextField(20);
        searchField.addActionListener(e -> performSearch());
        topPanel.add(searchField);
        
        searchButton = new JButton("🔍 Search");
        searchButton.addActionListener(e -> performSearch());
        topPanel.add(searchButton);
        
        addFavoriteButton = new JButton("⭐ Add to Favorites");
        addFavoriteButton.addActionListener(e -> addToFavorites());
        topPanel.add(addFavoriteButton);
        
        refreshButton = new JButton("🔄 Refresh");
        refreshButton.addActionListener(e -> loadWeather(currentCity));
        topPanel.add(refreshButton);
        
        JButton settingsButton = new JButton("⚙️ Settings");
        settingsButton.addActionListener(e -> showSettings());
        topPanel.add(settingsButton);
        
        statusLabel = new JLabel("Loading...");
        statusLabel.setFont(new Font("Arial", Font.PLAIN, 11));
        statusLabel.setForeground(themeManager.getSecondaryTextColor());
        topPanel.add(Box.createHorizontalGlue());
        topPanel.add(statusLabel);
        
        tabbedPane = new JTabbedPane();
        tabbedPane.setBackground(themeManager.getBackgroundColor());
        tabbedPane.setForeground(themeManager.getPrimaryTextColor());
        
        currentWeatherPanel = new WeatherPanel(themeManager);
        tabbedPane.addTab("Current Weather", currentWeatherPanel);
        
        forecastPanel = new ForecastPanel(themeManager);
        tabbedPane.addTab("Forecast", forecastPanel);
        
        favoritesPanel = new FavoritesPanel(favoritesService, themeManager);
        favoritesPanel.setOnCitySelected(() -> {
            String city = favoritesPanel.getSelectedCity();
            if (city != null) {
                loadWeather(city);
                tabbedPane.setSelectedIndex(0);
            }
        });
        tabbedPane.addTab("Favorites", favoritesPanel);
        
        alertsPanel = new AlertsPanel(alertService, themeManager);
        tabbedPane.addTab("Alerts", alertsPanel);
        
        historyPanel = new HistoryPanel(historyService, themeManager);
        tabbedPane.addTab("History", historyPanel);
        
        setLayout(new BorderLayout());
        add(topPanel, BorderLayout.NORTH);
        add(tabbedPane, BorderLayout.CENTER);
    }

    private void loadConfig() {
        String apiKey = configService.get("api.key", "");
        if (apiKey.isEmpty()) {
            promptForApiKey();
        } else {
            apiClient.setApiKey(apiKey);
        }
        
        String units = configService.get("api.units", "metric");
        apiClient.setUnits(units);
        
        String theme = configService.get("theme", "light");
        themeManager.setTheme(theme.equals("dark") ? ThemeManager.Theme.DARK : ThemeManager.Theme.LIGHT);
        
        currentCity = configService.get("last.city", "London");
        searchField.setText(currentCity);
    }

    private void promptForApiKey() {
        String apiKey = JOptionPane.showInputDialog(
            this,
            "Enter your OpenWeatherMap API key:\n\nGet a free key at: https://openweathermap.org/api",
            "API Key Configuration",
            JOptionPane.QUESTION_MESSAGE
        );
        
        if (apiKey != null && !apiKey.trim().isEmpty()) {
            configService.set("api.key", apiKey.trim());
            configService.save();
            apiClient.setApiKey(apiKey.trim());
        } else {
            JOptionPane.showMessageDialog(this, "API key is required to use this app.", "Error", JOptionPane.ERROR_MESSAGE);
            System.exit(0);
        }
    }

    private void performSearch() {
        String city = searchField.getText().trim();
        if (city.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a city name.", "Input Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        loadWeather(city);
    }

    private void addToFavorites() {
        if (!currentCity.isEmpty()) {
            favoritesService.addFavorite(currentCity);
            favoritesPanel.refreshList();
            JOptionPane.showMessageDialog(this, currentCity + " added to favorites.", "Success", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void loadWeather(String city) {
        new Thread(() -> {
            try {
                statusLabel.setText("Loading weather for " + city + "...");
                
                Weather weather = weatherService.getWeather(city);
                currentCity = weather.getCity();
                searchField.setText(currentCity);
                
                List<Forecast> forecasts = weatherService.getForecast(city);
                
                alertService.checkTemperature(city, weather.getTemperature());
                alertService.checkPrecipitation(city, 0);
                alertService.checkWindSpeed(city, weather.getWindSpeed());
                
                SwingUtilities.invokeLater(() -> {
                    currentWeatherPanel.setWeather(weather);
                    forecastPanel.setForecasts(forecasts);
                    alertsPanel.refreshAlerts();
                    statusLabel.setText("Last updated: " + java.time.LocalDateTime.now().format(
                        java.time.format.DateTimeFormatter.ofPattern("HH:mm:ss")
                    ));
                    
                    configService.set("last.city", currentCity);
                    configService.save();
                });
            } catch (Exception ex) {
                SwingUtilities.invokeLater(() -> {
                    statusLabel.setText("Error: " + ex.getMessage());
                    JOptionPane.showMessageDialog(WeatherDashboard.this, 
                        "Error loading weather: " + ex.getMessage(), 
                        "Weather Fetch Error", 
                        JOptionPane.ERROR_MESSAGE);
                });
            }
        }).start();
    }

    private void showSettings() {
        JPanel settingsPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        settingsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JLabel unitsLabel = new JLabel("Temperature Units:");
        String[] unitOptions = {"Metric (°C)", "Imperial (°F)"};
        JComboBox<String> unitsCombo = new JComboBox<>(unitOptions);
        String currentUnits = configService.get("api.units", "metric");
        unitsCombo.setSelectedIndex(currentUnits.equals("metric") ? 0 : 1);
        
        JLabel themeLabel = new JLabel("Theme:");
        String[] themeOptions = {"Light", "Dark"};
        JComboBox<String> themeCombo = new JComboBox<>(themeOptions);
        themeCombo.setSelectedIndex(themeManager.getTheme() == ThemeManager.Theme.DARK ? 1 : 0);
        
        JLabel refreshLabel = new JLabel("Refresh Interval (seconds):");
        JSpinner refreshSpinner = new JSpinner(new SpinnerNumberModel(600, 60, 3600, 60));
        refreshSpinner.setValue(configService.getInt("refresh.interval", 600));
        
        JLabel apiKeyLabel = new JLabel("API Key:");
        JPasswordField apiKeyField = new JPasswordField(20);
        apiKeyField.setText(configService.get("api.key", ""));
        
        settingsPanel.add(unitsLabel);
        settingsPanel.add(unitsCombo);
        settingsPanel.add(themeLabel);
        settingsPanel.add(themeCombo);
        settingsPanel.add(refreshLabel);
        settingsPanel.add(refreshSpinner);
        settingsPanel.add(apiKeyLabel);
        settingsPanel.add(apiKeyField);
        
        int result = JOptionPane.showConfirmDialog(
            this,
            settingsPanel,
            "Settings",
            JOptionPane.OK_CANCEL_OPTION
        );
        
        if (result == JOptionPane.OK_OPTION) {
            String units = unitsCombo.getSelectedIndex() == 0 ? "metric" : "imperial";
            configService.set("api.units", units);
            apiClient.setUnits(units);
            
            String theme = themeCombo.getSelectedIndex() == 0 ? "light" : "dark";
            configService.set("theme", theme);
            themeManager.setTheme(theme.equals("dark") ? ThemeManager.Theme.DARK : ThemeManager.Theme.LIGHT);
            
            int refreshInterval = (Integer) refreshSpinner.getValue();
            configService.setInt("refresh.interval", refreshInterval);
            
            String apiKey = new String(apiKeyField.getPassword());
            if (!apiKey.isEmpty()) {
                configService.set("api.key", apiKey);
                apiClient.setApiKey(apiKey);
            }
            
            configService.save();
            weatherService.clearCache();
            applyTheme();
            loadWeather(currentCity);
            JOptionPane.showMessageDialog(this, "Settings saved.", "Success", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void applyTheme() {
        Color bg = themeManager.getBackgroundColor();
        Color fg = themeManager.getPrimaryTextColor();
        
        getContentPane().setBackground(bg);
        setForeground(fg);
    }
}
