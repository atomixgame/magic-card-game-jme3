/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package magiccard.movement

import com.jme3.scene.Spatial
import com.jme3.math.Transform
/**
 *
 * @author hungcuong
 */
public interface Force {
    public Transform getForce();
    public void applyForce(Spatial sp);
}

