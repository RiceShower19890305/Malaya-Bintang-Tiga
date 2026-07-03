package com.weather.ui;

import com.weather.service.FavoritesService;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Panel displaying favorite cities.
 */
public class FavoritesPanel extends JPanel {
    private final FavoritesService favoritesService;
    private final ThemeManager themeManager;
    private JList<String> favoritesList;
    private DefaultListModel<String> listModel;
    private Runnable onCitySelected;

    public FavoritesPanel(FavoritesService favoritesService, ThemeManager themeManager) {
        this.favoritesService = favoritesService;
        this.themeManager = themeManager;
        initializeUI();
    }

    private void initializeUI() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createTitledBorder("Favorite Cities"));
        applyTheme();
        
        listModel = new DefaultListModel<>();
        favoritesList = new JList<>(listModel);
        favoritesList.setFont(new Font("Arial", Font.PLAIN, 13));
        favoritesList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 1) {
                    String selected = favoritesList.getSelectedValue();
                    if (selected != null && onCitySelected != null) {
                        onCitySelected.run();
                    }
                } else if (e.getClickCount() == 2) {
                    String selected = favoritesList.getSelectedValue();
                    if (selected != null) {
                        removeFavorite(selected);
                    }
                }
            }
            
            @Override
            public void mouseReleased(MouseEvent e) {
                if (SwingUtilities.isRightMouseButton(e)) {
                    int index = favoritesList.locationToIndex(e.getPoint());
                    if (index >= 0) {
                        favoritesList.setSelectedIndex(index);
                        showContextMenu(e.getX(), e.getY());
                    }
                }
            }
        });
        
        JScrollPane scrollPane = new JScrollPane(favoritesList);
        add(scrollPane, BorderLayout.CENTER);
        
        JLabel hintLabel = new JLabel("<html>Double-click to remove | Right-click for options</html>");
        hintLabel.setFont(new Font("Arial", Font.ITALIC, 10));
        hintLabel.setForeground(themeManager.getDisabledTextColor());
        add(hintLabel, BorderLayout.SOUTH);
        
        refreshList();
    }

    public void refreshList() {
        listModel.clear();
        for (String city : favoritesService.getFavorites()) {
            listModel.addElement(city);
        }
    }

    public void removeFavorite(String city) {
        favoritesService.removeFavorite(city);
        refreshList();
    }

    public String getSelectedCity() {
        return favoritesList.getSelectedValue();
    }

    public void setOnCitySelected(Runnable callback) {
        this.onCitySelected = callback;
    }

    private void showContextMenu(int x, int y) {
        JPopupMenu menu = new JPopupMenu();
        
        JMenuItem searchItem = new JMenuItem("Search this city");
        searchItem.addActionListener(e -> {
            if (onCitySelected != null) onCitySelected.run();
        });
        menu.add(searchItem);
        
        JMenuItem removeItem = new JMenuItem("Remove from Favorites");
        removeItem.addActionListener(e -> {
            String city = favoritesList.getSelectedValue();
            if (city != null) removeFavorite(city);
        });
        menu.add(removeItem);
        
        menu.show(this, x, y);
    }

    private void applyTheme() {
        setBackground(themeManager.getBackgroundColor());
    }
}
