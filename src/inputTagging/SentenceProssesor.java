package inputTagging;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import database.DatabaseProcedureCalls;

import edu.stanford.nlp.ling.HasWord;
import edu.stanford.nlp.ling.TaggedWord;
import edu.stanford.nlp.process.DocumentPreprocessor;
import edu.stanford.nlp.tagger.maxent.MaxentTagger;
import edu.stanford.nlp.util.StringUtils;

public class SentenceProssesor {
	
	public static void sentenceProssesor() throws FileNotFoundException, SQLException{
	
	MaxentTagger tagger =  new MaxentTagger 
		("edu/stanford/nlp/models/pos-tagger/english-left3words/english-left3words-distsim.tagger");//this is what tags each word
	DocumentPreprocessor tokenizer;//this is what splits up sentences
	
    String file = "C:\\test3.txt";
	int numsentence = 0;
	tokenizer = new DocumentPreprocessor(new FileReader(file));//tokenizer splits up sentences of input
	CharSequence quote = "``";
	boolean quotation = false;
	for (List<HasWord> sentence : tokenizer) {//for each individual sentence in the tokenizer
		List<TaggedWord> tagged = tagger.tagSentence(sentence); //tag the sentence
		List <String> sentenceTag = new ArrayList<String>(); 
		
		for (TaggedWord individword : tagged){
			String[] out = StringUtils.splitOnChar((individword.toString()),'/');
			CharSequence apos = "'";
	    	CharSequence aposR = "''";

	    	if (out[0].contains(quote)){
	    		System.out.println(out[0]);
	    		quotation = true;
	    		break;
	    		}
	    	  if (out[0].contains(apos) || out[1].contains(apos) || out[0].contains(quote)){
	    		  out[0] = out[0].replace(apos, aposR);
	    		  out[1] = out[1].replace(apos, aposR);
	    	  }
	    	  DatabaseProcedureCalls.addWord(out[0], out[1]);
			sentenceTag.add(out[1]);//stores the tag in a arraylist
		}
		if (quotation){
			System.out.println(quotation);
			quotation = false;
			continue;
		}
		String current1 = ""; //clear out the variable before each iteration
		String current2 = "";
		String current3 = "";
		String nextT = "";
	
		ListIterator<String> wordIt = sentenceTag.listIterator();//iterator for list of POS tags


		if (sentenceTag.size() > 3) {
			current1 = wordIt.next();
			current2 = wordIt.next();
			current3 = wordIt.next();
			DatabaseProcedureCalls.markFirst(current1, current2, current3);
			while(wordIt.hasNext()){//while there is something to process
				nextT = wordIt.next();
				DatabaseProcedureCalls.addParentChild(current1, current2, current3, nextT);
				current1 = current2; //current tag moved to previous position	
				current2 = current3;
				current3 = nextT;
			}
			DatabaseProcedureCalls.markLast(current1, current2, current3);
			System.out.println("Processesed " + numsentence++ +" sentences");
		}
	}
	}
}

