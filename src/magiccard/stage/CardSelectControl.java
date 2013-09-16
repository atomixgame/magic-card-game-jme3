/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package magiccard.stage;

import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Spatial;
import sg.atom.stage.WorldManager;
import sg.atom.stage.select.SpatialSelectControl;
import magiccard.gameplay.CardGamePlay;
import magiccard.gameplay.Card;
import magiccard.gameplay.CardSpatialControl;
import sg.atom.stage.select.EntitySelectCondition;

/**
 *
 * @author cuong.nguyenmanh2
 */
public class CardSelectControl extends SpatialSelectControl {

    private Vector3f bigScale;
    private Vector3f orginalScale;
    private Vector3f orginalPos;
    private Vector3f peakPos;
    private boolean skipOutHover;
    private String currentFunction;
    private CardGamePlay gameplay;
    private Card card;
    private boolean isBigger = false;
    private boolean isPeak = false;
    private boolean isHighlight = false;

    public CardSelectControl(WorldManager worldManager, CardGamePlay gameplay) {
        currentFunction = "Normal";
        this.gameplay = gameplay;
    }

    @Override
    public void setSpatial(Spatial spatial) {
        super.setSpatial(spatial);
        this.card = (Card) spatial.getControl(CardSpatialControl.class).getEntity();
        orginalScale = spatial.getLocalScale().clone();
        bigScale = orginalScale.mult(1.2f);
        orginalPos = spatial.getLocalTranslation().clone();
    }

    @Override
    protected void doSelected() {
        super.doSelected();
        //orginalScale = spatial.getLocalScale();
        //orginalPos = spatial.getLocalTranslation().clone();
        //peakPos = orginalPos.add(new Vector3f(0f, 0.4f, 0f));
        //spatial.setLocalTranslation(peakPos);
        //spatial.setLocalScale(bigScale);
    }

    @Override
    protected void doDeselected() {
        super.doDeselected();
        //spatial.setLocalScale(orginalScale);
        //spatial.setLocalTranslation(orginalPos.clone());

    }

    @Override
    protected void doHovered() {
        if (this.isHoverable()) {
            super.doHovered();
            //orginalScale = spatial.getLocalScale();
            EntitySelectCondition inHand = (EntitySelectCondition) gameplay.cardSelectRules.get("inHand");
            EntitySelectCondition inGround = (EntitySelectCondition) gameplay.cardSelectRules.get("inGround");
            if (inHand.isSelected(card)) {
                peak();
                bigger();
                highlight();
            } else if (inGround.isSelected(card)) {
                // The card is the ground
                highlight();
            }
        }
    }

    @Override
    protected void doOutHovered() {
        if (this.isHoverable()) {
            if (!this.skipOutHover) {
                super.doOutHovered();

                noPeak();
                smaller();
                noHighlight();

            }
        }
    }

    public String getCurrentFunction() {
        return currentFunction;
    }

    public void setCurrentFunction(String currentFunction) {
        this.currentFunction = currentFunction;
    }

    public void setSkipOutHover(boolean skipOutHover) {
        this.skipOutHover = skipOutHover;
    }

    public boolean isSkipOutHover() {
        return skipOutHover;
    }

    void bigger() {
        if (!isBigger) {
            spatial.setLocalScale(bigScale);
            isBigger = true;
        }
    }

    void peak() {
        if (!isPeak) {
            orginalPos = spatial.getLocalTranslation().clone();
            peakPos = orginalPos.add(new Vector3f(0f, 0.2f, 0.2f));
            spatial.setLocalTranslation(peakPos);
            isPeak = true;
        }
    }

    void noPeak() {
        if (isPeak) {
            spatial.setLocalTranslation(orginalPos.clone());
            isPeak = false;
        }
    }

    void smaller() {
        if (isBigger) {
            spatial.setLocalScale(orginalScale);
            isBigger = false;
        }
    }

    void highlight() {
        if (!isHighlight) {
            ((Geometry) spatial).getMaterial().setBoolean("MixGlow", true);
            isHighlight = true;
        }
    }

    void noHighlight() {
        if (isHighlight) {
            ((Geometry) spatial).getMaterial().setBoolean("MixGlow", false);
            isHighlight = false;
        }
    }
}
