package magiccard.ui.query

import de.lessvoid.nifty.screen.ScreenController
import de.lessvoid.nifty.screen.Screen
import de.lessvoid.nifty.elements.Element
import de.lessvoid.nifty.Nifty
/**
 *
 * @author cuong.nguyenmanh2
 */
/* Short,compact,low-performance version of GQuery for Nifty!!! */
public class GQuery {
    static GQuery defaultInstance
    Screen screen
    def currentEl
    
    // FIX : Try singleton in Groovy, BAD!
    private GQuery(Screen screen){
        this.screen = screen
        //defaultInstance= this
    }
    public static GQuery _(Screen screen){
        return getDefault(screen);
    }
    public static GQuery q(Screen screen){
        return getDefault(screen);
    }
    public static GQuery getDefault(Screen screen){
        if (defaultInstance==null){
            defaultInstance=new GQuery(screen)
        } else {
            defaultInstance.screen = screen;
        }
        
    }
    // Travel selector
    def travel(){
        
    }
    def nativeTravel(){
        
    }
    // Pass through
    def find(){
        
    }

    // with q
    def qTravel(){
        
    }
    def qFind(){
        
    }
    // One door
    public def qel(def nel){
        if (nel instanceof String){
            if (nel in ["button"]){
                // Builder
            }else{
                // Selector name
                qelByName(nel)
            }
        } else {
            return new GQElement(nel);
        }
    }
    
    //
    public def elByName(elName){
        screen.findElementByName(elName);
    }
    public def qelByName(elName){
        qel(elByName(elName))
    }
    /* Static */
    public static def elByName(screen,elName){
        screen.findElementByName(elName);
    }
    public static def qel(Element nel){
        return new GQElement(nel);
    }
    public static def qel(screen,elName){
        qel(elByName(screen,elName))
    }
}

