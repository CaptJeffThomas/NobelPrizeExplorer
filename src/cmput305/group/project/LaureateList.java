/*  Jeff Thomas
 *  CMPUT 305 Assn 6: Nobel Guys
 */

package cmput305.group.project;

import java.util.ArrayList;
import java.util.Scanner;


public class LaureateList {
    
    private ArrayList<Laureate> list;
    
    //creates new list with the parsed input from file
    public LaureateList(){
            list = new ArrayList<>();
            Scanner input = new Scanner(LaureateList.class.getResourceAsStream("nobel.txt"));
            Laureate heller = new Laureate();
             while(input.hasNextLine()) {
                String next = input.nextLine();
                
                //blank lines signify a new recipient will begin with the next line.  add our current one to the list and then wipe the slate clean
                if (next.equals("")){
                    list.add(heller);
                    heller = new Laureate();
                }
                
                //BEGIN THE AMATUER FILE PARSING!
                if (next.startsWith("year: ")){
                    heller.setYear(next.substring(6));
                }
                                
                if (next.startsWith("prize: ")){
                    heller.setPrize(next.substring(7));
                }
                                
                if (next.startsWith("name: ")){
                    heller.setName(next.substring(6));
                }
                                
                if (next.startsWith("gender:")){
                    heller.setGender(next.substring(8));
                }
                                
                if (next.startsWith("photo: ")){
                    heller.setPhoto(next.substring(7));
                }
                                
                if (next.startsWith("country: ")){
                    heller.setCountry(next.substring(9));
                }
                                
                if (next.startsWith("affiliation: ")){
                    heller.setAfiliation(next.substring(13));
                }
                                
                if (next.startsWith("birthyear: ")){
                    heller.setBirthYear(next.substring(11));
                }
                if( next.startsWith("deathyear: ")){
                    heller.setDeathYear(next.substring(11));
                }
                if (next.startsWith("biography: ")){
                    heller.setBiography(next.substring(11));
                }  
                if (next.startsWith("lecture: ")){
                    heller.setLecture(next.substring(9));
                }  
                                
             }   
             input.close();
    }
    
    public Laureate get(int i){
        return list.get(i);
    }
    public int size(){
        return list.size();
    }
    
    
    
}
