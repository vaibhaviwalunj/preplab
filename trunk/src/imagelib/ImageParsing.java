package imagelib;

import processing.core.*;

public class ImageParsing {
	public static float[] hist(PApplet app, PImage img) 
	{
		return histBW(app, img);
		
	}
	
	public static float[] histBW(PApplet app, PImage img)
	{
		PImage bwimg = img;
		bwimg.filter(PConstants.GRAY);
		
		int[] inthist = new int[256];
		float[] histogram = new float[256];
		
		int mColor;
		
		for (int i = 0; i < img.width; i++) {
			for (int j = 0; j < img.height; j++) {
				mColor = bwimg.get(i,j) >> 16 & 0xFF;
				inthist[mColor]++;
			}
		}
		
		float sum = 0.0f;
		for (int i = 0; i < 256; i++) {
			sum += inthist[i];
		}
		
		for (int i = 0; i < 256; i++) {
			histogram[i] = (float)inthist[i] / sum;
		}
		
		return histogram;
	}
	
	public static float[][] histColor(PApplet app, PImage img)
	{
		int[] rhist = new int[256];
		int[] ghist = new int[256];
		int[] bhist = new int[256];
		float[][] histogram = new float[3][256];
		
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
		
		float rsum = 0.0f;
		float gsum = 0.0f;
		float bsum = 0.0f;
		for (int i = 0; i < 256; i++) {
			rsum += rhist[i];
			gsum += ghist[i];
			bsum += bhist[i];
		}
		
		for (int i = 0; i < 256; i++) {
			histogram[0][i] = (float)rhist[i] / rsum;
			histogram[1][i] = (float)ghist[i] / gsum;
			histogram[2][i] = (float)bhist[i] / bsum;
		}
		
		return histogram;
	}
	
	public static float[] hueHist(PApplet app, PImage img) {
		return hueHist(app, img, 360);
	}
	
	public static float[] hueHist(PApplet app, PImage img, int arrayLimit)
	{
		app.colorMode(PConstants.HSB, arrayLimit - 1);
		
		int[] inthist = new int[arrayLimit];
		float[] histogram = new float[arrayLimit];
		
		img.loadPixels();
		
		int mColor;
		
		for (int i = 0; i < img.pixels.length; i++) {
			mColor = (int) app.hue(img.pixels[i]);
			inthist[mColor]++;
		}
		
		float sum = 0.0f;
		for (int i = 0; i < arrayLimit; i++) {
			sum += inthist[i];
		}
		
		for (int i = 0; i < arrayLimit; i++) {
			histogram[i] = (float)inthist[i] / sum;
		}
		
		return histogram;	
	}
	
	public static void main(String[] args) {
	}
}
