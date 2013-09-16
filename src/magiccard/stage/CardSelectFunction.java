package magiccard.stage;

import com.jme3.input.event.MouseButtonEvent;
import com.jme3.input.event.MouseMotionEvent;
import magiccard.gameplay.Card;
import magiccard.ui.UIInGame;
import sg.atom.ui.GameGUIManager;
import sg.atom.entity.Entity;
import sg.atom.stage.SelectManager;
import sg.atom.stage.select.function.SingleSelectFunction;

/**
 *
 * @author cuong.nguyenmanh2
 */
public class CardSelectFunction extends SingleSelectFunction {

    public CardSelectFunction(SelectManager manager) {
        super(manager);
    }

    @Override
    public void mouseMove(MouseMotionEvent evt) {
    }

    @Override
    public void mouseButton(MouseButtonEvent evt) {
        if (evt.getButtonIndex() == 0 && evt.isPressed()) {
            funcSelect();
        }
    }

    @Override
    protected void doSelectSingleEntity(Entity entity) {
        //super.doSelectSingleEntity(entity);

        // display the info to the screen
        GameGUIManager gameGUIManager = selectManager.getGameGUIManager();
        Card card = (Card) entity;
        // display entity info
        UIInGame info = (UIInGame) gameGUIManager.getNifty().getCurrentScreen().getScreenController();
        info.setCardInfoText(card.getFullDesc());

        info.setCardPicture(card.getPicture());
        //System.out.println(" Select card !");
        /*
        CardSelectControl csc = card.getSpatial().getControl(CardSelectControl.class);
        if (csc.isHovered()) {
            csc.doOutHovered();
        }
        csc.setHoverable(false);
        */
    }
}
