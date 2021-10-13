/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bookstoreapp;

import javafx.scene.control.CheckBox;

/**
 *
 * @author MARIA SAEED
 */
public class SilverCustomer extends Customer {

    private String password;
    private String username;
    private int points;
    private CheckBox select;

    public SilverCustomer(String username, String password, int points){
        this.username=username;
        this.password=password;
        this.points=points;
        this.select = new CheckBox();
    }
    
    
    @Override
    public boolean login(String user, String pass){
        return (user.equals(username)&&pass.equals(password));
    }

    @Override
    public boolean logout() {
        return true;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public int getPoints() {
       return points;
    }

    @Override
    public void setUsername(String username) {
        this.username=username;
    }

    @Override
    public void setPassword(String password) {
        this.password=password;
    }
    
    public CheckBox getSelect() {
        return select;
    }

    public void setSelect(CheckBox select) {
        this.select = select;
    }

}
