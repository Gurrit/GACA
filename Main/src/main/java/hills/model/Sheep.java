package hills.model;

import hills.controller.ServiceMediator;
import hills.util.math.Vec3;
import hills.util.math.shape.Sphere;
import hills.util.model.Model;

import java.util.Random;

/**
 * Created by Anders on 2017-04-03.
 */
public class Sheep extends Creature{
    /**
     * {@inheritDoc}
     */
    float radius;
    Random rand = new Random();
    ServiceMediator serviceMediator;
    private float move = 0;


    Sheep(Model model, Vec3 pos){
        this.model = model;
        this.pos = pos;
        this.healthPoints = 20;
        this.speed = 1;
        this.maxHealth = 20;
        this.radius = 1;
        serviceMediator = ServiceMediator.INSTANCE;
    }

    @Override
    public Sphere getBoundingSphere() {
        return new Sphere(pos, radius);
    }

    @Override
    public void updatePosition() {

    }

    @Override
    public void moveRandomly(){        // Moves one meter in random direction. Can be improved with 1D noise to create psudo-Random.
        float x = pos.getX();
        float z = pos.getZ();
        float y;
        double direction = decideDirection();
        x += Math.cos(direction);
        z += Math.sin(direction);
        y = getHeight(this);
        if(y<=3){
            return;
        }
        pos = new Vec3(x, y, z);
    }
    private double decideDirection(){
        double randomNum = serviceMediator.generateDirection(move+=50);
        return randomNum*2*Math.PI;
    }

}
