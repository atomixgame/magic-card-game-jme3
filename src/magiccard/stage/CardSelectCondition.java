/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package magiccard.stage;

import magiccard.gameplay.*;
import sg.atom.entity.SpatialEntity;
import sg.atom.stage.select.EntitySelectCondition;

/**
 *
 * @author cuong.nguyenmanh2
 */
public class CardSelectCondition extends EntitySelectCondition {
    
    CardGamePlay cardGamePlay;
    
    public CardSelectCondition(CardGamePlay cardGamePlay) {
        this.cardGamePlay = cardGamePlay;
    }
    
    @Override
    public boolean isSelected(SpatialEntity se) {
        Card card = (Card) se;
        
        if (cardGamePlay.isCurrentPlayer(cardGamePlay.whichPlayerCard(card))) {
            CardPlayer player = cardGamePlay.getCurrentPlayer();
            if (player.getHand().contains(card)) {
                return true;
            }
        }
        return false;
        
    }
    
    @Override
    public boolean isHovered(SpatialEntity se) {
        Card card = (Card) se;
        
        if (cardGamePlay.isCurrentPlayer(cardGamePlay.whichPlayerCard(card))) {
            CardPlayer player = cardGamePlay.getCurrentPlayer();
            if (player.getHand().contains(card)) {
                return true;
            }
        }
        return false;
        
    }
}
