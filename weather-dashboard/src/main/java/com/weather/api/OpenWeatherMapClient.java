package com.weather.api;

import com.weather.model.Weather;
import com.weather.model.Forecast;
import com.weather.model.Location;
import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

/**
 * OpenWeatherMap API client implementation.
 * Free API endpoint: api.openweathermap.org
 */
public class OpenWeatherMapClient implements WeatherApiClient {
    private static final String BASE_URL = "https://api.openweathermap.org";
    private static final String CURRENT_WEATHER_PATH = "/data/2.5/weather";
    private static final String FORECAST_PATH = "/data/2.5/forecast";
    private static final String GEO_PATH = "/geo/1.0/direct";
    
    private String apiKey;
    private final HttpClient httpClient = HttpClient.newHttpClient();
    private String units = "metric";

    public OpenWeatherMapClient() {}

    public OpenWeatherMapClient(String apiKey) {
        this.apiKey = apiKey;
    }

    @Override
    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public void setUnits(String units) {
        this.units = units;
    }

    @Override
    public Location getLocation(String cityName) throws Exception {
        if (apiKey == null || apiKey.isEmpty()) {
            throw new IllegalArgumentException("API key not set");
        }
        String url = String.format("%s%s?q=%s&limit=1&appid=%s",
                BASE_URL, GEO_PATH, cityName.replace(" ", "%20"), apiKey);
        
        HttpRequest request = HttpRequest.newBuilder().uri(new URI(url)).build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        
        if (response.statusCode() != 200) {
            throw new Exception("Geocoding failed: " + response.statusCode() + " - " + response.body());
        }
        
        JSONArray jsonArray = new JSONArray(response.body());
        if (jsonArray.length() == 0) {
            throw new Exception("City not found: " + cityName);
        }
        
        JSONObject obj = jsonArray.getJSONObject(0);
        Location location = new Location();
        location.setCity(obj.getString("name"));
        location.setCountry(obj.optString("country", "Unknown"));
        location.setLatitude(obj.getDouble("lat"));
        location.setLongitude(obj.getDouble("lon"));
        
        return location;
    }

    @Override
    public Weather getCurrentWeather(String cityName) throws Exception {
        Location location = getLocation(cityName);
        return getCurrentWeatherByCoords(location.getLatitude(), location.getLongitude());
    }

    @Override
    public Weather getCurrentWeatherByCoords(double latitude, double longitude) throws Exception {
        if (apiKey == null || apiKey.isEmpty()) {
            throw new IllegalArgumentException("API key not set");
        }
        
        String url = String.format("%s%s?lat=%.4f&lon=%.4f&units=%s&appid=%s",
                BASE_URL, CURRENT_WEATHER_PATH, latitude, longitude, units, apiKey);
        
        HttpRequest request = HttpRequest.newBuilder().uri(new URI(url)).build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        
        if (response.statusCode() != 200) {
            throw new Exception("Weather fetch failed: " + response.statusCode());
        }
        
        JSONObject json = new JSONObject(response.body());
        Weather weather = parseWeatherJson(json);
        
        return weather;
    }

    @Override
    public List<Forecast> getForecast(String cityName) throws Exception {
        Location location = getLocation(cityName);
        
        if (apiKey == null || apiKey.isEmpty()) {
            throw new IllegalArgumentException("API key not set");
        }
        
        String url = String.format("%s%s?lat=%.4f&lon=%.4f&units=%s&appid=%s",
                BASE_URL, FORECAST_PATH, location.getLatitude(), location.getLongitude(), units, apiKey);
        
        HttpRequest request = HttpRequest.newBuilder().uri(new URI(url)).build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        
        if (response.statusCode() != 200) {
            throw new Exception("Forecast fetch failed: " + response.statusCode());
        }
        
        JSONObject json = new JSONObject(response.body());
        List<Forecast> forecasts = new ArrayList<>();
        
        JSONArray list = json.getJSONArray("list");
        for (int i = 0; i < list.length(); i++) {
            JSONObject item = list.getJSONObject(i);
            Forecast forecast = parseForecastJson(item);
            forecasts.add(forecast);
        }
        
        return forecasts;
    }

