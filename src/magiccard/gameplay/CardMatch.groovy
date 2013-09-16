package magiccard.gameplay

import magiccard.gameplay.rule.CardGameRestriction
/**
 *
 * @author cuong.nguyenmanh2
 */
public class CardMatch {
    CardPlayer player1
    CardPlayer player2
    
    enum MatchStatus {
        Win,Lose,Draw,None,End
    }
        
    MatchStatus status = MatchStatus.None
    int player1Score = 0
    int player2Score =0
    CardGameRestriction restriction
}

