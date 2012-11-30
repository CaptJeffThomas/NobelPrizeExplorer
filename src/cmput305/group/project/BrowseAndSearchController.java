// CMPUT 305  Nobel Prize Explorer
// Team Omega Rhinocerous
// Alex Tai, Charles Logan, Kari Bronson, Jeff Thomas
// November 2012

package cmput305.group.project;

import cmput305.group.project.NobelCollectionManipulation.CountryComparator;
import cmput305.group.project.NobelCollectionManipulation.GenderComparator;
import cmput305.group.project.NobelCollectionManipulation.NameComparator;
import cmput305.group.project.NobelCollectionManipulation.PrizeComparator;
import cmput305.group.project.NobelCollectionManipulation.ReverseCountryComparator;
import cmput305.group.project.NobelCollectionManipulation.ReverseGenderComparator;
import cmput305.group.project.NobelCollectionManipulation.ReverseNameComparator;
import cmput305.group.project.NobelCollectionManipulation.ReversePrizeComparator;
import cmput305.group.project.NobelCollectionManipulation.ReverseYearComparator;
import cmput305.group.project.NobelCollectionManipulation.YearComparator;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.*;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Alex & Jeff Thomas
 */
public class BrowseAndSearchController {
    private BrowseAndSearchGUI view;
    private JTable laureateTable;
    private DefaultTableModel tableModel;
    private LaureateList laureates;
    private List <Laureate> filteredLaureates = new ArrayList();
    //private String [] filters = new String[4];
    private String filtersApplied = "Filters Applied: ";
    
    private Laureate selected;  // the selected Laureate in the table
    private int doubleClick;  // used to implement double click opening of Detailed View
    private NobelCollectionManipulation collMan;
    private int previousIndex = -1;
    
    public BrowseAndSearchController(BrowseAndSearchGUI view, LaureateList laureates)
    {
        this.view = view;
        this.laureateTable = view.getLaureateTable();
        this.laureates = laureates;
        resetFilteredLaureates();
        doubleClick = 0;
        setupColumnSorting();
        collMan = new NobelCollectionManipulation();

        
        populateOptions();
        view.setDefaultYear();
        view.addSearchButtonListener(new SearchButtonListener());
        view.addResetButtonListener(new ResetButtonListener());
        view.addCategoryOptionListener(new CategoryOptionListener());
        view.addCountryOptionListener(new CountryOptionListener());
        view.addGenderOptionListener(new GenderOptionListener());
        //view.addYearOptionListener(new YearOptionListener());
        view.addTimelineButtonListener(new TimelineButtonListener());
        view.addDetailsButtonListener(new DetailButtonListener());
        
        initTableModel();
        initTableColumns();
        initListSelectionModel();
        populateTable();
        
    }
    
    public void showGUI()
    {
        java.awt.EventQueue.invokeLater(new Runnable() {
        @Override
            public void run() 
            {
                view.setVisible(true);
            }
        });        
    }
 
    public void initTableModel()
    {
        tableModel = new DefaultTableModel();
        tableModel.addColumn("Year");
        tableModel.addColumn("Name");
        tableModel.addColumn("Category");
        tableModel.addColumn("Country");
        tableModel.addColumn("Gender");
        laureateTable.setModel(tableModel);
    }
    
    private void initTableColumns()
    {
        laureateTable.getColumnModel().getColumn(0).setPreferredWidth(75);
        laureateTable.getColumnModel().getColumn(1).setPreferredWidth(350);
        laureateTable.getColumnModel().getColumn(2).setPreferredWidth(200);
        laureateTable.getColumnModel().getColumn(3).setPreferredWidth(300);
        laureateTable.getColumnModel().getColumn(4).setPreferredWidth(75);
    }
    
        /* enables the user's interaction with single cells in the list */
    private void initListSelectionModel() {
        ListSelectionModel lsm = laureateTable.getSelectionModel();
        lsm.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        laureateTable.addMouseListener(new MouseAdapter() {           //Gets the Laureate object at the selected cell index
            @Override
            public void mouseClicked(MouseEvent e) {
   
                String selectedName = (String)tableModel.getValueAt(laureateTable.rowAtPoint(e.getPoint()), 1);
                
                //if the same laureate is clicked twice
                if(selected != null){
                    if(selected.getName().equals(selectedName)){
                        doubleClick++;
                        if(doubleClick % 2 == 1){
                                DetailMenu menu = new DetailMenu(selected);
                                menu.setVisible(true);
                        }
                    }
                    
                }
                        
                for(int i = 0; i < laureates.size(); i++){
                    if(laureates.get(i).getName().equals(selectedName)){
                        selected = laureates.get(i);
                    }
                }
               
                
            }
     
        });
    }
    
