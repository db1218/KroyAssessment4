package com.mozarellabytes.kroy.GUI;

import com.badlogic.gdx.math.Rectangle;

/**
 * Dark gray background segments under the stats
 * area in the top left corner. Used to make it easier
 * to create new elements
 */
public class GUIElement {

    /**
     * Background size of the element (dark gray)
     */
    private final Rectangle background;

    /**
     * Text displayed in the box
     */
    private String text;

    /**
     * Constructor for an Element
     * @param h height of element
     */
    public GUIElement(float h) {
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
