/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package magiccard.ui

import com.jme3.material.Material
import com.jme3.math.ColorRGBA
import com.jme3.scene.shape.Quad
import sg.atom.gameplay.GameLevel
import sg.atom.stage.GamePlayManager
import java.util.logging.Level
import java.util.logging.Logger

import de.lessvoid.nifty.screen.ScreenController
import de.lessvoid.nifty.screen.Screen
import de.lessvoid.nifty.Nifty
import de.lessvoid.nifty.controls.Button
import de.lessvoid.nifty.elements.Element
import de.lessvoid.nifty.elements.render.TextRenderer
import de.lessvoid.nifty.elements.render.ImageRenderer
import de.lessvoid.nifty.render.NiftyImage
import de.lessvoid.nifty.effects.*
import de.lessvoid.nifty.EndNotify

import magiccard.gameplay.CardLibrary
import magiccard.gameplay.CardGamePlay
import magiccard.gameplay.CardMatch
import magiccard.gameplay.TurnPhase
import static magiccard.gameplay.TurnPhase.TurnPhaseType.*
/**
 *
 * @author cuong.nguyenmanh2
 */
public class UIInGame implements ScreenController{
    CardGameGUIManager gameGUIManager;
    Screen screen;
    Element player1Score;
    Element player2Score;
    Element cardDesc;

    Element cardPicture;
    Element btnDP;
    Element btnSP;
    Element btnMP;
    Element btnBP;
    Element btnMP2;
    Element btnEP;
    
    Element txtAlertFlash;
    Element txtAlertSmall;
    Element alertSmallPanel;
    Element dialogUI;
    TurnPhase.TurnPhaseType currentTurnPhase;
    CardGamePlay gameplay;
    
    Element p1TurnText;
    Element p2TurnText;
    boolean resume = false;
    
    public UIInGame(CardGameGUIManager gameGUIManager) {
        this.gameGUIManager = gameGUIManager;
    }
    
    public void bind(Nifty nifty, Screen screen) {
        if (!screen.getScreenId().equals("PauseScreen")){
            if (!resume){
                this.screen = screen;
                gameGUIManager.getInputManager().setCursorVisible(true);
                gameplay = gameGUIManager.app.stageManager.gamePlayManager;
                player1Score = screen.findElementByName("Player1Score");
                player2Score =  screen.findElementByName("Player2Score");
        
                cardDesc = screen.findElementByName("CardDesc");
                cardPicture = screen.findElementByName("CardPicture");
                Logger.getLogger(this.class.getName()).log(Level.INFO, "Bind Ingame screen controller!");
        
                btnDP  = screen.findElementByName("btnDP");
                btnSP  = screen.findElementByName("btnSP");
                btnMP  = screen.findElementByName("btnMP");
                btnBP  = screen.findElementByName("btnBP");
                btnMP2  = screen.findElementByName("btnMP2");
                btnEP  = screen.findElementByName("btnEP");
        
                txtAlertFlash = screen.findElementByName("alertFlash");
                txtAlertSmall  = screen.findElementByName("alertSmall");
                alertSmallPanel  = screen.findElementByName("alertSmallPanel");
                dialogUI = screen.findElementByName("dialog");
                
                p1TurnText = screen.findElementByName("Player1Turn");
                p2TurnText = screen.findElementByName("Player2Turn");
                resume=true;
            }
        }
    }

    public void onStartScreen() {
        if (!screen.getScreenId().equals("PauseScreen")){
            player1Score.getRenderer(TextRenderer.class).text = "1000";
            player2Score.getRenderer(TextRenderer.class).text = "1000";
            alertSmall("Start the game");
            
        }
    }

    public void onEndScreen() {
    }
    
    public void setScores(int score1,int score2){
        player1Score.getRenderer(TextRenderer.class).text = ""+score1;
        player2Score.getRenderer(TextRenderer.class).text = ""+score2;
    }
    
    public void setCardInfoText(String text){
        text = text.replace("&lt;br&gt;","\n");
        text = text.replace("&lt;li&gt;","\no");
        text = text.replace("&middot;","\no");
        text = text.replace("&quot;","'");
        cardDesc.getRenderer(TextRenderer.class).text = text;
    }
    
    public void setTurn(String player){
        if (player.equals("Player1")){
            p1TurnText.getRenderer(TextRenderer.class).text = "Turn"
            p2TurnText.getRenderer(TextRenderer.class).text = ""
        } else {
            p1TurnText.getRenderer(TextRenderer.class).text = ""
            p2TurnText.getRenderer(TextRenderer.class).text = "Turn"
            disableAllButtons();
        }
    }
    public void setCardPicture(String path){
        NiftyImage img = gameGUIManager.getNifty().getRenderEngine().createImage(screen,path, false);
        cardPicture.getRenderer(ImageRenderer.class).setImage(img);
    }
    
