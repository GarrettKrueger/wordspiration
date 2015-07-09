package database;

import java.sql.*;

public class ConnectionManager {
	public static Connection dbase(){
		String dbURL1 = "jdbc:sqlserver://garrett-laptop\\gsqlserver;databaseName=test1;user=wordspiration;password=word;";
        try {
			Connection dbConn = DriverManager.getConnection(dbURL1);
			return (dbConn);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
        
	}
}
