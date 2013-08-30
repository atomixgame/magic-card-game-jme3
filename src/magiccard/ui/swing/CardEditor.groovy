package magiccard.ui.swing

import groovy.swing.SwingBuilder
import javax.swing.*
import javax.swing.tree.*
import javax.swing.tree.DefaultMutableTreeNode as TreeNode
import java.awt.BorderLayout as BL
import static java.awt.Color.*
import groovy.ui.ConsoleTextEditor

import javax.swing.event.DocumentListener
import javax.swing.event.DocumentEvent
import javax.swing.text.Document
import magiccard.gameplay.*
import javax.swing.event.TreeSelectionEvent
import javax.swing.event.TreeSelectionEvent
import javax.swing.event.TreeSelectionListener

cachedImages = [:]
cardLib = new CardLibrary();
cardLib.init()
cardLib.loadAllDecks()

swing = new SwingBuilder();
swing.build {
    jFrame = frame( title: 'Card Editor', size: [1024,768],
        locationRelativeTo: null, show: true ,defaultCloseOperation: JFrame.EXIT_ON_CLOSE){
        menuBar(){
            menu(text:"File"){
                menuItem(text:"Exit",actionPerformed:{dispose()})
            }

        }
        scrollPane(constraints:BL.WEST,preferredSize:[200,200]){
            cardTree=tree(){
                
            }
        }
        panel(constraints:BL.NORTH){
            borderLayout()
            toolBar(constraints:BL.CENTER){
                button(text:"Find")
                
                separator()
                panel(preferredSize:[200,20]){
                    label(text:"Progress")
                    loadProgressBar=progressBar()
                }
            }
        }
        testBedComp=panel(constraints:BL.CENTER, new CardViewer())
          
        tabbedPane(constraints:BL.SOUTH,preferredSize:[200,400]){
            scrollPane(title:"Deck"){
                cardsInDeckPanel = panel(){
                    boxLayout(axis:BoxLayout.X_AXIS)
                }
            }
            scrollPane(title:"Log"){
                log = textArea(text:"Log")
            }
            
        }
        tabbedPane(constraints:BL.EAST ,preferredSize:[300,200]){
            cte = scrollPane(title:"Source",new ConsoleTextEditor())
            codeViewer = cte.textEditor
            codeViewer.text = "println Hello"
            
            scrollPane(title:"Properties"){
                panel{
                    
                }
            }
        }
        
        
    }
}

void buildCardTree(){
    cardTree.model.root.removeAllChildren()
    cardLib.decks.each{name,deck->
        cardTree.model.root.add(new TreeNode(name))
    }
    cardTree.model.reload(cardTree.model.root)
    cardTree.getSelectionModel().addTreeSelectionListener(this as TreeSelectionListener);
    cardTree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
}

public void valueChanged(TreeSelectionEvent event) {
    def cardPackName = event.getNewLeadSelectionPath().getLastPathComponent().userObject;
    /*
    cardLib.allDeckPackImagePaths(cardPackName).each{cardPath->
        //println(cardPath)
    }
     */
        
    loadCardImages(cardLib.getDeckPackImagePaths(cardPackName))
}

void loadCardImages(List imgPaths){
    cardsInDeckPanel.removeAll()
    imgPaths.each{imgPath->
        cardsInDeckPanel.add(swing.panel(){
                label(icon:loadImage(imgPath))
            }
        )
    }
    cardsInDeckPanel.validate()
}

def getCardImage(path){
    if (cachedImages[path]==null){
        cachedImages[path] = loadImage(path)
    } else {
    }
    return cachedImages[path]
}
def loadImage(path){
    //println path
    return swing.imageIcon(file :path)
}

buildCardTree()
