package magiccard.gameplay

import java.util.Random
import sg.atom.gameplay.GamePlayManager
import sg.atom.gameplay.GameLevel
import sg.atom.gameplay.player.Player
import sg.atom.stage.select.EntitySelectCondition
import sg.atom.entity.SpatialEntity
import magiccard.CardGameStageManager
import magiccard.CardGame
import magiccard.gameplay.Card
import magiccard.stage.ClosureSelectCondition
import magiccard.stage.CardSelectControl
import static magiccard.gameplay.TurnPhase.TurnPhaseType.*

//import java.util.Collections
/**
 *
 * @author cuong.nguyenmanh2
 */
public class CardGamePlay extends GamePlayManager {
    // Story mode
    Campaign camp
    // League
    Tournament tour
    boolean onTour = false
    CardMatch match
    // The real human player who controlling
    CardPlayer currentPlayer
    CardPlayer opponent
    def uiInGame
    def inGameScreen  

    // Rule
    public Map cardSelectRules=[:];
    public Map userActions=[:]
    String currentSelectRule;
    // Handle turns
    Turn currentTurn;
    boolean initDuel = true;
    int turnCount = 0;
    def waitTimes = [:]
    def uiWaitQueue = []
    float TIMEMODE_DISABLE = -1
    // Util
    Random rand = new Random();
    TurnPhase.TurnPhaseType playerChangePhase
    
    public CardGamePlay(CardGame app) {
        super(app)
    }
    /* ROUTINES ===========================================*/
    /* Config */
    public void initInputListener(){
        
    }
    public void configGamePlay(){
        println "START GAMEPLAY"
        currentPlayer = new CardPlayer(stageManager,"Yugi")
        match = new CardMatch();
        
        match.player1 = currentPlayer
        // opponent from the Tournament
        if (tour){
        }
        
        // Setup the opponent : AI
        opponent= makeRandomPlayer("Kaiba")
        CardPlayerAI ai = new CardPlayerAI();
        opponent.setAI(ai);
        ai.setLevel(CardPlayerAI.AILevel.Normal);
        match.player2 = opponent

    }
    
    public void startLevel(GameLevel table){
        matchStart()
    }
    public void matchStart(){
        
        // Take out the 2 decks
        currentPlayer.orgDeck.clone(stageManager.cardLib.chooseRandomCards(20))
        opponent.orgDeck.clone(stageManager.cardLib.chooseRandomCards(20))
        
        currentPlayer.currentDeck.clone(currentPlayer.orgDeck)
        opponent.currentDeck.clone(opponent.orgDeck)
        
        // Shuffle them!
        currentPlayer.currentDeck.shuffle()
        opponent.currentDeck.shuffle()
        
        CardTable table = (CardTable)currentLevel
        table.createDeck(currentPlayer)
        table.createDeck(opponent)

        //table.createHand(currentPlayer)
        //table.createHand(opponent)
        
        //table.shuffle(currentPlayer)
        //table.shuffle(opponent)
        
        /*
        // Show them to the log
        println "Card list of (Current) Player 1" + currentPlayer.name
        showDeckLog(currentPlayer.currentDeck)
        println "Card list of Player 2" + opponent.name
        showDeckLog(opponent.currentDeck)
         */
        // Rule set
        createRules()
        setCardSelectCondition("noneSelect")
        // Set the score
        currentPlayer.score = 8000
        opponent.score = 8000

        // Decide who go first
        currentTurn = new Turn(turnCount);
        currentTurn.player = flipCoin(currentPlayer,opponent)

        currentTurn.currentPhase = new TurnPhase(DrawPhase);
        waitTimes["ToNextPhase"] = 0;
        if (initDuel){
            // UI change
            updateUI(null);
            updateUI{screen,ui->
                println(currentTurn.player.name + " go first !")
                uiInGame.alertFlash(currentTurn.player.name + " go first !")
                if (currentTurn.player == currentPlayer){
                    uiInGame.setTurn("Player1")
                } else {
                    uiInGame.setTurn("Player2")
                }
            }
            (1..5).each{
                fromDeckToHand(currentPlayer)
                fromDeckToHand(opponent)
            }
            initDuel = false;
                
        }
        
        stageManager.selectManager.enable = true;
    }

