import java.io.*;

import java.net.*;

import java.util.*;


public class TCPExampleTwo {


    private PrintWriter output;

    private BufferedReader input;

    public TCPExampleTwo(Socket socket) throws Exception {

        output = new PrintWriter(socket.getOutputStream(), true);

        input =
            new BufferedReader(new InputStreamReader(socket.getInputStream()));

    }


    public void startReceiving() throws Exception {

        String line;

        Scanner scan = new Scanner(System.in);

        while ((line = input.readLine()) != null) {

            System.out.println(line);
            String message = scan.nextLine();
            output.println(message);

        }

    }

    public void startSending() {

        Scanner stdin = new Scanner(System.in);

        System.err.println("Please enter data here");

        while (stdin.hasNextLine()) {

            output.println(stdin.nextLine());
            try{
                System.out.println(input.readLine());
            } catch (IOException e){
                System.out.println("No message from receiver");
            }
        }
    }


    public static void main(String[] args) {

        Socket socket = null;

        try {

            int port = Integer.parseInt(args[0]);

            if (args.length > 1) {

                socket = new Socket(args[1], port);

                System.err.println("Connected to " + args[1] + " on port " + port);

                TCPExampleTwo example = new TCPExampleTwo(socket);

                example.startSending();

            } else {

                ServerSocket serverSocket = new ServerSocket(port);

                System.err.println("Waiting for someone to connect");

                socket = serverSocket.accept();

                System.err.println("Accepted connection on port " + port);

                TCPExampleTwo example = new TCPExampleTwo(socket);

                example.startReceiving();

            }

        } catch (Exception e) {

            e.printStackTrace();

            System.err.println("\nUsage: java TCPExample <port> [host]");}

    }
}
