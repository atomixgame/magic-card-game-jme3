/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package magiccard.gameplay

public class TurnPhase{
    
    public static enum TurnPhaseType {DrawPhase,StandbyPhase,MainPhase,BattlePhase,MainPhase2,EndPhase
        public TurnPhaseType getNext(){
            return values()[(ordinal()+1) % values().length];
        }
    
        public  TurnPhaseType getPrevious(){
            return values()[(ordinal()-1 >0)?ordinal()-1 : values().length];
        }
    
        public  boolean isNext(TurnPhaseType phase){
            return ordinal()==phase.ordinal()+1;
        }
    
        public  boolean isPrevious(TurnPhaseType phase){
            return ordinal()==phase.ordinal()-1;
        }
    }
    
    TurnPhaseType type
    boolean monsterSummoned=false;    
    
    TurnPhase(TurnPhaseType type){
        this.type = type
    }


}


