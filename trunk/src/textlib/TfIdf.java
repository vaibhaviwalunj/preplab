package textlib;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.util.Comparator;
import java.util.Iterator;
import java.util.TreeMap;

/**
 * An instance of TfIdf contains TreeMaps for documents and corpus
 * Documents dictionary uses file names as keys, and the document class as values
 * Corpus dictionary uses words as keys and [#articles they appear in, idf] as values
 * 
 * To use TfIdf algorithm, user needs to create an instance of it
 * by giving it a folder address. 
 * When user calls BuildAllDocuments(), TfIdf values for words in each document is
 * calculated
 * User can call methods such as bestWordList or similarDocuments using the filename
 * to get results.
 * See Document class documentation for more info
 * 
 * @author Barkin Aygun
 *
 */
public class TfIdf {
	public TreeMap<String, Document> documents;
	public TreeMap<String, Double[]> allwords; //d_j: t_i elem d_j, idf_j
	public boolean corpusUpdated;
	public int docSize;
	
	/**
	 * Filename filter to accept .txt files
	 */
	FilenameFilter filter = new FilenameFilter() {
		public boolean accept(File dir, String name) {
			if (name.toLowerCase().endsWith(".txt")) return true;
			return false;
		}
	};
	
	/**
	 *  This comparator lets the library sort the documents 
	 *  according to their similarities 
	 */
	private static class ValueComparer implements Comparator<String> {
		private TreeMap<String, Double>  _data = null;
		public ValueComparer (TreeMap<String, Double> data){
			super();
			_data = data;
		}

         public int compare(String o1, String o2) {
        	 double e1 = _data.get(o1);
             double e2 = _data.get(o2);
             if (e1 > e2) return -1;
             if (e1 == e2) return 0;
             if (e1 < e2) return 1;
             return 0;
         }
	}
	
	public TfIdf(String foldername) {
		allwords = new TreeMap<String, Double[]>();
		documents = new TreeMap<String, Document>();
		docSize = 0;
				
		File datafolder = new File(foldername);
		if (datafolder.isDirectory()) {
			String[] files = datafolder.list(filter);
			for (int i = 0; i < files.length; i++) {
				docSize++;
				insertDocument(foldername + "/" + files[i]);
			}
		}
		else {
			docSize++;
			insertDocument(foldername);
		}
		corpusUpdated = false;
		if (corpusUpdated == false)
		{
			updateCorpus();
		}
	}
	
	public void updateCorpus() {
		String word;
		Double[] corpusdata;
		for (Iterator<String> it = allwords.keySet().iterator(); it.hasNext(); ) {
			word = it.next();
			corpusdata = allwords.get(word);
			corpusdata[1] = Math.log(docSize / corpusdata[0]);

			allwords.put(word, corpusdata);
		}	
		corpusUpdated = true;
	}
	
	public void buildDocument(String documentName) {
		Document doc = documents.get(documentName);
		if (doc == null) return;
		doc.calculateTfIdf(this);
	}
	
	public void buildAllDocuments() {
		String word;
		for (Iterator<String> it = documents.keySet().iterator(); it.hasNext(); ) {
			word = it.next();
			documents.get(word).calculateTfIdf(this);
		}
	}
	
	public void insertDocument(String filename) {
		BufferedReader br;
		try {
			br = new BufferedReader(new FileReader(filename));
			Document doc = new Document(br, this);
			documents.put(filename.substring(filename.lastIndexOf('/') + 1), doc);
			if (corpusUpdated == false) updateCorpus();
			//System.out.println(doc.sumof_n_kj);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void addWordOccurence(String word) {
		Double[] tempdata;
		if (allwords.get(word) == null) {
			tempdata = new Double[]{1.0,0.0};
			allwords.put(word, tempdata);
		} else {
			tempdata = allwords.get(word);
			tempdata[0]++;
			allwords.put(word,tempdata);
			
		}
	}
	
	public double cosSimilarity(Document doc1, Document doc2) {
		String word;
		double similarity = 0;
		for (Iterator<String> it = doc1.words.keySet().iterator(); it.hasNext(); ) {
			word = it.next();
			if (doc2.words.containsKey(word)) {
				similarity += doc1.words.get(word)[2] * doc2.words.get(word)[2];
			}
		}
		similarity = similarity / (doc1.vectorlength * doc2.vectorlength);
		return similarity;
	}
	
	public String[] similarDocuments(String docName) {
		TreeMap<String, Double> similarDocs = new TreeMap<String, Double>();
		String otherDoc;
		for (Iterator<String> it = documents.keySet().iterator(); it.hasNext(); ) {
			otherDoc = it.next();
			if (docName.equals(otherDoc)) continue;
			similarDocs.put(otherDoc, cosSimilarity(documents.get(docName), documents.get(otherDoc)));
		}
		TreeMap<String, Double> sortedSimilars = new TreeMap<String, Double>(new ValueComparer(similarDocs));
		sortedSimilars.putAll(similarDocs);
		return sortedSimilars.keySet().toArray(new String[1]);
	}
	
	public String[] bestWords(String docName, int numWords) {
		return documents.get(docName).bestWordList(numWords);
	}
	
	public String[] bestWords(String docName) {
		return documents.get(docName).bestWordList();
	}
	
	public String[] documentNames() {
		return documents.keySet().toArray(new String[1]);
	}
	
	public static void main(String[] args){
		//Test code for TfIdf
		TfIdf tf = new TfIdf("c:/sketchbook/tfidf_test/data/na/");
		String word;
		Double[] corpusdata;
		for (Iterator<String> it = tf.allwords.keySet().iterator(); it.hasNext(); ) {
			word = it.next();
			corpusdata = tf.allwords.get(word);
			System.out.println(word + " " + corpusdata[0] + " " + corpusdata[1]);
		}	
		tf.buildAllDocuments();
		String[] bwords;
		String[] bdocs;
		for (Iterator<String> it = tf.documents.keySet().iterator(); it.hasNext(); ) {
			word = it.next();
			System.out.println(word);
			System.out.println("------------------------------------------");
			bwords = tf.documents.get(word).bestWordList(5);
			bdocs = tf.similarDocuments(word);
			for (int i = 0; i < 5; i++) {
				System.out.print(bwords[i] + " ");
			}
			System.out.println();
			for (int i = 0; i < 5; i++) {
				System.out.println(bdocs[i] + " ");
			}
			System.out.println("\n\n");
		}
		
	}
	
}

