

package cmput305.group.project;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collections;
import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author KariBronson & Jeff Thomas
 */
public class PictureTableController {

    private JTable pictureTable;
    private DefaultTableModel tableModel;
    private Laureate[][] model;
    private PictureTableGUI view;
    
    private Laureate selected; //currently selected Laureate in the table
    private int numRows; //number of rows in our table
    private int numCols; //number of columns in our table
    private ArrayList<String> years; //list of every year a Nobel Prize was awarded.  Serves as the Column headers of our table

    /* initializes the controller */
    PictureTableController(LaureateList list, PictureTableGUI view) {
        this.pictureTable = view.getJTable();
        this.view = view;
        
        //sorts the lists into our 2D laureate array
        timeLineSort(list); 
        
        initTableModel();
        initListSelectionModel();
        view.addOpenListener(new EnterButtonListener());
        view.addCloseListener(new CloseButtonListener());
    }
  
    /* Creates the column headers, columns and rows then fills the cells of our jtable with the proper images */
    /* the string array  years  holds all the unique years that are used as column headers  */
    private void initTableModel() {
        

        JLabel[][] data = new JLabel[numRows][numCols];
        for(int i =0; i < numRows; i ++){
            for(int j =0; j < numCols; j++){
                    data[i][j] = new JLabel();           
            }
        }
        
        String[] columnNames = new String[numCols-1];   //creates a string list that holds all the column names. i.e. all the years prizes were given
        for(int i =0; i < numCols-1 ;i++){
            columnNames[i] = years.get(i);
        }
        
        tableModel = new DefaultTableModel(data, columnNames){
            
            @Override
            public Class getColumnClass(int column){
                return Icon.class;
            }
            
            @Override
            public boolean isCellEditable(int row, int column){
                 return false;
             } 
        };     
        
        tableModel.setRowCount(numRows); 
        pictureTable.setModel(tableModel);
        pictureTable.setRowHeight(100);
        
         for(int i =0; i < numRows-1; i ++){
            for(int j =0; j < numCols-1; j++){
                if(model[i][j] != null){
                    JLabel iconLabel = (JLabel) tableModel.getValueAt(i, j);
                    iconLabel.setIcon(ImageURLLoader.imageCache.IMAGE.imageCheck(model[i][j].getPhoto()));
                }
                
            }
         }
    }
      
    
    /* enables the user's interaction with single cells in the list */
    private void initListSelectionModel() {
        ListSelectionModel lsm = pictureTable.getSelectionModel();
        lsm.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        pictureTable.addMouseListener(new MouseAdapter(){           //Gets the Laureate object at the selected cell index
            
            @Override
            public void mouseClicked(MouseEvent e) {
            selected = model[pictureTable.rowAtPoint(e.getPoint())][pictureTable.columnAtPoint(e.getPoint())];
            }
        });
     }


    //Opens Detailed Menu on selected Laureate
    private class EnterButtonListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent ae) {
                if(selected != null){
                    DetailMenu menu = new DetailMenu(selected);
                    menu.setVisible(true);
                }
                
        }
    }
    

    // Closes Timeline view
    private class CloseButtonListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent ae) {
            view.dispose();
        }
    }
    
   
   
    
      /* returns a 2d array of laureate objects sorted into Prize and Year rows and columns respectively.  */
    private void timeLineSort(LaureateList list){
        
        years = uniqueYears(list); //chronologically sorted list of years i.e. past - present
        ArrayList<String> prizes = uniquePrizes(list); //alphabetically sorted list of prizes  i.e. Biology, Literature, Physics
        this.numRows = prizes.size();
        this.numCols = years.size();       
        model = new Laureate[numRows][numCols];   
        
        /* lets fill our sortedList with the proper Laureates */
        for(int i = 0; i < list.size(); i++){
            //As the lists prizes and years are already sorted, their indices match the final position the Laureate should hold in the sortedList
            int row, col;
            row = prizes.indexOf(list.get(i).getPrize());
            col = years.indexOf(list.get(i).getYear());
            model[row][col] = list.get(i);
        }
    }
    
    
    /* returns a list containing the unique prize categories, in alphabetical order, from the given LaureateList i.e. Physics, Literature, Biology */
    private ArrayList<String> uniquePrizes(LaureateList list){
        
        ArrayList<String> prizes = new ArrayList<>();
        for(int i = 0; i < list.size(); i++){
            //if our list does not have the prize, add it
            if(!prizes.contains(list.get(i).getPrize())){
                prizes.add(list.get(i).getPrize());
            }
        }
        Collections.sort(prizes);
        return prizes;
    }
    
    
    /* returns a chronologically sorted list of Years the Nobel Prize has been awarded from the given list*/
    private ArrayList<String> uniqueYears(LaureateList list){
        ArrayList<String> uniqueYears = new ArrayList<>();
        for(int i = 0; i < list.size(); i++){
            //if our list does not have the year, add it
            if(!uniqueYears.contains(list.get(i).getYear())){
                uniqueYears.add(list.get(i).getYear());
            }
        }
        
        Collections.sort(uniqueYears);
        return uniqueYears;
    }
    
}