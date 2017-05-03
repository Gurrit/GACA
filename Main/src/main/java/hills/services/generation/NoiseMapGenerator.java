package hills.services.generation;

import hills.services.terrain.TerrainService;

/**
 * Created by gustav on 2017-03-21.
 */

public class NoiseMapGenerator {
    private final int WIDTH = TerrainService.TERRAIN_WIDTH;
    private final int HEIGHT = TerrainService.TERRAIN_HEIGHT;

    OpenSimplexNoise noise;
    double[][] greenMatrix = new double[WIDTH][HEIGHT];

	public NoiseMapGenerator(long seed) {
		noise = new OpenSimplexNoise(seed);
	}

	void setSeed(long seed){
    	noise.setSeed(seed); 
    }

    double[][] createMatrix(double frequency, double scale, boolean zeroToOne){
        double[][] matrix = new double[WIDTH+1][HEIGHT+1];
        for(int y = 0; y <= HEIGHT; y++) {
            for (int x = 0; x <= WIDTH; x++) {
                double value = noise.eval(x / frequency, y / frequency)*scale;
                if(!zeroToOne) {
                    int rgb = 0x010101 * (int) ((value + 1) * 127.5);
                    matrix[x][y] = rgb;
                }
                else{
                    matrix[x][y] = value;
                }
            }
        }
        this.greenMatrix = matrix;
        return matrix;
    }

}