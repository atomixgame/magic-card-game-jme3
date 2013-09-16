package magiccard;

import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import magiccard.gameplay.CardGamePlay;
import magiccard.gameplay.CardLibrary;
import magiccard.gameplay.CardTable;
import magiccard.stage.CardGameSelectManager;
import sg.atom.stage.StageManager;
import sg.atom.stage.WorldManager;

/**
 *
 * @author cuong.nguyenmanh2
 */
public class CardGameStageManager extends StageManager {

    CardTable table;
    CardLibrary cardLib;

    public CardGameStageManager(CardGame app) {
        super(app);
    }

    @Override
    public void initStage() {
        this.worldManager = new WorldManager(app, new Node("_worldNode"));
        cardLib = new CardLibrary();
        cardLib.init();
        //cardLib.getAllCardPics();
        this.gamePlayManager = new CardGamePlay(getApp());
        table = new CardTable(gamePlayManager, this.getWorldManager(), "Level1", "");
        currentLevel = table;
        worldManager.initWorld(currentLevel);
    }

    @Override
    public void configStage() {
        //super.configStage();

        gamePlayManager.initGamePlay(currentLevel);
        selectManager = new CardGameSelectManager(gameGUIManager, this, worldManager);
        selectManager.init();
        //getSelectManager().setCurrentFunction("SelectCard");

        configStageCustom();

    }
    @Override
    public void configStageCustom() {
        super.configStageCustom();

        getApp().getCamera().setLocation(table.getCamPos().setX(0));
        getApp().getCamera().lookAt(table.getCenter().setX(0), Vector3f.UNIT_Y);
        //getApp().getFlyByCamera().setEnabled(true);
    }
    /**
     *
     * @return
     */
    @Override
    public CardGameSelectManager getSelectManager() {
        return (CardGameSelectManager) super.getSelectManager();
    }

    public void goInGame() {
        //finishStage();
        selectManager.setupInputListener();
        attachStage();
        doReadyToPlay();
        this.gamePaused = false;
    }

    public void simpleUpdate(float tpf) {
    }

    public void setCamOperation(boolean camOp, boolean cursorVisible) {
        getApp().getFlyByCamera().setEnabled(camOp);
        getApp().getFlyByCamera().setDragToRotate(!camOp);
        inputManager.setCursorVisible(cursorVisible);
    }



    @Override
    public CardGame getApp() {
        return (CardGame) super.getApp();
    }

    public void goOutGame() {
        worldManager.detachWorld();
        gamePaused = true;
        resume = true;
    }

    public void pauseGame() {
        gamePaused = true;
        selectManager.setEnable(false);
        worldManager.detachWorld();
    }

    public void resumeGame() {
        gamePaused = false;
        selectManager.setEnable(true);
        worldManager.attachWorld();
    }
}
