/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bookstoreapp;

/**
 *
 * @author MARIA SAEED
 */
public abstract class Customer {
    
    abstract public boolean login(String username, String password);
    abstract public boolean logout();
    abstract public String getUsername();
    abstract public void setUsername(String username);
    abstract public String getPassword();
    abstract public void setPassword(String password);
    abstract public int getPoints();
    
}
