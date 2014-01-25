import org.bukkit.ChatColor;

import java.awt.Color;

/**
 * Represents the palette that chatcolor uses.
 */
public final class ChatColorPalette {
    // Internal mechanisms
    private ChatColorPalette() {}

    private static Color c(int r, int g, int b) {
        return new Color(r, g, b);
    }

    private static double getDistance(Color c1, Color c2) {
        double rmean = (c1.getRed() + c2.getRed()) / 2.0;
        double r = c1.getRed() - c2.getRed();
        double g = c1.getGreen() - c2.getGreen();
        int b = c1.getBlue() - c2.getBlue();
        double weightR = 2 + rmean / 256.0;
        double weightG = 4.0;
        double weightB = 2 + (255 - rmean) / 256.0;
        return weightR * r * r + weightG * g * g + weightB * b * b;
    }

    private static final Color[] colors = {
            // foreground
            c(0, 0, 0), c(0, 0, 170), c(0, 170, 0),
            c(0, 170, 170), c(170, 0, 0), c(170, 0, 170),
            c(255, 170, 0), c(170, 170, 170), c(85, 85, 85),
            c(85, 85, 255), c(85, 255, 85), c(85, 255, 255),
            c(255, 85, 85), c(255, 85, 255), c(255, 255, 85),
            c(255, 255, 255),
    };

    /**
     * A fuzzy matching function to grab colors that are very close together in RGB values
     * to allow for some slight distortion.
     *
     * @param c1
     * @param c2
     * @return if they are a match
     */
    public static boolean areIdentical(Color c1, Color c2) {
        return Math.abs(c1.getRed()-c2.getRed()) <= 5 &&
                Math.abs(c1.getGreen()-c2.getGreen()) <= 5 &&
                Math.abs(c1.getBlue()-c2.getBlue()) <= 5;

    }

    /**
     * Get the index of the closest matching color in the palette to the given
     * color.
     *
     * @param r The red component of the color.
     * @param b The blue component of the color.
     * @param g The green component of the color.
     * @return The index in the palette.
     */
    public static ChatColor matchColor(int r, int g, int b) {
        return matchColor(new Color(r, g, b));
    }

    /**
     * Get the index of the closest matching color in the palette to the given
     * color.
     *
     * @param color The Color to match.
     * @return The ChatColor in the palette.
     */
    public static ChatColor matchColor(Color color) {
        if (color.getAlpha() < 128) return ChatColor.BLACK;

        int index = 0;
        double best = -1;

        for(int i = 0; i < colors.length; i++) {
            if(areIdentical(colors[i], color)) {
                return ChatColor.values()[i];
            }
        }

        for (int i = 0; i < colors.length; i++) {
            double distance = getDistance(color, colors[i]);
            if (distance < best || best == -1) {
                best = distance;
                index = i;
            }
        }

        // Minecraft has 15 colors
        return ChatColor.values()[index];
    }
}
