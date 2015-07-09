package outputGeneration;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import database.TagTrioSet;
import database.ThreeCalls;

public class RandomTag {
	
 public static String [] firstTag() throws SQLException{
	 
	 Map <TagTrioSet, Integer> firsts = new HashMap <TagTrioSet, Integer>();
	 firsts = ThreeCalls.p_firstAvg();
	 List<TagTrioSet> averages = new ArrayList<TagTrioSet>();
	 int start = 1;
	 int end;
	 int i;
	 for (Map.Entry<TagTrioSet, Integer> pair : firsts.entrySet()){
		 end = pair.getValue() + start;
		 for (i = start; i < end; i++){
			 averages.add(pair.getKey());
		 }
		 start =  i;
	 }

	 int index = GetRandNum.rand(averages.size());
	 String [] tag = new String[3];
	 tag = averages.get(index).getSet();
	 return tag;
 }
 
 public static String nextTag(String ptag1, String ptag2, String ptag3) throws SQLException{
	 Map <String, Integer> averages = new HashMap <String, Integer>();
	 averages = ThreeCalls.p_nextAvg(ptag1, ptag2, ptag3);
	 if (averages.isEmpty()){return "ENDRUN";}
	 List<String> next = new ArrayList<String>();
	 int start = 1;
	 int end;
	 int i = 1;
	 for (Map.Entry<String, Integer> pair : averages.entrySet()){
		 int pairAmount = pair.getValue();
		 if (pairAmount > 1){
			 end =  pairAmount + start;
			 for (i = start; i < end; i++){
				 next.add(pair.getKey());
			 }
			 start = i;
			 int index = GetRandNum.rand(next.size());
			 String tag = next.get(index);
			 return tag;
		}
	 }
	 return "";
 }
}
