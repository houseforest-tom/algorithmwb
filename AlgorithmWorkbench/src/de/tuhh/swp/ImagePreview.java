package de.tuhh.swp;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Created by Tom on 16.12.2015.
 */
public class ImagePreview extends JButton {

    private ImageValue image;
    private boolean hovered;
    private boolean hoveringEnabled;
    private ActionListener actionListener;
    private MouseAdapter mouseAdapter;

    public ImagePreview(ImageValue image, int size) {
        this.image = image;
        this.hovered = false;
        this.hoveringEnabled = false;
        Workbench.setComponentSize(this, size, size);

        this.actionListener = (ActionEvent e) -> {
            onClick();
        };

        this.mouseAdapter = new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                hovered = true;
            }

            public void mouseExited(MouseEvent e) {
                hovered = false;
            }
        };
    }

    private Color multiplyColors(Color a, Color b) {
        return new Color(
                a.getRed() * b.getRed() / 256,
                a.getGreen() * b.getGreen() / 256,
                a.getBlue() * b.getBlue() / 256
        );
    }

    public void enableHovering() {
        this.hoveringEnabled = true;
        addActionListener(actionListener);
        addMouseListener(mouseAdapter);
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
                    pixel = (int) image.getPixel(x, y);
                    color = new Color(pixel, pixel, pixel);
                    if (hoveringEnabled && hovered && pixel >= 16) {
                        color = multiplyColors(color, new Color(0x64, 0x95, 0xed));
                    }else if(image.getLabel() != (byte)0xff){
                        color = multiplyColors(color, new Color(0xbf, 0xff, 0x00));
                    }
                    g.setColor(color);
                    g.fillRect(x * xScale, y * yScale, xScale, yScale);
                }
            }
        }
    }

    private void onClick() {
        JFrame assignFrame = new JFrame("Assign Label");
        assignFrame.setLayout(new FlowLayout(FlowLayout.CENTER));
        assignFrame.setLocationRelativeTo(this);
        ImagePreview preview = new ImagePreview(this.image, this.getSize().width);
        RadioButtonArray labelOptions = new RadioButtonArray(new String[]{"0", "1", "2", "3", "4", "5", "6", "7", "8", "9"});
        labelOptions.setSelection("" + image.getLabel());
        JButton applyButton = new JButton("Assign");
        assignFrame.add(preview);
        assignFrame.add(labelOptions);
        assignFrame.add(applyButton);
        applyButton.addActionListener((ActionEvent e) -> {
            assignFrame.dispatchEvent(new WindowEvent(assignFrame, WindowEvent.WINDOW_CLOSING));
            byte label = Byte.valueOf(labelOptions.getSelection());
            this.image.setLabel(label);
        });
        assignFrame.pack();
        assignFrame.setVisible(true);
    }
}
