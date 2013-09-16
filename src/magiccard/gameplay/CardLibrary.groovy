/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package magiccard.gameplay

import magiccard.gameplay.CardLibrary
import java.security.CodeSource
import magiccard.gen.URandom
import magiccard.gen.RandomEnum
import magiccard.CardGame
import java.util.Random
import org.codehaus.groovy.control.customizers.ImportCustomizer
import org.codehaus.groovy.control.CompilerConfiguration

public class CardLibrary {
    int numOfCards = 100
    List cards = []
    List cardPics = []
    def cardPicDir = "Textures/Cards/";
    def cardDefDir = "../References/Yugi/AllCardPacks/"
    def appDir = ""
    Random RND = new Random();
    Map decks=[:]
    
    public CardLibrary(){
        
    }
    
    void init(){
        /*
        namePattern = names.split()
        getAllCardPics()
        cards.addAll(makeRandomCards(numOfCards))
        //Library card list
        
        cards.each{
        println it.name
        }
         */ 
        int cardCount = 0;
        int deckCount = 0;
        appDir = CardGame.getProjectPath();
        println(appDir);
        // From the all pack dir
        new File(cardDefDir).eachFile{aFile->
           
            if (aFile.isDirectory()){
                if (deckCount>0&&deckCount<10){
                    try {
                        def sDeck = getDeckPackFromScript(aFile.name)
                        sDeck.packPath = cardDefDir+aFile.name
                        //println(aFile.name)
                        
                        sDeck.cardList.each{card->
                            //println card.name + " " + card.attack + " " + card.defend
                            cardCount++
                            String imgPath = appDir +"//"+ cardDefDir+aFile.name+"//"+card.picture
                            
                            File imgFile = new File(imgPath)
                            if (imgFile.isFile()){
                                card.picture = aFile.name+"//"+card.picture
                                cards << card
                            
                            } else {
                                throw new RuntimeException("Image not found ! "+imgPath)
                            }
                            
                        }

                        //decks[aFile.name] = sDeck
                    } catch(Exception e){
                        println("Bad syntax in "+ aFile.name)
                    }  catch(Error e){
                        println("Bad syntax in "+ aFile.name)
                    }
                    
                }
                deckCount++;
            }
            
        }
        println "Total " + cardCount +"cards!"
        numOfCards = cardCount;
        // load all the .txt files
        
    }
    //GroovyScriptEngine gse = new GroovyScriptEngine([cardDefDir]);
    public def getDeckPackFromScript(String name){
        // Add imports for script.
        def importCustomizer = new ImportCustomizer()
        importCustomizer.addStarImport('magiccard.gameplay.')
        def configuration = new CompilerConfiguration()
        configuration.addCompilationCustomizers(importCustomizer)
      
        String path = name +"/" +name+"_cards.txt"
        
        // Bind vars
        Binding binding = new Binding();
        def deck = new Deck();
        binding.setVariable("deck", deck)
        
        // Create shell and execute script.
        //gse.run(path, binding);

        def shell = new GroovyShell(binding,configuration)
        shell.evaluate new File(cardDefDir+path)
        return deck;
    }
    
    public def getDeckPackImagePaths(String name){
        def result = []
        String path = cardDefDir + name +"/img/" 
        println (path)
        new File(path).eachFile{aFile->
            if (aFile.isFile()&&getExtension(aFile).equals("jpg")){
                result << path + aFile.name
            }
        }
        return result
    }
    
    def getCardsByCat(String cat){
        
    }
    
    def getCardsByElement(String elementType){
        
    }
    
    def getCardsByStars(int numOfStars){
        
    }
    public List chooseRandomCards(int num){
        def rcards = []
        if (!cards.isEmpty()){
            (1..num).each{
                def orgCard = cards.get(RND.nextInt(cards.size() -1))
                rcards<<new Card(orgCard)
            }
        } else {
            throw new RuntimeException("CardLibrary is empty !")
        }
        return rcards
    }
      
    public void getAllCardPics(){
        
        def assetDir = "/../assets/";
        File f= new File(getJarPath()+assetDir+cardPicDir);
        //println f.absolutePath;
        
        f.eachFileMatch(~/.*?\.jpg/){ 
            //println it.name 
            cardPics << (cardPicDir + it.name)
        }

    }
    
    public String getJarPath(){
        CodeSource codeSource = this.class.getProtectionDomain().getCodeSource();
        File jarFile = new File(codeSource.getLocation().toURI().getPath());
        String jarDir = jarFile.getParentFile().getPath();
        return jarDir;
    }
    /*
     * Get the extension of a file.
     */  
    public static String getExtension(File f) {
        String ext = null;
        String s = f.getName();
        int i = s.lastIndexOf('.');

        if (i > 0 &&  i < s.length() - 1) {
            ext = s.substring(i+1).toLowerCase();
        }
        return ext;
    }

    class RandomCardGenerator{
        def names = 'and marc son bert wick ness ton shire step ley ing sley'
        def namePattern
        Random RND = new Random();
        int maxAtk = 3500
        int maxDeffend = 3500
    
        public String getRandomMagicName(){
            String rname = ((0..2).collect { 
                    namePattern[new Random().nextInt(namePattern.size())] 
                }.join('').capitalize() + ' ' + (0..7).collect { 
                    namePattern[new Random().nextInt(namePattern.size())] 
                }.join('').capitalize())
        
            return rname
        }
    
        public List makeRandomCards(int num){
            def rcards = []
            (1..num).each{
                def card = new Card(getRandomMagicName())
                card.desc = card.name +
            """
is a crazy card you know. This a 
long long long longlong longlong long
long longlong longlong longlong longlong long
long longlong longlong longlong long text
            """
                card.longDesc = card.name +
            """
is a crazy card you know. This a 
long long long longlong longlong long
long longlong longlong longlong longlong long
long longlong longlong longlong long text
            """ 
            
                card.cardType = new RandomEnum<Card.CardType>(Card.CardType.class).random()
                card.picture = URandom.random(cardPics)
                //card.type = URandom.random(Card.CardType.class);
                switch (card.cardType){
                case Card.CardType.Monster:
                    // Random demon
                    card.attack = RND.nextInt(maxAtk)
                    card.defend = RND.nextInt(maxDeffend)
                    card.desc = card.desc+ "\n Attack:"+card.attack+"\n  Defend:"+card.defend;
                    break
                case Card.CardType.Magic:
                    // Random magic
                    break
                case Card.CardType.Trap:
                    // Random trap
                    break
                }
            
                // add card to library
                rcards << card
            }
            return rcards
        }
    }
    
    /* Generate cards */
}


