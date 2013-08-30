package magiccard.gameplay.ai


import magiccard.*
import magiccard.gameplay.*
import static magiccard.gameplay.TurnPhase.TurnPhaseType.*
/**
 * This class is the AI for playing YugiOh Magic Card game.
 * The main technique used is DecisionTree & NeutralNetwork to decide actions
 */
public class CardPlayerAI {
    enum AILevel {Starter,Normal,HardCore,Duelist,Best;
        int deep;
    }
    AILevel level;
    CardGamePlay gamePlay;
    CardPlayer player;
    enum StragegySituation {GoodToAttack,ShouldDefend,Unknown,Dangeous}
    StragegySituation situation= StragegySituation.Unknown;
    def memories = [:]
    // The list of special card (id) that will have higher priority at any time
    def listOfSpecialCards = []
    
    /**
     *AI sepecific params
     **/    
    int maxTime = 5000
    int maxSteps = 500
    int maxBranch = 50
    int maxGuess = 5
    
    int randomness = 30

    // save delayed action 
    def actions
    public CardPlayerAI(CardGamePlay gamePlay){
        this.gamePlay = gamePlay;
        this.actions = []
    }
        
    public String toString(){
        return "AI level : "+this.level.toString() + " status :" + this.situation.toString()
    }
    public int think(){
        int startTime = System.currentTimeMillis()
        if (gamePlay.currentTurn.currentPhase.type == MainPhase){
            // try to summon
            if (!gamePlay.currentTurn.currentPhase.monsterSummoned){
                def summonableCards = player.hand.findAll{card-> canSummon(card)==true};
                if (!summonableCards.isEmpty()){
                    def bestCard = summonableCards.max{card-> card.attack}
                    actions<<{
                        println ("AI want to summon "+bestCard.toString())
                        summon(bestCard)
                    }
                } else {
                    if (player.ground.isEmpty()){
                        // Dont have any card on the Ground?
                        situation = StragegySituation.Dangerous;
                    }
                }
            } else if (gamePlay.currentTurn.currentPhase.type == BattlePhase){
                gamePlay.nextPhase()
            }else {
                gamePlay.nextPhase()
            }
        }
        int takeTime = System.currentTimeMillis()-startTime;
        println("AI think in "+ takeTime +" ms")
        return takeTime;
    }
    
    public void act(){
        actions.each{delayedAct->
            delayedAct()
        }
            
        actions.clear();
    }
    //self action

    void summon(Card card){
        gamePlay.notifyMoveCard(card,"enableHover")
        gamePlay.fromHandToGround(card);
        gamePlay.currentTurn.currentPhase.monsterSummoned = true;
    }
    /**
     * MINIMAX SUPPORT
     * See the game as the minimum forecastable loss problem and calculate the case
     * In fact, you can use minimax as a base heristic case and then extend
     */
    
    /**
     * CASE BASE SUPPORT
     * Not for a naive approach anymore. 
     * In fact, you can build a heristic case and calculate the result in a few next round to see if the value you gain (win) or lost is sastified
     */
    def tryCase(Case aCase){
        return result;
    }
    def askForSelect(type,inCardList,info,condition){
        
    }
    public Case findBestSummonCase(){
        
    }
    public Case findBestMagicCase(){
        
    }
    public Case findBestSupportCase(){
        
    }
    public Case findBestOveralCase(){}

    
    // self evaluation
    public float evalGoodToAttack(Card card){
        
    }
    public float evalGoodToDefend(Card card){
        
    }
    /* This function evaluate the value of these two cards when the AI choose to support targetCard with the approriate supportCard*/
    public float evalGoodToSupport(Card targetCard,Card supportCard){
        
    }
    /* This function evaluate the value of this card when they want to keep or discard it by purpose */
    public float evalGoodOveral(Card card){
        
    }
    // Rule
    public boolean canSummon(Card aCard){
        

        // enough tribute
        int stars = aCard.level.toInteger()
        boolean enoughStar=false;
        if ( stars > 6){
            if (stars - 5 > player.ground.size()){
                enoughStar =true
            } else {
                enoughStar =false
            }
             
        } else {
            enoughStar=true;
        }
        //(!aCard.summonCancel) && && sastifySummonCondition(aCard)
        return (enoughStar && aCard.isMonsterCard());
    }
    

    
    
}

