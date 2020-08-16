/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clientInfo;

import java.io.Serializable;

/**
 *
 * @author Lazar
 */
public class ClientInfo implements Serializable{
    
    private String ipAddress;
    private int port,port2;
    private String userID;
    
    public ClientInfo(String ipAddress,int port,int port2,String userID){
        
        this.ipAddress=ipAddress;
        this.port=port;
        this.port2=port2;
        this.userID=userID;
        
    }
    
    public String getIPAddress(){
        
        return this.ipAddress;
        
    }
    
    public int getPort(){
        
        return this.port;
        
    }
    
    public int getPort2(){
        
        return this.port2;
        
    }
    
    public String getUserID(){
        
        return this.userID;
        
    }
    
    public void setPort(int port){
        
        this.port=port;
        
    }
    
    public void setPort2(int port2){
        
        this.port2=port2;
        
    }
    
}