    public void nextPhase(){
        switch(currentTurn.currentPhase.type){
            // What phase
                
        case DrawPhase:
            currentTurn.currentPhase = new TurnPhase(StandbyPhase)
            break;
        case StandbyPhase:
            currentTurn.currentPhase = new TurnPhase(MainPhase)
            break;
        case MainPhase:
            currentTurn.currentPhase = new TurnPhase(BattlePhase)
            break;
        case BattlePhase:
            currentTurn.currentPhase = new TurnPhase(MainPhase2)
            break;
        case MainPhase2:
            currentTurn.currentPhase = new TurnPhase(EndPhase)
            break;
        case EndPhase:
            turnCount++;
            currentTurn = new Turn(turnCount);
            currentTurn.currentPhase  = new TurnPhase(DrawPhase)
            break;
        }
        
        if (uiInGame!=null){
            updateUI{screen,ui->
                uiInGame.setTurnPhase(currentTurn.currentPhase.type)
                uiInGame.alertFlash(currentTurn.currentPhase.type.toString())
            }
        }
        println("CurrentTurnPhase :"+currentTurn.currentPhase.type.toString());
        waitTimes["ToNextPhase"] = 1;
        userActions["userCanceledPhase"] = false;
    }
    /* Update */   
    public void update(float tpf){
 
        if (waitTimes["ToNextPhase"]==0){
            // Turn flow
            switch(currentTurn.currentPhase.type){
                // What phase
                
            case DrawPhase:
                currentTurn.currentPhase.monsterSummoned = false;
                fromDeckToHand(currentTurn.player)
                nextPhase()
                break;
            case StandbyPhase:
                /*
                The effects of certain cards are activated during the Standby Phase. 
                Be sure to take note of such cards & resolve their abilities during this phase.
                 */
                //activeCards()
                
                nextPhase()
                break;
            case MainPhase:
                if (isPlayerChangePhase(TurnPhase.TurnPhaseType.MainPhase)){
                    nextPhase()
                } else {
                    // player summon one
                    setCardSelectCondition("mainPhaseCards")
                    if (userActions["userCanceledPhase"]){
                        nextPhase();
                    } else {
                        if (!stageManager.selectManager.currentSelection.isEmpty()){
                            def list = stageManager.selectManager.currentSelection
                            def card = list.remove(0)
                        
                            if (cardSelectRules["inHandCurrent"].isSelected(card)){
                                switch (card.cardType){
                                case "NormalMonster":
                                    if (!currentTurn.currentPhase.monsterSummoned){
                                        notifyMoveCard(card,"enableHover")
                                        fromHandToGround(card)
                                        currentTurn.currentPhase.monsterSummoned = true;
                                    } else {
                                        println("You can not summon twice !")
                                        updateUI{screen,ui->
                                            uiInGame.alertSmall("You can not summon twice !")
                                        }
                                    }
                                    break;
                                case "Magic":
                                    notifyMoveCard(card,"enableHover")
                                    fromHandToMagic(card)
                                    break;
                                case "Trap":
                                    notifyMoveCard(card,"enableHover")
                                    fromHandToMagic(card)
                                    break;    
                            
                                }
                            } else if (cardSelectRules["inGround"].isSelected(card)){
                                println ("Destroy card")
                                destroyCard(card)
                            }

                        }
                    }
                }
                break;
            case BattlePhase:
                nextPhase()
                break;
            case MainPhase2:
                nextPhase()
                break;
            case EndPhase:
                // whose turn
                waitTimes["ToNextPhase"]=TIMEMODE_DISABLE
                currentTurn.player = otherPlayer(currentTurn.player)
                //nextPhase()
                break;
            }
            // update things
            
            
        } else {
            //println("WTime update" + wtime)
            if (waitTimes["ToNextPhase"]==TIMEMODE_DISABLE) {
                // Time Mode disable
            } else if (waitTimes["ToNextPhase"]<0) {
                waitTimes["ToNextPhase"] =0;
            } else {
                waitTimes["ToNextPhase"] -= tpf;
            }
        }
        
        updateUI(null);
                
    }
    
    public void updateGameplay(){
        /*
        while (!checkEndGame()){
        turnCount++;
        }
         */
    }
   
