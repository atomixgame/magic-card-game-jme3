
package magiccard.ui.query

import de.lessvoid.nifty.screen.ScreenController
import de.lessvoid.nifty.screen.Screen
import de.lessvoid.nifty.Nifty
import de.lessvoid.nifty.controls.Button
import de.lessvoid.nifty.controls.button.ButtonControl
import de.lessvoid.nifty.elements.Element
import de.lessvoid.nifty.elements.render.TextRenderer
import de.lessvoid.nifty.elements.render.ImageRenderer
import de.lessvoid.nifty.render.NiftyImage
import de.lessvoid.nifty.effects.*
import de.lessvoid.nifty.EndNotify


/**
 *
 * @author cuong.nguyenmanh2
 */
public  class GQElement implements GroovyInterceptable {
    /* Proxy or Decorator pattern */
    public Element el
    public GQElement(Element el){
        this.el = el
    }
        
    // Intercept the delegate, with invoke magics!
    public def invokeMethod(String name, args) {
        // List passthrough
        if (["each","find"].contains(name)){
            //println name + " * "
            def clos = args[0]
            if (!clos instanceof Closure){
                throw new IllegalArgumentException("No Closure");
            }
            if (!el.getElements().isEmpty()){
                el.getElements().collect{new GQElement(it)}."$name"(clos)
            }else {
                //throw new IllegalArgumentException("No Closure");
            }
        } else {
            def metaMethod = metaClass.getMetaMethod(name, args)
            if (metaMethod!=null){
                metaMethod.invoke(this, args)
            
            } else {
                return el.invokeMethod(name, args)
            }
        }
    }
    public def getProperty(String name){
        if (metaClass.hasProperty(this,name)){
            return this."$name"
        } else {
            return el."$name"
        }
    }    
    void setProperty(String name, value) {
        if (metaClass.hasProperty(this,name)){
            this."$name"=value
        } else {
            el."$name"=value
        }
        
    }
    // selector
        
    // Nifty currently only support one level deep - Id. Which is sad!
    public def getText(){
        def btn = el.getControl(ButtonControl.class)
        if (btn!=null){
            btn.getText();
        } else {
            el.getRenderer(TextRenderer.class)?.getText();
        }
        return this;
    }
    public def setText(str){
        def btn = el.getControl(ButtonControl.class)
        if (btn!=null){
            btn.setText(str);
        } else {
            // direct
            el.getRenderer(TextRenderer.class)?.setText(str);
        }
        return this;
    }
    public def startEffect(eventName,clos,efName){
        el.startEffect(EffectEventId."$eventName", clos as EndNotify, efName);
        return this;
    }
    /* List passthrough */
    public def qels(){
        if (!el.getElements().isEmpty()){
            el.getElements().collect{new GQElement(it)}
        }
    }
    public def els(){
        if (!el.getElements().isEmpty()){
            el.getElements()
        }
    }
}
    

