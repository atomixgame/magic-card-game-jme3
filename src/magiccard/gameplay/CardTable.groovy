package magiccard.gameplay

import com.jme3.asset.AssetManager
import com.jme3.material.Material
import com.jme3.math.ColorRGBA
import com.jme3.math.Vector3f
import com.jme3.scene.Node
import com.jme3.scene.Geometry
import com.jme3.scene.Spatial
import com.jme3.scene.shape.Box
import com.jme3.scene.shape.Quad
import java.security.CodeSource
import sg.atom.gameplay.GameLevel
import sg.atom.gameplay.GamePlayManager
import sg.atom.stage.WorldManager

import com.jme3.material.RenderState
import com.jme3.renderer.queue.RenderQueue
import com.jme3.scene.shape.Sphere
import com.jme3.math.Quaternion
import com.jme3.math.FastMath
import magiccard.stage.CardSelectControl
import sg.atom.fx.particle.ParticleFactory
import sg.atom.fx.particle.ExplosionNodeControl
import com.jme3.light.PointLight
import com.jme3.effect.ParticleEmitter

/**
 *
 * @author cuong.nguyenmanh2
 */

/*
 * CardTable represent the GameLevel. It helps
 * 
 * Load, Create, Arrange (positions),Animate the CardSpatial and its Character's model
 */
class CardTable extends GameLevel{

    private Spatial cardOrg;
    AssetManager assetManager
    
    // Layout to position cards and table
    Vector3f center = new Vector3f(3,0,-5);
    float handLength= 2.5f
    float spaceBetweenCard = 0.5f
    float peakPos= 0.2f
    float boardSizeX = 16;
    float boardSizeY= 16;
    
    Vector3f scaledCardSize =  new Vector3f(1.9f,2.7f,0.02f);
    Vector3f gridGapSize = new Vector3f(1,1,1);
    public static faceUpQuat = new Quaternion().fromAngles(0f,FastMath.PI,0f);
    public static faceDownQuat = new Quaternion().fromAngles(-FastMath.PI,-FastMath.PI,0f);
    List actions =[]
    
    public CardTable(GamePlayManager gameplay,WorldManager world,String name,String path){
        super(gameplay,world,name,path)
        assetManager = world.assetManager
    }
    /* Load */
    public void createCardBoard() {
        //levelNode = new Node("LevelNode");

        Quad cardBoardShape = new Quad(boardSizeX,boardSizeY);
        Geometry cardBoard = new Geometry("CardBoardGeo", cardBoardShape);

        Material mat = new Material(assetManager, "MatDefs/ColoredTextured.j3md");
        mat.setTexture("ColorMap", assetManager.loadTexture("Textures/CardBoard/DiffuseTex.png"));
        mat.setColor("Color", new ColorRGBA(1, 1, 1, 0.9f));
        mat.getAdditionalRenderState().setBlendMode(RenderState.BlendMode.Alpha);
        cardBoard.setMaterial(mat);
        cardBoard.setLocalTranslation(center.add((float)(-boardSizeX/2),(float)(-boardSizeY/2),0f));
        cardBoard.setQueueBucket(RenderQueue.Bucket.Transparent);
        
        levelNode.attachChild(cardBoard);
        
        // Color makers
        createSphere(0.1f,ColorRGBA.Red,center);
        //createSphere(0.1f,ColorRGBA.Blue,vec3(3,5,-5));
        //createSphere(0.1f,ColorRGBA.Yellow,vec3(-2,5,-5));
        
        createCardOrg();

    }
    public void createSphere(float size,ColorRGBA color,Vector3f pos){
        Sphere sh=new Sphere(8,8,size)
        Geometry sGeo = new Geometry("S",sh)
        Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        sGeo.material = mat
        mat.setColor("Color",color)
        sGeo.localTranslation = pos
        levelNode.attachChild(sGeo)
    }
    public void createCardOrg(){
        cardOrg = ((Node) ((Node) assetManager.loadModel("Models/Cards/Template/card1.j3o")).getChild("Card")).getChild("Cube1");
    }
    public void loadCharacters(){
        
        Quaternion rot= new Quaternion().fromAngleAxis(FastMath.HALF_PI,Vector3f.UNIT_X)
        /*
        Spatial demon = assetManager.loadModel("Models/Demons/BlueEyeWhiteDragon/BlueEyes.j3o");
        demon.scale(0.04f);
        demon.setLocalRotation(rot);
        levelNode.attachChild(demon);
         */
        Spatial witch = assetManager.loadModel("Models/Demons/Magicians/White/WhiteFemaleMagician.j3o");
        witch.scale(0.5f);
        witch.setLocalTranslation(new Vector3f(3f,4f,-5f));
        witch.setLocalRotation(rot);
        levelNode.attachChild(witch);
        
        Spatial samurai = assetManager.loadModel("Models/Demons/Warior/SamuraiWarior.j3o");
        samurai.setLocalTranslation(new Vector3f(6f,4f,-5f));
        samurai.scale(0.5f);
        samurai.setLocalRotation(rot);
        levelNode.attachChild(samurai);
        
        /** A white, spot light source. */ 
        PointLight lamp = new PointLight();
        lamp.setPosition(getCamPos());
        lamp.setColor(ColorRGBA.White);
        levelNode.addLight(lamp); 
    }
    Node explosionPrefab;
    
    
   /* Create */
    public void createDeck(CardPlayer player) {     
        // create Cards for player
        
        player.currentDeck.cardList.eachWithIndex{card,i->
            //fromDeckToHand(player)
            def newCard = createCard(card)          
        }
        arrangeDeck(player)
    }

