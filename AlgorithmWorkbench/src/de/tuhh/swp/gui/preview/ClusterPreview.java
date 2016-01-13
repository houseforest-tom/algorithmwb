package de.tuhh.swp.gui.preview;

import de.tuhh.swp.algorithm.KMean;
import de.tuhh.swp.gui.panel.NumPadPanel;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Created by Tom on 13.01.2016.
 */
public class ClusterPreview extends ImagePreview {

    private boolean hovered;
    private KMean.KMeanCluster cluster;

    public ClusterPreview(KMean.KMeanCluster cluster) {
        super(cluster, 140);
        this.hovered = false;
        this.cluster = cluster;

        addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                hovered = true;
            }

            public void mouseExited(MouseEvent e) {
                hovered = false;
            }
        });

        addActionListener((ActionEvent e) -> onClick());
    }

    private void onClick() {
        JFrame assignFrame = new JFrame("Assign Label");
        assignFrame.setLayout(new MigLayout());
        assignFrame.setLocationRelativeTo(this);
        ImagePreview preview = new ImagePreview(this.cluster, this.getSize().width);
        NumPadPanel numpad = new NumPadPanel();
        numpad.setSelection("" + cluster.getLabel());
        JButton applyButton = new JButton("Assign");
        assignFrame.add(preview);
        assignFrame.add(numpad, "wrap 16");
        assignFrame.add(applyButton, "span 2, growx");
        assignFrame.setFocusable(true);
        assignFrame.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                int code = e.getKeyCode();
                if (code >= KeyEvent.VK_NUMPAD0 && code <= KeyEvent.VK_NUMPAD9) {
                    int num = code - KeyEvent.VK_NUMPAD0;
                    numpad.setSelection("" + num);
                } else if (code == KeyEvent.VK_ENTER) {
                    applyButton.doClick();
                }
            }
        });
        applyButton.addActionListener((ActionEvent e) -> {
            assignFrame.dispatchEvent(new WindowEvent(assignFrame, WindowEvent.WINDOW_CLOSING));
            byte label = Byte.valueOf(numpad.getSelection());
            cluster.setLabel(label);
        });
        assignFrame.pack();
        assignFrame.setVisible(true);
    }

    @Override
    public void paintComponent(Graphics g) {
        if (cluster != null) {
            int pixel;
            int imageWidth = cluster.getDefinition().width;
            int imageHeight = cluster.getDefinition().height;
            int xScale = (int) ((float) getWidth() / (float) imageWidth);
            int yScale = (int) ((float) getHeight() / (float) imageHeight);
            int x, y;
            Color color;
            for (y = 0; y < imageHeight; ++y) {
                for (x = 0; x < imageWidth; ++x) {
                    pixel = (int) cluster.getPixel(x, y);
                    color = new Color(pixel, pixel, pixel);
                    if (hovered && pixel >= 16) {
                        color = multiplyColors(color, new Color(0x64, 0x95, 0xed));
                    } else if (cluster.getLabel() != (byte) 0xff) {
                        color = multiplyColors(color, new Color(0xbf, 0xff, 0x00));
                    }
                    g.setColor(color);
                    g.fillRect(x * xScale, y * yScale, xScale, yScale);
                }
            }
        }
    }
}
