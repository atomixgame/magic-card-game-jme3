/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package magiccard.gameplay

import com.jme3.scene.Spatial
import sg.atom.entity.SpatialEntity
/**
 *
 * @author cuong.nguyenmanh2
 */
public class Card  extends SpatialEntity{
    public enum CardType {Magic,Monster,Trap}
    public enum CardRaces {Demon,Human,Beast,Ghost}
    public enum CardElementType {Ice,Fire,Earth,Air,Ocean,Tree}
    String cardId=""
    String name = ""
    String picture = ""

    int attack = -1
    int defend = -1
    String cardType = ""
    
    String attribute = ""
    String cardSubType = ""
    int level = -1
    
    String summonRule = ""
    String effect = ""
    String desc = ""
    String longDesc = ""
    String character = ""
    String flipScript=""
    String effectScript=""
    String rarity=""
    Card orgCard
    
    public Card(String name){
        super(name,name)
        this.name = name
    }
    
    public Card(Card orgCard){
        super(orgCard.name,orgCard.name)
        this.orgCard = orgCard
        this.cardId = orgCard.cardId
        this.name = orgCard.name
        this.picture = orgCard.picture
        this.attack = orgCard.attack
        this.defend = orgCard.defend
        this.cardType = orgCard.cardType
        //this.cardElementType = orgCard.cardElementType
        this.level = orgCard.level
        this.desc = orgCard.desc
        this.longDesc = orgCard.longDesc
    }

    public String getFullDesc(){
        String str = "";

        if (cardType.toLowerCase().endsWith("monster")){        
            str +="Attack:  "+this.attack+"\n";
            str +="Defend:  "+this.defend+"\n";
            str +="-------------------------\n";
        } else {
    
        }
        str +="["+cardId+"] "+this.longDesc+"\n";
        return str;
    } 
    
    public String toString(){
        String str="";
        str =str + "Name : "+this.name+"\n"
        str =str +" picture : "+this.picture +"\n"
        str =str +" attack : "+this.attack+"\n"
        str =str +" defend : "+this.defend+"\n"
        str =str +" cardType : "+this.cardType+"\n"
        //this.cardElementType = orgCard.cardElementType
        str =str +" level : "+this.level+"\n"
        
    }
    
    public boolean isMonsterCard(){
        return cardType.toLowerCase().endsWith("monster");
    }
    public boolean isTrapCard(){
        return cardType.toLowerCase().endsWith("trap");
    }
}