    public void createHand(CardPlayer player){
        player.hand.eachWithIndex{card,i->
            //fromDeckToHand(player)
            def newCard = createCard(card)          
        }
        arrangeHand(player)
    }
    void createEffects(){
        ParticleFactory pf = new ParticleFactory(assetManager);
        /*
        ParticleEmitter flame = pf.createFlame();
        flame.setLocalTranslation(new Vector3f(6f,4f,-5f));
        
        ParticleEmitter rain = pf.createRain("");
        rain.setLocalTranslation(new Vector3f(0,0,-5f));
        
        ParticleEmitter spark = pf.createSpark();
        spark.setLocalTranslation(new Vector3f(0,0,-5f));
         
        explosionPrefab = pf.createExplosionNode();
        explosionPrefab.getControl(ExplosionNodeControl.class).setMaxTime(2f)
         */
    }
    void addExplosion(Vector3f pos){
        def explosion = explosionPrefab.clone();
        explosion.localTranslation =pos.clone();
        /*
        levelNode.attachChild(flame);
        levelNode.attachChild(rain);
        levelNode.attachChild(spark);
         */
        levelNode.attachChild(explosion);
    }
    void destroy(CardPlayer player,Card card){
        addExplosion(card.spatial.localTranslation)
        CardSpatialControl cc=getCardControl(card.spatial)
        cc.gigle = true;
        actions<<[
            time:1f,
            do:{
                card.spatial.removeFromParent()
            }
        ]
    }
    
    public void loadLevel(){
        super.loadLevel()
        createCardBoard()
        createEffects();
        //loadDemons();
        //loadCharacters();
    }
    /*
    public void constructTable(Node levelNode){
    Box cardShape = new Box(2,0.05,6)
        
    (-1..1).each{ side ->
    (1..2).each{ row ->
    (1..5).each{ cardIndex ->
    Geometry cardHolderGeo = new Geometry("Card ",cardShape)
                    
    x = (float)cardIndex
    z = (float)(side * 5 + row * 2.5)
    y =0f;
                    
    cardHolderGeo.localTranslate = vec3(x,y,z)
    levelNode.attachChild(cardHolderGeo);
    }
    }
    }
    }
     */

