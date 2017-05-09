package hills.model;

import hills.util.math.Vec2;
import hills.util.math.Vec3;
import hills.util.math.shape.Sphere;
import hills.util.model.Model;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Anders on 2017-04-03.
 */
public abstract class Creature implements IWoundable, IMovable, ICollidable{

    /**
     * {@inheritDoc}
     */

    protected Vec3 pos;
    @Setter @Getter protected float speed;
    protected int healthPoints;
    protected int maxHealth;
    protected Model model;
    protected Vec2 velocity;
    private List<OnCreatureMoveListener> listeners = new ArrayList<>();

    @Override
    public abstract Sphere getBoundingSphere();

    public void updatePosition(Vec3 diff) {
        this.pos = this.pos.add(diff);
    }

    @Override
    public void setPosition(Vec3 pos) {
        this.pos = pos;
    }

    @Override
    public Vec2 get2DPos() {
        return new Vec2(pos.getX(), pos.getY());
    }

    @Override
    public Vec3 get3DPos() {
        return pos;
    }

    @Override
    public int getHealth(){return healthPoints;}

    @Override
    public int getMaxHealth(){return maxHealth;}

    @Override
    public void takeDamage(int amount){
        healthPoints -= amount;
    }

    public Vec2 getVelocity(){
        return velocity;
    }

    protected float getHeight(Creature creature){          // This is a Horrible implementation, should be changed.
        return listeners.get(0).getCreaturePosition(creature);
    }

    public void addListener(OnCreatureMoveListener listener){
        listeners.add(listener);
    }
    @Override
    public void setHeight(float height){
        pos = new Vec3(pos.getX(), height, pos.getZ());
    }
}
