
import java.net.Socket;
import java.io.*;
import java.nio.file.*;
// charger les options
import static java.nio.file.StandardOpenOption.*;

public class Client {


    private  Socket  CreateSocket(int port) throws IOException {
        // creation du socket dans le port 
        Socket socket = new Socket("localhost", port);
        System.out.println("\nConnected!\n");

        return socket;
    }

    private OutputStream GetOutputStream(Socket socket) throws IOException {

        return socket.getOutputStream();
    }

    private DataOutputStream CreateOutputStream(Socket socket) throws IOException {

        OutputStream output=this.GetOutputStream(socket);

        return new DataOutputStream(output);
    }



    public void sendFile(int port) throws IOException{

        Socket socket=this.CreateSocket(port)   ;

        System.out.println("Envoie de fichier sur serveur1 en cours ...\n");

        DataOutputStream dataOutputStream=this.CreateOutputStream(socket);
        //initialisation de donnée
        Path chemin = Paths.get("attachement.txt");
        InputStream input = null;
        input = Files.newInputStream(chemin);
        String message = null;
        String contenu="";
        input = Files.newInputStream(chemin);

        BufferedReader reader = new BufferedReader(new InputStreamReader(input));

        do{
            message = reader.readLine();
            if(message!=null){
                contenu+=message;
                contenu+="\n";
            }
        }while(message!=null);

        input.close();


        dataOutputStream.writeUTF(contenu);
        //envoie de donnée
        dataOutputStream.flush();

        //fermeture de io
        dataOutputStream.close();

        System.out.println("____Fermeture du socket____\n");
        socket.close();


    }


    public static void main(String[] args) throws IOException {

        Client client=new Client();

        client.sendFile(7771);


    }
}

