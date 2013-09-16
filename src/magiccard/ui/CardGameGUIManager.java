/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package magiccard.ui;

import com.jme3.cursors.plugins.JmeCursor;
import com.jme3.material.Material;
import com.jme3.math.Vector2f;
import com.jme3.renderer.ViewPort;
import com.jme3.ui.Picture;
import de.lessvoid.nifty.screen.Screen;
import magiccard.CardGame;
import magiccard.CardGameStageManager;
import sg.atom.core.AtomMain;
import sg.atom.ui.GameGUIManager;
import sg.atom.ui.common.UILoadingScreenController;

/**
 *
 * @author cuong.nguyenmanh2
 */
public class CardGameGUIManager extends GameGUIManager {

    private Material bgMat;
    private Screen oldScreen;

    public CardGameGUIManager(AtomMain app) {
        super(app);

    }

    @Override
    public void initGUI() {

        super.initGUI();
        initBackground("Interface/Images/bg/Blue-Black-Hole.jpg");
        //initBackground("Interface/Images/bg/07.jpg");
    }

    @Override
    public void setupCommonScreens() {
        nifty.registerScreenController(new MainMenuScreenUI(this),
                new UIInGame(this),
                new UILoadingScreenController(this));
        nifty.addXml("Interface/Ingame/Ingame.xml");
        nifty.addXml("Interface/MainMenu/MainMenu.xml");
        nifty.addXml("Interface/MainMenu/Loading.xml");
        nifty.addXml("Interface/MainMenu/PauseScreen.xml");
        nifty.addXml("Interface/MainMenu/Options/Options.xml");
    }

    public void setBackground(String bgFile) {
        bgMat.setTexture("ColorMap", assetManager.loadTexture(bgFile));
    }

    public Material getBgMat() {
        return bgMat;
    }

    void initBackground(String bgFile) {
        bgMat = new Material(assetManager, "MatDefs/Deform2D/Deform2D.j3md");
        bgMat.setTexture("ColorMap", assetManager.loadTexture(bgFile));

        bgMat.setBoolean("MixBlob", true);
        bgMat.setBoolean("MixColAlpha", true);
        bgMat.setVector2("ScrollXY", new Vector2f(0.1f, 0.1f));
        bgMat.setVector2("SkewXY", new Vector2f(0.1f, 0.1f));

        bgMat.setVector2("ScaleXY", new Vector2f(8f, 8f));
        bgMat.setFloat("ScaleTime", 1f);

        createBgQuad();
    }

    public void createBgQuad() {
        Picture p = new Picture("background");
        p.setMaterial(bgMat);
        p.setWidth(app.getSettings().getWidth());
        p.setHeight(app.getSettings().getHeight());
        p.setPosition(0, 0);

        p.updateGeometricState();

        ViewPort pv = app.getRenderManager().createPreView("background", app.getCamera());
        pv.setClearFlags(true, true, true);
        pv.attachScene(p);

        app.getViewPort().setClearFlags(false, true, true);
        p.updateGeometricState();
    }

    public void setBackground(Material bgMat) {
    }

    @Override
    public void initCursors() {
        JmeCursor cursor1 = (JmeCursor) assetManager.loadAsset("Textures/Cursors/GLOVE CURSOR 2.cur");
        //getStageManager().setCamOperation(false, true);
        getApp().getFlyByCamera().setEnabled(false);
        inputManager.setMouseCursor(cursor1);
        inputManager.setCursorVisible(true);
    }

    public void goInGame() {
        goToScreen("InGameScreen");

    }

    public void simpleUpdate(float tpf) {
    }

    @Override
    public CardGame getApp() {
        return (CardGame) super.getApp();
    }

    @Override
    public CardGameStageManager getStageManager() {
        return (CardGameStageManager) super.getStageManager();
    }

    public void goOutGame() {
        nifty.gotoScreen("MainMenuScreen");
    }

    public void pauseGame() {
        //UIInGame inGameScreenControl = (UIInGame) nifty.getCurrentScreen().getScreenController();
        //inGameScreenControl.setResume(true);
        nifty.gotoScreen("PauseScreen");
        setBackground("Interface/Images/bg/Blue-Black-Hole.jpg");
    }

    public void resumeGame() {
        //nifty.gotoScreen("MainMenuScreen");
        nifty.gotoScreen("InGameScreen");
        //UIInGame inGameScreenControl = (UIInGame) nifty.getCurrentScreen().getScreenController();
        //inGameScreenControl.setResume(false);
    }
}