    private class TimelineButtonListener implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent e)
        {
            PictureTableGUI timeView = new PictureTableGUI();
            PictureTableController timeController = new PictureTableController(laureates, timeView);
            timeView.setVisible(true);
        }
    }
    
    private class SearchButtonListener implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent e)
        {
            String searchCategory = view.getSearchCategory();
            String searchTerms = view.getSearchTerms();
            //Call appropriate filter
            
        }
    }
    
    private class ResetButtonListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            resetFilteredLaureates();
            populateOptions();
            populateTable();
            view.setDefaultYear();
            filtersApplied = "Filters Applied: ";
            view.setFilterText(filtersApplied);
            view.resetSearch();
        }
        
    }
    
        //Opens Detailed Menu on selected Laureate
    private class DetailButtonListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent ae) {
          
            if (selected != null) {
                DetailMenu menu = new DetailMenu(selected);
                menu.setVisible(true);
            }

        }
    }
    
    private class CategoryOptionListener implements ActionListener{
        
        @Override
        public void actionPerformed(ActionEvent e) {
            String selectedOption = view.getCategoryOption();
            
            if(selectedOption != null && !selectedOption.contains("ALL"))
            {
                if(!filtersApplied.contains(selectedOption)){
                    filtersApplied = filtersApplied.concat("  > " + selectedOption);
                    view.setFilterText(filtersApplied);
                }
                
                System.out.println(selectedOption);
                List <String> searchTerms = new ArrayList();
                searchTerms.add(selectedOption);
                filteredLaureates = NobelCollectionManipulation.filterByPrize(filteredLaureates, searchTerms);
                populateOptions();
                populateTable();
                view.removeCategoryOption("ALL");
            }
        }
    }
    
    private class CountryOptionListener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            String selectedOption = view.getCountryOption();
            if(selectedOption != null && !selectedOption.contains("ALL"))
            {
                if(!filtersApplied.contains(selectedOption)){
                    filtersApplied = filtersApplied.concat("  > " + selectedOption);
                    view.setFilterText(filtersApplied);
                }
                System.out.println(selectedOption);
                List <String> searchTerms = new ArrayList();
                searchTerms.add(selectedOption);
                filteredLaureates = NobelCollectionManipulation.filterByCountry(filteredLaureates, searchTerms);
                populateOptions();
                populateTable();
                view.removeCountryOption("ALL");
            }
        }
    }
    
    private class GenderOptionListener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            String selectedOption = view.getGenderOption();
            if(selectedOption != null && !selectedOption.contains("ALL"))
            {
                if(!filtersApplied.contains(selectedOption)){ 
                    filtersApplied = filtersApplied.concat("  > " + selectedOption);
                    view.setFilterText(filtersApplied);
                }
                System.out.println(selectedOption);
                List <String> searchTerms = new ArrayList();
                searchTerms.add(selectedOption);
                filteredLaureates = NobelCollectionManipulation.filterByGender(filteredLaureates, searchTerms);
                populateOptions();
                populateTable();
                view.removeGenderOption("ALL");
            }
        }
    }
    
    private class YearOptionListener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            String selectedOption = view.getYearOption();
            if(selectedOption != null && !selectedOption.contains("ALL"))
            {
                if(!filtersApplied.contains(selectedOption)){
                    filtersApplied = filtersApplied.concat("  > " + selectedOption);
                    view.setFilterText(filtersApplied);
                }
                System.out.println(selectedOption);
                List <String> searchTerms = new ArrayList();
                searchTerms.add(selectedOption);
                filteredLaureates = NobelCollectionManipulation.filterByYear(filteredLaureates, searchTerms);
                populateOptions();
                populateTable();
                view.removeYearOption("ALL");
            }
        }
    }
    
    
    public void resetFilteredLaureates()
    {

            for(int count = 0; count < laureates.size(); count++)
            {
                filteredLaureates.add(laureates.get(count));
            }
    }
    
    public void populateOptions()
    {
        view.resetAllOptions();
        Set <String> tempCategory = new TreeSet<String>();
        tempCategory.add("ALL");
        for(int count = 0; count < filteredLaureates.size(); count++)
        {
            tempCategory.add(filteredLaureates.get(count).getPrize());
        }
        view.addCategoryOption(tempCategory);
        
        tempCategory.clear();
        tempCategory.add("ALL");
        for(int count = 0; count < filteredLaureates.size(); count++)
        {
            tempCategory.add(filteredLaureates.get(count).getCountry());
        }
        view.addCountryOption(tempCategory);
       
        tempCategory.clear();
        tempCategory.add("ALL");
        for(int count = 0; count < filteredLaureates.size(); count++)
        {
            tempCategory.add(filteredLaureates.get(count).getGender());
        }
        view.addGenderOption(tempCategory);
        
        tempCategory.clear();
        tempCategory.add("ALL");
        for(int count = 0; count < filteredLaureates.size(); count++)
        {
            tempCategory.add(filteredLaureates.get(count).getYear());
        }
        view.addYearOption(tempCategory);
    }
    
    public void populateTable()
    {
        tableModel.setRowCount(0);
        for(Laureate person: filteredLaureates)
        {
            Object[] rowInfo = {person.getYear(), person.getName(), person.getPrize(),
                person.getCountry(), person.getGender()};
            tableModel.addRow(rowInfo);
            
        }
    }
    
    
    // allows our column headers to be interactive and perfrom ascending/descending sorting accoring to the clumn clicked.
    public void setupColumnSorting(){
        
          laureateTable.getTableHeader().addMouseListener(new MouseAdapter() {
          @Override
          public void mouseClicked(MouseEvent mouseEvent) {
              
            int index = laureateTable.columnAtPoint(mouseEvent.getPoint());
            
            switch(index){
                case 0:  //sort by year
                    System.out.println("Clicked on Year column");
                   if(previousIndex != index){
                     YearComparator comp = collMan.new YearComparator();
                     Collections.sort(filteredLaureates, comp);
                     previousIndex = index;
                   }
                   else{
                     ReverseYearComparator comp = collMan.new ReverseYearComparator();
                     Collections.sort(filteredLaureates, comp);
                     previousIndex = -1;
                   }
                     populateTable();
                break;

                case 1: //sort by name
                    System.out.println("Clicked on Name column");
                    if(previousIndex != index){
                     NameComparator comp = collMan.new NameComparator();
                     Collections.sort(filteredLaureates, comp);
                     previousIndex = index;
                   }
                   else{
                     ReverseNameComparator comp = collMan.new ReverseNameComparator();
                     Collections.sort(filteredLaureates, comp);
                     previousIndex = -1;
                   }
                     populateTable();
                break;

                case 2:  //sort by Prize category
                    System.out.println("Clicked on Prize column");
                   if(previousIndex != index){
                     PrizeComparator comp = collMan.new PrizeComparator();
                     Collections.sort(filteredLaureates, comp);
                     previousIndex = index;
                   }
                   else{
                     ReversePrizeComparator comp = collMan.new ReversePrizeComparator();
                     Collections.sort(filteredLaureates, comp);
                     previousIndex = -1;
                   }
                    populateTable();
                break;

                case 3: //sort by country
                    System.out.println("Clicked on Country column");
                   if(previousIndex != index){
                     CountryComparator comp = collMan.new CountryComparator();
                     Collections.sort(filteredLaureates, comp);
                     previousIndex = index;
                   }
                   else{
                     ReverseCountryComparator comp = collMan.new ReverseCountryComparator();
                     Collections.sort(filteredLaureates, comp);
                     previousIndex = -1;
                   }
                    populateTable();
                break;

                case 4: //sort by gender
                    System.out.println("Clicked on Gender column");
                   if(previousIndex != index){
                     GenderComparator comp = collMan.new GenderComparator();
                     Collections.sort(filteredLaureates, comp);
                     previousIndex = index;
                   }
                   else{
                     ReverseGenderComparator comp = collMan.new ReverseGenderComparator();
                     Collections.sort(filteredLaureates, comp);
                     previousIndex = -1;
                   }
                    populateTable();
                break;
            }

        }
      });
    }
}
