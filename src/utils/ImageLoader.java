package utils;

import javax.swing.*;
import java.awt.*;
import java.io.InputStream;
import java.net.URL;

public class ImageLoader {
    
    /**
     * Load an image as ImageIcon from resources (works both in IDE and JAR)
     * @param imagePath Path relative to resources root (e.g., "img/logo.png")
     * @return ImageIcon or null if image not found
     */
    public static ImageIcon loadImage(String imagePath) {
        try {
            // Try to load from classpath resources first (works in JAR)
            URL imageUrl = ImageLoader.class.getClassLoader().getResource(imagePath);
            if (imageUrl != null) {
                return new ImageIcon(imageUrl);
            }
            
            // Fallback: try to load from file system (for development)
            String projectRoot = System.getProperty("user.dir");
            String fullPath = projectRoot + "/" + imagePath;
            ImageIcon icon = new ImageIcon(fullPath);
            
            // Check if image was loaded successfully
            if (icon.getIconWidth() > 0) {
                return icon;
            }
            
            System.err.println("Warning: Could not load image: " + imagePath);
            return null;
            
        } catch (Exception e) {
            System.err.println("Error loading image " + imagePath + ": " + e.getMessage());
            return null;
        }
    }
    
    /**
     * Load and scale an image
     * @param imagePath Path relative to resources root
     * @param width Target width
     * @param height Target height
     * @return Scaled ImageIcon or null if image not found
     */
    public static ImageIcon loadScaledImage(String imagePath, int width, int height) {
        ImageIcon originalIcon = loadImage(imagePath);
        if (originalIcon != null) {
            Image scaledImage = originalIcon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
            return new ImageIcon(scaledImage);
        }
        return null;
    }
    
    /**
     * Check if an image resource exists
     * @param imagePath Path relative to resources root
     * @return true if image exists, false otherwise
     */
    public static boolean imageExists(String imagePath) {
        URL imageUrl = ImageLoader.class.getClassLoader().getResource(imagePath);
        return imageUrl != null;
    }
} 