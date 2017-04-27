import hills.services.generation.Terrain;
import hills.services.generation.TerrainData;
import org.junit.Before;
import org.junit.Test;

import java.util.Random;

import static junit.framework.TestCase.assertNotNull;
import static junit.framework.TestCase.assertTrue;

/**
 * Created by gustav on 2017-03-28.
 */
public class terrainTest {

	Terrain t;

	@Before
	public void testTerrain() {
		Random random = new Random();
		t = new Terrain(random.nextLong());
		assertNotNull(t);
	}

    @Test
    public void testCreateIsland(){
        TerrainData[][] terrain = t.createfinalIsland();
        double smallest = 1;
        double largest = 0;
        for(int x = 0; x<terrain.length; x++){
            for (int y = 0; y < terrain[0].length; y++){
                assertTrue(terrain[x][y].getPosition().getY()<=1);
                assertTrue(terrain[x][y].getPosition().getY()>=0);
                if(smallest>terrain[x][y].getPosition().getY()){
                    smallest = terrain[x][y].getPosition().getY();
                }
                if(largest<terrain[x][y].getPosition().getY()){
                    largest = terrain[x][y].getPosition().getY();
                }
            }
        }
        assertTrue(largest>0.95);
        assertTrue(smallest<0.05);
    }
    @Test
    public void testCreateFinalMap(){
        TerrainData[][] terrain = t.createfinalIsland();
        double smallest = 255;
        double largest = 0;
        for(int x = 0; x<terrain.length; x++){
            for (int y = 0; y < terrain[0].length; y++){
                assertTrue(terrain[x][y].getPosition().getZ()<=255);
                assertTrue(terrain[x][y].getPosition().getZ()>=0);
                if(terrain[x][y].getPosition().getZ() < smallest){
                    smallest = terrain[x][y].getPosition().getZ();
                }
                if(largest < terrain[x][y].getPosition().getZ()){
                    largest = terrain[x][y].getPosition().getZ();
                }
            }
        }
        assertTrue(largest>0.95);
        assertTrue(smallest<0.05);
    }
}
