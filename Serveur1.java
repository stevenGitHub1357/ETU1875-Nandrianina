import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.*;
import static java.nio.file.StandardOpenOption.*;


public class Serveur1 {

    private  Socket  CreateSocket(int port) throws IOException {
        // creation du socket dans le port 777
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

        Socket socket=this.CreateSocket(port);

        System.out.println("Envoie de fichier sur serveur2 en cours ...\n");

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


    private void sendFileServer(int port) throws IOException{

        Serveur1 client=new Serveur1();

        client.sendFile(port);

    }


    public void EcrireFichier(String s){

        Path chemin = Paths.get("attachement.txt");

        // convertit String en un tableau d'octets
        byte[] data = s.getBytes();

        OutputStream output = null;
        try {
            // Un objet BufferedOutputStream est affecté à la référence OutputStream.
            output = new BufferedOutputStream(Files.newOutputStream(chemin, CREATE));
            // Ecrire dans le fichier
            output.write(data);

            // vider le tampon
            output.flush();

            // fermer le fichier
            output.close();

        } catch (Exception e) {
            System.out.println("Message erruer: " + e);
        }

    }

    public static void main(String[] args) throws IOException {

        Serveur1 serv=new Serveur1();
        ServerSocket ss = new ServerSocket(7771);
        System.out.println("attend du connection...");
        Socket socket = ss.accept(); 
        System.out.println("Connection venant " + socket + "!");

        InputStream inputStream = socket.getInputStream();

        DataInputStream dataInputStream = new DataInputStream(inputStream);

        // lire le message de la prise 
        String message = dataInputStream.readUTF();

        serv.EcrireFichier(message);



        System.out.println("fermenture...");
        ss.close();
        socket.close();

        serv.sendFileServer(7772);

    }
}

