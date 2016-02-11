/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package computingjobmanager;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author jubayer
 */
public class Server {
    ServerSocket server;
    int id = 0;
    
    List<ClientInfo> clients = new ArrayList<>();
    List<Machine> machines = new ArrayList<>();
    
    long defaultAllocationTime = 20000;
    
    public Server(){
        
        for(int i=0;i<3;i++){
            machines.add(new Machine(i, false, 0));
        }
        
        try {
            server = new ServerSocket(7756);
            new ClientAdder().start();
        } catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static void main(String[] args){
        new Server();
    }
    
    class ClientAdder extends Thread{
        @Override
        public void run() {
            while(true){
                try {
                    Socket client = server.accept();
                    DataInputStream dataInputStream = new DataInputStream(client.getInputStream());
                    DataOutputStream dataOutputStream = new DataOutputStream(client.getOutputStream());
                    ClientInfo clientInfo = new ClientInfo(id, dataInputStream, dataOutputStream, client);
                    clientInfo.dataOutputStream.writeUTF(String.valueOf(id));
                    id++;
                    clients.add(clientInfo);
                    new ClientActionHandler(clientInfo).start();
                    System.out.println("ADDED : "+clientInfo.id);
                } catch (IOException ex) {
                    Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
    
    class ClientActionHandler extends Thread {
        ClientInfo clientInfo;
        public ClientActionHandler(ClientInfo clientInfo){
            this.clientInfo = clientInfo;
        }
        
        @Override
        public void run(){
            while(true){
                try {
                    if(clientInfo.dataInputStream.available()>0){
                        String res = clientInfo.dataInputStream.readUTF();
                        handleRequest(res);
                    }
                } catch (IOException ex) {
                    Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        
        public void handleRequest(String request){
            if(request.contains("list")){
                String res = "Available Machines : \n";
                for(Machine m : machines){
                    if(m.isBusy()){
                        res += m.getId() + " UnAvailable" + "\n";
                    }else{
                        res += m.getId() + " Available" + "\n";
                    }
                }
                try {
                    clients.get(Integer.parseInt(request.split(":")[1])).dataOutputStream.writeUTF(res);
                } catch (IOException ex) {
                    Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
                }
            }else if(request.contains("get")){
                int mid = Integer.parseInt(request.split(":")[2]);
                Machine machine = machines.get(mid);
                if(machine.isBusy()){
                    try {
                        clients.get(Integer.parseInt(request.split(":")[1])).dataOutputStream.writeUTF("This machine is allocated to someoneelse.Try again in "+((machine.getUntil()-System.currentTimeMillis())/1000)+" seconds");
                    } catch (IOException ex) {
                        Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }else{
                    long currentTime = System.currentTimeMillis();
                    long endTime = currentTime+defaultAllocationTime;
                    machine.setUntil(endTime);
                    machine.setBusy(true);
                    machine.reset(endTime);
                    try {
                        clients.get(Integer.parseInt(request.split(":")[1])).dataOutputStream.writeUTF("You have got machine id "+machine.getId()+" for "+((machine.getUntil()-System.currentTimeMillis())/1000)+" seconds");
                    } catch (IOException ex) {
                        Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }
    }
    
}
