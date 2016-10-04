package com.teamturtle.infinityrun.models.level;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;

import java.util.ArrayList;

/**
 * Created by ostmos on 2016-10-04.
 */
public class LevelDataHandler {

    private Json json;

    public LevelDataHandler() {
        json = new Json();
    }

    //Used to create json file
    /*public static void createLevels() {
        System.out.println("hej");
        FileHandle file = Gdx.files.internal("data/levels.json");
        Json json = new Json();
        ArrayList<LevelImpl> levels = new ArrayList<LevelImpl>();
        int[] j = {1, 2, 3};
        for(int i = 1; i <= 15; i++) {
            levels.add(new LevelImpl(i, "levels/level" + i % 5 + ".tmx", j));
        }
        String text = json.prettyPrint(levels);
        FileHandle jsonfile = Gdx.files.local("data/levels.json");
        jsonfile.writeString(text, true);
        System.out.println("hej2");
    }*/

    public ArrayList<Level> getLevels() {
        FileHandle file = Gdx.files.local("data/levels.json");
        ArrayList<JsonValue> jsonValues = json.fromJson(ArrayList.class, file);
        System.out.println(jsonValues.size());
        ArrayList<Level> levels = json.fromJson(ArrayList.class, LevelImpl.class, file.readString());
        return levels;
    }

    public Level getLevel(int id) {
        for (Level level : getLevels()) {
            if (level.getId() == id) {
                return level;
            }
        }
        return null;
    }
}
