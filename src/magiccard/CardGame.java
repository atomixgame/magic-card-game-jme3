package magiccard;

import com.jme3.asset.plugins.FileLocator;
import com.jme3.audio.AudioRenderer;
import com.jme3.niftygui.NiftyJmeDisplay;
import com.jme3.renderer.RenderManager;
import com.jme3.system.AppSettings;
import de.lessvoid.nifty.Nifty;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.logging.Level;
import java.util.logging.Logger;
import magiccard.state.MainMenuState;
import magiccard.ui.CardGameGUIManager;
import sg.atom.core.AtomMain;

/**
 *
 */
public class CardGame extends AtomMain {

    protected Nifty nifty;
    protected NiftyJmeDisplay niftyDisplay;
    protected AudioRenderer audioRenderer;

    public static void main(String[] args) {

        CardGame app = new CardGame();

        AppSettings settings = new AppSettings(true);
        /*
         settings.setHeight(600);
         settings.setWidth(800);
         */
        settings.setHeight(768);
        settings.setWidth(1027);
        app.setShowSettings(false);
        app.setSettings(settings);
        app.start();

    }

    @Override
    public void simpleInitApp() {
        String projectDir = CardGame.getProjectParentPath();
        projectDir = projectDir.replaceAll("/", "//");
        System.out.println(" " + projectDir);
        assetManager.registerLocator(projectDir + "//References//Yugi//AllCardPacks//", FileLocator.class);

        setDisplayStatView(false);
        initGameStateManager();
        startup();
    }

    @Override
    public void initGUI() {
        gameGUIManager = new CardGameGUIManager(this);
        gameGUIManager.initGUI();
    }

    @Override
    public void initStage() {
        stageManager = new CardGameStageManager(this);
        stageManager.initStage();
    }

    @Override
    public void initGameStateManager() {
        gameStateManager = new CardGameStateManager(this);
        gameStateManager.initState();
    }

    @Override
    public void startup() {
        gameStateManager.setStartupState(MainMenuState.class);
        gameStateManager.startUp();
    }

    @Override
    public void simpleUpdate(float tpf) {
        //TODO: add update code
    }

    @Override
    public void simpleRender(RenderManager rm) {
        //TODO: add render code
    }

    @Override
    public CardGameGUIManager getGameGUIManager() {
        return (CardGameGUIManager) super.getGameGUIManager();
    }

    @Override
    public CardGameStateManager getGameStateManager() {
        return (CardGameStateManager) super.getGameStateManager();
    }

    public static String getProjectPath() {
        return getProjectParentPath() + "MagicCards";
    }

    public static String getProjectParentPath() {
        String buildDir = CardGame.getCurrentJarFilePath();
        String projectDir = buildDir.substring(1, buildDir.lastIndexOf("MagicCards"));
        return projectDir;
    }

    public static String getCurrentJarFilePath() {
        try {
            String path = CardGame.class.getProtectionDomain().getCodeSource().getLocation().getPath();
            String decodedPath = URLDecoder.decode(path, "UTF-8");
            return decodedPath;
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(CardGame.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "";
    }
}
