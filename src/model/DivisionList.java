package model;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**Model for Division List.
 *
 * @author Joseph Merritt
 * */
public class DivisionList {

    private static ObservableList<Division> allDivisions = FXCollections.observableArrayList();

    /**Adds division to allDivisions list.
     * @param division the division to add to allDivisions list
     * */
    public static void addDivision(Division division){
        allDivisions.add(division);
    }

    /**Removes selected division from allDivisions list.
     * @param selectedDivision the division to remove from allDivisions list
     * @return true if selectedDivision is not null or false if null
     * */
    public static boolean deleteDivision(Division selectedDivision) {
        if (selectedDivision != null){
            allDivisions.remove(selectedDivision);
            return true;
        }
        else
            return false;
    }

    /**Search for division by id.
     * @param divisionId the divisionId to find in allDivisions list
     * @return the division from allDivisions list with corresponding id or null if divisionId does not exist
     * */
    public static Division lookupDivision(int divisionId) {
        for(Division p : allDivisions){
            if(p.getId() == divisionId){
                return p;
            }
        }
        return null;
    }

    /**Search for division by name.
     * @param divisionName the string to compare to allDivisions list
     * @return list of all divisions with matching characters to divisionName String
     * */
    public static ObservableList<Division> lookupDivision(String divisionName) {
        ObservableList<Division> divisionList= FXCollections.observableArrayList();
        ObservableList<Division> allDivisions = DivisionList.getAllDivisions();

        for(Division p : allDivisions){
            if(p.getName().contains(divisionName)){
                divisionList.add(p);
            }
        }

        return divisionList;
    }

    /**Updates selected division in allDivisions list.
     * @param index the index for updated division
     * @param selectedDivision the division to update
     * */
    public static void updateDivision(int index, Division selectedDivision){
        allDivisions.set(index,selectedDivision);
    }

    /**Returns list of all divisions.
     * @return the list of all divisions.
     * */
    public static ObservableList<Division> getAllDivisions(){
        return allDivisions;
    }

    /**Clears allDivisions list.*/
    public static void clearAllDivisions(){allDivisions.clear();}

}
