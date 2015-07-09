package database;

import java.sql.CallableStatement;
//import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
//import java.util.HashMap;
//import java.util.Map;
import java.sql.ResultSet;

public class DatabaseProcedureCalls{

	public static void addWord(String word, String tag) {
        try {
            String call = "{call dbo.addword(?,?)}";
            CallableStatement pCall = ConnectionManager.dbase().prepareCall(call);
            pCall.setString(1, word);
            pCall.setString(2, tag);
            pCall.execute();
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        return;
    }

public static void markFirst(String ptag1, String ptag2, String ptag3) {
        try {
            String call = "{call dbo.markFirst(?,?,?)}";
            CallableStatement pCall = ConnectionManager.dbase().prepareCall(call);
            pCall.setString(1, ptag1);
            pCall.setString(2, ptag2);
            pCall.setString(3, ptag3);
            pCall.execute();
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        return;
    }
	
 public static void addParentChild(String ptag1, String ptag2, String ptag3, String cTag) {
        try {
            String call = "{call dbo.addParentChild(?,?,?,?)}";
            CallableStatement pCall = ConnectionManager.dbase().prepareCall(call);
            pCall.setString(1, ptag1);
            pCall.setString(2, ptag2);
            pCall.setString(3, ptag3);
            pCall.setString(4, cTag);
            pCall.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return;
    }

 public static void markLast(String ptag1, String ptag2, String ptag3) {
        try {
            String call = "{ call dbo.markLast(?,?,?)}";
            CallableStatement pCall = ConnectionManager.dbase().prepareCall(call);
            pCall.setString(1, ptag1);
            pCall.setString(2, ptag2);
            pCall.setString(3, ptag3);
            pCall.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return;
    }

	public static String getWord(String tag) throws SQLException{
		String query = "SELECT TOP 1 word FROM word where " + 
						"tag = '" + tag + "' ORDER BY NEWID()";
		Statement qStatement;
		qStatement = ConnectionManager.dbase().createStatement();
		ResultSet Result = qStatement.executeQuery(query);
		String word = "";
		while (Result.next()){
			word = Result.getString("WORD");
			}
		return word;
	}
}