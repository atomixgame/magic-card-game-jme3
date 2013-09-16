/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package magiccard.gameplay

import com.jme3.scene.control.AbstractControl
import com.jme3.math.Vector3f
import com.jme3.renderer.RenderManager
import com.jme3.renderer.ViewPort
import com.jme3.math.FastMath;
import sg.atom.entity.SpatialEntityControl
import sg.atom.stage.SelectManager
import sg.atom.stage.WorldManager
import com.jme3.math.Quaternion
/**
 *
 * @author hungcuong
 */
public class CardSpatialControl extends SpatialEntityControl{

    boolean stopMove = false;
    boolean stopRot = false;
    boolean gigle = false;
    Vector3f targetPos;
    Vector3f oldPos;
    Quaternion oldRot;
    Quaternion targetRot;
    float timeRot = 0;
    float speedRot = 0.3f;
    float speedPos = 0.04f;
    
    public CardSpatialControl(WorldManager worldManager, Card card) {
        super(worldManager, card);
    }
    
    void pos(Vector3f targetPos){
        this.targetPos = targetPos.clone();;
        this.oldPos = spatial.getLocalTranslation().clone();
        stopMove = false;
    }
    void rot(Quaternion targetRot){
        this.targetRot = targetRot.clone();
        this.oldRot = spatial.getLocalRotation().clone();
        timeRot = 0;
        stopRot = false;
    } 
    public void controlRender(RenderManager render, ViewPort viewPort){
         
    }       
            
    public void controlUpdate(float tpf){
        updatePos(tpf);
        updateRot(tpf);
        if (gigle){
            updateGiggle();
        }
    }
    void updatePos(float tpf){
        if (targetPos!=null){
            Vector3f newPos;
            Vector3f currentPos = spatial.getLocalTranslation();
            float dis = currentPos.distance(targetPos)
            if (!stopMove){
                if ( dis> speedPos){
                    Vector3f force = targetPos.subtract(currentPos).normalize().mult(speedPos)
                    newPos = currentPos.add(force)
                } else {
                    newPos = targetPos.clone();
                    stopMove = true;
                
                }
                spatial.setLocalTranslation(newPos)
            }
            
        }
    }
    
    void updateRot(float tpf){
        if (targetRot!=null&&oldRot!=null){
            if (!stopRot){
                Quaternion newRot = new Quaternion();
                if ( timeRot <speedRot){
                    newRot.slerp(oldRot,targetRot,(float)(timeRot/speedRot))
                    timeRot += tpf;
                } else {
                    stopRot = true;
                    newRot.set(targetRot)
                }
                spatial.setLocalRotation(newRot);
            }
        }
    }

    void updateGiggle(){
        float x =(0.5f-FastMath.nextRandomFloat()) * 0.005f;
        float y =(0.5f-FastMath.nextRandomFloat()) * 0.005f;
        float z =0;
        Vector3f oldPos=spatial.getLocalTranslation().clone();
        spatial.setLocalTranslation(oldPos.add(x,y,z));
    }
}

