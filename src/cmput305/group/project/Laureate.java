/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cmput305.group.project;

public class Laureate {
    /*Global Variables*/
    private String year;
    private String prize;
    private String name;
    private String longName;
    private String gender;
    private String photo;
    private String country;
    private String afiliation;
    private String birthYear;
    private String deathYear;
    private String biography;
    private String lecture;
    
    public Laureate(){
        year ="1901";
        prize ="Information not found";
        name ="Information not found";
        longName ="Not Available";
        gender = "Information not found";
        photo = "Unable to retrieve";
        country ="Information not found";
        afiliation="Information not found";
        birthYear ="Information not found";
        deathYear ="Present";
        biography ="Not Available";
        lecture ="Not Available";
    }

   /*Get methods to have variables acessable*/
   public String getYear(){return year;}
   public String getPrize(){return prize;}
   public String getName(){return name;}
   public String getLongName(){return longName;}
   public String getGender(){return gender;}
   public String getPhoto(){return photo;}
   public String getCountry(){return country;};
   public String getAfiliation(){return afiliation;};
   public String getBirth(){return birthYear;}
   public String getDeath(){return deathYear;}
   public String getBiography(){return biography;}
   public String getLecture(){return lecture;}

   /*Set methods for when reading data from file*/
   public void setYear(String y){this.year = y;}
   public void setPrize(String p){this.prize = p;}
   public void setName(String n){this.name = n;}
   public void setLongName(String lN){this.longName = lN;}
   public void setGender(String g){this.gender = g;}
   public void setPhoto(String p){this.photo = p;}
   public void setCountry(String c){this.country = c;}
   public void setAfiliation(String a) {this.afiliation = a;}
   public void setBirthYear(String b){this.birthYear = b;}
   public void setDeathYear(String d){this.deathYear = d;}
   public void setBiography(String b){this.biography = b;}
   public void setLecture(String l){this.lecture = l;}
}