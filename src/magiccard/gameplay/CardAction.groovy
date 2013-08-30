/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package magiccard.gameplay

    
public class CardAction{
    enum CardActionType{
        Attack,Switch,Effect,Flip,Trap
    }
    Card starter;
    List<Card> targets;
}