    public void setTurnPhase(TurnPhase.TurnPhaseType nextTurnPhase){
        if (this.currentTurnPhase!=nextTurnPhase){
            setButtonFocus();
            this.currentTurnPhase = nextTurnPhase;
        }
        //this.screen.resetLayout() 
    }
    
    void setButtonFocus(TurnPhase.TurnPhaseType nextTurnPhase){
        switch(nextTurnPhase){
            // What phase
                
        case DrawPhase:
            btnDP.enable();
            btnDP.setFocus();
            btnSP.disableFocus();
            btnMP.disableFocus();
            btnBP.disableFocus();
            btnMP2.disableFocus();
            btnEP.disableFocus();
            break;
        case StandbyPhase:
            btnDP.disable();
            btnSP.enable();
            
            btnSP.setFocus();
            btnMP.disableFocus();
            btnBP.disableFocus();
            btnMP2.disableFocus();
            btnEP.disableFocus();
            break;
        case MainPhase:
            btnDP.disable();
            btnSP.disable();
            btnMP.enable();
            btnMP.setFocus();
            btnBP.disableFocus();
            btnMP2.disableFocus();
            btnEP.disableFocus();
            break;
        case BattlePhase:
            btnDP.disable();
            btnSP.disable();
            btnMP.disable();
            btnBP.enable();
            btnBP.setFocus();
            btnMP2.disableFocus();
            btnEP.disableFocus();
            break;
        case MainPhase2:
            btnDP.disable();
            btnSP.disable();
            btnMP.disable();
            btnBP.disable();
            btnMP2.enable();
            btnMP2.setFocus();
            btnEP.disableFocus();
            break;
        case EndPhase:
            btnEP.setFocus();
            //println("EndPhase button activate!")
            break;
        }
    }
    void disableAllButtons(){
        btnDP.disable();
        btnSP.disable();
        btnMP.disable();
        btnBP.disable();
        btnMP2.disable();
        btnEP.disable();
    }
    
    
    public void alertFlash(String alertMsg){
        txtAlertFlash.getRenderer(TextRenderer.class).setText(alertMsg);
        //println("Set text !");
        //alert.startEffect(EffectEventId.onCustom,null,"showIt");
        txtAlertFlash.startEffect(EffectEventId.onCustom,new InGameDefaultNotify(),"endIt");
    }
    
    public void alertSmall(String alertMsg){
        txtAlertSmall.getRenderer(TextRenderer.class).setText(alertMsg);
        alertSmallPanel.startEffect(EffectEventId.onCustom,new InGameDefaultNotify(),"endIt");
    }
    
    class InGameDefaultNotify implements EndNotify{
        InGameDefaultNotify() {
        }
        @Override
        public void perform() {
            //txtAlertSmall.getRenderer(TextRenderer.class).setText("");
        }
    }
    
    public void drawPhase(){
        goToPhase(DrawPhase)
    }
    public void mainPhase(){
        goToPhase(MainPhase)
    }
    public void standByPhase(){
        goToPhase(StandbyPhase)
    }
    public void battlePhase(){
        goToPhase(BattlePhase)
    }
    public void mainPhase2(){
        goToPhase(MainPhase2)
    }
    public void endPhase(){
        showDialog()
    }
    void goToPhase(TurnPhase.TurnPhaseType aPhase){
        if (currentTurnPhase.getNext()==aPhase){
            gameplay.setPlayerChangePhase(aPhase)
        } else {
            setButtonFocus(currentTurnPhase);
            alertSmall("You can not change Phase at this time");
        }
    }
    
    class DialogUINotify implements EndNotify {

        boolean isOpen;

        DialogUINotify(boolean isOpen) {
            this.isOpen = isOpen;
        }

        public void perform() {
            //inventoryOpen = isOpen;
        }
    }

    void closeDialog() {
        //dialogUI.resetAllEffects();
        dialogUI.startEffect(EffectEventId.onCustom, new DialogUINotify(false), "moveOut");
    }
    /*
    void showDialog(String message,String[] options,String[] buttons) {
    dialogUI.resetAllEffects();
    dialogUI.startEffect(EffectEventId.onCustom, new DialogUINotify(true), "moveIn");
    }
     */
    void showDialog() {
        if (dialogUI!=null){
            //dialogUI.resetAllEffects();
            dialogUI.startEffect(EffectEventId.onCustom, new DialogUINotify(true), "moveIn");
        } else {
            println("dialog is null!")
        }
    }
}

