package com.teamturtle.infinityrun.map_parsing;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
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
public class ObstacleParser implements MapParser {

    private final World world;
    private final TiledMap tiledMap;
    private final String obstacleLayerName;

    public ObstacleParser(World world, TiledMap tiledMap, String obstacleLayerName) {
        this.world = world;
        this.tiledMap = tiledMap;
        this.obstacleLayerName = obstacleLayerName;
    }

    @Override
    public void parse() {
        BodyDef bdef = new BodyDef();
        PolygonShape shape = new PolygonShape();
        FixtureDef fdef = new FixtureDef();


        for (MapObject object : tiledMap.getLayers().get(obstacleLayerName).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            bdef.type = BodyDef.BodyType.StaticBody;
            bdef.position.set(((rect.getX() + rect.getWidth() / 2) / InfinityRun.PPM)
                    , ((rect.getY() + rect.getHeight() / 2) / InfinityRun.PPM));
            Body body = world.createBody(bdef);

            shape.setAsBox((rect.getWidth() / 2) / InfinityRun.PPM
                    , (rect.getHeight() / 2) / InfinityRun.PPM);
            fdef.shape = shape;
            fdef.isSensor = true;
            body.createFixture(fdef).setUserData("obstacle");
        }
    }

    @Override
    public List<? extends Entity> getEntities() {
        return null;
    }
}
