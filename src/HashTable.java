import java.text.DecimalFormat;
import java.util.LinkedList;

public class HashTable implements IHashTable {
    /**
     * Implements a hashtable
     * @author Kelvin
     * @since 11/3/18
     */

    //You will need a HashTable of LinkedLists.
    private int nelems;  //Number of element stored in the hash table
    private int expand;  //Number of times that the table has been expanded
    private int collision;  //Number of collisions since last expansion
    private String statsFileName;     //FilePath for the file to write statistics upon every rehash
    private boolean printStats = false;   //Boolean to decide whether to write statistics to file or not after rehashing
    private LinkedList<String>[] hash; // creates hash
    private double loadFactor = (double)(2)/3; // for load factor
    private int chainLength; // checks chainlength
    private int numberofLetters = 27; // for hash function
    //You are allowed to add more :)

    public HashTable(int size) {
        /**
         * Constructor hash table
         * @param size takes in size for table
         */
        //creates a hashtable and initiates with linkedlist.
        hash = new LinkedList[size];
        for (int i = 0; i < size; i++){
            LinkedList<String> links = new LinkedList<>();
            hash[i] = links;
        }
        printStats = false;
        nelems = 0;
    }

    public HashTable(int size, String fileName){
        /**
         *Creates hashtable
         * @param size takes in size for hash table
         * @param fileName reads a file to put in hashtable
         */
        //creates hashtable for input
        hash = new LinkedList[size];
        for (int i = 0; i < size; i++){
            LinkedList<String> links = new LinkedList<>();
            hash[i] = links;
        }
        printStats = true;
        statsFileName = fileName;
        nelems = 0;

        // Set printStats to true and statsFileName to fileName
    }

    public boolean insert(String value) {
        /**
         * Puts into hash table
         * @param value the value to input
         * @return boolean if inputed
         */
        if (value == null) {
            throw new NullPointerException();
        }
        // first check if already in
        int check = hashVal(value);
        for (int i = 0; i < hash[check].size() ; i++ ) {
            if (hash[check].contains(value)) {
                return false;
            }
        }
        //for collision
        if (! hash[check].isEmpty()){
            collision = collision + 1;
        }
        //adds element into
        hash[check].add(value);
        nelems++;
        //check if needed to be resized
        if (hash.length * loadFactor < nelems){
            this.rehash();
        }
        return true;
    }

    public boolean delete(String value) {
        /**
         * Deletes an input from hash table
         * @param value string to be deleted
         * @return boolean if deleted or not
         */
        if (value == null) {
            throw new NullPointerException();
        }
        // deletes values if in
        int check = hashVal(value);
        for (int j = 0; j < hash[check].size(); j++){
            if (hash[check].get(j).equals(value)){
                hash[check].remove(j);
                nelems = nelems - 1;
                return true;
            }
        }
        return false;
    }

    public boolean lookup(String value) {
        /**
         * Checks to see if in hashtable
         * @param value string to be inputed.
         * @return boolean if inside or not
         */
        if (value == null) {
            throw new NullPointerException();
        }
        //checks to see if value is in
        int check = hashVal(value);
        for (int j = 0; j < hash[check].size(); j++){
            if (hash[check].contains(value)) {
                return true;
            }
        }
        return false;
    }

    public void printTable() {
        /**
         * Prints out hashtable
         */
        //prints out table first look at buckets then at linkedlist
        for (int i = 0; i < hash.length; i++){
            System.out.println(i + ":");
            for (int j = 0; j < hash[i].size(); j++){
                if (j == hash[i].size()-1) {
                    System.out.print(hash[i].get(j));
                }
                else {
                    System.out.print(hash[i].get(j) + ",");
                }
            }
        }
    }

    public int getSize() {
        /**
         *Returns the size of table
         * @return the size of table
         */
        //returns size
        return nelems;
    }

    private int hashVal(String str) {
        /**
         * Creates hashvalue for string input
         * @param str string to be inputed
         * @return the hashvalue to be used to input string
         */

        if (str == null) {
            throw new NullPointerException();
        }
        // hash function multiple each character by 27 and add string value
        // modules the result to check in size with hash table.
        int hashVal = 0;
        for (int j = 0; j < str.length(); j++) {
            int letter = str.charAt(j);
            hashVal = (hashVal * numberofLetters + letter) % hash.length;
        }
        return hashVal;
    }

    private void rehash() {
        /**
         * Resizes the hashtable
         */
        //creates hash table with double the size and store old hash in temp
        LinkedList<String> [] temp = hash;
        hash = new LinkedList[temp.length*2];
        for (int i = 0; i < hash.length; i++){
            LinkedList<String> links = new LinkedList<>();
            hash[i] = links;
        }
        //add every element into new hashtable
        for (int i = 0; i < temp.length; i++){
            if (temp[i] != null) {
                for (int j = 0; j < temp[i].size(); j++) {
                    insert(temp[i].get(j));
                }
            }
        }
        int temp2  = 0;
        for (int i = 0; i < hash.length; i++){
            for (int j = 0; j < hash[i].size(); j++){
                temp2++;
            }
            if (temp2 > chainLength) {
                chainLength = temp2;
            }
        }
        //prints stats
        if (printStats == true) {
            printStatistics();
        }
        chainLength = 0;
        expand = expand + 1;
        collision = 0;
    }

    private void printStatistics() {
        /**
         * Prints out the data of the hash table.
         */
        //prints the stats out
        DecimalFormat df = new DecimalFormat("#.##");
        System.out.println(expand + " resizes, load factor " +
                df.format(loadFactor*hash.length) + ", " +
                collision + " collisions, " + chainLength + " longest chain");
    }
}
