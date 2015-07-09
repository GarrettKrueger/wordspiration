package database;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import database.ConnectionManager;

public class ThreeCalls {
	public static void p_addParentChild(String ptag1, String ptag2, String ptag3, String ctag){
		String where = " ptag1 = '" + ptag1 + "' AND ptag2 = '" + ptag2 + "' AND ptag3 = '" + ptag3 + "'";
		try {
			String numQuery = "select count(*) as NUM from parent_tag2 where" + where;
			Statement qStatement;
			qStatement = ConnectionManager.dbase().createStatement();
			ResultSet numResult = qStatement.executeQuery(numQuery);
			int numCheck = 0;
			while (numResult.next()){
				numCheck = numResult.getInt("NUM");
				
			}
			
			if (numCheck == 0){
				String insert = "insert into parent_tag2 values ('"+ ptag1 + "','" + ptag2 + "','" + ptag3 + "',1,0,0)";

				qStatement.executeUpdate(insert);

			} else {
				String update = "UPDATE parent_tag2 Set total_use = 1 + (select total_use from parent_tag2 where "+ where + ")" +
						" where " + where;
				
				qStatement.executeUpdate(update);
			}
			
			numQuery = "select count(*) as NUM from child_tag2 where" + where + " AND ctag = '" + ctag + "'";
			numResult = qStatement.executeQuery(numQuery);
			
			while (numResult.next()){
				numCheck = numResult.getInt("NUM");
			}
			if (numCheck == 0){
				String insert = "insert into child_tag2 values ('"
						+ ptag1 + "','" + ptag2 + "','" + ptag3 + "','" + ctag + "',1)";
				qStatement.executeUpdate(insert);
			} else {
				String update = "UPDATE child_tag2 Set total_use = 1 + (select total_use from parent_tag2 where "+ 
						where + " AND ctag = '" + ctag + "') where " + where + " AND ctag = '" + ctag + "'";
				//
				qStatement.executeUpdate(update);

			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static void p_markFirst(String ptag1, String ptag2, String ptag3){
		String where = " ptag1 = '" + ptag1 + "' AND ptag2 = '" + ptag2 + "' AND ptag3 = '" + ptag3 + "'";
		try {
			String numQuery = "select count(*) from parent_tag2 where" + where;
			Statement qStatement;
			qStatement = ConnectionManager.dbase().createStatement();
			ResultSet numResult = qStatement.executeQuery(numQuery);
			int numCheck = 0;
			while (numResult.next()){
				numCheck = numResult.getInt("1");
			}
			if (numCheck == 0){
				String insert = "insert into parent_tag2 values ('"
						+ ptag1 + "','" + ptag2 + "','" + ptag3 + "',0,1,0)";
				qStatement.executeUpdate(insert);
			} else {
				String numNum = "Select isFirst from parent_tag2 where " + where;
				numResult = qStatement.executeQuery(numNum);
				while (numResult.next()){
					numCheck = numResult.getInt("isFirst");
				}
				numCheck++;
				String update = "UPDATE parent_tag2 Set isFirst = " + numCheck + 
						" where " + where;
				qStatement.executeUpdate(update);
			} 
		}catch (SQLException e) {
				e.printStackTrace();
			}
		
	}
	
	public static void p_markLast(String ptag1, String ptag2, String ptag3){
		String where = " ptag1 = '" + ptag1 + "' AND ptag2 = '" + ptag2 + "' AND ptag3 = '" + ptag3 + "'";
		try {
			String numQuery = "select count(*) from parent_tag2 where" + where;
			Statement qStatement;
			qStatement = ConnectionManager.dbase().createStatement();
			ResultSet numResult = qStatement.executeQuery(numQuery);
			int numCheck = 0;
			while (numResult.next()){
				numCheck = numResult.getInt("1");
			}
			
			if (numCheck == 0){
				String insert ="insert into parent_tag2 values ('"
						+ ptag1 + "','" + ptag2 + "','" + ptag3 + "',1,0,1)";
				qStatement.executeUpdate(insert);
			} else {

				String update = "UPDATE parent_tag2 Set total_use = 1 + (select total_use from parent_tag2 where "+ where +"), isLast = 1 + "
						+ " (select isLast from parent_tag2 where " + where + ") where " + where;
				System.out.println(update);
				qStatement.executeUpdate(update);
			} 
		}catch (SQLException e) {
				e.printStackTrace();
			}
	}
	
	public static Map <TagTrioSet, Integer> p_firstAvg() throws SQLException{
		Map <TagTrioSet, Integer> averages = new HashMap <TagTrioSet, Integer>();
		String numQuery = "Select SUM(firstU) as Count " +
						"from parentTag";

		Statement qStatement;
		qStatement = ConnectionManager.dbase().createStatement();
		ResultSet numResult = qStatement.executeQuery(numQuery);
		int numSentences = 0;
		while (numResult.next()){
			numSentences = numResult.getInt("Count");
			}
		if (numSentences > 0){
			Connection conn = ConnectionManager.dbase();
			Statement stmt = null;
			String listquery = "SELECT ptag1, ptag2, ptag3, firstU" +
								" from parentTag" +
								" where firstU > 0";
			stmt = conn.createStatement();
		    ResultSet rs = stmt.executeQuery(listquery);
		    while (rs.next()){
				String parent1 = rs.getString("ptag1");
				String parent2 = rs.getString("ptag2");
				String parent3 = rs.getString("ptag3");
				TagTrioSet parents = new TagTrioSet(parent1, parent2, parent3);
				int numTimes = rs.getInt("firstU");
				numTimes = (numTimes * 100)/numSentences;

				averages.put(parents, numTimes);
				}
			}
		return averages;
		}

	public static Map<String, Integer> p_nextAvg(String ptag1, String ptag2, String ptag3) throws SQLException{
	Map <String, Integer> averages = new HashMap <String, Integer>();
	String where = " ptag1 = '" + ptag1 + "' AND ptag2 = '" + ptag2 + "' AND ptag3 = '" + ptag3 + "'";
		String numQuery = "Select SUM(totalU) as Count" +
						" from parentTag" +
						" where " + where;
			Statement qStatement;
			qStatement = ConnectionManager.dbase().createStatement();
			ResultSet numResult = qStatement.executeQuery(numQuery);
			int numSentences = 0;
			while (numResult.next()){
				numSentences = numResult.getInt("Count");
				}
			if (numSentences > 0){
				String listquery = "SELECT childT, totalU" +
									" from childTag" +
									" where " + where + " AND totalU > 0";
				ResultSet callResults = qStatement.executeQuery(listquery);
				
				while (callResults.next()){
					String child = callResults.getString("childT");
					int numTimes = callResults.getInt("totalU");
					averages.put(child, numTimes);
				}
			}
			return averages;
		}
			
	public static void addWord(String word, String tag) throws SQLException{
	
		word.toLowerCase();
		String where = " tag = '" + tag + "' AND word = '" + word + "'";

		String numQuery = "select count(*) as NUM from word where" + where;
		Statement qStatement;
		qStatement = ConnectionManager.dbase().createStatement();
		ResultSet numResult = qStatement.executeQuery(numQuery);
		int numCheck = 0;
		while (numResult.next()){
			numCheck = numResult.getInt("NUM");
		}
		
		if (numCheck == 0){
			String insert = "insert into word values ('"+ tag + "','" + word + "')";

			qStatement.executeUpdate(insert);
		}
	}
}
