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
                   DetailMenu menu = new DetailMenu(list.get(0));
                   menu.setVisible(true);
              }
        });
    }
}