    public void updateUI(def action){
        if (inGameScreen ==null || uiInGame==null){
            // Make sure it will not be NULL in next update
            inGameScreen  = stageManager.gameGUIManager.nifty.getScreen("InGameScreen")
            if (inGameScreen.isRunning()){
                uiInGame = inGameScreen.screenController;
            }

            // The UI is not fully build! Add the action in the event Queue
            if (action!=null){
                uiWaitQueue << action
                // do not thing else
                println("The UI is not fully build! Add the action in the event Queue. " + uiWaitQueue.size())
            }

        } else {
            // do all the waited jobs
            uiWaitQueue.each{delayedAct->
                delayedAct(inGameScreen,uiInGame)
            }
            
            uiWaitQueue.clear();
            
            // and the current job
            if (action!=null){
                action(inGameScreen,uiInGame)
            }
            
        }
    }
    // Utils =====================================================
    
    public def actionValid(String actionName){
        
    }
    
    public boolean setPlayerChangePhase(TurnPhase.TurnPhaseType newPhase){
        playerChangePhase = newPhase;
        // should be change to the phase after
    }
    public boolean isPlayerChangePhase(TurnPhase.TurnPhaseType currentPhase){
        return false;
    }    
    // Player Manager Functions
    public boolean isCurrentPlayer(player){
        return (currentPlayer == player)
    }  
    public CardPlayer getCurrentPlayer(){
        return currentPlayer;
    }
    public CardPlayer otherPlayer(CardPlayer player1){
        return (currentPlayer==player1)?opponent:currentPlayer;
    }
    public CardPlayer makeRandomPlayer(name){
        
        def player = new CardPlayer(stageManager,name)
        //player.name = name
        
        // make fake random deck
        return player
    }
    
    // Match Function

    // Gameplay function
    public boolean checkEndGame(){
        if (match.player1.score<=0||player2.score<=0){
            if (match.player2.score<=0 && player2.score<=0){
                match.status = CardMatch.MatchStatus.Draw
            } else if (match.player1.score<=0){
                match.status = CardMatch.MatchStatus.Lose  
            } else {
                match.status = CardMatch.MatchStatus.Win
            }
            return true;
        } else if (match.player1.currentDeck.cardList.size()<=0||player2.currentDeck.cardList.size()<=0){
            if (match.player1.currentDeck.cardList.size()<=0 && player2.currentDeck.cardList.size()<=0){
                match.status = CardMatch.MatchStatus.Draw
            } else if (match.player1.currentDeck.cardList.size()<=0) {
                match.status = CardMatch.MatchStatus.Lose  
            } else {
                match.status = CardMatch.MatchStatus.Win  
            }
            return true;
        }
        return true;
    }
    
    public CardPlayer whichPlayerCard(Card card){
        if (match.player1.orgDeck.cardList.contains(card)){
            return match.player1;
        } else {
            return match.player2;
        }
    }
    void fromDeckToHand(CardPlayer player){
        Card card = player.fromDeckToHand()
        //table.fromDeckToHand(card,player)
        CardTable table =  (CardTable) currentLevel
        table.arrangeHand(player)
    }
    void fromHandToGround(Card card){
        CardPlayer player = whichPlayerCard(card);
        println("Selected Card belongs to"+player.name)
        
        CardTable table = (CardTable) currentLevel
        table.fromHandToGround(player,card)
        
        player.hand.remove(card)
        player.ground << card
        
        table.arrangeHand(player)
    }
    
    void destroyCard(card){
        CardPlayer player = whichPlayerCard(card);
        
        CardTable table = (CardTable) currentLevel
        table.destroy(player,card)
        
        player.ground.remove(card)
        
    }
    void fromHandToMagic(Card card){
        CardPlayer player = whichPlayerCard(card);
        println("Selected Card belongs to"+player.name)
        
        CardTable table = (CardTable) currentLevel
        table.fromHandToMagic(player,card)
        
        player.hand.remove(card)
        player.magic << card
        
        table.arrangeHand(player)
    }
    void putDownTable(pos,up){
        
    }
    
    void activeCard(Card starterCard,List targetCards){
        if (starterCard.cardType ==Card.CardType.Magic||starterCard.cardType ==Card.CardType.Trap){
            effect(starterCard,targetCards)
        } else {
            
        }
    }
    
    public void executeScript(String script){
        
    }
    /* Gameplay mechanics */
    def detach(){
        
    }
    def discard(){
        
    }
    def banish(){
        
    }
    public void flip(Card starterCard){
        // notify to chain
        notifyFlip(starterCard)
        executeScript(flipScript)
    }
    public void effect(Card starterCard,def targetCards){
        // notify to chain
        notifyEffect(starterCard)
        executeScript(flipScript)
    }
    
