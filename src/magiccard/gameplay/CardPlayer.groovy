/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package magiccard.gameplay

import sg.atom.gameplay.player.Player;
import magiccard.CardGameStageManager;
import magiccard.gameplay.rule.CardGameRestriction

/**
 *
 * @author cuong.nguyenmanh2
 */
public class CardPlayer extends Player{
    //String name
    String picture =""
    int level = 0
    int score = 0
    int totalScore = 0
    int win = 0
    int lose = 0
    int draw = 0
        
    String desc =""
    String longDesc =""
    
    Deck currentDeck
    Deck orgDeck
    List hand = []
    List grave = []
    List ground = []
    List magic = []
    
    boolean aIPlayer = false
    CardPlayerAI aI;
    CardGameRestriction restriction
    
    public CardPlayer(CardGameStageManager stage, String name) {
        super(stage,name)
        orgDeck = new Deck()
        currentDeck = new Deck()
    }
    
    public setAI(CardPlayerAI aI){
        this.aI= aI;
        aIPlayer =true;
    }
    
    public Card fromDeckToHand(){
        // Draw one card from deck to hand if there any card
        if (!currentDeck.cardList.isEmpty()){
            def topCard = currentDeck.cardList.remove(0)
            hand<<topCard
            return topCard;
        } else {
            return null;
        }
    }
    
    public boolean askForUse(Card.CardType type,List inCardList,def info,def condition){
        CardGamePlay gamePlay = stageManager.getGamePlayManager();
        cardLib = stageManager.cardLib;
        Card starterCard;
        List<Card> targetCards;
        
        // player select the card
        if (isAIPlayer()){
            starterCard = cardPlayerAI.askForSelect(type,inCardList,info,condition)
        } else {
            starterCard = gamePlay.askForSelect(type,inCardList,info,condition)
        }
        // player select target if need
        if (isAIPlayer()){
            targetCards = cardPlayerAI.askForSelect(type,inCardList,info,condition)
        } else {
            targetCards = gamePlay.askForSelect(type,inCardList,info,condition)
        }
        // active the card
        boolean validAction = gamePlay.activeCard(starterCard,targetCards)
        return validAction;
    }
    
    public boolean activeTrapCards(){
        boolean validAction;
        if (!restriction.trapCancel && magic.any{it.cardType==Card.CardType.Trap}){
            // if any immediate trap cards ready
            // ask for use trap cards
            validAction = askForUse(Card.CardType.Trap,magic,null,null)
        } else if (!restriction.magicCancel && hand.any{it.cardType==Card.CardType.Trap}){
            // ask for immediate spell card
            validAction = askForUse(Card.CardType.Trap,hand,null,null)
        }
        
        return validAction
    }
}

