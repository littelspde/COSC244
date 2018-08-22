import java.util.*;

/**
 * This is the data structure that stores string/code pairs so they can
 * be easily retrieved.  It also contains a constant (GROW_CODE) which is
 * used as a flag to indicate that the next code needs one extra bit to
 * store it (compared to the largest of the previous codes).
 */
public class LookUpTable {


   private static final int CHARACTER_SET_SIZE = 256;
 
   private static final int COMMAND_CODES  = 1;
   private static final int FIRST_CODE = CHARACTER_SET_SIZE + COMMAND_CODES;

   /** This code indicates that the bitfield size needs to grow. */
   public static final int GROW_CODE = FIRST_CODE - 1;
   private Hashtable<String,Integer> string2codeLUT;
   private Hashtable<Integer,String> code2stringLUT;
   private int nextCode;

   /**
    * Create a new look-up table, initialised with codes for every
    * 8-bit character.
    */
   public LookUpTable() {
      String string;
      string2codeLUT  = new Hashtable<String,Integer>();
      code2stringLUT  = new Hashtable<Integer,String>();
      for (int code=0; code < CHARACTER_SET_SIZE; code++) {
         string = String.valueOf((char) code );
         string2codeLUT.put(string, code);
         code2stringLUT.put(code, string);
      }
      nextCode = FIRST_CODE;
   }

   /**
    * Determines whether a given string has already been added to the
    * look-up table (and can therefore has a matching code).
    *
    * @param str the string to search this look-up table for.
    * @return true if the look-up table contains str, otherwise false.
    */
   public boolean hasString(String str) {
      return getCode(str) != null;
   }

   /**
    * Determines whether a given code is contained in the
    * look-up table (and can therefore has a matching string).
    *
    * @param code the code to search this look-up table for.
    * @return true if the look-up table contains code, otherwise false.
    */
   public boolean hasCode(int code) {
      return getString(code) != null;
   }

   /**
    * Add a new string to this look-up table.
    * @param str a string to add to the look-up table.
    */
   public void add(String str) {
      string2codeLUT.put(str, nextCode);
      code2stringLUT.put(nextCode, str);
      nextCode++;
   }
   
   /**
    * Look up the compression code for a given string.
    * @param str the string to look up.
    * @return the code corresponding to the given string, or null if
    * the string is not in the table.
    */
   public Integer getCode(String str) {
      return string2codeLUT.get(str);
   }

   /**
    * Look up a string associated with a given compression code.
    * @param code the code to look up.
    * @return the string associated with the given code, or null if
    * the code is not in the table.
    */
   public String getString(int code) {
      return code2stringLUT.get(code);
   }
}
