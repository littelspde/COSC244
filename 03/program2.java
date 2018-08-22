/* COSC244 - Lab3 - Program 2
   Author Josh King
   26/07/19 */

import java.lang.StringBuilder;
import java.io.*;

public class program2{
    
    public static void main (String[] args){


        InputStreamReader reader = new InputStreamReader(System.in);
        BufferedReader in = new BufferedReader(reader);

        String input = "";

        while (input != null){
            try{
                input = in.readLine();
                StringBuilder inputRev = new StringBuilder(input);
                System.out.println(inputRev.reverse());
            } catch (IOException e) {
                System.err.println("Invalid Input");
                System.exit(1);
            }
                                
        }
    }
}
