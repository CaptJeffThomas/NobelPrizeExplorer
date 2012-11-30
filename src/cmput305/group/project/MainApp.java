/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cmput305.group.project;

/**
 *
 * @author Jeff
 */
public class MainApp {

        public static void main(String[] args) {
        java.awt.EventQueue.invokeLater(new Runnable() {
              public void run() {
                   LaureateList list = new LaureateList();
                   BrowseAndSearchGUI mainView  = new BrowseAndSearchGUI();
                   BrowseAndSearchController mainCon = new BrowseAndSearchController( mainView , list);
                   mainView.setVisible(true);
                   
              }
        });
    } 
}
