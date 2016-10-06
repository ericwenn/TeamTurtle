package com.teamturtle.infinityrun.tests.models.level;

import com.badlogic.gdx.Gdx;
import com.teamturtle.infinityrun.models.level.Level;
import com.teamturtle.infinityrun.models.level.LevelDataHandler;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

import de.tomgrill.gdxtesting.GdxTestRunner;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Created by Henrik on 2016-10-06.
 */
@RunWith(GdxTestRunner.class)
public class LevelDataHandlerTest {

    private static final int LEVEL_AMOUNT = 15;

    @Test
    public void levelJsonFileExists() {
        assertTrue(Gdx.files.internal("data/levels.json").exists());
    }

    @Test
    public void shouldGetLevels() {
        LevelDataHandler handler = new LevelDataHandler();
        List<Level> levels = handler.getLevels();
        assertEquals(levels.size(), LEVEL_AMOUNT);
        for (Level level : levels) {
            int[] categoryIDs = level.getCategoryIDs();
            int id = level.getId();
            String mapUrl = level.getMapUrl();
            assertNotNull(categoryIDs);
            assertNotNull(id);
            assertNotNull(mapUrl);
        }
    }

    @Test
    public void shouldGetLevelById() {
        LevelDataHandler handler = new LevelDataHandler();
        List<Level>levels = handler.getLevels();
        List<Integer> ids = new ArrayList<Integer>();
        for (Level level : levels) {
            ids.add(level.getId());
        }
        List<Level> levelsFromHandler = new ArrayList<Level>();
        for (Integer id : ids) {
            levelsFromHandler.add(handler.getLevel(id));
        }
        for(int i = 0; i < levelsFromHandler.size(); i++) {
            Level level = levelsFromHandler.get(i);
            assertEquals(level.getMapUrl(), levels.get(i).getMapUrl());
            assertArrayEquals(level.getCategoryIDs(), levels.get(i).getCategoryIDs());
            assertEquals(level.getId(), levels.get(i).getId());
        }
    }

}
