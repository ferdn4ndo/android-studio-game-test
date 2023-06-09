package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.objects.PsicodelicFloor;

/**
 * Created by Rodrigo on 04/06/2015.
 */
public class Level {
    public static final String TAG = Level.class.getName();

    public enum BLOCK_TYPE {
        EMPTY(255, 255, 255), // white
        PLAYER_SPAWNPOINT(0, 255, 0), // green
        FLOOR(255, 0, 0); // red

        private int color;

        BLOCK_TYPE(int r, int g, int b) {
            color = r << 24 | g << 16 | b << 8 | 0xff;
        }

        public boolean sameColor(int color) {
            return this.color == color;
        }

        public int getColor() {
            return color;
        }
    }

    // objects
    public Array<PsicodelicFloor> floors;

    // decoration
    //TODO decoration files

    public Level (String filename) {
        init(filename);
    }

    private void init (String filename) {
        // objects
        floors = new Array<PsicodelicFloor>();
        // load image file that represents the level data
        Pixmap pixmap = new Pixmap(Gdx.files.internal(filename));
        // scan pixels from top-left to bottom-right
        int lastPixel = -1;
        for (int pixelY = 0; pixelY < pixmap.getHeight(); pixelY++) {
            for (int pixelX = 0; pixelX < pixmap.getWidth(); pixelX++) {
                AbstractGameObject obj = null;
                float offsetHeight = 0;
                // height grows from bottom to top
                float baseHeight = pixmap.getHeight() - pixelY;
                // get color of current pixel as 32-bit RGBA value
                int currentPixel = pixmap.getPixel(pixelX, pixelY);
            // find matching color value to identify block type at (x,y)
            // point and create the corresponding game object if there is
            // a match
            // empty space
                if (BLOCK_TYPE.EMPTY.sameColor(currentPixel)) {
                    // do nothing
                }
            // floor
                else if (BLOCK_TYPE.FLOOR.sameColor(currentPixel)) {
                    if (lastPixel != currentPixel) {
                        obj = new PsicodelicFloor();
                        float heightIncreaseFactor = 0.25f;
                        offsetHeight = -2.5f;
                        obj.position.set(pixelX, baseHeight * obj.dimension.y
                                * heightIncreaseFactor + offsetHeight);
                        floors.add((PsicodelicFloor) obj);
                    } else {
                        floors.get(floors.size - 1).increaseLength(1);
                    }
                }
                // player spawn point
                else if
                        (BLOCK_TYPE.PLAYER_SPAWNPOINT.sameColor(currentPixel)) {
                }

                // unknown object/pixel color
                else {
                    int r = 0xff & (currentPixel >>> 24); //red color channel
                    int g = 0xff & (currentPixel >>> 16); //green color channel
                    int b = 0xff & (currentPixel >>> 8); //blue color channel
                    int a = 0xff & currentPixel; //alpha channel
                    Gdx.app.error(TAG, "Unknown object at x<" + pixelX + "> y<"
                            + pixelY + ">: r<" + r+ "> g<" + g + "> b<" + b + "> a<" + a + ">");
                }
                lastPixel = currentPixel;
            }
        }
        // free memory
        pixmap.dispose();
        Gdx.app.debug(TAG, "level '" + filename + "' loaded");
    }
    public void render (SpriteBatch batch) {
        // Draw Floor
        for (PsicodelicFloor floor : floors)
            floor.render(batch);
    }
}

