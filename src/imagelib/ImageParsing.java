package imagelib;

import processing.core.*;

public class ImageParsing {
	public static double[] hist(PApplet app, PImage img) 
	{
		return histBW(app, img);
		
	}
	
	public static double[] histBW(PApplet app, PImage img)
	{
		PImage bwimg = img;
		bwimg.filter(PConstants.GRAY);
		
		int[] inthist = new int[256];
		double[] histogram = new double[256];
		
		int mColor;
		
		for (int i = 0; i < img.width; i++) {
			for (int j = 0; j < img.height; j++) {
				mColor = bwimg.get(i,j) >> 16 & 0xFF;
				inthist[mColor]++;
			}
		}
		
		double sum = 0.0;
		for (int i = 0; i < 256; i++) {
			sum += inthist[i];
		}
		
		for (int i = 0; i < 256; i++) {
			histogram[i] = (double)inthist[i] / sum;
		}
		
		return histogram;
	}
	
	public static double[][] histColor(PApplet app, PImage img)
	{
		int[] rhist = new int[256];
		int[] ghist = new int[256];
		int[] bhist = new int[256];
		double[][] histogram = new double[3][256];
		
		int r,g,b;
		
		for (int i = 0; i < img.width; i++) {
			for (int j = 0; j < img.height; j++) {
				r = img.get(i,j) >> 16 & 0xFF;
				g = img.get(i,j) >> 8 & 0xFF;
				b = img.get(i,j) & 0xFF;
				rhist[r]++;
				ghist[g]++;
				bhist[b]++;
			}
		}
		
		double rsum = 0.0;
		double gsum = 0.0;
		double bsum = 0.0;
		for (int i = 0; i < 256; i++) {
			rsum += rhist[i];
			gsum += ghist[i];
			bsum += bhist[i];
		}
		
		for (int i = 0; i < 256; i++) {
			histogram[0][i] = (double)rhist[i] / rsum;
			histogram[1][i] = (double)ghist[i] / gsum;
			histogram[2][i] = (double)bhist[i] / bsum;
		}
		
		return histogram;
	}
	
	
	public static double[] hueHist(PApplet app, PImage img)
	{
		int[] inthist = new int[360];
		double[] histogram = new double[360];
		
		img.loadPixels();
		
		int mColor;
		
		for (int i = 0; i < img.pixels.length; i++) {
			mColor = (int) app.hue(img.pixels[i]);
			inthist[mColor]++;
		}
		
		double sum = 0.0;
		for (int i = 0; i < 360; i++) {
			sum += inthist[i];
		}
		
		for (int i = 0; i < 360; i++) {
			histogram[i] = (double)inthist[i] / sum;
		}
		
		
		return histogram;	
	}
	
	public static void main(String[] args) {
	}
}
