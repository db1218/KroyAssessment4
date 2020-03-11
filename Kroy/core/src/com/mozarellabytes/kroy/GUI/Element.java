package com.mozarellabytes.kroy.GUI;

import com.badlogic.gdx.math.Rectangle;

public class Element {

    private final Rectangle background;
    private String text;

    public Element(float h) {
        this.background = new Rectangle(0, 0, 290, h);
        this.text = "";
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public Rectangle getBackground() {
        return background;
    }

}
