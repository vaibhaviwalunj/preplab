package similarity;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.TreeMap;

public class Similarity {
	public static final int COSINE = 0;
	public static final int L2 = 1;
	public static final int L1 = 2;
	
	/**
	 * COSINE similarity:
	 * 	normalized dot product
	 * 
	 * L2 metric:
	 * 	root of sum of squares of differences
	 * 
	 * L1 metric:
	 * 	sum of differences
	 * @param vec1
	 * @param vec2
	 * @param type
	 * @return
	 */
	public static double calculateSimilarity(double[] vec1, double[] vec2, int type) {
		double similarity = 0;
		assert(vec1.length == vec2.length);
		for (int i = 0; i < vec1.length; i++) {
			switch (type) {
			case(COSINE):
				similarity += vec1[i] * vec2[i];
				break;
			case(L2):
				similarity += Math.pow(vec1[i] - vec2[i], 2);
				break;
			case(L1):
				similarity += Math.abs(vec1[i] - vec2[i]);
				break;
			}
		}
		if (type == COSINE)
			similarity = similarity / (vectorLength(vec1) * vectorLength(vec2));
		else if (type == L2)
			similarity = Math.sqrt(similarity);
		return similarity;
	}
	
	public static double calculateSimilarity(double[] vec1, double[] vec2) {
		return calculateSimilarity(vec1, vec2, L2);
	}
	
	public static int[] similarVectors(double[][] vectors, int index, int type) {
		int[] indices = new int[vectors.length - 1];
		int ind = 0;
		TreeMap<Integer, Double> unsortedSimilarities = new TreeMap<Integer, Double>();
		for (int i = 0; i < vectors.length; i++) {
			if (i == index) continue;
			unsortedSimilarities.put(i, calculateSimilarity(vectors[index], vectors[i], type));
		}
		
		TreeMap<Integer, Double> sortedSimilarities = 
			new TreeMap<Integer, Double>(new ValueComparer(unsortedSimilarities));
		sortedSimilarities.putAll(unsortedSimilarities);
		for (Iterator<Integer> it = sortedSimilarities.keySet().iterator(); it.hasNext(); ) {
			indices[ind++] = it.next();
		}
		return indices;		
	}
	
	public static int[] similarVectors(double[][] vectors, int index) {
		return similarVectors(vectors, index, COSINE);
	}

	public static int[] similarVectors(ArrayList<double[]> vectors, int index, int type) {
		int[] indices = new int[vectors.size() - 1];
		int ind = 0;
		TreeMap<Integer, Double> unsortedSimilarities = new TreeMap<Integer, Double>();
		for (int i = 0; i < vectors.size(); i++) {
			if (i == index) continue;
			unsortedSimilarities.put(i, calculateSimilarity(vectors.get(index), vectors.get(i), type));
		}
		
		TreeMap<Integer, Double> sortedSimilarities = 
			new TreeMap<Integer, Double>(new ValueComparer(unsortedSimilarities));
		sortedSimilarities.putAll(unsortedSimilarities);
		for (Iterator<Integer> it = sortedSimilarities.keySet().iterator(); it.hasNext(); ) {
			indices[ind++] = it.next();
		}
		return indices;		
	}
	
	public static int[] similarVectors(ArrayList<double[]> vectors, int index) {
		return similarVectors(vectors, index, COSINE);
	}
		
	
	private static double vectorLength(double[] vec) {
		double len = 0;
		for (int i = 0; i < vec.length; i++) {
			len += vec[i] * vec[i];
		}
		len = Math.sqrt(len);
		return len;			
	}
	

	private static class ValueComparer implements Comparator<Integer> {
		private TreeMap<Integer, Double>  _data = null;
		public ValueComparer (TreeMap<Integer, Double> data){
			super();
			_data = data;
		}

         public int compare(Integer o1, Integer o2) {
        	 double e1 = _data.get(o1);
             double e2 = _data.get(o2);
             if (e1 > e2) return -1;
             if (e1 == e2) return 0;
             if (e1 < e2) return 1;
             return 0;
         }
	}

}
