package faixaScanner;

/******************************************************************************
 *  Compilation:  javac EdgeDetector.java
 *  Execution:    java EdgeDetector filename
 * 
 *  Reads in an image from a file, and flips it horizontally.
 *
 *  % java EdgeDetector baboon.jpg
 *
 ******************************************************************************/

import java.awt.Color;
import java.io.File;

public class EdgeDetector {

    // truncate color component to be between 0 and 255
    public static int truncate(int a) {
        if      (a <   0) return 0;
        else if (a > 255) return 255;
        else              return a;
    }

    public static void main(String[] args) {

        int[][] filter1 = {
            { -1,  0,  1 },
            { -2,  0,  2 },
            { -1,  0,  1 }
        };

        int[][] filter2 = {
            {  1,  2,  1 },
            {  0,  0,  0 },
            { -1, -2, -1 }
        };

		String dir="C:\\Users\\Renan Fucci\\Dropbox\\Estante Magica_Renan_Fucci\\Pasta_Renan_Fucci\\2015-08-17\\"
				+"imagensFaixaScanner\\";
		String arq="img20150814_20045845";
		//apply it to an image
		
        Picture pic0 = new Picture(new File(dir+arq+"(b_w).jpg"));
        int width    = pic0.width();
        int height   = pic0.height();
        Picture pic1 = new Picture(width, height);


        for (int y = 1; y < height - 1; y++) {
            for (int x = 1; x < width - 1; x++) {

                // get 3-by-3 array of colors in neighborhood
                int[][] gray = new int[3][3];
                for (int i = 0; i < 3; i++) {
                    for (int j = 0; j < 3; j++) {
                        gray[i][j] = (int) Luminance.lum(pic0.get(x-1+i, y-1+j));
                    }
                }

                // apply filter
                int gray1 = 0, gray2 = 0;
                for (int i = 0; i < 3; i++) {
                    for (int j = 0; j < 3; j++) {
                        gray1 += gray[i][j] * filter1[i][j];
                        gray2 += gray[i][j] * filter2[i][j];
                    }
                }
                // int magnitude = 255 - truncate(Math.abs(gray1) + Math.abs(gray2));
                int magnitude = 255 - truncate((int) Math.sqrt(gray1*gray1 + gray2*gray2));
                Color grayscale = new Color(magnitude, magnitude, magnitude);
                pic1.set(x, y, grayscale);
            }
        }
       // pic0.save(new File(dir+arq+"(pic0).jpg"));
        pic1.save(new File(dir+arq+"(pic1).jpg"));
        // pic1.save("baboon-edge.jpg");
    }

   
}

class Luminance {

    // return the monochrome luminance of given color
    public static double lum(Color color) {
        int r = color.getRed();
        int g = color.getGreen();
        int b = color.getBlue();
        return .299*r + .587*g + .114*b;
    }

    // return a gray version of this Color
    public static Color toGray(Color color) {
        int y = (int) (Math.round(lum(color)));   // round to nearest int
        Color gray = new Color(y, y, y);
        return gray;
    }

    // are the two colors compatible?
    public static boolean compatible(Color a, Color b) {
        return Math.abs(lum(a) - lum(b)) >= 128.0;
    }

    // test client
    public static void main(String[] args) {
        int[] a = new int[6];
        for (int i = 0; i < 6; i++) {
            a[i] = Integer.parseInt(args[i]);
        }
        Color c1 = new Color(a[0], a[1], a[2]);
        Color c2 = new Color(a[3], a[4], a[5]);
        System.out.println("c1 = " + c1);
        System.out.println("c2 = " + c2);
        System.out.println("lum(c1) =  " + lum(c1));
        System.out.println("lum(c2) =  " + lum(c2));
        System.out.println(compatible(c1, c2));
    }
}
