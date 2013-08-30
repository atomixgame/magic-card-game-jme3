/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package magiccard;

import java.util.logging.Level;
import java.util.logging.Logger;
import magiccard.state.InGameState;
import magiccard.state.LoadingState;
import magiccard.state.MainMenuState;
import sg.atom.core.AtomMain;
import sg.atom.core.GameStateManager;

/**
 *
 * @author cuong.nguyenmanh2
 */
public class CardGameStateManager extends GameStateManager {

    public CardGameStateManager(AtomMain app) {
        super(app);
    }

    public void goInGame() {
        LoadingState loadingState = stateManager.getState(LoadingState.class);
        boolean detached = stateManager.detach(loadingState);
        stateManager.attach(new InGameState());
        Logger.getLogger(CardGameStateManager.class.getName()).log(Level.INFO, "Detach Loading State");
    }

    @Override
    public void loadGame() {
        MainMenuState menuState = stateManager.getState(MainMenuState.class);
        boolean detached = stateManager.detach(menuState);
        stateManager.attach(new LoadingState());
    }

}
