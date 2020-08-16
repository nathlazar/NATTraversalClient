/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nattraversalclient;

import clientInfo.ClientInfo;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Lazar
 */
public class ClientIntroduce {
    
    private DatagramSocket socket;
    private DatagramPacket sendPacket,receivePacket;
    private HashMap<String,ClientInfo> usersList=new HashMap<String,ClientInfo>();
    private int port1=7070,port2=7071,soTimeout1=20000,soTimeout2=1000,currentSoTimeout=soTimeout1,currentPort=port1;
    
    public ClientIntroduce(){
        
        try {
            
            //String host=InetAddress.getLocalHost().getHostName();
            String host="bbb-desktop";
            System.out.println(host);
            while(true){
                
                int timeout=0;
                
                try{
                    
                    socket=new DatagramSocket();
                    socket.setSoTimeout(currentSoTimeout);
                    byte[] data=host.getBytes();
                    //sendPacket=new DatagramPacket(data,data.length,InetAddress.getByName("127.0.0.1"),currentPort);
                    sendPacket=new DatagramPacket(data,data.length,InetAddress.getByName("94.229.71.151"),currentPort);
                    try {
                        
                        socket.send(sendPacket);
                        receivePacket=new DatagramPacket(new byte[1024],1024);
                        socket.receive(receivePacket);
                        String response = new String(receivePacket.getData());
                        String[] splitResponse = response.split("-");
                        InetAddress ip = InetAddress.getByName(splitResponse[0].substring(1));
                        int port = Integer.parseInt(splitResponse[1]);
                        String hostName=splitResponse[2].trim()+"-"+splitResponse[3].trim();
                        System.out.println(" IP: "+ip+" PORT: "+port);
                        
                        if(!hostName.equals(host)){
                            
                            if(usersList.containsKey(hostName)){
                                
                                usersList.get(hostName).setPort(port);
                                usersList.get(hostName).setPort2(port);
                                
                            }
                            else{
                                
                                usersList.put(hostName,new ClientInfo(ip.toString().substring(1),port,port,hostName));
                                
                            }
                            
                        }

                        int localPort=socket.getLocalPort();
                        socket.close();

                        socket=new DatagramSocket(localPort);
                        socket.setSoTimeout(1000);

                        while(true){

                            if(!connectToUsers()) break;
  
                            try{

                                receivePacket=new DatagramPacket(new byte[1024],1024);
                                socket.receive(receivePacket);
                                System.out.println(new String(receivePacket.getData()));

                            }
                            catch(IOException e){//receive client packet exception

                                System.out.println("TIME OUT");
                                timeout++;
                                if(timeout==15){
                                    
                                    System.out.println("reconnecting to relay server");
                                    break;
                                    
                                }

                            } 

                        }
                        
                    } 
                    catch (IOException ex) {//receive server packet exception
                        
                        //Logger.getLogger(ClientIntroduce.class.getName()).log(Level.SEVERE, null, ex);
                        if(currentPort==7070){
                        
                            currentPort=7071;
                            currentSoTimeout=soTimeout2;
                                    
                        }
                        else {

                            currentPort=7070;
                            currentSoTimeout=soTimeout1;
                            
                        }
                        
                        System.out.println("reconnecting to relay server on port "+currentPort);
                        socket.close();

                    }
                    
                } 
                catch (SocketException ex) {//main socket exception
                    
                    Logger.getLogger(ClientIntroduce.class.getName()).log(Level.SEVERE, null, ex);
                    
                    
                }
                
            }
        } 
        catch (UnknownHostException ex) {//unknown host exception 
            
            Logger.getLogger(ClientIntroduce.class.getName()).log(Level.SEVERE, null, ex);
            
        } 
        
        
    }
    
    private synchronized boolean connectToUsers(){
        
        if(!usersList.isEmpty()){
            
            for(String key:usersList.keySet()){
                
                
                try {
                    
                    
                    byte[] data="musta".getBytes();
                    DatagramPacket sendPacket=new DatagramPacket(data,data.length,
                            InetAddress.getByName(usersList.get(key).getIPAddress()),usersList.get(key).getPort());
                    socket.send(sendPacket);
                    System.out.println("send to "+usersList.get(key).getUserID()+" "
                            +usersList.get(key).getIPAddress()+" "+usersList.get(key).getPort());
                    
                } 
                catch (IOException ex) {
                
                    Logger.getLogger(ClientIntroduce.class.getName()).log(Level.SEVERE, null, ex);
                
                }
                
            }
            
            return true;
        
        }
        else{
            
            return false;
            
        }
    }
    
}
