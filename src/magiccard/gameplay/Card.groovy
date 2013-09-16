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
    
    /* Constants */
    
    public enum CardType {Magic,Monster,Trap}
    public enum CardRaces {Demon,Human,Beast,Ghost}
    public enum CardElementType {Ice,Fire,Earth,Air,Ocean,Tree}
    
    
    /*
Attributes	

    DARK
    DIVINE
    EARTH
    FIRE
    LAUGH
    LIGHT
    WATER
    WIND 

Types	

    Aqua
    Beast
    Beast-Warrior
    Charisma†
    Creator God
    Dinosaur
    Divine-Beast
    Dragon
    Fairy
    Fiend
    Fish
    Insect
    Machine
    Plant
    Psychic
    Pyro
    Reptile
    Rock
    Sea Serpent
    Spellcaster
    Thunder
    Warrior
    Winged Beast
    Zombie
    Archetype
    Human
    Immortal
    ?
    ???
    Black Magic
    White Magic
    Illusion Magic
    Series 

Secondary types	

    Toon
    Spirit
    Union
    Gemini
    Tuner
    Dark Tuner
    Armor
    Plus
    Minus
    Plus Minus


    */
    
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
    def effected = []
    
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
}

