package imagej;

/**
 * Background Correction
 *
 * @version 1.0 July, 2001
 *
 * This plugin does: 1. generates a background image estimated through
 * iterations of the minimum ranking and the number of iterations is defined by
 * the user; 2. substracts the background image from the orginal image and
 * generates a result image. 3. auto-contrast the result image.
 *
 * This filter works quite well for images collected under uneven illumination.
 * It only works for 8 bit grayscale images.
 *
 * Requires ImageJ version 1.23 or above.
 *
 * For the sample image, the uneven background can be corrected using 3
 * iterations of Minimum ranking with Radius=4 (default setting).
 *
 * @author	Terry Wu, Ph.D.
 * @author University of Minnesota
 * @author	JavaPlugins@yahoo.com
 *
 * June, 2008: jerome mutterer replaced rank type2 by ranktype RankFilters.MIN
 * (2 is now MAX)
 *
 */
/**
 *
 * @author
 */
import ij.*;
import ij.gui.*;
import ij.measure.*;
import ij.plugin.filter.*;
import ij.process.*;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

public class Background_Correction_IJ {

    ImagePlus copy1, copy2;
    int numberIteration, radius;
    boolean autoContrast;

//    public static void main(String[] args) {
//        try {
//            String dir = "C:\\Users\\_\\Dropbox\\Estante Magica_Renan_Fucci\\Pasta_Renan_Fucci\\2016-08-22\\teste\\";
//            BufferedImage imagem =  ImageIO.read(new File(dir+"digitalizar0081.jpg"));
//            Background_Correction_IJ  background_Correction_IJ = new  Background_Correction_IJ();
//            BufferedImage imgout;
//            int numberIteration= 50;
//            int radius = 50;
//            long start = System.currentTimeMillis();
//            imgout = background_Correction_IJ.limpaFundo(imagem, numberIteration, radius, true);
//            long elapsedTime = System.currentTimeMillis() - start;
//            ImageIO.write(imgout, "JPG", new File(dir+"digitalizar0081_backgroundCorrectImageJ.jpg"));
//            System.out.println((elapsedTime/1000.0)+" segs");
//        } catch (Exception ex) {
//            System.err.println(ex.getMessage());
//            ex.printStackTrace();
//        }
//    }

    public Background_Correction_IJ() {

    }

    public BufferedImage limpaFundo(BufferedImage imagem, int numberIteration, int radius, boolean autoContrast) {
        ImageProcessor imageProcessor1, imageProcessor2, ip;
        BufferedImage imagemGray;

        this.numberIteration = numberIteration;
        this.radius = radius;
        this.autoContrast = autoContrast;
        imagemGray = new BufferedImage(imagem.getWidth(), imagem.getHeight(), BufferedImage.TYPE_USHORT_GRAY);
        for (int coluna = 0; coluna < imagem.getWidth(); coluna++) {
            for (int linha = 0; linha < imagem.getHeight(); linha++) {
                imagemGray.setRGB(coluna, linha, new Color(imagem.getRGB(coluna, linha)).getRGB());
            }
        }
        
        ip = new ShortProcessor(imagemGray);

        // Make 2 copies of the original image
        copy1 = duplicateImage(ip);
        imageProcessor1 = copy1.getProcessor();
        copy2 = duplicateImage(ip);
        imageProcessor2 = copy2.getProcessor();

        // Get the background image
        estimateBackground(imageProcessor2, numberIteration, radius);

        // Substract background from the original image
        imageProcessor1.copyBits(imageProcessor2, 0, 0, Blitter.SUBTRACT);

        // Auto-contrast result image
        if (autoContrast) {
            this.autoAdjust(copy1, imageProcessor1);
        }

        imagemGray = copy1.getBufferedImage();

        // Display the result image
        copy2 = null;
        copy1 = null;
        ip = null;

        return imagemGray;
    }

    void estimateBackground(ImageProcessor ip, int iteration, int radius) {
        RankFilters rankFilter = new RankFilters();
        for (int i = 1; i <= iteration; i++) {
            rankFilter.rank(ip, radius, RankFilters.MIN);
        }
    }

    ImagePlus duplicateImage(ImageProcessor iProcessor) {
        int w = iProcessor.getWidth();
        int h = iProcessor.getHeight();
        ImagePlus iPlus = NewImage.createByteImage("Image", w, h, 1, NewImage.FILL_BLACK);
        ImageProcessor imageProcessor = iPlus.getProcessor();
        imageProcessor.copyBits(iProcessor, 0, 0, Blitter.COPY);
        return iPlus;
    }

    boolean validInput(GenericDialog dialog) {
        // Nothing happens if Cancel button is pressed
        if (dialog.wasCanceled()) {
            return false;
        }

        // Get the number of iterations and radius
        numberIteration = (int) dialog.getNextNumber();
        radius = (int) dialog.getNextNumber();
        autoContrast = dialog.getNextBoolean();

        if (numberIteration <= 0 || radius <= 0) {
            IJ.showMessage("Invalid Numbers!\n"
                    + "Enter Integers equal or greater than 1");
            return false;
        }
        return true;
    }

//    The following autoAjust() method is a slightly modified version of the autoAjust()
//    from ij.plugin.frame.ContrastAdjuster by Wayne Rasband <wayne@codon.nih.gov>
    void autoAdjust(ImagePlus imp, ImageProcessor ip) {
        double min, max;
        Calibration cal = imp.getCalibration();
        imp.setCalibration(null);
        ImageStatistics stats = imp.getStatistics();
        imp.setCalibration(cal);
        int[] histogram = stats.histogram;
        int threshold = stats.pixelCount / 5000;
        int i = -1;
        boolean found = false;
        do {
            i++;
            found = histogram[i] > threshold;
        } while (!found && i < 255);
        int hmin = i;
        i = 256;
        do {
            i--;
            found = histogram[i] > threshold;
        } while (!found && i > 0);
        int hmax = i;
        if (hmax > hmin) {
            imp.killRoi();
            min = stats.histMin + hmin * stats.binSize;
            max = stats.histMin + hmax * stats.binSize;
            ip.setMinAndMax(min, max);
        }
        Roi roi = imp.getRoi();
        if (roi != null) {
            ImageProcessor mask = roi.getMask();
            if (mask != null) {
                ip.reset(mask);
            }
        }
    }
}
