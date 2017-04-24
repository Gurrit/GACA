package hills.engine.system.domainModel;

import hills.engine.math.Vec2;
import hills.engine.math.Vec3;

/**
 * Created by Anders on 2017-04-03.
 */
public interface IMovable {

    /**
     * adds the variable diff to the current position
     */
    void updatePosition();

    /**
     * sets the objects position to that of the parameter
     * @param pos position to be set
     */
    void setPosition(Vec3 pos);


    /**
     * gives the x,y coordinates in form of a Vec2
     * @return the vector with the x,y coordinates
     */
    Vec2 get2DPos();

    /**
     * gives the x,y,z coordinates in form of a Vec3
     * @return the vector with the x,y,z coordinates
     */
    Vec3 get3DPos();

    Vec3 getVelocity();

}