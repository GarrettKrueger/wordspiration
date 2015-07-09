package inputTagging;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Iterator;

public class TagProssesor {
	/*name is the POS tag
	 * previous is a hashmap storing each hit of a POS tag prior to the tag in name
	 * next is a hashmap storing each hit of a POS tag after the tag in name*/
	private String name;
	private HashMap<String, Integer> next;
	private int totalUse;
	private int isFirst;
	private int isLast;
	
	public TagProssesor(String inTag){
		setName(inTag); //constructor
		setTotalUse();
		setFirst();
		setLast();
	}
	
	public String getName() {return name;}
				
	public void setName(String name) {this.name = name;}
		
	public HashMap<String, Integer> getNext() {return next;}
		
	public void setNext(HashMap<String, Integer> next) {this.next = next;}
	
	public int getFirst() {return isFirst;}
	
	public void setFirst() {this.isFirst = 0;}
	
	public int getLast() {return isLast;}
	
	public void setLast() {this.isLast = 0;}
	
	public void setTotalUse() {this.totalUse = 0;}
		
	public void addNext(String tag){
		if (tag.equals(".") || tag.equals("!") || tag.equals("?")){
			this.addLast();
		} else {
		int plusIt = this.next.get(tag);
		plusIt++;
		this.next.put(tag, plusIt);
		}
	}
	
	public void used(){
		this.totalUse++;
	}
	
	public void addFirst(){
		this.isFirst++;
	}
	
	public void addLast(){
		this.isLast++;
	}

	public void processStats(Connection dbase){
		String query;
		int first = 0;
		int last = 0 ;
		int tUse = 0;
		try{
			Statement stmt = dbase.createStatement();
			query = "Select total_use, t_first, t_last from wordspiration.settings_parent where TRIM(P_TAG) = '" + name + "'";
			ResultSet rs = stmt.executeQuery(query);
			while (rs.next()){
				first = rs.getInt("t_first");
				last = rs.getInt("t_last");
				tUse = rs.getInt("total_use");
			}
			first += isFirst;
			last += isLast;
			tUse += totalUse;
			query = "UPDATE wordspiration.settings_parent set t_first = " + first + ", t_last = " + last + ", total_use = " + tUse
					+ " where TRIM(p_tag) = '" + name + "'";
			stmt.executeUpdate(query);
			
			Iterator<String> nextIterator = this.next.keySet().iterator();
			while(nextIterator.hasNext()){
			  String key = nextIterator.next();
			  int times = this.next.get(key);
			  if (times > 0){ 
				  query = "Select total_use from wordspiration.settings_child where TRIM(P_TAG) = '" + name 
						  + "' and TRIM(C_TAG) = '" + key + "'";
				  rs = stmt.executeQuery(query);
				  while (rs.next()){
						tUse = rs.getInt("total_use");
				  }
				  tUse += times;
				  query = "UPDATE wordspiration.settings_child set total_use = " + tUse
							+ " where TRIM(P_TAG) = '" + name + "' and TRIM(C_TAG) = '" + key + "'";
				  stmt.executeUpdate(query);
			  };
			}

			} catch (SQLException se){
				se.printStackTrace();
			}
		return;
	}
}


