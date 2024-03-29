package textlib;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Comparator;
import java.util.Iterator;
import java.util.SortedMap;
import java.util.StringTokenizer;
import java.util.TreeMap;

public class Document {
	public TreeMap<String, Double[]> words; // n_ij, tf_ij, tf_idf
	int sumof_n_kj;
	double vectorlength;
	
	/** inner class to do sorting of the map **/
	private static class ValueComparer implements Comparator<String> {
		private TreeMap<String, Double[]>  _data = null;
		public ValueComparer (TreeMap<String, Double[]> data){
			super();
			_data = data;
		}

         public int compare(String o1, String o2) {
        	 double e1 = ((Double[]) _data.get(o1))[2];
             double e2 = ((Double[]) _data.get(o2))[2];
             if (e1 > e2) return -1;
             if (e1 == e2) return 0;
             if (e1 < e2) return 1;
             return 0;
         }
	}


	
	public Document(BufferedReader br, TfIdf parent) {
		String line;
		String word;
		StringTokenizer tokens;
		sumof_n_kj = 0;
		vectorlength = 0;
		Double[] tempdata;
		words = new TreeMap<String, Double[]>();
		try {
			line = br.readLine();
			while (line != null) {
				tokens = new StringTokenizer(line, ":; \"\',.[]{}()!?-/");
				while(tokens.hasMoreTokens()) {
					word = tokens.nextToken().toLowerCase();
					word.trim();
					if (word.length() < 2) continue;
					if (words.get(word) == null) {
						tempdata = new Double[]{1.0,0.0,0.0};
						words.put(word, tempdata);
					}
					else {
						tempdata = words.get(word);
						tempdata[0]++;
						words.put(word,tempdata);
					}
					sumof_n_kj++;
				}
				line = br.readLine();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// Iterate through the words to fill their tf's
		for (Iterator<String> it = words.keySet().iterator(); it.hasNext(); ) {
			word = it.next();
			tempdata = words.get(word);
			tempdata[1] = tempdata[0] / (float) sumof_n_kj;
			words.put(word,tempdata);
			parent.addWordOccurence(word);
		}		
	}
	
	public void calculateTfIdf(TfIdf parent) {
		String word;
		Double[] corpusdata;
		Double[] worddata;
		double tfidf;
		for (Iterator<String> it = words.keySet().iterator(); it.hasNext(); ) {
			word = it.next();
			corpusdata= parent.allwords.get(word);
			worddata = words.get(word);
			tfidf = worddata[1] * corpusdata[1];
			worddata[2] = tfidf;
			vectorlength += tfidf * tfidf;
			words.put(word, worddata);
		}
		vectorlength = Math.sqrt(vectorlength);
	}
	
	public void printData() {
		String word;
		Double[] td;
		for (Iterator<String> it = words.keySet().iterator(); it.hasNext(); ) {
			word = it.next();
			td = words.get(word);
			System.out.println(word + "\t" + td[0] + "\t" + td[1] + "\t" + td[2]);
		}
	}
	
	public String[] bestWordList(int numWords) {
		SortedMap<String, Double[]> sortedWords = new TreeMap<String, Double[]>(new Document.ValueComparer(words));
		sortedWords.putAll(words);
		int counter = 0;
		String[] bestwords = new String[numWords];
		for (Iterator<String> it = sortedWords.keySet().iterator(); it.hasNext() && (counter < numWords); counter++) {
			bestwords[counter] = it.next();
		}
		return bestwords;
	}
	
	public String[] bestWordList() {
		return bestWordList(10);
	}
}
