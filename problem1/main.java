
import java.util.ArrayList;
import java.util.Arrays;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 *
 * @author peterrokowski
 * Takes in input from the command-line arguments, only one String, and determines
 * what position that String has in a sorted list of all possible permutations of
 * the characters in the String. It does so without generated that large list as 
 * that would be n! and grow too large to hold on a disc, it would also take far
 * too long to complete. Instead this program uses a crude arithmetic encoding 
 * scheme to generate sublist sizes by looking at each character, one at a time.
 * 
 * This program uses ArrayLists to hold all the characters in a sorted order, and
 * holds all unique characters, again in order. 
 * 
 * The SortedMap contains characters, as keys and an array of longs representing
 * characteristics of the letters as the values. The characteristics are 
 * arr[0] = frequency
 * arr[1] = minimum possible position
 * arr[2] = maximum possible position
 *
 * For now the positions are stored only for debugging purposes, however they 
 * could be used more efficiently a refined version of this program.
 * 
 * This problem was Solved for the Athena Corporation for a programming assessment
 * completed on 11/12/2014
 */
public class main {

    static long[] factValues = new long[100];
    static ArrayList<Character> letters = new ArrayList<>();
    static SortedMap<Character, long[]> map = new TreeMap<>();
    static ArrayList<Character> uniquechars = new ArrayList<>();
    static long[] range = new long[3];

    public static void main(String[] args) {
        String seed = args[0].toUpperCase();
        //System.out.println(args[0]);
        long i = listSize(seed.toCharArray());
        System.out.println("Map-Solution Result: " + findPosMap(seed, i, 0));
    }

    /**
     * Bread and butter of the program, recursively generates sublist ranges by
     * looking one character at a time
     * 
     * Each time through decrement the frequency of the character in order to 
     * properly calculate the chance of a character being next in the word
     * 
     * Once an overall range is determined, call findPosMap itself again 
     * removing a character of the word
     * 
     * By the end of this add 1 to the resulting minimum of the array to get 
     * final position
     * 
     * Essentially works by finding thresholds of the big list using sorted array
     * of characters where a word can appear, After threshold is found treat the
     * minimum threshold and maximum threshold as the beginning and end of the list
     * then recalculate.
     * 
     * @param word The string word that is passed to the function
     * @param range represents the range that needs to be subdivided to make
     * increments
     * @param curr current minimum value passed to the method. It represents the
     * current minimum position a string can have in a sorted list of
     * permutations
     * @return long Returns a long that holds the final position or the seed
     * word
     */
    public static long findPosMap(String word, long range, double curr) {
        int size = word.length();

        if (size == 1) {
            /* Base case ends recursive call by taking the current casting it as
            a long and adding 1 before returning that value */
            return (long) curr + 1;
        }

        char c = word.charAt(0);
        long[] arrPoint = map.get(c);
        long frequency = arrPoint[0];
        double min;
        double max;

        int index = letters.indexOf(c);
        //Find position of the letter in the sorted list

        double increment = (double) range / (double) size;
        /* Calculate the possible increments by taking the range of the list and 
        dividing by the size*/

        
        min = curr + (increment * index);
        /* Take the current minimum range and add the increment * the index to 
        get the new minimum range */
        max = min + (increment * frequency);
        /* Add minimum to the product of the increment and frequency to get the 
        new maximum of possble positions
        */
        
        long[] tmp = new long[3];
        tmp[0] = frequency - 1;
        tmp[1] = (long) min;
        tmp[2] = (long) max;

        map.put(c, tmp);
        //Add the array of new frequency and min/max to the sorted list

        String newword = word.substring(1, size);
        letters = remove(letters, c);
        /* Remove the letter from the letters to know mark that the letter has 
        been used */

        long difference = (long) (max - min); 
        //Caclulate range for next sequence

        return findPosMap(newword, difference, min);
    }

    /**
     * A custom method used to determine a possible list size of all permutations
     * of characters in a given String
     * 
     * It takes in a character array of the String, sorts the characters, and sets 
     * up the lists representing Sorted characters, and sorted unique characters
     * 
     * It also sets up the sortHashMap and instantiates the frequencies of the
     * characters
     * 
     * Follows formula of N! / N1! * N2! * - * Nn!
     * where N1 represents all frequencies of unique characters
     * 
     * @param word 
     * @return
     */
    public static long listSize(char[] word) {
        int wordSize = word.length;
        char c;
        long count[];
        Arrays.sort(word);
        for (int i = 0; i < wordSize; i++) {
            c = word[i];
            if (map.containsKey(c)) {
                //If map has the character then simply increment the characters frequency
                count = map.get(c);
                count[0]++;
            } else { 
                /*If the map doesn't have the character then instantiate an
                array, set the frequency to 1 and add it to the map. */
                count = new long[3];
                count[0] = 1;
                uniquechars.add(c);
                //add the character to unique characters list
                map.put(c, count);
            }
            letters.add(c); 
            //add the character to a sorted arraylist of all characters
        }

        long product = 1;

        for (int i = 0; i < uniquechars.size(); i++) {
            long[] tmp = map.get(uniquechars.get(i));
            product *= factorial((int) tmp[0]);
        }
        return factorial(wordSize) / product;
    }

    /**
     * dynamic programming approach used to calculate factorial values,
     * Necessary for combinatorics calculations
     *
     * Dynamic programming used for faster access on larger seed numbers.
     *
     * @param num
     * @return
     */
    public static long factorial(int num) { //Dynamic programming to keep a global reference of factorial digits
        //if Num is greater than 16 use BigInt
        if (num <= 1) {
            return 1;
        } else {
            if (factValues[num] != 0) {
                return factValues[num];
            } else {
                return factValues[num] = (factorial(num - 1) * num);
            }
        }
    }

    /**
     * Custom Arraylist remove method, used to fix indexing issues on
     * traditional arraylist.remove(): Removing an element in the middle of the
     * list would make an index call return a negative value 
     * 
     * Here I simply loop through the list adding each element to the newlist 
     * except for the first occurrence of character c.
     *
     * @param list
     * @param c
     * @return
     */
    public static ArrayList remove(ArrayList<Character> list, char c) {
        ArrayList<Character> t = new ArrayList<>();
        int count = 0;
        for (int i = 0; i < list.size(); i++) {
            if ((list.get(i) != c) || count != 0) {
                t.add(list.get(i));
            } else {
                count++;
            } //Done to resolve removing all of the characters
        }
        return t;
    }
}