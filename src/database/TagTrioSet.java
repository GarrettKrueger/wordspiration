package database;

public class TagTrioSet {
	private final String ptag1;
	private final String ptag2;
	private final String ptag3;
	
	public TagTrioSet(String ptag1, String ptag2, String ptag3){
		this.ptag1 = ptag1;
		this.ptag2 = ptag2;
		this.ptag3 = ptag3;
	}
	
	public String[] getSet(){
		String[] result = {this.ptag1, this.ptag2, this.ptag3};
		return result;
	}
	
}
