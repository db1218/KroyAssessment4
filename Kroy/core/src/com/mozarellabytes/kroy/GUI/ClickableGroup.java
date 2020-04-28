package com.mozarellabytes.kroy.GUI;

/**********************************************************************************
                             Added for assessment 4
 **********************************************************************************/

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

/** Added for assessment 4
 *
 * Used in the SaveScreen to allow the user to click
 * anywhere in the save item (including any text or
 * the picture) and the save will be selected.
 */
public class ClickableGroup extends Table {

    public ClickableGroup() {
        super();
    }

    @Override
    public Actor hit(float x, float y, boolean touchable) {
        if (touchable && this.getTouchable() != Touchable.enabled) return null;
        return x >= 0 && x < this.getWidth() && y >= 0 && y < this.getHeight() ? this : null;
    }

}
