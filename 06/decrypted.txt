import java.io.*;

public class Caesar{

    public static void main (String[] args){

        InputStream input = System.in;
        OutputStream output = System.out;

        int charIn;
        int offset = 0;

        if (args.length > 0) {
            try {
                offset = Integer.parseInt(args[0]);
            } catch (NumberFormatException e) {
                System.err.println("Argument" + args[0] + " must be an integer.");
                System.exit(1);
            }
        }

        try{
            charIn = input.read();
            while(charIn != -1){
                

                charIn += offset;
                charIn = charIn % 255;
                
                output.write(charIn);
                
                charIn = input.read();
            }
            input.close();
            output.close();
        } catch (IOException e){
            System.err.println("Invalid Input");
            System.exit(1);
        }
    }
}

