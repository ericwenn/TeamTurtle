package com.teamturtle.infinityrun.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.teamturtle.infinityrun.InfinityRun;
import com.teamturtle.infinityrun.PathConstants;
import com.teamturtle.infinityrun.collisions.EventHandler;
import com.teamturtle.infinityrun.collisions.IEventHandler;
import com.teamturtle.infinityrun.map_parsing.EmojiParser;
import com.teamturtle.infinityrun.map_parsing.GroundParser;
import com.teamturtle.infinityrun.map_parsing.MapParser;
import com.teamturtle.infinityrun.map_parsing.MissionParser;
import com.teamturtle.infinityrun.map_parsing.SensorParser;
import com.teamturtle.infinityrun.models.Mission;
import com.teamturtle.infinityrun.models.MissionHandler;
import com.teamturtle.infinityrun.models.level.Level;
import com.teamturtle.infinityrun.models.words.Word;
import com.teamturtle.infinityrun.models.words.WordLoader;
import com.teamturtle.infinityrun.sound.FxSound;
import com.teamturtle.infinityrun.sprites.Entity;
import com.teamturtle.infinityrun.sprites.JumpAnimations;
import com.teamturtle.infinityrun.sprites.Player;
import com.teamturtle.infinityrun.sprites.PlayerTail;
import com.teamturtle.infinityrun.sprites.emoji.Emoji;
import com.teamturtle.infinityrun.stages.MissionStage;
import com.teamturtle.infinityrun.stages.ProgressBarStage;
import com.teamturtle.infinityrun.stages.pause.IPauseStageHandler;
import com.teamturtle.infinityrun.stages.pause.PauseButtonStage;
import com.teamturtle.infinityrun.stages.pause.PauseStage;
import com.teamturtle.infinityrun.storage.PlayerData;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by ericwenn on 9/20/16.
 */

public class GameScreen extends AbstractScreen implements IPauseStageHandler {

    private enum State {
        PLAY, PAUSE, LOST_GAME, WON_GAME
    }

    private static final float GRAVITY = -10;
    private static final int pBtnXMax = 400, pBtnXMin = 365, pBtnYMax = 240, pBtnYMin = 193;

    private Texture bg, mountains, trees;
    private float mountainsPos1, mountainsPos2;
    private float treePos1, treePos2;
    private float oldCamX;
    private boolean isSendingToQuiz = false;
    private static final float TREE_PARALLAX_FACTOR = 1.6f, MOUNTAINS_PARALLAX_FACTOR = 1.2f;

    private FillViewport mFillViewport;
    private FillViewport touchViewport;

    private Player mPlayer;
    private PlayerTail mPlayerTail;
    private EventHandler mEventHandler;

    private final TiledMap tiledMap;
    private OrthogonalTiledMapRenderer tiledMapRenderer;

    private World world;
    // TODO remove before publish
//    private Box2DDebugRenderer b2dr;

    private List<? extends Entity> emojiSprites;
    private final IScreenObserver screenObserver;

    private MissionHandler mMissionHandler;
    private MissionStage mMissionStage;
    private ProgressBarStage mProgressStage;

    private PauseStage pauseStage;
    private PauseButtonStage pauseButtonStage;

    private State state;

    private OrthographicCamera mFixedCamera;
    private final List<Word> possibleWords;
    private final List<Word> discoverdWords;
    private final List<Word> oldWords;
    private final WordLoader wordLoader;

    private final Level level;
    private Mission activeMission;
    private boolean hasSuccededInAllMissions = true;

    private final JumpAnimations mJumpAnimations;

    private final PlayerData playerData;
    private List<Emoji> drawBigEmojiList;

    public static final Color SUCCESS_COLOR = new Color((float) 50 / 255, (float) 205 / 255, (float) 50 / 255, 1);
    public static final Color FAILURE_COLOR = new Color((float) 194 / 255, (float) 59 / 255, (float) 34 / 255, 1);
    public static final Color NEUTRAL_PLAYER_COLOR = new Color((float) 240 / 255, (float) 213 / 255, (float) 0 / 255, 1);

