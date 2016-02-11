/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package computingjobmanager;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author jubayer
 */
public class Client {
    private int id;
    
    private Socket client;
    private DataInputStream dataInputStream;
    private DataOutputStream dataOutputStream;
    
    public Client(){
        try {
            client = new Socket("localhost",7756);
            dataInputStream = new DataInputStream(client.getInputStream());
            dataOutputStream = new DataOutputStream(client.getOutputStream());
            id = Integer.parseInt(dataInputStream.readUTF());
            
            Scanner sc = new Scanner(System.in);
            while(true){

                dataOutputStream.writeUTF("list"+":"+id);

                String response = dataInputStream.readUTF();

                System.out.println(response);

                System.out.print("Enter machine id : ");
                int mid = sc.nextInt();

                dataOutputStream.writeUTF("get:"+id+":"+mid);

                response = dataInputStream.readUTF();
                System.out.println(response);
            }              
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static void main(String[] args){
        new Client();
    }
    
}
