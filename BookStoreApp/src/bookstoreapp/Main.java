/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bookstoreapp;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 *
 * @author MARIA SAEED
 */
public class Main extends Application {
    List<Book> arrList = new ArrayList<Book>();
    List<List <String>> CustomerarrList = new ArrayList<>();
    List<List <String>> BookarrList = new ArrayList<>();
    ObservableList<Book> selectb = FXCollections.observableArrayList(), allb;
    
    @Override
    public void start(Stage primaryStage) {
        
        //The initial login screen:
        primaryStage.setTitle("Book Store Application");
        
        //sign in button
        Button loginButton = new Button();
        loginButton.setText("Login");
        
        //layout of screen
        GridPane loginGrid=new GridPane();
        loginGrid.setAlignment(Pos.CENTER);
        loginGrid.setHgap(10);
        loginGrid.setVgap(10);
        loginGrid.setPadding(new Insets(25, 25, 25, 25));
        
        //Adding text to screen
        Text scenetitle = new Text("Welcome to the BookStore App");
        scenetitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        loginGrid.add(scenetitle, 0, 0, 2, 1);
        
        //Username box
        Label userName = new Label("User Name:");
        loginGrid.add(userName, 0, 1);
        TextField userTextField = new TextField();
        loginGrid.add(userTextField, 1, 1);

        //Passowrd box
        Label pw = new Label("Password:");
        loginGrid.add(pw, 0, 2);
        PasswordField pwBox = new PasswordField();
        loginGrid.add(pwBox, 1, 2);
        
        //add button to loginGrid
        HBox hbButton = new HBox(10);
        hbButton.setAlignment(Pos.CENTER);
        hbButton.getChildren().add(loginButton);
        loginGrid.add(hbButton, 1, 4);
        
        Label invalid = new Label("Incorrect password or username");//in case of invalid login;
        
        Admin admin=new Admin();
        
        loginButton.setOnAction((ActionEvent e) -> {
            //getting customerlogininfo from textfile
            //File dataFile=new File("customerList.txt");
            
            String username= userTextField.getText();//checking what is written in the fields by user
            String password= pwBox.getText();
            
            if(username.equals("admin") && password.equals("admin")){
                ownerLoginScreen(primaryStage, admin);
            }
            else{
                try{
                    if(admin.verification(username,password)){
                        customerScreen(primaryStage,admin);
                    }else
                        loginGrid.add(invalid,1,3);
                }catch(IOException e1){
                    loginGrid.add(invalid, 1, 3);
                }
            }
        });
           
        Scene loginScene=new Scene(loginGrid,600, 400);
        primaryStage.setScene(loginScene);
        primaryStage.show();
    }

