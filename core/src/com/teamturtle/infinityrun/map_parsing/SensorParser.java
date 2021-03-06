package com.teamturtle.infinityrun.map_parsing;

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

public class SensorParser implements MapParser {

    public enum Type {
        OBSTACLE("obstacle"), GOAL("goal"), QUEST("quest");

        private String layerName;

        Type(String layerName) {
            this.layerName = layerName;
        }

        public String getName() {
            return layerName;
        }
    }

    private final World world;
    private final TiledMap tiledMap;
    private final String layerName;

    public SensorParser(World world, TiledMap tiledMap, Type type) {
        this.world = world;
        this.tiledMap = tiledMap;
        this.layerName = type.layerName;
    }

    @Override
    public void parse() {
        BodyDef bdef = new BodyDef();
        PolygonShape shape = new PolygonShape();
        FixtureDef fdef = new FixtureDef();


        for (MapObject object : tiledMap.getLayers().get(layerName).getObjects()) {
            if (object instanceof RectangleMapObject) {
                Rectangle rect = ((RectangleMapObject) object).getRectangle();
                bdef.type = BodyDef.BodyType.StaticBody;
                bdef.position.set(((rect.getX() + rect.getWidth() / 2) / InfinityRun.PPM)
                        , ((rect.getY() + rect.getHeight() / 2) / InfinityRun.PPM));
                Body body = world.createBody(bdef);

                shape.setAsBox((rect.getWidth() / 2) / InfinityRun.PPM
                        , (rect.getHeight() / 2) / InfinityRun.PPM);
                fdef.shape = shape;
                fdef.isSensor = true;
                body.createFixture(fdef).setUserData(layerName);
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
                Body body = world.createBody(bdef);

                shape.set(vertices);
                fdef.shape = shape;
                fdef.isSensor = true;
                body.createFixture(fdef).setUserData(layerName);
            }

            // Copy pasted code that could be used for polyline objects
            /*if (object instanceof PolylineMapObject) {
                float vertices[] = ((PolylineMapObject) object).getPolyline()
                        .getTransformedVertices();
                for (int x = 0; x < vertices.length; x++) {
                    vertices[x] /= InfinityRun.PPM;
                }
                ChainShape shape2 = new ChainShape();
                shape2.createChain(vertices);
                def.position.set(0, 0);
                cuerpos.add(world.createBody(def));
                cuerpos.get(cuerpos.size - 1).createFixture(shape2, 0);
            }*/
        }
    }

    @Override
    public List<? extends Entity> getEntities() {
        return null;
    }
}
