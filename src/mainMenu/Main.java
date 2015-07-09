package mainMenu;

import inputTagging.SentenceProssesor;

import java.io.FileNotFoundException;
import java.sql.SQLException;
import java.util.Scanner;

import outputGeneration.RandomTag;
import database.DatabaseProcedureCalls;

public class Main {

	public static void main(String[] args) throws FileNotFoundException, SQLException {
		boolean run = true;
		int i;
		Scanner scan = new Scanner(System.in);
		System.out.println("Welcome to Wordspiration!");
		while (run){
			System.out.println("What would you like to do?");
			System.out.println("press 1 to input a file");
			System.out.println("press 2 to generate sentences");
			System.out.println("press 3 to exit");
			
			i = scan.nextInt();
			
			if (i == 1){
				SentenceProssesor.sentenceProssesor();
			}
			if (i == 2){
				System.out.println("how many sentences do you want?");
				int num = scan.nextInt();
				for (int s = 0; s < num; s++){
				String[] print = RandomTag.firstTag();
				String tag1 = DatabaseProcedureCalls.getWord(print[0]);
				String tag2 = DatabaseProcedureCalls.getWord(print[1]);
				String tag3 = DatabaseProcedureCalls.getWord(print[2]);
				System.out.print(tag1 + " " + tag2 + " " + tag3);
				String outputTag = "";
				while (!outputTag.equals("ENDRUN")){
					outputTag = RandomTag.nextTag(print[0], print[1], print[2]);
					print[0] = print[1];
					print[1] = print[2];
					print[2] = outputTag;
					//count++;
				}
				System.out.println();
				}
			System.out.println();
			}
			if (i == 3){
			run = !run;}
		}
		scan.close();
	}

}
