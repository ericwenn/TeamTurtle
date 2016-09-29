package com.teamturtle.infinityrun.map_parsing;

import com.teamturtle.infinityrun.sprites.Entity;

import java.util.List;

/**
 * Created by ericwenn on 9/27/16.
 */
public interface MapParser {

    void parse();
    List<? extends Entity> getEntities();
}
