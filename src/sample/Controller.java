package sample;

import com.jfoenix.controls.*;
import com.jfoenix.controls.JFXListView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.Initializable;
import java.net.URL;
import java.sql.*;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.UUID;

public class Controller implements Initializable
{
    public JFXButton loadButton;
    public JFXButton createButton;
    public JFXButton deleteButton;
    public JFXListView studentTable;
    public JFXButton filter1Button;
    public JFXButton filter2Button;
    public JFXButton filter3Button;


    final String AWS_URL = "jdbc:jtds:sqlserver://testdb2.cusuxagy1uhx.us-east-1.rds.amazonaws.com:1433/OttoDBHW";
    final String username = "admin";
    final String pass = "password123";


    public void createTable(String url, String user, String pass)
    {
        try {
            try
            {
                Class.forName("net.sourceforge.jtds.jdbc.Driver");//Required connection string for SQL Server
            }
            catch (ClassNotFoundException e) {
                e.printStackTrace();
                System.out.println("CONNECTION ERROR 1"); //General detection of error placement
            }
            Connection conn = DriverManager.getConnection(AWS_URL, username, pass);
            Statement stmt = conn.createStatement();
            try
            {
             stmt.execute("CREATE TABLE Student("+
                     "ID UNIQUEIDENTIFIER NOT NULL," +
                     "NAME VARCHAR(40)," +
                     "AGE INT," +
                     "MAJOR VARCHAR(40)," +
                     "GPA DECIMAL (3,2))"); //Show 3 total decimal places, two on the right.
                     System.out.println("TABLE HAS BEEN CREATED.");
            }
            catch(Exception f)
            {
                System.out.println("WARNING: TABLE ALREADY EXISTS, DID NOT CREATE.");
            }

            try {
                for (int i = 0; i < 10; i++)
                {
                    UUID ID = UUID.randomUUID(); //RNG for UUID creation
                    Random factor = new Random();
                    Student addStudent = new Student();
                    addStudent.ID = ID; //Declaring student variables
                    addStudent.Name = "Student: " + i; //Incrementing student's
                    addStudent.Age = new Random().nextInt(80 + 1 - 18 ) +18; //RNG for age 18-80
                    addStudent.Major = "CIS";
                    addStudent.GPA = 1.00 + (4.00 - 1.00) * factor.nextDouble(); //RNG for GPA between 1.00 and 4.00

                    stmt.executeUpdate("INSERT INTO Student VALUES ('" + addStudent.ID + "' , '" + addStudent.Name + "' , '" +
                             addStudent.Age + "' , '"+ addStudent.Major + "' , '" + addStudent.GPA + "')");
                }

            }
            catch(Exception g)
            {
                g.printStackTrace();
                System.out.println("CONNECTION ERROR 2");//General detection of error placement
            }
            stmt.close();
            conn.close();
        }
        catch(Exception e)
        {
            String msg = e.getMessage();
            System.out.println(msg);
        }
    }

    public void loadTableData(String url, String username, String pass)
    {
        try {
            try
            {
                Class.forName("net.sourceforge.jtds.jdbc.Driver");//Required connection string for SQL Server
            }
            catch (ClassNotFoundException e)
            {
                e.printStackTrace();
                System.out.println("CONNECTION ERROR 3");//General detection of error placement
            }
         Connection conn = DriverManager.getConnection(url, username, pass);
         Statement stmt = conn.createStatement();

         try
         {
          String tableScript = "SELECT ID, Name, Age, Major, GPA FROM Student;";
             ResultSet result = stmt.executeQuery(tableScript);
             ObservableList<Student> studList = FXCollections.observableArrayList();

             while (result.next()) //Result set contains query results
             {
              Student tempStudent = new Student();
              tempStudent.ID = UUID.fromString(result.getString("ID"));
              tempStudent.Name = result.getString("Name");
              tempStudent.Age = result.getInt("Age");
              tempStudent.Major = result.getString("Major");
              tempStudent.GPA = result.getDouble("GPA");
              studList.add(tempStudent);
             }

             studentTable.setItems(studList); //Implicates temp objects into ListView
             studentTable.getItems(); //Displaying contents from the ListView
             System.out.println("DATA LOADED SUCCESSFULLY.");
             stmt.close();
             conn.close();
         }

         catch(Exception j)
         {
            j.printStackTrace();
         }
        }
        catch(Exception h)
        {
            h.printStackTrace();
            System.out.println("CONNECTION ERROR 4");//General detection of error placement
        }
    }