 /* Routines */   
    public void simpleUpdate(float tpf){
        // see if anything queued
        for (Iterator it=actions.iterator(); it.hasNext(); ) {
            def action = it.next();
            if ((action.time -= tpf) <0) {
                action.do()
                it.remove();
            } else {
                // do nothing but wait
            }
        }
    }   
    /* Utils */
    public Vector3f getCamPos(){
        return center.add(0f,-8f,20f)
    }
    Quaternion getCardRot(boolean faceUp){
        if (faceUp){
            return faceUpQuat.clone()
        } else {
            return faceDownQuat.clone()
        }
    }

    Vector3f vec3(float x,float y,float z){
        return new Vector3f(x,y,z);
    }
    
    /* Arrange and Pos */
    public void arrangeDeck(CardPlayer player){
        int numOfCards = player.currentDeck.cardList.size()
                    
        Vector3f centerDeck = getCenterHandPos(player);
        Vector3f gap = vec3(0.4f,0,0.02f);
        Vector3f handSize = vec3(0,0,0.5f);
        
        gap = handSize.divide(vec3(1,1,(float) numOfCards));
        player.currentDeck.cardList.eachWithIndex{card,i->
            //fromDeckToHand(player)
            def newCard = card.spatial
            
            //Quaternion rotPIY =new Quaternion().fromAngleAxis(FastMath.PI,Vector3f.UNIT_Y);
            //Quaternion rotPIZ =new Quaternion().fromAngleAxis(FastMath.PI,Vector3f.UNIT_Z);
            //Quaternion rotXYZ =new Quaternion().fromAngles(0f,FastMath.PI,0f)
            //newCard.setLocalRotation(rotXYZ)

            Vector3f cardPos = gap.mult(vec3((float)i,1f,(float)i));
            Vector3f deckPos;
            if (isCurrentPlayer(player)){
                deckPos =vec3(9f,-6.5,-5)
            } else {
                deckPos =vec3(-3f,6.5,-5)
            }
            newCard.setLocalTranslation(deckPos.add(cardPos))
        }
    }
    public void arrangeHand(CardPlayer player){
        int numOfCards = player.hand.size()
                    
        Vector3f centerHand = getCenterHandPos(player);
        Vector3f gap = vec3(0.4f,0f,0.02f);
        float squareSize= boardSizeX-6f
        Vector3f handSize = vec3(squareSize,0f,0.02f);
        
        gap = handSize.divide(vec3((float) numOfCards,1,1));
        boolean faceUp = isCurrentPlayer(player);
        player.hand.eachWithIndex{card,i->
            //fromDeckToHand(player)
            def newCard = card.spatial
            
            Quaternion rotPIY =new Quaternion().fromAngleAxis(FastMath.PI,Vector3f.UNIT_Y);
            Quaternion rotPIZ =new Quaternion().fromAngleAxis(FastMath.PI,Vector3f.UNIT_Z);
            Quaternion rotXYZ =getCardRot(faceUp)
            //newCard.setLocalRotation(rotXYZ)

            Vector3f cardPos = gap.mult(vec3((float)i,1f,(float)i));
            Vector3f handPos = centerHand.add(0f,0f,0f);
            //newCard.setLocalTranslation(handPos.add(cardPos));
            CardSpatialControl cc=getCardControl(card.spatial)
            cc.pos(handPos.add(cardPos))
            cc.rot(rotXYZ)

        }
        
    }
    Geometry createCard(Card card) {
        // extract the card info
        String path = card.picture
        
        // create the geometry
        Geometry newCard = (Geometry) cardOrg.clone();
        Material cloneMat = newCard.getMaterial().clone();
        cloneMat.setTexture("ColorMap2", assetManager.loadTexture(path));
        newCard.setMaterial(cloneMat);
        //cloneMat.setBoolean("MixGlow",true);
        levelNode.attachChild(newCard);
        card.spatial = newCard;
        newCard.setLocalScale(-0.4f,-0.4f,0.4f)
        // attach the control
        newCard.addControl(new CardSpatialControl(worldManager,card));
        newCard.addControl(new CardSelectControl(worldManager,gamePlayManager));
        return newCard;
    }
    
