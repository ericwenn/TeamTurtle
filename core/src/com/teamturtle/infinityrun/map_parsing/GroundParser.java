package com.teamturtle.infinityrun.map_parsing;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.PolygonMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.teamturtle.infinityrun.InfinityRun;
import com.teamturtle.infinityrun.sprites.Entity;

import java.util.List;

/**
 * Created by ericwenn on 9/27/16.
 */
public class GroundParser implements MapParser {

    private final World world;
    private final TiledMap tiledMap;
    private final String groundLayerName;

    public GroundParser(World world, TiledMap tiledMap, String groundLayerName) {
        this.world = world;
        this.tiledMap = tiledMap;
        this.groundLayerName = groundLayerName;
    }

    @Override
    public void parse() {
        BodyDef bdef = new BodyDef();
        PolygonShape shape = new PolygonShape();
        FixtureDef fdef = new FixtureDef();
        Body body;


        createRoof();





        for (MapObject object : tiledMap.getLayers().get(groundLayerName).getObjects()) {
            if (object instanceof RectangleMapObject) {
                Rectangle rect = ((RectangleMapObject) object).getRectangle();

                bdef.type = BodyDef.BodyType.StaticBody;
                bdef.position.set(((rect.getX() + rect.getWidth() / 2) / InfinityRun.PPM)
                        , ((rect.getY() + rect.getHeight() / 2) / InfinityRun.PPM));
                body = world.createBody(bdef);

                shape.setAsBox((rect.getWidth() / 2) / InfinityRun.PPM
                        , (rect.getHeight() / 2) / InfinityRun.PPM);
                fdef.shape = shape;
                body.createFixture(fdef).setUserData("ground");
            }

            if (object instanceof PolygonMapObject) {

                Polygon pol = ((PolygonMapObject) object).getPolygon();

                float vertices[] = pol.getTransformedVertices();

                for (int x = 0; x < vertices.length; x++) {
                    vertices[x] /= InfinityRun.PPM;
                }

                bdef.type = BodyDef.BodyType.StaticBody;
                bdef.position.set(((pol.getOriginX()) / InfinityRun.PPM)
                        , ((pol.getOriginY()) / InfinityRun.PPM));
                body = world.createBody(bdef);

                shape.set(vertices);
                fdef.shape = shape;
                body.createFixture(fdef).setUserData("ground");
            }
        }
    }


    /**
     * Create a "roof" so user cant jump over the height of the map
     */
    private void createRoof() {

        BodyDef bdef = new BodyDef();
        PolygonShape shape = new PolygonShape();
        FixtureDef fdef = new FixtureDef();
        Body body;


        int levelBoxWidth = (Integer) tiledMap.getProperties().get("tilewidth");
        int levelBoxHeight = (Integer) tiledMap.getProperties().get("tileheight");
        int levelNHorizontal = (Integer) tiledMap.getProperties().get("width");
        int levelNVertical = (Integer) tiledMap.getProperties().get("height");

        int boxX = 0;
        int boxY = levelBoxHeight * levelNVertical - 1;
        int boxHeight = 1;
        int boxWidth = levelBoxWidth * levelNHorizontal;


        Rectangle roofRect = new Rectangle(boxX, boxY, boxWidth, boxHeight);
        bdef.type = BodyDef.BodyType.StaticBody;
        bdef.position.set(((roofRect.getX() + roofRect.getWidth() / 2) / InfinityRun.PPM)
                , ((roofRect.getY() + roofRect.getHeight() / 2) / InfinityRun.PPM));
        body = world.createBody(bdef);

        shape.setAsBox((roofRect.getWidth() / 2) / InfinityRun.PPM
                , (roofRect.getHeight() / 2) / InfinityRun.PPM);
        fdef.shape = shape;
        body.createFixture(fdef).setUserData("ground");
    }



    @Override
    public List<? extends Entity> getEntities() {
        return null;
    }
}
