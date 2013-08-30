/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package magiccard.stage

import magiccard.gameplay.Card
import sg.atom.entity.SpatialEntity
import sg.atom.stage.select.EntitySelectCondition
/**
 *
 * @author hungcuong
 */
public class ClosureSelectCondition extends EntitySelectCondition{
    Closure cardSelectCondition;
    ClosureSelectCondition(Closure cardSelectCondition){
        this.cardSelectCondition= cardSelectCondition;
    }
    @Override
    public boolean isSelected(SpatialEntity se) {
        Card card = (Card) se;
        return cardSelectCondition(card)
    }
    
    @Override
    public boolean isHovered(SpatialEntity se) {
        Card card = (Card) se;
        return cardSelectCondition(card)
        
    }
}

