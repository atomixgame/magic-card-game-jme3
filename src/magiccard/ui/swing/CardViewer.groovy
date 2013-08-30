package magiccard.ui.swing

import javax.swing.JPanel
import groovy.swing.j2d.*
class CardViewer extends GraphicsPanel{
    GraphicsBuilder gb = new GraphicsBuilder();
    
    CardViewer(){
        super()
        setCard()
    }
    void setCard(){
        this.go = getDrawCard()
    }
    
    def getDrawCard(){
        gb.rect(x:0,y:0,width:20,height:20)
    }
}

