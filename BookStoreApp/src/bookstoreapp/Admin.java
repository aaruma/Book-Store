/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bookstoreapp;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;

/**
 *
 * @author MARIA SAEED
 */
public class Admin {
    
    private String password;
    private String username;
    private int points;
    private Customer temp;
    
    public boolean verification(String user, String pass) throws IOException{
        boolean verification=false;
        try{
            BufferedReader list= new BufferedReader(new FileReader(new File("customerInformation.txt")));
            String line;
            String[] array;

            while ((line=list.readLine())!=null){
                array=line.split(",");
                username=array[0];
                password=array[1];
                points=Integer.parseInt(array[2]);
                Customer customer;
            if(points<1000){
                customer=new SilverCustomer(username,password,points);
                if(customer.login(user,pass)){
                    temp=customer;
                    verification=true;
                }
            }
            else if(points>=1000){
                customer=new GoldCustomer(username,password,points);
                if(customer.login(user,pass)){
                    temp=customer;
                    verification=true;
                }
            }
            } 
            list.close();
        }catch (IOException e){
            System.out.println("file does not exist");
        }
        
        return verification;
    }
    
    //method that adds a new file for a new customer
    public void addCustomer(String username, String password){

        File customerFile=new File("customerInformation.txt");
        SilverCustomer newCustomer=new SilverCustomer("","",0);
        try{
            FileWriter writingCustomerToFile=new FileWriter(customerFile,true);
            
            try (BufferedWriter writer=new BufferedWriter(writingCustomerToFile)) {
                writer.write(username+ ","+password+ ","+0+"\n");
            }
        }catch(IOException e){
            System.out.println("could not add customer");
        }
        
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPoints(int points) {
        this.points = points;
    }
    
    public int getPoints(){
        return temp.getPoints();
    }
    
    public String getStatus(){
        if(temp instanceof SilverCustomer)
            return "SILVER";
        else
            return "GOLD";
    }
    
}
