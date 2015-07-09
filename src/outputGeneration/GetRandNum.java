package outputGeneration;

import java.util.Random;

public class GetRandNum {
	static Random rand = new Random();
	
		public static int rand(int size){
			return rand.nextInt(size);
		}
}
