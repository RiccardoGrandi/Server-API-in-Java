package com.example;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;

import com.fasterxml.jackson.databind.ObjectMapper;

public class App 
{
    public static void main( String[] args )
    {
            
            try {
                ServerSocket server = new ServerSocket(8080);
                while(true){
                    Socket client = server.accept();
                    BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
                    PrintWriter out = new PrintWriter(client.getOutputStream());

                    String richiesta = "";

                    richiesta = in.readLine();
                    System.out.println(richiesta);
                    String[] riga = richiesta.split(" ");
                    String path = riga[1];
                    
                    System.out.println("--" + path + "--");

                    do{
                        richiesta = in.readLine();
                        System.out.println(richiesta);
                        if(richiesta.isEmpty() || richiesta.equals(null)) break;
                    }while(true);

                    sendBinaryFile(client, path);
                    
                    out.flush();
                    client.close();
                }          

            } catch (IOException e) {
                e.printStackTrace();
            }
    }

    private static void sendBinaryFile(Socket socket, String path) {
        if(path.endsWith("/")){
            path = path + "index.html";
        }
        if(path.equals("/classe.json")){
            creaClasse();
        }

        try{

            File file = new File("./htdocs" + path);
            InputStream in = new FileInputStream(file);
            
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());

            out.writeBytes("HTTP/1.1 200 OK\n" );
            out.writeBytes("Content-Length: " + file.length()+ "\n");
            out.writeBytes("Server: Java HTTP Server from Taiti: 1.0\n");
            out.writeBytes("Date: " + new Date()+ "\n");
            out.writeBytes("Content-Type: " + getContentType(path) + "\n");
            out.writeBytes("\n");

            byte[] buf = new byte[8192];
            int n;
            while((n = in.read(buf)) != -1){
                out.write(buf, 0, n);
                System.out.println(buf);             
            } 
            out.close();
            in.close();
        }catch(FileNotFoundException ntF){
            try {
                DataOutputStream out = new DataOutputStream(socket.getOutputStream());
                if(getContentType(path).equals("text/plain charset=utf-8\n")){
                    out.writeBytes("HTTP/1.1 301 Move Permanently\n" );
                    out.writeBytes("location: " + path+"/");
                }
                else
                    out.writeBytes("HTTP/1.1 404 not found");

            } catch(IOException e){
                System.out.println("IOexception");
            }
        }catch(IOException e){
            System.out.println("IOexception");
        }
           
    }

    private static String getContentType(String path){
        String type = "text/plain charset=utf-8\n";
        try {
            type = path.split("\\.")[1];
            System.out.println("----------------------- "+ type);
            switch (type) {
                case "html":
                case "css":
                    type = "text/" + type+ "; charset=utf-8\n";
                    break;
                case "jpg":
                case "png":
                case "jpeg": 
                    type = "image/" + type;
                    break;
                case "json":  

                case "js":    
                    type = "application/" + type;
                    break;
            }
        } catch(IndexOutOfBoundsException inxU){
            System.out.println(type);
        }
        return type;
    }

    private static void creaClasse(){
        Alunno a1 = new Alunno("Mattia", "Pascal", new Date(1904 - 1900, 0, 1));
        Alunno a2 = new Alunno("Riccardo","Grandi", new Date(2005 - 1900, 0, 20));
        Alunno a3 = new Alunno("Anatolie", "Pavlov", new Date(2003 - 1900, 11, 10));
        Alunno a4 = new Alunno("Alessio", "Didi", new Date(2005 - 1900, 4, 03));
        Alunno a5 = new Alunno("Gigi", "Topo", new Date(2004 - 1900, 1, 28));

        ArrayList<Alunno> alunni = new ArrayList<>();
        alunni.add(a1);
        alunni.add(a2);
        alunni.add(a3);
        alunni.add(a4);
        alunni.add(a5);

        Classe classe = new Classe(5, "^DIA", "08-2W", alunni);
        
        System.out.println("creata la classe");
        
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            objectMapper.writeValue(new File("htdocs/classe.json"), classe);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}