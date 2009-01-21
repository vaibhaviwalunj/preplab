package text;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

public class TfIdf {
	TreeMap<String, Document> documents;
	TreeMap<String, Integer> corpus;
	boolean corpusUpdated;
	
	FilenameFilter filter = new FilenameFilter() {
		public boolean accept(File dir, String name) {
			if (name.toLowerCase().endsWith(".txt")) return true;
			return false;
		}
	};
	
	public TfIdf(String foldername) {
		File datafolder = new File(foldername);
		if (datafolder.isDirectory()) {
			String[] files = datafolder.list(filter);
			for (int i = 0; i < files.length; i++) {
				InsertDocument(foldername + "/" + files[i]);
			}
		}
		else {
			InsertDocument(foldername);
		}
	}
	
	public void InsertDocument(String filename) {
		corpus = new TreeMap<String, Integer>();
		documents = new TreeMap<String, Document>();
		BufferedReader br;
		try {
			br = new BufferedReader(new FileReader(filename));
			Document doc = new Document(br, this);
			documents.put(filename, doc);
			System.out.println(doc.sumof_n_kj);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
		
	}

	public static void main(String[] args){
		TfIdf tf = new TfIdf("c:/sketchbook/tfidf_test/data");
		String word;
		
		/*for (Iterator<String> it = tf.corpus.keySet().iterator(); it.hasNext(); ) {
			word = it.next();
			System.out.println(word + " " + tf.corpus.get(word));
		}	*/	
		for (Iterator<String> it = tf.documents.keySet().iterator(); it.hasNext(); ) {
			word = it.next();
			tf.documents.get(word).printData();
		}	
	}
}

