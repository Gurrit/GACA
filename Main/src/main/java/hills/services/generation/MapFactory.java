package hills.services.generation;

import java.util.Random;

/**
 * Created by gustav on 2017-04-27.
 */
public class MapFactory implements IMapFactory {

    RandomWalker walker;
    Random r;
    Terrain terrain;
    public MapFactory(){
        r = new Random();
        terrain = new Terrain(r.nextLong());
        walker = new RandomWalker();
    }
    @Override
    public void generateWorldImage() {
        terrain.createfinalIsland();
    }


    @Override
    public double generateDirection(float x) {
        return walker.generate(x);
    }
}