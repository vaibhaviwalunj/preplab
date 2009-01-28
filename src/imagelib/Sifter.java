package imagelib;

/**
 * Sifter.java
 *
 * Part of the library for the class 10-615 Art That Learns taught in
 * Carnegie Mellon University on Spring 2009 semester
 *
 */


// The Java SIFT library
import mpi.cbg.fly.*;
// Processing library
import processing.core.*;

// Java Libraries
import java.util.Vector;

/**
 * Class that handles the SIFT wrappers. SIFT is explained in: 
 * <a href="http://en.wikipedia.org/wiki/Scale-invariant_feature_transform">Scale Invariant Feature Transform</a>
 * 
 * Java SIFT code is taken from <a href="http://fly.mpi-cbg.de/~saalfeld/javasift.html>Stephan Saalfeld</a>'s
 * page and stripped down to be callable from other Java programs without needing ImageJ.
 * 
 * 
 * 
 * @author Barkin Aygun 
 *  
 *   
 * @see <a href="http://dev.processing.org/reference/core/javadoc/processing/core/PApplet.html">
 * processing.core.PApplet</a>
 * 
 * @usage Library
 */
public class Sifter {
	
	// steps
	private static int steps = 3;
	// initial sigma
	private static float initial_sigma = 1.6f;
	// feature descriptor size
	private static int fdsize = 4;
	// feature descriptor orientation bins
	private static int fdbins = 8;
	// size restrictions for scale octaves, use octaves < max_size and > min_size only
	private static int min_size = 64;
	private static int max_size = 1024;

	/**
	 * Set true to double the size of the image by linear interpolation to
	 * ( with * 2 + 1 ) * ( height * 2 + 1 ).  Thus we can start identifying
	 * DoG extrema with $\sigma = INITIAL_SIGMA / 2$ like proposed by
	 * \citet{Lowe04}.
	 * 
	 * This is useful for images scmaller than 1000px per side only. 
	 */ 
	//private static boolean upscale = false;
	//private static float scale = 1.0f;
	
	/**
	 * For using PImages directly from Processing for SIFT
	 * @param image	PImage to be converted
	 * @return FloatArray2D
	 */
	private static FloatArray2D ConvertImage(PImage image) {
		// Set image to grayscale and load pixels for reading
		int count = 0;
		FloatArray2D result;
		PImage bwimage = image;
		bwimage.filter(PImage.GRAY);
		bwimage.loadPixels();
		
		result = new FloatArray2D(bwimage.width, bwimage.height);
		for (int x = 0; x < bwimage.height; x++) {
			for (int y = 0; y < bwimage.width; y++) {
				result.data[count] = bwimage.pixels[count++] / 255.0f;
			}
		}
		
		return result;
	}
	
	/**
	 * Runs sift using default parameters on a given PImage
	 * @param image PImage to run Sift on
	 * @return Vector<Feature> Set of features found on the image
	 * 
	 * @see Feature
	 */
	public static Vector<Feature> runSift(PImage image) {
		return runSift(image, steps, initial_sigma, fdsize, fdbins, min_size, max_size);
	}
	
	/**
	 * Runs sift on a given PImage with given parameters
	 * 
	 * @param image PImage to run Sift on
	 * @param steps Number of steps
	 * @param initial_sigma Initial Blur Factor
	 * @param fdsize Feature Descriptor Size
	 * @param fdbins Number of Feature Descriptor Bins
	 * @param min_size Minimum size for a feature
	 * @param max_size Maximum size for a feature
	 * @return Vector of Features
	 * 
	 * @see Feature
	 */
	public static Vector<Feature> runSift(PImage image, int steps, float initial_sigma,
									int fdsize, int fdbins, int min_size, int max_size) {
		Vector<mpi.cbg.fly.Feature> fs;
		FloatArray2DSIFT sift = new FloatArray2DSIFT( fdsize, fdbins );
		FloatArray2D fa = ConvertImage(image);
		Filter.enhance(fa, 1.0f);
		
		fa = Filter.computeGaussianFastMirror(fa, (float )Math.sqrt(initial_sigma * initial_sigma - 0.25) );
		
		sift.init(fa, steps, initial_sigma, min_size, max_size);
		fs = sift.run(max_size);
		
		Vector<Feature> fs1 = new Vector<Feature>(fs.size());
		for (mpi.cbg.fly.Feature f : fs) {
			Feature tempf = new Feature(f.scale, f.orientation, f.location, f.descriptor);;
			fs1.add(tempf);
		}
		
		return fs1;
	}
}