    public GameScreen(SpriteBatch mSpriteBatch, IScreenObserver screenObserver, Level level) {
        super(mSpriteBatch);
        this.screenObserver = screenObserver;

        this.level = level;

        //Load tilemap
        TmxMapLoader tmxMapLoader = new TmxMapLoader();
        tiledMap = tmxMapLoader.load(level.getMapUrl());

        //TODO: Move WordLoader to Level
        wordLoader = new WordLoader();

        int[] cats = level.getCategoryIDs();

        possibleWords = new ArrayList<Word>();

        for (int i = 0; i < cats.length; i++) {
            possibleWords.addAll(wordLoader.getWordsFromCategory(cats[i]));
        }
        oldWords = new ArrayList<Word>();
        discoverdWords = new ArrayList<Word>();

        playerData = new PlayerData();
        mJumpAnimations = new JumpAnimations();

        Gdx.input.setInputProcessor(this);
    }

    @Override
    public void show() {
        state = State.PLAY;

        mMissionStage = new MissionStage();

        pauseButtonStage = new PauseButtonStage();

        pauseStage = new PauseStage(this, screenObserver, level);

        // FillViewport "letterboxing"
        this.mFillViewport = new FillViewport(InfinityRun.WIDTH / InfinityRun.PPM
                , InfinityRun.HEIGHT / InfinityRun.PPM);

        //Used to transform touch input position for diffrent screen sizes
        touchViewport = new FillViewport(InfinityRun.WIDTH, InfinityRun.HEIGHT);

        // Init background from file and setup starting positions to have continous background.
        bg = new Texture(PathConstants.BACKGROUND_PATH);
        mountains = new Texture(PathConstants.MOUNTAINS_PATH);
        trees = new Texture(PathConstants.TREE_PATH);

//        Creates position of mountain and tree texture
        mountainsPos1 = 0;
        mountainsPos2 = mountains.getWidth() / InfinityRun.PPM;
        treePos1 = 0;
        treePos2 = trees.getWidth() / InfinityRun.PPM;

        oldCamX = getOrthoCam().position.x;

//        Creates a new camera for our static background
        mFixedCamera = new OrthographicCamera(mFillViewport.getWorldWidth(), mFillViewport.getWorldHeight());
        mFixedCamera.position.set(mFillViewport.getWorldWidth() / 2,
                mFillViewport.getWorldHeight() / 2,
                0);

        // TODO Remove this before production
//        b2dr = new Box2DDebugRenderer();

        tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap, 1 / InfinityRun.PPM);

        //Creates box2d world and enteties
        setUpWorld();

        // Create EventHandler with actions. Still not connected to the world.
        setupEventHandler();

        world.setContactListener(mEventHandler);
        drawBigEmojiList = new ArrayList<Emoji>();

        mProgressStage = new ProgressBarStage(tiledMap, mMissionHandler.getMissions());