    @Override
    public boolean isAvailable() {
        try {
            String url = BASE_URL + "/data/2.5/weather?q=London&appid=test";
            HttpRequest request = HttpRequest.newBuilder().uri(new URI(url)).build();
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            return response.statusCode() < 500;
        } catch (Exception e) {
            return false;
        }
    }

    private Weather parseWeatherJson(JSONObject json) {
        Weather weather = new Weather();
        
        weather.setCity(json.getString("name"));
        weather.setCountry(json.optString("country", ""));
        
        JSONObject coord = json.getJSONObject("coord");
        weather.setLatitude(coord.getDouble("lat"));
        weather.setLongitude(coord.getDouble("lon"));
        
        JSONObject main = json.getJSONObject("main");
        weather.setTemperature(main.getDouble("temp"));
        weather.setFeelsLike(main.getDouble("feels_like"));
        weather.setTempMin(main.getDouble("temp_min"));
        weather.setTempMax(main.getDouble("temp_max"));
        weather.setHumidity(main.getInt("humidity"));
        weather.setPressure(main.getInt("pressure"));
        
        JSONObject wind = json.optJSONObject("wind");
        if (wind != null) {
            weather.setWindSpeed(wind.getDouble("speed"));
            weather.setWindDegree(wind.optInt("deg", 0));
        }
        
        JSONObject clouds = json.optJSONObject("clouds");
        if (clouds != null) {
            weather.setCloudiness(clouds.getInt("all"));
        }
        
        JSONArray weatherArray = json.getJSONArray("weather");
        if (weatherArray.length() > 0) {
            JSONObject w = weatherArray.getJSONObject(0);
            weather.setDescription(w.getString("description"));
            weather.setIcon(w.getString("icon"));
        }
        
        long dt = json.getLong("dt");
        weather.setTimestamp(LocalDateTime.ofInstant(Instant.ofEpochSecond(dt), ZoneId.systemDefault()));
        
        if (json.has("sys")) {
            JSONObject sys = json.getJSONObject("sys");
            long sunrise = sys.getLong("sunrise");
            long sunset = sys.getLong("sunset");
            weather.setSunrise(formatTime(sunrise));
            weather.setSunset(formatTime(sunset));
        }
        
        return weather;
    }

    private Forecast parseForecastJson(JSONObject json) {
        Forecast forecast = new Forecast();
        
        long dt = json.getLong("dt");
        forecast.setDateTime(LocalDateTime.ofInstant(Instant.ofEpochSecond(dt), ZoneId.systemDefault()));
        
        JSONObject main = json.getJSONObject("main");
        forecast.setTemperature(main.getDouble("temp"));
        forecast.setTempMin(main.getDouble("temp_min"));
        forecast.setTempMax(main.getDouble("temp_max"));
        forecast.setHumidity(main.getInt("humidity"));
        forecast.setPressure(main.getInt("pressure"));
        
        JSONObject wind = json.optJSONObject("wind");
        if (wind != null) {
            forecast.setWindSpeed(wind.getDouble("speed"));
        }
        
        JSONObject clouds = json.optJSONObject("clouds");
        if (clouds != null) {
            forecast.setCloudiness(clouds.getInt("all"));
        }
        
        JSONArray weatherArray = json.getJSONArray("weather");
        if (weatherArray.length() > 0) {
            JSONObject w = weatherArray.getJSONObject(0);
            forecast.setDescription(w.getString("description"));
            forecast.setIcon(w.getString("icon"));
        }
        
        if (json.has("rain")) {
            JSONObject rain = json.getJSONObject("rain");
            forecast.setPrecipitation(rain.optDouble("3h", 0.0));
        }
        
        return forecast;
    }

    private String formatTime(long epochSeconds) {
        LocalDateTime dt = LocalDateTime.ofInstant(Instant.ofEpochSecond(epochSeconds), ZoneId.systemDefault());
        return String.format("%02d:%02d", dt.getHour(), dt.getMinute());
    }
}
