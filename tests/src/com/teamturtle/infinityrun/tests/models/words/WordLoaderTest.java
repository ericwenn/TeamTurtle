package com.teamturtle.infinityrun.tests.models.words;

import com.badlogic.gdx.Gdx;
import com.teamturtle.infinityrun.models.words.Word;
import com.teamturtle.infinityrun.models.words.WordLoader;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import de.tomgrill.gdxtesting.GdxTestRunner;

/**
 * Created by Henrik on 2016-10-06.
 */
@RunWith(GdxTestRunner.class)
public class WordLoaderTest {

    private static final int WORD_AMOUNT = 120;

    @Test
    public void wordJsonFileExists() {
        assertTrue(Gdx.files.internal("data/words.json").exists());
    }

    @Test
    public void shouldGetWords() {
        WordLoader wordLoader = new WordLoader();
        HashMap<String, Word> words = wordLoader.getWords();
        Iterator i = words.entrySet().iterator();
        while (i.hasNext()) {
            Map.Entry set = (Map.Entry) i.next();
            assertNotNull(set.getKey());
            assertNotNull(set.getValue());
            i.remove();
        }
    }

    @Test
    public void shouldGetAllWords() {
        WordLoader wordLoader = new WordLoader();
        HashMap<String, Word> words = wordLoader.getWords();
        assertEquals(words.size(), WORD_AMOUNT);
    }

    @Test
    public void shouldGetWordsFromCategory() {

    }

}