    protected void notifyFlip(Card card){
        
    }
    protected void notifyEffect(Card card){
        
    }

    void attack(Card staterCard,Card targetCard){
        
        if (targetCard!=null){
            // Those two card should be monsters
            if (staterCard.cardType==Card.CardType.Monster && targetCard.cardType == Card.CardType.Monster){
                CardPlayer player1 = whichPlayerCard(staterCard);
                CardPlayer player2 = otherPlayer(player1)
                //starterCard.attackAnimation();
                player2.activeTrapCards();
            
                if (!match.restriction.attackCancel&&!player1.restriction.attackCancel){
                    // normal attack
                    if (startedCard.attack>targetCard.defend){
                        // player2 damaged
                        player2.score -= startedCard.attack-targetCard.defend
                    } else {
                        // player1 get return-damage
                        player1.score -= targetCard.defend - startedCard.attack
                    }
                }
            }
        } else{
            // if target ==null means attack directly to the opponent
            
        }
        
    }
    
    
    void deffend(){
        
    }
    void shuttleDeck(){
        
    }

    void createRules(){
        createSelectConditions();
    }
    /* Select */
    void createSelectConditions(){
        // RULE :  The card is in hand
        def inHandRule ={card->

            CardPlayer player = whichPlayerCard(card)
            if (player.getHand().contains(card)) {
                return true;
            }
            return false; 
        }
        cardSelectRules["inHand"] =new ClosureSelectCondition(inHandRule);
        
        // RULE :  The card is in hand of the current player - who play this device!
        def inHandCurrentRule ={card->
            if (isCurrentPlayer(whichPlayerCard(card))) {
                CardPlayer player = getCurrentPlayer();
                if (player.getHand().contains(card)) {
                    return true;
                }
            }
            return false; 
        }
        cardSelectRules["inHandCurrent"] =new ClosureSelectCondition(inHandCurrentRule);
        
        // RULE : The card is in hand of the current turn player - who has this turn!
        def inHandTurnRule ={card->
            if (isCurrentPlayer(whichPlayerCard(card))) {
                CardPlayer player = getCurrentPlayer();
                if (player.getHand().contains(card)) {
                    return true;
                }
            }
            return false; 
        }
        cardSelectRules["inHandTurn"] =new ClosureSelectCondition(inHandTurnRule);
        // RULE: The card is in ground
        def inGroundRule ={card->

            CardPlayer player = whichPlayerCard(card);
            if (player.ground.contains(card)) {
                return true;
            }
            
            return false; 
        }
        cardSelectRules["inGround"] =new ClosureSelectCondition(inGroundRule);
        // RULE: Card in player Main phase
        def mainPhaseCards = {card->
            if (isCurrentPlayer(whichPlayerCard(card))) {
                CardPlayer player = getCurrentPlayer();
                if (player.hand.contains(card)||player.ground.contains(card)||player.magic.contains(card)) {
                    //println("Condition true");
                    return true;
                } 
            }
            return false; 
        }
        cardSelectRules["mainPhaseCards"] =new ClosureSelectCondition(mainPhaseCards);
        // RULE: Can not select any thing
        def noneSelect = {card->
            return false;
        }
        cardSelectRules["noneSelect"] =new ClosureSelectCondition(noneSelect);
        
    }
    public EntitySelectCondition getCardSelectCondition(){
        return cardSelectRules[currentSelectRule]
    }
    // Utility functions
    
    public void setCardSelectCondition(String con){
        if (!con.equals(currentSelectRule)){
            currentSelectRule = con
            stageManager.selectManager.setEntitySelectCondition(cardSelectRules[currentSelectRule])
        }
    }
    /* Utils */
    public void showDeckLog(deck){
        deck.cardList.each{
            println it.name
            println it.longDesc
        }
    }
    
    public def flipCoin(def a,def b){
        if (rand.nextInt(100)<50){
            return a
        } else {
            return b
        }
    }
    
    void notifyMoveCard(Card card,String type){
        CardSelectControl csc = card.getSpatial().getControl(CardSelectControl.class);
        if (csc!=null){
            switch(type){
            case "noHover":
                csc.doOutHovered()
                csc.setSkipOutHover(true)
                csc.setHoverable(false)
                break;
            case "enableHover":
                csc.doOutHovered()
                csc.setSkipOutHover(false)
                csc.setHoverable(true)
                break;
                
            }
        }
    }
}