    CardSpatialControl getCardControl(Spatial spatial){
        return spatial.getControl(CardSpatialControl.class)
    }
    
    void putToGrave(){
        
    }
    boolean isCurrentPlayer(CardPlayer player){
        return gamePlayManager.isCurrentPlayer(player)
    }
    
    Vector3f getCenterHandPos(CardPlayer player){
        Vector3f centerHandPos;
        float halfSizeY = (float)((boardSizeY - 0.3f)/2f);
        if (isCurrentPlayer(player)){
            centerHandPos = center.add(0f,-halfSizeY,0.5f)
        } else {
            centerHandPos = center.add(0f,halfSizeY,0.5f)
        }
        return centerHandPos;
    }
    
    Vector3f handPos(CardPlayer player,int index,boolean peak){
        Vector3f centerHandPos = getCenterHandPos(player)

        int numHandCards =player.hand.size()
        float indexf=0
        
        if (index == -1){
            numHandCards ++
            indexf = numHandCards
        } else if (index == -2){
            indexf = numHandCards + 2
        } else if (index == -3){
            indexf = numHandCards
            numHandCards = 5
        }
        horizontalPos = handLength * indexf + numHandCards
        
        Vector3f nextCardPos = vec3(0,horizontalPos,0).add(centerHandPos)
        
        if (peak){
            nextCardPos.add(vec3(0,peakPos,0))
        }
        return nextCardPos;
    }
    
    Vector3f deckPos(CardPlayer player){
        if (isCurrentPlayer(player)){
            return vec3(1,1,1)
        } else {
            return vec3(1,4,1)
        }
    }
    Vector3f gravePos(CardPlayer player){
        if (isCurrentPlayer(player)){
            return vec3(1,1,1)
        } else {
            return vec3(1,4,1)
        }
    }
    Vector3f groundPos(CardPlayer player,int index){
        if (isCurrentPlayer(player)){
            return vec3(-0.8f,-1.7f,-5).add(scaledCardSize.mult(vec3((float)index,0f,0f)))
        } else {
            return vec3(-0.8f,1.7f,-5).add(scaledCardSize.mult(vec3((float)index,0f,0f)))
        }
    }
    Vector3f magicPos(CardPlayer player,int index){
        if (isCurrentPlayer(player)){
            return vec3(-0.8f,-4.5f,-5).add(scaledCardSize.mult(vec3((float)index,0f,0f)))
        } else {
            return vec3(-0.8f,4.5f,-5).add(scaledCardSize.mult(vec3((float)index,0f,0f)))
        }
    }
    void fromDeckToHand(CardPlayer player,Card card){
        Vector3f nextCardPos=handPos(player,-3,false)
        CardSpatialControl cc=getCardControl(card.spatial)
        cc.pos(nextCardPos)
        //cc.faceUp()
        //cc.moveTo(nextCardPos,3)  
    }
    void fromHandToMagic(CardPlayer player,Card card){
        Vector3f nextCardPos=magicPos(player,player.magic.size())
        CardSpatialControl cc=getCardControl(card.spatial)
        cc.pos(nextCardPos)
        cc.rot(getCardRot(false))

    }
    
    void fromHandToGround(CardPlayer player,Card card){
        Vector3f nextCardPos=groundPos(player,player.ground.size())
        CardSpatialControl cc=getCardControl(card.spatial)
        cc.pos(nextCardPos)

    }
    void putDownTable(pos,up){
        
    }
    
    void flip(){
        
    }
    void attack(){
        
    }
    void deffend(){
        
    }
    void shuttleDeck(){
        
    }

}

