package imagelib;

import java.io.Serializable;
import java.util.StringTokenizer;

/**
 * SIFT feature container
 */
public class Feature implements Comparable< Feature >, Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public float scale;
	public float orientation;
	public float[] location;
	public float[] descriptor;
	public String imageId;
	public String matchId;
	public boolean matched;
	
	/** Dummy constructor for Serialization to work properly. */
	public Feature() {}
	
	public Feature( float s, float o, float[] l, float[] d )
	{
		scale = s;
		orientation = o;
		location = l;
		descriptor = d;
		matched = false;
	}
	
	public Feature( String encodedFeature) {
		StringTokenizer st = new StringTokenizer(encodedFeature, ",");
		String nt;
		int dcount = 0;
		
		nt = st.nextToken();
		imageId = nt;
		
		nt = st.nextToken();
		scale = Float.parseFloat(nt);
		
		nt = st.nextToken();
		orientation = Float.parseFloat(nt);
		
		location = new float[2];
		location[0] = Float.parseFloat(st.nextToken());
		location[1] = Float.parseFloat(st.nextToken());
		
		descriptor = new float[st.countTokens()];
		while(st.hasMoreTokens()) {
			descriptor[dcount++] = Float.parseFloat(st.nextToken());
		}
		matched = false;
	}

	/**
	 * comparator for making Features sortable
	 * please note, that the comparator returns -1 for
	 * this.scale &gt; o.scale, to sort the features in a descending order  
	 */
	public int compareTo( Feature f )
	{
		return scale < f.scale ? 1 : scale == f.scale ? 0 : -1;
	}
	
	public float descriptorDistance( Feature f )
	{
		float d = 0;
		for ( int i = 0; i < descriptor.length; ++i )
		{
			float a = descriptor[ i ] - f.descriptor[ i ];
			d += a * a;
		}
		return ( float )Math.sqrt( d );
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(this.imageId + ",");
		sb.append(this.scale + ",");
		sb.append(this.orientation + ",");
		sb.append(this.location[0] + "," + this.location[1] + ",");
		for (float d : this.descriptor) {
			sb.append(d + ",");
		}
		sb.append("\n");
		return sb.toString();
	}
}
