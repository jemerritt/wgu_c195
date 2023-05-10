package model;


import helper.CountryQuery;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.SQLException;

/**Model for Country List.
 *
 * @author Joseph Merritt
 * */
public class CountryList {

    private static ObservableList<Country> allCountries = FXCollections.observableArrayList();

    /**Adds country to allCountries list.
     * @param country the country to add to allCountries list
     * */
    public static void addCountry(Country country){
        allCountries.add(country);
    }

    /**Removes selected countries from allCountries list.
     * @param selectedCountry the countries to remove from allCountries list
     * @return true if selectedCountry is not null or false if null
     * */
    public static boolean deleteCountry(Country selectedCountry) {
        if (selectedCountry != null){
            allCountries.remove(selectedCountry);
            return true;
        }
        else
            return false;
    }

    /**Search for country by id.
     * @param countryId the countryId to find in allCountries list
     * @return the country from allCountries list with corresponding id or null if countryId does not exist
     * */
    public static Country lookupCountry(int countryId) {
        for(Country p : allCountries){
            if(p.getId() == countryId){
                return p;
            }
        }
        return null;
    }

    /**Search for country by name.
     * @param countryName the string to compare to allCountries list
     * @return list of all countries with matching characters to countryName String
     * */
    public static ObservableList<Country> lookupCountry(String countryName) throws SQLException {
        ObservableList<Country> countryList= FXCollections.observableArrayList();
        ObservableList<Country> allCountries = CountryList.getAllCountries();

        for(Country p : allCountries){
            if(p.getName().contains(countryName)){
                countryList.add(p);
            }
        }

        return countryList;
    }

    /**Updates selected country in allCountries list.
     * @param index the index for updated country
     * @param selectedCountry the country to update
     * */
    public static void updateCountry(int index, Country selectedCountry){
        allCountries.set(index,selectedCountry);
    }

    /**Returns list of all countries.
     * @return the list of all countries.
     * */
    public static ObservableList<Country> getAllCountries() throws SQLException {

        clearAllCountries();
        CountryQuery.selectAllCountries();

        return allCountries;
    }

    /**Clears allAppointments list.*/
    public static void clearAllCountries(){allCountries.clear();}

}
