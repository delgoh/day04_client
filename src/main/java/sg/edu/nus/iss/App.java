package sg.edu.nus.iss;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.Console;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class App 
{
    public static void main( String[] args ) throws UnknownHostException, IOException
    {
        System.out.println( "Hello Client!" );
        if (args.length != 2) {
            System.out.println("Please run the file with the following 2 arguments: <ServerName> <ServerPort>");
            System.exit(0);
        }

        String serverHost = args[0];
        String serverPort = args[1];
        Socket socket = new Socket(serverHost, Integer.parseInt(serverPort));

        Console cons = System.console();
        String keyboardInput = "";
        String serverMsg = "this is unchanged";

        try (InputStream is = socket.getInputStream()) {
            BufferedInputStream bis = new BufferedInputStream(is);
            DataInputStream dis = new DataInputStream(bis);
            
            try (OutputStream os = socket.getOutputStream()) {
                BufferedOutputStream bos = new BufferedOutputStream(os);
                DataOutputStream dos = new DataOutputStream(bos);

                while (!keyboardInput.equals("close")) {
                    keyboardInput = cons.readLine("Enter a command: ");

                    dos.writeUTF(keyboardInput);
                    dos.flush();

                    serverMsg = dis.readUTF();
                    
                    System.out.println(serverMsg);
                }

                dos.close();
                bos.close();
                os.close();

            } catch (EOFException e) {
                e.printStackTrace();
            }

            dis.close();
            bis.close();
            is.close();

        } catch (EOFException e) {
            System.out.println("it is not opening stream");
            socket.close();
        }
    }
}
