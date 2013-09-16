package magiccard.gameplay

/**
 * This class is the AI for playing YugiOh Magic Card game.
 * 
 * The main technique used is DecisionTree & NeutralNetwork to decide actions
 */
public class CardPlayerAI {
    enum AILevel {Starter,Normal,HardCore,Duelist,Best}
    
    AILevel level;
    
    CardGamePlay gamePlay;
    CardPlayer player;
    enum StragegySituation {GoodToAttack,ShouldDefend,Unknown,Dangeous}
    StragegySituation situation;
    
    /* Human Emotion model */
    public CardPlayerAI(CardGamePlay gamePlay){
        this.gamePlay = gamePlay;
    }
    
    public void think(){
        if (gamePlay.current){
            //
            def summonableCards = hand.findAll{card-> canSummon(card)==true};
            if (!summonableCards.isEmpty()){
                def bestCard = summonableCards.max{card-> card.attack}
                summon(bestCard)
                
            } else {
                if (ground.isEmpty()){
                    // Dont have any card on the Ground?
                    situation = StragegySituation.Dangerous;
                }
            }
            
        }
    }
    
    void summon(Card aCard){
        
    }
    // Rule
    boolean canSummon(Card aCard){
        

        // enough tribute
        if (aCard.stars > 6){
            if (aCard.stars - 5 > ground.size()){

            }
        } else {
            
        }
        
        return ((card.preventConstrant==false) && (enoughStar)&& sastifySummonCondition);
    }
    
    def askForSelect(type,inCardList,info,condition){
        
    }
    
    
}

