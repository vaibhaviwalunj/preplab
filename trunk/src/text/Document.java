package text;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Map;
import java.util.SortedMap;
import java.util.StringTokenizer;
import java.util.TreeMap;

public class Document {
	private Map<String, Float[]> words; // n_ij, tf_ij, idf_i
	int sumof_n_kj;
	
	/** inner class to do sorting of the map **/
	private static class ValueComparer implements Comparator<String> {
		private TreeMap<String, Float[]>  _data = null;
		public ValueComparer (TreeMap<String, Float[]> data){
			super();
			_data = data;
		}

         public int compare(String o1, String o2) {
        	 float e1 = ((Float[]) _data.get(o1))[2];
             float e2 = ((Float[]) _data.get(o2))[2];
             if (e1 < e2) return -1;
             if (e1 == e2) return 0;
             if (e1 > e2) return 1;
             return 0;
         }
	}


	
	public Document(BufferedReader br, TfIdf parent) {
		String line;
		String word;
		StringTokenizer tokens;
		sumof_n_kj = 0;
		Float[] tempdata;
		words = new TreeMap<String, Float[]>();
		try {
			line = br.readLine();
			while (line != null) {
				tokens = new StringTokenizer(line, " ");
				while(tokens.hasMoreTokens()) {
					word = tokens.nextToken();
					if (words.get(word) == null) {
						tempdata = new Float[]{0.0f,0.0f,0.0f};
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
			if (parent.corpus.get(word) == null) {
				parent.corpus.put(word, 1);
			} else {
				parent.corpus.put(word, parent.corpus.get(word) + 1);
			}
		}		
	}
	
	public void printData() {
		String word;
		Float[] td;
		for (Iterator<String> it = words.keySet().iterator(); it.hasNext(); ) {
			word = it.next();
			td = words.get(word);
			System.out.println(word + "\t" + td[0] + "\t" + td[1] + "\t" + td[2]);
		}
	}
}
