package de.tuhh.swp.gui.component;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Tom on 13.01.2016.
 */
public class HeadingLabel extends JLabel {
    public static String font = "arial";

    public HeadingLabel(String heading, int fontSize) {
        super(heading);
        setFont(new Font(font, Font.BOLD, fontSize));
    }
}
