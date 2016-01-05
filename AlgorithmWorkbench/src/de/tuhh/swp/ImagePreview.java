package de.tuhh.swp;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Tom on 16.12.2015.
 */
public class ImagePreview extends JPanel {

    private ImageValue image;

    public ImagePreview(ImageValue image) {
        this.image = image;
    }

    public void setImage(ImageValue image) {
        this.image = image;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (image != null) {
            int color;
            int imageWidth = image.getDefinition().width;
            int imageHeight = image.getDefinition().height;
            int xScale = (int)((float)getWidth() / (float)imageWidth);
            int yScale = (int)((float)getHeight() / (float)imageHeight);
            int x, y;
            for (y = 0; y < imageHeight; ++y) {
                for (x = 0; x < imageWidth; ++x) {
                    color = (int)image.getPixel(x, y);
                    g.setColor(new Color(color, color, color));
                    g.fillRect(x * xScale, y * yScale, xScale, yScale);
                }
            }
        }
    }
}
