package model;


import helper.CustomerQuery;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.SQLException;

/**Model for Customer List.
 *
 * @author Joseph Merritt
 * */
public class CustomerList {

    private static ObservableList<Customer> allCustomers = FXCollections.observableArrayList();

    /**Adds customer to allCustomers list.
     * @param customer the customer to add to allCustomers list
     * */
    public static void addCustomer(Customer customer){
        allCustomers.add(customer);
    }

    /**Removes selected customer from allCustomers list.
     * @param selectedCustomer the customer to remove from allCustomers list
     * @return true if selectedCustomer is not null or false if null
     * */
    public static boolean deleteCustomer(Customer selectedCustomer) throws SQLException {
        if (selectedCustomer != null){
            CustomerQuery.deleteCustomer(selectedCustomer.getId());
            allCustomers.remove(selectedCustomer);
            return true;
        }
        else
            return false;
    }

    /**Search for customer by id.
     * @param customerId the customerId to find in allCustomers list
     * @return the customer from allCustomers list with corresponding id or null if customerId does not exist
     * */
    public static Customer lookupCustomer(int customerId) {
        for(Customer p : allCustomers){
            if(p.getId() == customerId){
                return p;
            }
        }
        return null;
    }

    /**Search for customer by name.
     * @param customerName the string to compare to allCustomers list
     * @return list of all customers with matching characters to customerName String
     * */
    public static ObservableList<Customer> lookupCustomer(String customerName) throws SQLException {
        ObservableList<Customer> customerList= FXCollections.observableArrayList();
        ObservableList<Customer> allCustomers = CustomerList.getAllCustomers();

        for(Customer p : allCustomers){
            if(p.getName().contains(customerName)){
                customerList.add(p);
            }
        }

        return customerList;
    }

    /**Updates selected customer in allCustomers list.
     * @param index the index for updated customer
     * @param selectedCustomer the customer to update
     * */
    public static void updateCustomer(int index, Customer selectedCustomer){
        allCustomers.set(index,selectedCustomer);
    }

    /**Returns list of all customers.
     * @return the list of all customers.
     * */
    public static ObservableList<Customer> getAllCustomers() throws SQLException {

        clearAllCustomer();
        CustomerQuery.selectAllCustomers();

        return allCustomers;
    }

    /**Clears allCustomers list.*/
    public static void clearAllCustomer(){allCustomers.clear();}

}
