package cmput305.group.project;

import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingWorker;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Kari Bronson & Jeff Thomas
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

        //C olumn Header
        String[] columnNames = new String[numCols];
        for (int i = 0; i < numCols; i++) {
            columnNames[i] = years.get(i);
        }

        //Fills Table with laureate objects and columnNames
        tableModel = new DefaultTableModel(model, columnNames) {
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                return ImageIcon.class;
            }
        };
                for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numCols; j++) {
                if(tableModel!=null && model[i][j]!=null){
                GrabImage worker = new GrabImage(tableModel, i, j, model[i][j].getPhoto());
                worker.execute();
                }
            }
        }
        tableModel.setRowCount(numRows);
        pictureTable.setModel(tableModel);
        pictureTable.setRowHeight(100);
}

    /* enables the user's interaction with single cells in the list */
    private void initListSelectionModel() {
        ListSelectionModel lsm = pictureTable.getSelectionModel();
        lsm.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        pictureTable.addMouseListener(new MouseAdapter() {           //Gets the Laureate object at the selected cell index
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
            if (selected != null) {
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
    private void timeLineSort(LaureateList list) {

        years = uniqueYears(list); //chronologically sorted list of years i.e. past - present
        ArrayList<String> prizes = uniquePrizes(list); //alphabetically sorted list of prizes  i.e. Biology, Literature, Physics
        this.numRows = prizes.size();
        this.numCols = years.size();
        model = new Laureate[numRows][numCols];

        /* lets fill our sortedList with the proper Laureates */
        for (int i = 0; i < list.size(); i++) {
            //As the lists prizes and years are already sorted, their indices match the final position the Laureate should hold in the sortedList
            int row, col;
            row = prizes.indexOf(list.get(i).getPrize());
            col = years.indexOf(list.get(i).getYear());
            model[row][col] = list.get(i);
        }
    }

    /* returns a list containing the unique prize categories, in alphabetical order, from the given LaureateList i.e. Physics, Literature, Biology */
    private ArrayList<String> uniquePrizes(LaureateList list) {

        ArrayList<String> prizes = new ArrayList<String>();
        for (int i = 0; i < list.size(); i++) {
            //if our list does not have the prize, add it
            if (!prizes.contains(list.get(i).getPrize())) {
                prizes.add(list.get(i).getPrize());
            }
        }
        Collections.sort(prizes);
        return prizes;
    }

    /* returns a chronologically sorted list of Years the Nobel Prize has been awarded from the given list*/
    private ArrayList<String> uniqueYears(LaureateList list) {
        ArrayList<String> uniqueYears = new ArrayList<String>();
        for (int i = 0; i < list.size(); i++) {
            //if our list does not have the year, add it
            if (!uniqueYears.contains(list.get(i).getYear())) {
                uniqueYears.add(list.get(i).getYear());
            }
        }

        Collections.sort(uniqueYears);
        return uniqueYears;
    }

 

    public class GrabImage extends SwingWorker<ImageIcon, Object> {


        private DefaultTableModel tableModel;
        private int row;
        private int column;
        private String url;
        
        public GrabImage(DefaultTableModel tableModel, int row, int column, String url) {
            this.tableModel = tableModel;
            this.row = row;
            this.column = column;
            this.url = url;
        }

        @Override
        protected ImageIcon doInBackground() throws Exception {
            Image image = null;
            ImageIcon helper = null;
            try {
                //URL placed in a URL object
                URL newURL;
                newURL = new URL(this.url);
                //grabs image
                image = ImageIO.read(newURL);

             } catch (IOException e) {
            }
            if (image != null) {
                helper = new ImageIcon(image);
            } else {
                System.out.println("Image Is Null");
            }

            //Done is called when return image is called
            return helper;

        }

        @Override
        protected void done() {
            try {
                tableModel.setValueAt(get(), row, column);
            } catch (Exception ignore) {
                System.out.println("Label.setIcon Is unable to process");
            }
        }
    }
}