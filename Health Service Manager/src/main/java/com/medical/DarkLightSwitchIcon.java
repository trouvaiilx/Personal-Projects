/**
 * @author : Yuuji
 * DarkLightSwitchIcon.java
 * Latest Update: 12-25-2024
 */

package com.medical;

// Import necessary classes for graphics and UI
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;

import javax.swing.AbstractButton;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JComponent;

import com.formdev.flatlaf.ui.FlatUIUtils;
import com.formdev.flatlaf.util.AnimatedIcon;
import com.formdev.flatlaf.util.ColorFunctions;
import com.formdev.flatlaf.util.UIScale;

/**
 * Represents an animated icon that switches between
 * dark and light themes.
 */
public class DarkLightSwitchIcon implements AnimatedIcon {

    // The gap between icons.
    private final int iconGap = 3;

    // The dark icon representation.
    private final Icon darkIcon = resizeIcon(
        new ImageIcon("src/main/resources/dark.png")
    );

    // The light icon representation.
    private final Icon lightIcon = resizeIcon(
        new ImageIcon("src/main/resources/light.png")
    );

    // The color used for the dark theme.
    private final Color darkColor = new Color(80, 80, 80);

    // The color used for the light theme.
    private final Color lightColor = new Color(230, 230, 230);

    /**
     * Returns the border arc for the specified component.
     *
     * @param com the component to get the border arc for
     * @return the border arc
     */
    private float getBorderArc(Component com) {
        return FlatUIUtils.getBorderArc((JComponent) com);
    }

    /**
     * Returns the duration of the animation in milliseconds.
     *
     * @return the animation duration
     */
    @Override
    public int getAnimationDuration() {
        return 500;
    }

    /**
     * Paints the animated icon for switching between
     * dark and light themes.
     *
     * @param c the component to paint on
     * @param g the graphics context
     * @param x the x coordinate for the icon
     * @param y the y coordinate for the icon
     * @param animatedValue the value representing the animation
     * progress (0.0 to 1.0)
     */
    @Override
    public void paintIconAnimated(
        Component c,
        Graphics g,
        int x,
        int y,
        float animatedValue
    ) {
        Graphics2D g2 = (Graphics2D) g.create();
        FlatUIUtils.setRenderingHints(g2);
        Color color = ColorFunctions.mix(
            darkColor,
            lightColor,
            animatedValue
        );
        int size = getIconHeight();
        int width = getIconWidth();
        float arc = Math.min(getBorderArc(c), size);
        float animatedX = x + (width - size) * animatedValue;

        g2.setColor(color);
        g2.fill(new RoundRectangle2D.Double(
            animatedX,
            y,
            size,
            size,
            arc,
            arc
        ));
        float darkY = (y - size + (animatedValue * size));
        float lightY = y + animatedValue * size;
        g2.setClip(new Rectangle(x, y, width, size));
        paintIcon(
            c,
            (Graphics2D) g2.create(),
            animatedX,
            darkY,
            darkIcon,
            animatedValue
        );
        paintIcon(
            c,
            (Graphics2D) g2.create(),
            animatedX,
            lightY,
            lightIcon,
            1f - animatedValue
        );
        g2.dispose();
    }

    /**
     * Paints the specified icon at the given coordinates
     * with the specified alpha.
     *
     * @param c the component to paint on
     * @param g the graphics context
     * @param x the x coordinate
     * @param y the y coordinate
     * @param icon the icon to paint
     * @param alpha the alpha transparency value
     */
    private void paintIcon(
        Component c,
        Graphics2D g,
        float x,
        float y,
        Icon icon,
        float alpha
    ) {
        int gap = UIScale.scale(iconGap);
        g.translate(x, y);
        g.setComposite(AlphaComposite.SrcOver.derive(alpha));
        icon.paintIcon(c, g, gap, gap);
        g.dispose();
    }

    /**
     * Returns the value representing the selection state of
     * the component.
     *
     * @param c the component to check
     * @return 1 if the component is selected, otherwise 0
     */
    @Override
    public float getValue(Component c) {
        return ((AbstractButton) c).isSelected() ? 1 : 0;
    }

    /**
     * Returns the width of the icon, calculated based on the
     * dark and light icons.
     *
     * @return the total width of the icon
     */
    @Override
    public int getIconWidth() {
        // The space between the center icons.
        int centerSpace = 5;
        return darkIcon.getIconWidth()
            + lightIcon.getIconWidth()
            + UIScale.scale(centerSpace)
            + UIScale.scale(iconGap) * 4;
    }

    /**
     * Returns the height of the icon, calculated based on the
     * dark and light icons.
     *
     * @return the maximum height of the icon
     */
    @Override
    public int getIconHeight() {
        return Math.max(darkIcon.getIconHeight(),
                lightIcon.getIconHeight())
            + UIScale.scale(iconGap) * 2;
    }

    /**
     * Resizes the given icon to the specified width and height.
     *
     * @param icon the icon to resize
     * @return the resized icon
     */
    private Icon resizeIcon(Icon icon) {
        BufferedImage resizedImage = new BufferedImage(
                20,
                20,
            BufferedImage.TYPE_INT_ARGB
        );
        Graphics2D g2d = resizedImage.createGraphics();

        g2d.drawImage(
            ((ImageIcon) icon).getImage(),
            0,
            0,
                20,
                20,
            null
        );
        g2d.dispose();

        return new ImageIcon(resizedImage);
    }
}
