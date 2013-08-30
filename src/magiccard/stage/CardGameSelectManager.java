package magiccard.stage;

import sg.atom.core.GameGUIManager;
import sg.atom.core.StageManager;
import sg.atom.stage.SelectManager;
import sg.atom.stage.WorldManager;
import sg.atom.stage.select.SelectFunction;
import magiccard.gameplay.*;

/**
 *
 * @author cuong.nguyenmanh2
 */
public class CardGameSelectManager extends SelectManager {

    SelectFunction cardSelect;

    public CardGameSelectManager(GameGUIManager gameGUIManager, StageManager stageManager, WorldManager worldManager) {
        super(gameGUIManager, stageManager, worldManager);

    }

    @Override
    public void init(){
        super.init();
        // 
        System.out.println(" Finish init Card Select Manager");
        //CardGamePlay gamePlay= (CardGamePlay)this.getStageManager().getGamePlayManager();
        cardSelect = new CardSelectFunction(this);    
        //setEntitySelectCondition(gamePlay.getCardSelectCondition());
        setCurrentFunction("SelectCard");
        setTrackHover(true);
    }
    
    public void setCurrentFunction(String func) {
        if (func.equals("SelectCard")) {
            setSelectFunction(cardSelect);
        } else {
            setSelectFunction(null);
        }
    }
}
