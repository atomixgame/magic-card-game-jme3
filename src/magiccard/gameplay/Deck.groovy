package magiccard.gameplay
import java.util.Random
/**
 *
 * @author cuong.nguyenmanh2
 */
public class Deck {
    String name = ""
    String version = ""
    String author = ""
    int status = -1
    int level = -1
    String packPath = ""
    private static Random RND=new Random()
    List cardList=[]
    public void clone(List orgCardList){
        cardList.addAll(orgCardList)
    }
        
    public void clone(Deck orgDeck){
        cardList.addAll(orgDeck.cardList)
        
    }
    public void shuffle(){
        Collections.shuffle(cardList, RND)
    }
    public void shuffleLocal(){
        Collections.shuffle(cardList, RND)
    }
}