    public void deleteTable(String url, String username, String pass)
    {
        try
        {
            try
            {
                Class.forName("net.sourceforge.jtds.jdbc.Driver");//Required connection string for SQL Server
            }
            catch (ClassNotFoundException e)
            {
                e.printStackTrace();
                System.out.println("CONNECTION ERROR 5");//General detection of error placement
            }
            Connection conn = DriverManager.getConnection(url, username, pass);
            Statement stmt = conn.createStatement();
            stmt.execute("DROP TABLE Student");
            stmt.close();
            conn.close();
            System.out.println("TABLE HAS BEEN DELETED.");
        }

        catch(Exception k)
        {
            String msg = k.getMessage();
            System.out.println("WARNING: TABLE DID NOT DELETE.");
            System.out.println(msg);
        }
    }

    public void filterQuery(String url, String username, String pass, String clause)
    {
        try
        {
            try
            {
                Class.forName("net.sourceforge.jtds.jdbc.Driver");//Required connection string for SQL Server
            }
            catch (ClassNotFoundException e)
            {
                e.printStackTrace();
                System.out.println("CONNECTION ERROR 6");//General detection of error placement
            }
            Connection conn = DriverManager.getConnection(url, username, pass);
            Statement stmt = conn.createStatement();

            try
            {
                String tableScript = "SELECT ID, Name, Age, Major, GPA FROM Student" +
                        " WHERE " + clause ; //Generates SQL Query with added clause (condition)
                ResultSet result = stmt.executeQuery(tableScript);
                ObservableList<Student> studList = FXCollections.observableArrayList();

                while (result.next())
                {
                    Student tempStudent = new Student();
                    tempStudent.ID = UUID.fromString(result.getString("ID"));
                    tempStudent.Name = result.getString("Name");
                    tempStudent.Age = result.getInt("Age");
                    tempStudent.Major = result.getString("Major");
                    tempStudent.GPA = result.getDouble("GPA");
                    studList.add(tempStudent);
                }

                studentTable.setItems(studList);
                studentTable.getItems();
                System.out.println("DATA LOADED SUCCESSFULLY.");
                stmt.close();
                conn.close();
            }

            catch(Exception j)
            {
                j.printStackTrace();
            }
        }
        catch(Exception h)
        {
            h.printStackTrace();
            System.out.println("CONNECTION ERROR 7");//General detection of error placement
        }
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) //Put the buttons here
    {
        createButton.setOnAction(new EventHandler<ActionEvent>()
        {
            @Override
            public void handle(ActionEvent actionEvent)
            {
                createTable(AWS_URL, username, pass);
            }
        });

        loadButton.setOnAction(new EventHandler<ActionEvent>()
        {
            @Override
            public void handle(ActionEvent actionEvent)
            {
                loadTableData(AWS_URL, username, pass);
            }
        });

        deleteButton.setOnAction(new EventHandler<ActionEvent>()
        {
            @Override
            public void handle(ActionEvent actionEvent)
            {
                deleteTable(AWS_URL, username, pass);
            }
        });

        filter1Button.setOnAction(new EventHandler<ActionEvent>()
        {
            @Override
            public void handle(ActionEvent actionEvent)
            {
                String clause1 = "Age >= 30"; //First filter button clause
                filterQuery(AWS_URL, username, pass, clause1);
            }
        });

        filter2Button.setOnAction(new EventHandler<ActionEvent>()
        {
            @Override
            public void handle(ActionEvent actionEvent)
            {
                String clause2 = "GPA >= 3.00"; //Second filter button clause
                filterQuery(AWS_URL, username, pass, clause2);
            }
        });

        filter3Button.setOnAction(new EventHandler<ActionEvent>()
        {
            @Override
            public void handle(ActionEvent actionEvent)
            {
                String clause3 = "Age > 30 AND GPA <= 2.50"; //Third filter button clause
                filterQuery(AWS_URL, username, pass, clause3);
            }
        });
    }
}
