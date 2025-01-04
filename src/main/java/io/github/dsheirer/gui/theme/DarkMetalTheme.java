package io.github.dsheirer.gui.theme;
import javax.swing.plaf.ColorUIResource;
import javax.swing.plaf.metal.*;

import java.awt.*;
public class DarkMetalTheme extends DefaultMetalTheme {
    // Define custom colors for primary elements
    private final ColorUIResource primary1 = new ColorUIResource(45, 45, 45);
    private final ColorUIResource primary2 = new ColorUIResource(60, 60, 60);
    private final ColorUIResource primary3 = new ColorUIResource(75, 75, 75);

    // Define custom colors for secondary elements
    private final ColorUIResource secondary1 = new ColorUIResource(30, 30, 30);
    private final ColorUIResource secondary2 = new ColorUIResource(50, 50, 50);
    private final ColorUIResource secondary3 = new ColorUIResource(70, 70, 70);

    // Define default text and control colors
    private final ColorUIResource textColor = new ColorUIResource(220, 220, 220);
    private final ColorUIResource controlColor = new ColorUIResource(50, 50, 50);

    @Override
    protected ColorUIResource getPrimary1() {
        return primary1;
    }

    @Override
    protected ColorUIResource getPrimary2() {
        return primary2;
    }

    @Override
    protected ColorUIResource getPrimary3() {
        return primary3;
    }

    @Override
    protected ColorUIResource getSecondary1() {
        return secondary1;
    }

    @Override
    protected ColorUIResource getSecondary2() {
        return secondary2;
    }

    @Override
    protected ColorUIResource getSecondary3() {
        return secondary3;
    }

    @Override
    public ColorUIResource getControlTextColor() {
        return textColor;
    }

    @Override
    public ColorUIResource getSystemTextColor() {
        return textColor;
    }

    @Override
    public ColorUIResource getUserTextColor() {
        return textColor;
    }


    public ColorUIResource getMenuTextColor() {
        return textColor;
    }

    @Override
    public ColorUIResource getControl() {
        return controlColor;
    }

    @Override
    public ColorUIResource getWindowBackground() {
        return new ColorUIResource(40, 40, 40);
    }

    @Override
    public String getName() {
        return "Dark Metal Theme";
    }
}