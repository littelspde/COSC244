import java.io.*;
import java.util.Scanner;

public class BitLevel{

    public static void main (String[] args){

        File inFile = new File(args[0]);
        File outFile = new File(args[1]);

        try{
            FileInputStream input = new FileInputStream(inFile);
            FileOutputStream output = new FileOutputStream(outFile);
        
            Scanner scan = new Scanner(System.in);
            System.out.println("Enter key:");
            String keyInput = scan.next();

            int[] keyArray = new int[keyInput.length()];

            for (int i = 0; i < keyInput.length(); i++){

                char temp = keyInput.charAt(i);
                keyArray[i] = Character.getNumericValue(temp);
            }

            try{
                int readIn = input.read();
                int i = 0;
                
                while (readIn != -1){
                    output.write(readIn^keyArray[i]);
                    i++;
                    i = i % keyInput.length();
                    readIn = input.read();
                }
                input.close();
                output.close();
                
            } catch (IOException e){
                System.out.println("Invalid input.");
                System.exit(1);
            }
        } catch (FileNotFoundException e){
            System.out.print("File not found.");
            System.exit(1);
        }
        
    }

}