    public void ownerLoginScreen(Stage primaryStage, Admin admin){
        Admin owner;
        GridPane ownerGrid=new GridPane();
        
        //screen layout
        ownerGrid.setAlignment(Pos.CENTER);
        ownerGrid.setHgap(10);
        ownerGrid.setVgap(10);
        ownerGrid.setPadding(new Insets(25, 25, 25, 25));
        
        Text scenetitle = new Text("Welcome Admin");
        scenetitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        ownerGrid.add(scenetitle, 0, 0, 2, 1);

        //Books button
        Button booksButton = new Button();
        booksButton.setText("Books");
        
        //Customers button
        Button customersButton = new Button();
        customersButton.setText("Customers");
        
        //logout button
        Button logoutButton = new Button();
        logoutButton.setText("Logout");
        
        //add button to loginGrid
        VBox vbButton = new VBox(30);
        vbButton.setAlignment(Pos.TOP_CENTER);
        
        booksButton.setPrefWidth(500);
        booksButton.setPrefHeight(40);
        
        customersButton.setPrefWidth(500);
        customersButton.setPrefHeight(40);
        
        logoutButton.setPrefWidth(500);
        logoutButton.setPrefHeight(40);
        
        vbButton.getChildren().addAll(booksButton,customersButton,logoutButton);
        ownerGrid.add(vbButton, 0, 0);
        
        booksButton.setOnAction((ActionEvent e) -> {
            addBookScreen(primaryStage,admin);
        });
        
        customersButton.setOnAction((ActionEvent e) -> {
            addCustomerScreen(primaryStage,admin);
            //System.out.println("open customers screen");
        });
        
        logoutButton.setOnAction((ActionEvent e) -> {
            start(primaryStage);
        });
        
        Scene scene = new Scene(ownerGrid, 600, 400);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    
    public void addBookScreen(Stage primaryStage, Admin admin){
        GridPane addBookGrid=new GridPane();
        TableView<Book> table=new TableView<>();
        VBox tableBox=new VBox(10);
        HBox bookNameBox=new HBox(10);
        HBox bookPriceBox=new HBox(10);
        HBox middleBox=new HBox(10);
        HBox bottomBox=new HBox(10);
        
        //Table properties
        TableColumn<Book, String> bookCol = new TableColumn<>("Book");  //name of book colume
        bookCol.setMinWidth(270);
        bookCol.setCellValueFactory(new PropertyValueFactory("bookName"));
        
        TableColumn<Book, Double> priceCol = new TableColumn<>("Price");    //price of book column
        priceCol.setMinWidth(100);
        priceCol.setCellValueFactory(new PropertyValueFactory("bookPrice"));
        
        TableColumn selectCol = new TableColumn<>("Select");
        selectCol.setMinWidth(50);
        selectCol.setCellValueFactory(new PropertyValueFactory("select"));
        //Displays previous books
        try{
            BufferedReader bookList = new BufferedReader(new FileReader(new File("bookList.txt")));
            String line;
            String[] array;

            while ((line=bookList.readLine())!=null){
                array = line.split(",");
                table.getItems().add(new Book((array[0]),Double.parseDouble(array[1])));
            }
            bookList.close();
        }catch (IOException e){
            System.out.println("error in addbook method");
        }
        
        //table setup
        table.getColumns().addAll(bookCol,priceCol, selectCol);
        tableBox.getChildren().addAll(table);
        addBookGrid.add(tableBox,0,1);
        
        //-------------------------------------------------------------------------------
        //Middle part of screen setup - Fix the alignment of input boxes
        //bookname box
        Label bookNameText=new Label("Book Name:");
        TextField bookNameInput = new TextField();
        bookNameBox.setAlignment(Pos.CENTER);
        bookNameBox.getChildren().addAll(bookNameText,bookNameInput);
        //book price box
        Label bookPriceText=new Label("Price:");
        TextField bookPriceInput = new TextField();
        bookPriceBox.setAlignment(Pos.CENTER);
        bookPriceBox.getChildren().addAll(bookPriceText,bookPriceInput);
        //add button
        Button addButton=new Button("Add");
        addButton.setOnAction((ActionEvent e) -> {
            Book newBook=new Book("",0);
            try{
                newBook.setBookName(bookNameInput.getText());
                newBook.setBookPrice(Double.parseDouble(bookPriceInput.getText()));
                table.getItems().add(newBook);
                System.out.println("Added.");

                /* Converts Textbox input to textfile */
                StringBuilder sb = new StringBuilder();
                sb.append(String.format("%s,%s",bookNameInput.getText(), Double.parseDouble(bookPriceInput.getText())));

                File file = new File("bookList.txt");
                FileWriter fw = new FileWriter(file, true);
                fw.write(sb.toString() + "\n");
                fw.close();
                System.out.println("Recorded.");   
            }catch(IOException e1){
                System.out.println("xc");
            }
            
            bookNameInput.clear();
            bookPriceInput.clear();
        });
        middleBox.getChildren().addAll(bookNameBox,bookPriceBox,addButton);
        middleBox.setAlignment(Pos.CENTER);
        addBookGrid.add(middleBox,0,2);
        
        //Bottom part of the screen------------------------
        Button deleteButton=new Button("Delete");
        deleteButton.setOnAction((ActionEvent e) ->{
            ObservableList<Book> selectb = FXCollections.observableArrayList(), allb;
            Book removeBook = new Book("",0);

//            List<List <String>> BookarrList = new ArrayList<>();

            allb = table.getItems();
            
            for(Book a : allb){
               if(a.getSelect().isSelected()){
                   selectb.add(a);
                   System.out.println("Deleted");
               } 
             }
//            selectb = table.getSelectionModel().getSelectedItems();

            selectb.forEach(allb::remove);

            /*Stores tableview into an arrayList*/
            for (int i = 0; i < allb.size();i++){
                 removeBook= allb.get(i);
                 BookarrList.add(new ArrayList<>());
                 BookarrList.get(i).add(String.format("%s,%s",removeBook.getBookName(), removeBook.getBookPrice()));
            }

            /* Displays arrayList int textfile */      
            File oldfile = new File("bookList.txt");
            File newfile = new File("deletedBookList.txt");

            try{
                FileWriter fw = new FileWriter("deletedBookList.txt", true);
                BufferedWriter bw = new BufferedWriter(fw);
                PrintWriter pw = new PrintWriter(bw);

                FileReader fr = new FileReader("bookList.txt");
                BufferedReader br = new BufferedReader(fr);

                /* Checks the contents in the arrayList */
                for (int i = 0; i < BookarrList.size();i++){
                    for (int j = 0; j < BookarrList.get(i).size();j++){
                        pw.write(BookarrList.get(i).get(j) + "\n");
                    }
                }
                pw.flush();
                fr.close();
                br.close();
                bw.close();
                fw.close();

                System.out.println("Recorded.");

                oldfile.delete();
                File dump = new File("bookList.txt");
                newfile.renameTo(dump);
                }catch(IOException e4){
                    System.out.println("Error! File not found.");
                }    
        });
        
        Button backButton=new Button("Back");
        backButton.setOnAction((ActionEvent e) -> {
            ownerLoginScreen(primaryStage,admin);
        });
       
        bottomBox.setAlignment(Pos.CENTER);
        bottomBox.getChildren().addAll(deleteButton,backButton);
        addBookGrid.add(bottomBox,0,3);
        
        
        //screen layout
        addBookGrid.setAlignment(Pos.CENTER);
        addBookGrid.setHgap(10);
        addBookGrid.setVgap(10);
        addBookGrid.setPadding(new Insets(25, 25, 25, 25));
        
        Scene scene=new Scene(addBookGrid,600, 400);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    
    public void addCustomerScreen(Stage primaryStage, Admin admin){
        GridPane addCustomerPane=new GridPane();
        TableView<SilverCustomer> table=new TableView<>();
        VBox tableBox=new VBox(10);
        HBox usernameBox=new HBox(10);
        HBox passwordBox=new HBox(10);
        HBox middleBox=new HBox(10);
        HBox bottomBox=new HBox(10);
        
        //Table properties
        TableColumn<SilverCustomer, String> usernameCol = new TableColumn<>("Username");  //name of book colume
        usernameCol.setMinWidth(270);
        usernameCol.setCellValueFactory(new PropertyValueFactory("username"));
        
        TableColumn<SilverCustomer, String> passwordCol = new TableColumn<>("Password");    //price of book column
        passwordCol.setMinWidth(100);
        passwordCol.setCellValueFactory(new PropertyValueFactory("password"));
        
        TableColumn<SilverCustomer, Integer> pointsCol = new TableColumn<>("Points");    //price of book column
        pointsCol.setMinWidth(100);
        pointsCol.setCellValueFactory(new PropertyValueFactory("points"));
        
        TableColumn selectCol = new TableColumn<>("Select");    //price of book column
        selectCol.setMinWidth(50);
        selectCol.setCellValueFactory(new PropertyValueFactory("select"));
        //Displays previous customers
        try{
            BufferedReader customerList = new BufferedReader(new FileReader(new File("customerInformation.txt")));
            String line;
            String[] array;

            while ((line=customerList.readLine())!=null){
                array = line.split(",");
                table.getItems().add(new SilverCustomer((array[0]),(array[1]),Integer.parseInt(array[2])));
            }
            customerList.close();
        }catch (IOException e){
            System.out.println("error in addcustomer method");
        }
        //table setup
        table.getColumns().addAll(usernameCol,passwordCol,pointsCol,selectCol);
        tableBox.getChildren().addAll(table);
        addCustomerPane.add(tableBox,0,1);
        
        //middle pane
        //username box
        Label usernameText=new Label("Username: ");
        TextField usernameInput = new TextField();
        usernameBox.setAlignment(Pos.CENTER);
        usernameBox.getChildren().addAll(usernameText,usernameInput);
        //book price box
        Label passwordText=new Label("Password: ");
        TextField passwordInput = new TextField();
        passwordBox.setAlignment(Pos.CENTER);
        passwordBox.getChildren().addAll(passwordText,passwordInput);
        //add button
        Button addButton=new Button("Add");
        addButton.setOnAction((ActionEvent e) -> {
            SilverCustomer newCustomer=new SilverCustomer("","",0);
            try{
                newCustomer.setUsername(usernameInput.getText());
                newCustomer.setPassword(passwordInput.getText());
                table.getItems().add(newCustomer);
                System.out.println("Added.");

                /* Converts Textbox input to textfile */
                StringBuilder sb = new StringBuilder();
                sb.append(String.format("%s,%s,%s",usernameInput.getText(), passwordInput.getText(),"0"));

                File file = new File("customerInformation.txt");
                FileWriter fw = new FileWriter(file, true);
                fw.write(sb.toString() + "\n");
                fw.close();
                System.out.println("Recorded.");   
            }catch(IOException e1){
                System.out.println("xc");
            }
            
            usernameInput.clear();
            passwordInput.clear();
        });
        middleBox.getChildren().addAll(usernameBox,passwordBox,addButton);
        middleBox.setAlignment(Pos.CENTER);
        addCustomerPane.add(middleBox,0,2);
        
        //Bottom part of the screen------------------------
        Button deleteButton=new Button("Delete");
        deleteButton.setOnAction((ActionEvent e) ->{
            ObservableList<SilverCustomer> selectb = FXCollections.observableArrayList(), allb;
            SilverCustomer removeCustomer= new SilverCustomer("","",0);

        //    List<List <String>> CustomerarrList = new ArrayList<>();

            allb = table.getItems();
            
            for(SilverCustomer a : allb){
               if(a.getSelect().isSelected()){
                   selectb.add(a);
                   System.out.println("Deleted");
               } 
             }
        //    selectb = table.getSelectionModel().getSelectedItems();

            selectb.forEach(allb::remove);

            /*Stores tableview into an arrayList*/
            for (int i = 0; i < allb.size();i++){
                 removeCustomer= allb.get(i);
                 CustomerarrList.add(new ArrayList<>());
                 CustomerarrList.get(i).add(String.format("%s,%s,%s",removeCustomer.getUsername(), removeCustomer.getPassword(),Integer.toString(removeCustomer.getPoints())));
            }

            /* Displays arrayList int textfile */      
            File oldfile = new File("customerInformation.txt");
            File newfile = new File("deletedCustomerInformation.txt");

            try{
                FileWriter fw = new FileWriter("deletedCustomerInformation.txt", true);
                BufferedWriter bw = new BufferedWriter(fw);
                PrintWriter pw = new PrintWriter(bw);

                FileReader fr = new FileReader("customerInformation.txt");
                BufferedReader br = new BufferedReader(fr);

                /* Checks the contents in the arrayList */
                for (int i = 0; i < CustomerarrList.size();i++){
                    for (int j = 0; j < CustomerarrList.get(i).size();j++){
                        pw.write(CustomerarrList.get(i).get(j) + "\n");
                    }
                }
                pw.flush();
                fr.close();
                br.close();
                bw.close();
                fw.close();

                System.out.println("Recorded.");

                oldfile.delete();
                File dump = new File("customerInformation.txt");
                newfile.renameTo(dump);
                }catch(IOException e4){
                    System.out.println("Error! File not found.");
                }    
        });
        
        Button backButton=new Button("Back");
        backButton.setOnAction((ActionEvent e) -> {
            ownerLoginScreen(primaryStage,admin);
        });
       
        bottomBox.setAlignment(Pos.CENTER);
        bottomBox.getChildren().addAll(deleteButton,backButton);
        addCustomerPane.add(bottomBox,0,3);
        
        //Screen layout
        addCustomerPane.setAlignment(Pos.CENTER);
        addCustomerPane.setHgap(10);
        addCustomerPane.setVgap(10);
        addCustomerPane.setPadding(new Insets(25, 25, 25, 25));
        Scene scene = new Scene(addCustomerPane, 600, 400);
        primaryStage.setScene(scene);
        primaryStage.show();
        
    }
    
    public void customerScreen(Stage primaryStage, Admin admin){
        GridPane customerGrid=new GridPane();
        TableView<Book> table=new TableView<>();
        VBox tableBox=new VBox();
        
        //screen layout
        customerGrid.setAlignment(Pos.CENTER);
        customerGrid.setHgap(10);
        customerGrid.setVgap(10);
        customerGrid.setPadding(new Insets(25, 25, 25, 25));
       
        //title of scene
        Text sceneTitle = new Text("Welcome  " + admin.getUsername());
        sceneTitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));

        //Text that shows on the screen
        Label numPoints=new Label("You have "+admin.getPoints()+" points");
        Label status=new Label("You are a "+admin.getStatus()+" customer");
        
        VBox labels=new VBox(5);
        labels.setAlignment(Pos.CENTER);
        labels.getChildren().addAll(sceneTitle,numPoints,status);
        customerGrid.add(labels, 0, 1);
        
        //mid part + table
        //Table properties
        CheckBox select;
        TableColumn<Book, String> bookCol = new TableColumn<>("Book");  //name of book colume
        bookCol.setMinWidth(270);
        bookCol.setCellValueFactory(new PropertyValueFactory("bookName"));
        
        TableColumn<Book, Double> priceCol = new TableColumn<>("Price");    //price of book column
        priceCol.setMinWidth(100);
        priceCol.setCellValueFactory(new PropertyValueFactory("bookPrice"));
        
        TableColumn<Book,Double> selectCol = new TableColumn();
        selectCol.setMinWidth(50);
        selectCol.setCellValueFactory(new PropertyValueFactory<>("select"));
        
        //Displays previous books
        try{
            BufferedReader bookList = new BufferedReader(new FileReader(new File("bookList.txt")));
            String line;
            String[] array;

            while ((line=bookList.readLine())!=null){
                array = line.split(",");
                table.getItems().add(new Book((array[0]),Double.parseDouble(array[1])));
            }
            bookList.close();
        }catch (IOException e){
            System.out.println("error in addbook method");
        }
        
        //table setup
        table.getColumns().addAll(bookCol,priceCol,selectCol);
        tableBox.getChildren().addAll(table);
        customerGrid.add(tableBox,0,2);

        //buttons +bottom part of screen
        Button buyButton=new Button();//buy the books with this button
        buyButton.setText("Buy");
        buyButton.setOnAction(e -> {
            Book buyBook = new Book("",0);


            allb = table.getItems();
        //    selectb = table.getSelectionModel().getSelectedItems();
            
            for(Book a : allb){
               if(a.getSelect().isSelected()){
                   selectb.add(a);
                   System.out.println("Added to cart.");
               } 
             }

            for (int i = 0; i < selectb.size();i++){
                 buyBook= selectb.get(i);
                 arrList.add(buyBook);
            }
            
        //    selectb.removeAll();
        //    arrList.removeAll(selectb);
            customerCostScreen(primaryStage, admin);
        });
        
        Button buyWithPointsButton=new Button();
        buyWithPointsButton.setText("Redeem Points & Buy");
        buyWithPointsButton.setOnAction(e -> {
            Book buyBook = new Book("",0);


            allb = table.getItems();
        //    selectb = table.getSelectionModel().getSelectedItems();
            
            for(Book a : allb){
               if(a.getSelect().isSelected()){
                   selectb.add(a);
                   System.out.println("Added to cart.");
               } 
             }

            for (int i = 0; i < selectb.size();i++){
                 buyBook= selectb.get(i);
                 arrList.add(buyBook);
            }
            
        //    selectb.removeAll();
        //    arrList.removeAll(selectb);
            customerCostScreen(primaryStage, admin);
        });
        
        Button logoutButton=new Button();
        logoutButton.setText("Logout");
        logoutButton.setOnAction((ActionEvent e) -> {
            start(primaryStage);
            //System.out.println("return to login screen");
        });
        
        HBox hbButton = new HBox(10);
        hbButton.setAlignment(Pos.CENTER);
        hbButton.getChildren().addAll(buyButton,buyWithPointsButton,logoutButton);
        customerGrid.add(hbButton, 0, 3);

        Scene scene = new Scene(customerGrid, 600, 400);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    
    
    public boolean buyNormal(){
        boolean buyNormal = true;
        return buyNormal;
    }
    public boolean buyWithPoints(){
        boolean buyPoints = true;
        return buyPoints;
    }
    public double totalCost(){
        double totCost = 0.0;
        for(Book a : arrList){
            totCost = totCost + a.getBookPrice();
        }
       return totCost;
    }
    public double updatePoints(){
        double points = 0;
        
        if(buyNormal() == true){
            for(Book a : arrList){
                points = points + 10*(a.getBookPrice());
                System.out.println("YOu earned " + points + " points.\n");
            }
        }
        else if(buyWithPoints() == true){
            double useablePoints = points/100 ;
            if(useablePoints >= totalCost()){
                useablePoints = useablePoints - totalCost();
                points = 100 * useablePoints;
            }
            else{
                 double due = totalCost() - useablePoints;
                 points = due;
                 System.out.println("You have " + due + " money due. \n Pay normally.\n");
                 points = 10*due;
                System.out.println("YOu earned " + points + " points.\n");
            }
        }
        return points;
    }
    public String getStatus() {
        String status;
        if(updatePoints() < 1000){
            status = "SILVER";
        } else status = "GOLD";
        
        return status;
    }
    public void customerCostScreen(Stage primaryStage, Admin admin){
        Label totcost = new Label("Total Cost: " + totalCost());
        Label points = new Label("Points: " + updatePoints() +", Status: " + getStatus());
        
        Button logoutt = new Button("Logout");
        
        logoutt.setOnAction(e -> {

            FileWriter fw = null;
            try {
 
                /* Converts Textbox input to textfile */
                StringBuilder sb = new StringBuilder();
                sb.append(String.format("%s,%s,%s",admin.getUsername(), admin.getPassword(),(int)updatePoints()));
                
                File file = new File("customerInformation.txt");
                fw = new FileWriter(file,true);
                fw.write(sb.toString() + "\n");
                fw.close();
                
                start(primaryStage);
                arrList.clear();
            //    arrList = new ArrayList<Book>();
            } catch (IOException ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                try {
                    fw.close();
                } catch (IOException ex) {
                    Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        VBox midCostScreen = new VBox();
        midCostScreen.getChildren().addAll(totcost,points,logoutt);
        midCostScreen.setAlignment(Pos.CENTER);
        midCostScreen.setSpacing(20);
        
        Scene scene = new Scene(midCostScreen, 600, 400);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    

    
    
    
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
