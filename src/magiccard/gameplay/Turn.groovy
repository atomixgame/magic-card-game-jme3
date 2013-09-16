/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package magiccard.gameplay

public class Turn{
    CardPlayer player
    TurnPhase currentPhase
    List<CardAction> actions
    int num;
    public Turn(int num){
        this.num = num;
    }
    
}

