package imagelib;

import processing.core.*;

public class ImageParsing {
	public static final char RED = 0;
	public static final char GREEN = 1;
	public static final char BLUE = 2;
	public static final char ALL = 3;
	
	public static double[] hist(PApplet app, PImage img) 
	{
		return histBW(app, img, 256);
	}
	
	public static double[] histBW(PApplet app, PImage img, int bins)
	{
		PImage bwimg = img;
		bwimg.filter(PConstants.GRAY);
		
		int[] inthist = new int[bins];
		double[] histogram = new double[bins];
		
		int mColor;
		
		for (int i = 0; i < img.pixels.length; i++)
		 {
				mColor = bwimg.pixels[i] >> 16 & 0xFF;
				inthist[(int) Math.floor(mColor * bins / 256)]++;
			}
		
		for (int i = 0; i < bins; i++) {
			histogram[i] = (double)inthist[i] / (img.pixels.length);
		}
		
		return histogram;
	}
	
	public static double[] histColor(PApplet app, PImage img) {
		return histColor(app, img, 256, ALL);
	}
	
	public static double[] histColor(PApplet app, PImage img, int bins) {
		return histColor(app, img, bins, ALL);
	}
	
	public static double[] histColor(PApplet app, PImage img, char COLOR) {
		return histColor(app, img, 256, COLOR);
	}
	
	public static double[] histColor(PApplet app, PImage img, int bins, int COLOR)
	{
		int[] rhist = new int[bins];
		int[] ghist = new int[bins];
		int[] bhist = new int[bins];
		
		double[] histogram;
		
		
		int r,g,b;
		
		img.loadPixels();
		
		for (int i = 0; i < img.pixels.length; i++)
		{
			r = img.pixels[i] >> 16 & 0xFF;
			g = img.pixels[i] >> 8 & 0xFF;
			b = img.pixels[i] & 0xFF;
			rhist[(int) Math.floor(r * bins / 256)]++;
			ghist[(int) Math.floor(g * bins / 256)]++;
			bhist[(int) Math.floor(b * bins / 256)]++;
		}
		
		if (COLOR == ALL) {
			histogram = new double[bins * 3];
			for (int i = 0; i < bins; i++) {
				histogram[i] = (double)rhist[i] / img.pixels.length;
				histogram[bins+i] = (double)ghist[i] / img.pixels.length;
				histogram[bins+bins+i] = (double)bhist[i] / img.pixels.length;
			}
		}
		else if (COLOR == RED){
			histogram = new double[bins];
			for (int i = 0; i < bins; i++) {
				histogram[i] = (double)rhist[i] / img.pixels.length;
			}
		}
		else if (COLOR == GREEN){
			histogram = new double[bins];
			for (int i = 0; i < bins; i++) {
				histogram[i] = (double)ghist[i] / img.pixels.length;
			}
		}
		else if (COLOR == BLUE){
			histogram = new double[bins];
			for (int i = 0; i < bins; i++) {
				histogram[i] = (double)bhist[i] / img.pixels.length;
			}
		}
		else
		{
			histogram = new double[1];
		}
		
		return histogram;
	}
	public static double[] histHue(PApplet app, PImage img) {
		return histHue(app, img, 360);
	}
	
	public static double[] histHue(PApplet app, PImage img, int hueLim)
	{
		int[] inthist = new int[hueLim];
		double[] histogram = new double[hueLim];
		
		img.loadPixels();
		
		int mColor;
		
		for (int i = 0; i < img.pixels.length; i++) {
			mColor = (int) app.hue(img.pixels[i]);
			inthist[(int) Math.floor(mColor * hueLim / 360) ]++;
		}
		
		for (int i = 0; i < hueLim; i++) {
			histogram[i] = (double)inthist[i] / (img.pixels.length);
		}
		
		
		return histogram;	
	}
	
	public static void main(String[] args) {
	}
}
