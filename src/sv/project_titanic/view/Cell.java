package sv.project_titanic.view;

import java.awt.Color;
import java.awt.Rectangle;

class Cell extends Rectangle {
    private Color color;

    public Cell() {
        super();

        this.color = Color.BLUE;
    }

    public Color getColor() {
        return this.color;
    }

    public void setColor(Color c) {
        this.color = c;
    }
}

