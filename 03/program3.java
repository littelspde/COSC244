/* COSC244 - Lab3 - Program 3
   Author Josh King
   26/07/19 */

import java.io.IOException;
import java.util.Scanner;

public class program3{

    public static void main(String[] args){

        Scanner scan = new Scanner(System.in);
        String input;

        while(scan.hasNextLine()){
 
            input = scan.nextLine();
            System.out.println(input.toUpperCase());
        }
    }
}
            
