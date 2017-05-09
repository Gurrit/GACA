package hills.model;

import hills.controller.ServiceMediator;
import hills.util.math.Vec2;
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


    public Sheep(Model model, Vec3 pos){
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

    public void updatePosition() {

    }


    @Override
    public void addVelocity(Vec2 deltaVelocity) {
        //TODO
    }

    @Override
    public void addVelocity(Vec3 deltaVelocity) {
        //TODO
    }
}
