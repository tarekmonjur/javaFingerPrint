package fingerprint;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 * Created by Tarek Monjur.
 */

public abstract class DatabaseConnection {

    private static Connection DB;
    private static String Host;
    private static String Port;
    private static String Database;
    private static String UserName;
    private static String Password;

    private static String[] mysql = {"localhost","3306","finger_print","root",""};
//    private static String[] mysql = {"localhost","3306","finger_print","root","Ais1#Ais1"};
    private static String[] oracle = {"localhost","3306","finger_print","root",""};

    protected static Connection dbConnection(String dbDriver)
    {
        if(dbDriver.equals("mysql")){
            Host = mysql[0];
            Port = mysql[1];
            Database = mysql[2];
            UserName = mysql[3];
            Password = mysql[4];
        }else if(dbDriver.equals("oracle")){
            Host = oracle[0];
            Port = oracle[1];
            Database = oracle[2];
            UserName = oracle[3];
            Password = oracle[4];
        }

        return makeConnection(dbDriver, Host, Port, Database, UserName, Password);
    }


    private static Connection makeConnection(String driver, String host, String port, String db, String userName, String password)
    {
        try{
            Class.forName("com."+driver+".jdbc.Driver");
            DB = DriverManager.getConnection("jdbc:"+driver+"://"+host+":"+port+"/"+db, userName, password);
        }catch(Exception e){
            System.out.println(e);
        }
        return DB;
    }
}