        activeMission = mMissionHandler.getNextMission();
        new Timer().scheduleTask(new Timer.Task() {
            @Override
            public void run() {
                FxSound.KOR.play();
            }
        }, 0.6f);
    }

    private void gameUpdate(float delta) {
        handleInput();
        world.step(1 / 60f, 6, 2);
        mPlayer.update(delta);

        for (Entity entity : emojiSprites) {
            entity.update(delta);
        }

        mPlayerTail.update(delta);

        mJumpAnimations.update(delta);
        mProgressStage.updatePlayerProgress(mPlayer.getX() * InfinityRun.PPM);
    }


    @Override
    public void render(float delta) {
        switch (state) {
            case PLAY:
                gameUpdate(delta);
                renderWorld();
                mMissionStage.draw();
                pauseButtonStage.draw();
                mProgressStage.draw();
                break;
            case LOST_GAME:
                screenObserver.levelFailed(level);
                break;
            case WON_GAME:
                gameUpdate(delta);
                renderWorld();
                mPlayer.setScale(.9f);
                mPlayerTail.setScale(.9f);
                if (!isSendingToQuiz) {
                    Timer timer = new Timer();
                    timer.scheduleTask(new Timer.Task() {
                        @Override
                        public void run() {
                            screenObserver.levelCompleted(level, oldWords, discoverdWords, hasSuccededInAllMissions ? 2 : 1);
                        }
                    }, 1.5f);
                    isSendingToQuiz = true;
                }
                break;
            case PAUSE:
                renderWorld();
                mMissionStage.draw();
                pauseStage.draw();
                break;
            default:
                render(delta);
        }
    }

    private void renderWorld() {
        tiledMapRenderer.setView(getOrthoCam());
        Gdx.gl.glClearColor(0, 0, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

//        Draw static background
        getSpriteBatch().setProjectionMatrix(mFixedCamera.combined);
        getSpriteBatch().begin();
        getSpriteBatch().draw(bg, -getViewport().getWorldWidth() / 2,
                -getViewport().getWorldHeight() / 2,
                InfinityRun.WIDTH / InfinityRun.PPM,
                InfinityRun.HEIGHT / InfinityRun.PPM);
        getSpriteBatch().end();

//        Draw dynamic content
        getSpriteBatch().setProjectionMatrix(getCamera().combined);
        drawParallaxContent();
        tiledMapRenderer.render();
        getSpriteBatch().begin();

        mPlayerTail.render(getSpriteBatch());
        mPlayer.render(getSpriteBatch());
        for (Entity entity : emojiSprites) {
            entity.render(getSpriteBatch());
        }

        mJumpAnimations.render(getSpriteBatch());
        getSpriteBatch().end();

        getCamera().position.set(mPlayer.getX() + (mFillViewport.getWorldWidth() / 3)
                , mFillViewport.getWorldHeight() / 2, 0);
        getCamera().update();
        //Use to show collision rectangles
        //b2dr.render(world, getOrthoCam().combined);
        drawBigEmojis();
    }

    private void handleInput() {
        if ((Gdx.input.justTouched() || Gdx.input.isKeyJustPressed(Input.Keys.SPACE))) {
            Vector2 touchPos = new Vector2(Gdx.input.getX(), Gdx.input.getY());
            touchViewport.unproject(touchPos);
            if (touchPos.x > pBtnXMin && touchPos.x < pBtnXMax
                    && touchPos.y > pBtnYMin && touchPos.y < pBtnYMax) {
                pauseBtnClick();
            } else {
                if (mPlayer.tryToJump()) {
                    mJumpAnimations.createNew(mPlayer.getX() + Player.PLAYER_WIDTH / (InfinityRun.PPM * 2), mPlayer.getY() + Player.PLAYER_HEIGHT / (InfinityRun.PPM * 2));
                }
            }
        }

    }

    @Override
    public void resize(int width, int height) {
        touchViewport.update(width, height);
    }

    @Override
    public void pause() {
        pauseStage = new PauseStage(this, screenObserver, level);
        Gdx.input.setInputProcessor(pauseStage);
        state = State.PAUSE;
    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {
        world.setContactListener(null);
    }

    @Override
    public void buildStage() {

    }

    @Override
    public void dispose() {
        super.dispose();
        for (Entity ent : emojiSprites) {
            ent.dispose();
        }
//        b2dr.dispose();
        tiledMapRenderer.dispose();

        mPlayer.dispose();
        mPlayerTail.dispose();

        bg.dispose();
        mountains.dispose();
        trees.dispose();

        world.dispose();
        pauseStage.dispose();
        mProgressStage.dispose();
        mMissionStage.dispose();
        tiledMap.dispose();
        pauseButtonStage.dispose();

    }

    private void drawParallaxContent() {
        getSpriteBatch().begin();
//        Gets how much screen scrolled since last render()
        float deltaPosX = getOrthoCam().position.x - oldCamX;

//        Updates mountains and trees position, based on a parallax factor
        mountainsPos1 += (deltaPosX / MOUNTAINS_PARALLAX_FACTOR);
        mountainsPos2 += (deltaPosX / MOUNTAINS_PARALLAX_FACTOR);

        treePos1 += (deltaPosX / TREE_PARALLAX_FACTOR);
        treePos2 += (deltaPosX / TREE_PARALLAX_FACTOR);

//        Makes sure mountains and trees never stops scrolling
        if (mountainsPos1 + mountains.getWidth() / InfinityRun.PPM < getOrthoCam().position.x - getOrthoCam().viewportWidth / 2) {
            mountainsPos1 += (mountains.getWidth() * 2) / InfinityRun.PPM;
            float tmpPos = mountainsPos1;
            mountainsPos1 = mountainsPos2;
            mountainsPos2 = tmpPos;
        }
        mountainsPos2 = mountainsPos1 + (mountains.getWidth() / InfinityRun.PPM);

        if (treePos1 + trees.getWidth() / InfinityRun.PPM < getOrthoCam().position.x - getOrthoCam().viewportWidth / 2)
            treePos1 += (trees.getWidth() * 2) / InfinityRun.PPM;
        if (treePos2 + trees.getWidth() / InfinityRun.PPM < getOrthoCam().position.x - getOrthoCam().viewportWidth / 2)
            treePos2 += (trees.getWidth() * 2) / InfinityRun.PPM;

//        Renders mountains and trees
        getSpriteBatch().draw(mountains, mountainsPos1, 0, trees.getWidth() / InfinityRun.PPM, trees.getHeight() / InfinityRun.PPM);
        getSpriteBatch().draw(mountains, mountainsPos2, 0, trees.getWidth() / InfinityRun.PPM, trees.getHeight() / InfinityRun.PPM);

        getSpriteBatch().draw(trees, treePos1, 0, trees.getWidth() / InfinityRun.PPM, trees.getHeight() / InfinityRun.PPM);
        getSpriteBatch().draw(trees, treePos2, 0, trees.getWidth() / InfinityRun.PPM, trees.getHeight() / InfinityRun.PPM);

        oldCamX = getOrthoCam().position.x;
        getSpriteBatch().end();
    }

    private void drawBigEmojis() {
        for (Entity entity : emojiSprites) {
            Emoji emoji = (Emoji) entity;
            if (emoji.hasExploded() && emoji.shouldDraw() && !drawBigEmojiList.contains(emoji)) {
                drawBigEmojiList.add(emoji);
                emoji.setStartY(mPlayer.getY());
            }
        }
        for (Emoji emoji : drawBigEmojiList) {
            emoji.drawExplodedText();
        }
    }

    private void setUpWorld() {
        // Setup world with regular gravity, and sleeping bodies
        world = new World(new Vector2(0, GRAVITY), true);

        mPlayer = new Player(world);
        mPlayerTail = new PlayerTail(mPlayer);

        MapParser groundParser = new GroundParser(world, tiledMap, "ground");
        groundParser.parse();


        MissionParser missionParser = new MissionParser(tiledMap, "quest");
        mMissionHandler = missionParser.getMissionHandler();


        MapParser emojiParser = new EmojiParser(world, tiledMap, "emoji_placeholders", mMissionHandler, possibleWords);
        emojiParser.parse();
        emojiSprites = emojiParser.getEntities();

        MapParser obstacleParser = new SensorParser(world, tiledMap, SensorParser.Type.OBSTACLE);
        obstacleParser.parse();

        MapParser goalParser = new SensorParser(world, tiledMap, SensorParser.Type.GOAL);
        goalParser.parse();

        MapParser questParser = new SensorParser(world, tiledMap, SensorParser.Type.QUEST);
        questParser.parse();
    }

    private void setupEventHandler() {

        EventHandler eventHandler = new EventHandler();

        eventHandler.onCollisionWithEmoji(new IEventHandler.EmojiCollisionListener() {
            @Override
            public void onCollision(Player p, Emoji e) {
                e.triggerExplode();

                if (!activeMission.isPassed()) {
                    activeMission.markPassed();

                    if (activeMission.getCorrectWord().equals(e.getWordModel())) {
                        FxSound.RIGHT_ANSWER.play(0.1f);
                        mPlayerTail.setColor(SUCCESS_COLOR);
                        mPlayer.setColor(SUCCESS_COLOR);
                        mJumpAnimations.setColor(SUCCESS_COLOR);
                        mProgressStage.updateMissionStatus(activeMission, ProgressBarStage.MissionStatus.PASSED);
                        mMissionStage.onEmojiCollision(SUCCESS_COLOR);
                    } else {
                        FxSound.WRONG_ANSWER.play(0.3f);
                        mPlayerTail.setColor(FAILURE_COLOR);
                        mPlayer.setColor(FAILURE_COLOR);
                        mJumpAnimations.setColor(FAILURE_COLOR);
                        mProgressStage.updateMissionStatus(activeMission, ProgressBarStage.MissionStatus.FAILED);
                        mMissionStage.onEmojiCollision(FAILURE_COLOR);
                    }
                    Timer.schedule(new Timer.Task() {
                        @Override
                        public void run() {
                            mPlayerTail.setColor(NEUTRAL_PLAYER_COLOR);
                            mPlayer.setColor(NEUTRAL_PLAYER_COLOR);
                            mJumpAnimations.setColor(NEUTRAL_PLAYER_COLOR);
                        }
                    }, 2);


                    if (!activeMission.getCorrectWord().equals(e.getWordModel()) && hasSuccededInAllMissions) {
                        hasSuccededInAllMissions = false;
                    }

                    Word word = e.getWordModel();
                    if (playerData.hasPlayerCollectedWord(word)) {
                        if (!discoverdWords.contains(word) && !oldWords.contains(word))
                            oldWords.add(e.getWordModel());
                    } else {
                        discoverdWords.add(e.getWordModel());
                        playerData.playerCollectedWord(e.getWordModel());
                    }
                }

            }
        });

        eventHandler.onCollisionWithGround(new IEventHandler.GroundCollisionListener() {
            @Override
            public void onCollision(IEventHandler.HitDirection d) {
                if (d == IEventHandler.HitDirection.DOWNWARDS) {
                    mPlayer.resetJump();
                }
            }
        });

        eventHandler.onCollisionWithObstacle(new IEventHandler.ObstacleCollisionListener() {
            @Override
            public void onCollision(Player p) {
                state = State.LOST_GAME;
            }
        });

        eventHandler.onLevelFinished(new IEventHandler.LevelFinishedListener() {
            @Override
            public void onLevelFinished() {
                FxSound[] sounds = {FxSound.MALGANG1, FxSound.MALGANG2,
                        FxSound.MALGANG3, FxSound.MALGANG4, FxSound.MALGANG5,
                        FxSound.MALGANG6, FxSound.MALGANG7};
                Random rand = new Random();
                int soundId = rand.nextInt(sounds.length - 1);
                sounds[soundId].play();
                state = State.WON_GAME;
            }
        });

        eventHandler.onQuestChanged(new IEventHandler.QuestChangedListener() {
            @Override
            public void onQuestChanged() {
                try {
                    if (!activeMission.isPassed()) {
                        mProgressStage.updateMissionStatus(activeMission, ProgressBarStage.MissionStatus.FAILED);
                        boolean isFirstMission = mMissionHandler.getMissions().indexOf(activeMission) == 0;
                        if (hasSuccededInAllMissions && !isFirstMission) {
                            hasSuccededInAllMissions = false;
                        }
                    }
                    activeMission = mMissionHandler.getNextMission();
                    mMissionStage.setMission(activeMission);
                } catch (IndexOutOfBoundsException e) {
                    Gdx.app.error("GameScreen", "IndexOutOfBoundsException: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        });

        this.mEventHandler = eventHandler;
    }
    
    @Override
    public void continueBtnClick() {
        Gdx.input.setInputProcessor(this);
        state = State.PLAY;
    }

    private void pauseBtnClick() {
        pause();
    }
}
