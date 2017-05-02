package hills.services.generation;

import hills.services.terrain.TerrainService;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

/**
 * Created by gustav on 2017-03-21.
 */

public class NoiseMapGenerator {            // This file should be cleaned up for readability.
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

    public void create2DNoiseImage(String name, double frequency, double scale){        // this should probably be created in a thread, takes about .25 secs with current values
        BufferedImage image = new BufferedImage(WIDTH+1, HEIGHT+1, BufferedImage.TYPE_INT_RGB);
            double[][] greens = createMatrix(frequency, scale, false);
            for (int y = 0; y <= HEIGHT; y++) {
                for (int x = 0; x <= WIDTH; x++) {
                    int r = ((int)(greens[x][y]*255))<<16 & 0xFF0000;
                    int g = ((int)(greens[x][y]*255))<<8 & 0xFF00;
                    int b = ((int)(greens[x][y]*255)) & 0xFF;
                    int rgb = r+b+g;
                    image.setRGB(x,y,rgb);
                }
            }
        try {
            ImageIO.write(image, "png", new File("src/main/resources/" + name + ".png"));
        }
        catch (IOException io){
            System.out.println("failed to create file");
            io.printStackTrace();
        }
    }
    protected double getDoubleValue(int x, int y){
        return greenMatrix[x][y] / 255.0;
    }
/*
    private int[] arrayFromMatrix(int[][] matrix){
        int width = matrix.length;
        int height = matrix[0].length;
        int length = width*height;
        int[] arr = new int[length];
        int index = 0;
        for(int i = 0; i<width; i++) {
            for (int j = 0; j<height; j++) {
                arr[index] = matrix[i][j];
                index++;
            }
        }
        return arr;
    }
    */
    /*
    Do Not modify. create a copy to do that.
     */
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
