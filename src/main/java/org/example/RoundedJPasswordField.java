package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

public class RoundedJPasswordField extends JPasswordField {
    private Shape shape;
    public RoundedJPasswordField(int size) {
        super(size);
        setOpaque(false);
        setEditable(true);
        setEnabled(true);
    }
    protected void paintComponent(Graphics g) {
        g.setColor(getBackground());
        g.fillRoundRect(0, 0, getWidth()-1, getHeight()-1, 25, 25);
        super.paintComponent(g);
    }
    protected void paintBorder(Graphics g) {
        g.setColor(getForeground());
        g.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 25, 25);
    }
    public boolean contains(int x, int y) {
        if (shape == null || !shape.getBounds().equals(getBounds())) {
            shape = new RoundRectangle2D.Float(0, 0, getWidth()-1, getHeight()-1, 15, 15);
        }
        return shape.contains(x, y);
    }

    @Override
    public void cut() {

    }

    @Override
    public void copy() {

    }

    @Override
    public void paste() {
     super.paste();
    }
}
