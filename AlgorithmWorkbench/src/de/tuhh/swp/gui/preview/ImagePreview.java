package de.tuhh.swp.gui.preview;

import de.tuhh.swp.Workbench;
import de.tuhh.swp.image.ImageValue;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Tom on 16.12.2015.
 */
public class ImagePreview extends JButton {

    private ImageValue image;

    public ImagePreview(ImageValue image, int size) {
        this.image = image;
        Workbench.setComponentSize(this, size, size);
    }

    protected Color multiplyColors(Color a, Color b) {
        return new Color(
                a.getRed() * b.getRed() / 256,
                a.getGreen() * b.getGreen() / 256,
                a.getBlue() * b.getBlue() / 256
        );
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (image != null) {
            int pixel;
            int imageWidth = image.getDefinition().width;
            int imageHeight = image.getDefinition().height;
            int xScale = (int) ((float) getWidth() / (float) imageWidth);
            int yScale = (int) ((float) getHeight() / (float) imageHeight);
            int x, y;
            Color color;
            for (y = 0; y < imageHeight; ++y) {
                for (x = 0; x < imageWidth; ++x) {
                    pixel = (int) (255 - image.getPixel(x, y));
                    color = new Color(pixel, pixel, pixel);
                    g.setColor(color);
                    g.fillRect(x * xScale, y * yScale, xScale, yScale);
                }
            }
        }
    }

    public void setImage(ImageValue image) {
        this.image = image;
    }
}
