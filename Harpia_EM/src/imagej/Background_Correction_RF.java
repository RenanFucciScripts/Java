/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imagej;

import io.IO;
import ij.IJ;
import ij.ImagePlus;
import ij.gui.GenericDialog;
import ij.gui.NewImage;
import ij.gui.Roi;
import ij.measure.Calibration;
import ij.plugin.filter.RankFilters;
import ij.process.Blitter;
import ij.process.ImageProcessor;
import ij.process.ImageStatistics;
import ij.process.ShortProcessor;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

/**
 *
 * @author _
 */
public class Background_Correction_RF {

    ImagePlus copy1, copy2;
    int numberIteration, radius;
    boolean autoContrast;

    public static void main(String[] args) {
        try {
            String dir = "C:\\Users\\_\\Dropbox\\Estante Magica_Renan_Fucci\\Pasta_Renan_Fucci\\2016-08-22\\teste\\";
            BufferedImage imagem = ImageIO.read(new File(dir + "digitalizar0081.jpg"));
            Background_Correction_RF background_Correction_rf = new Background_Correction_RF();
            BufferedImage imgout;
            int numberIteration = 50;
            int radius = 50;
            long start = System.currentTimeMillis();
            imgout = background_Correction_rf.limpaFundo(imagem, numberIteration, radius, true);
            long elapsedTime = System.currentTimeMillis() - start;
            ImageIO.write(imgout, "JPG", new File(dir + "digitalizar0081_backgroundCorrect_RF.jpg"));
            String tempo  = (elapsedTime / 1000.0) + " segs (RF)";
            IO.criarTxtdoTempo(dir+"digitalizar0081_backgroundCorrect_RF", tempo);
            System.out.println(tempo);
        } catch (Exception ex) {
            System.err.println(ex.getMessage());
            ex.printStackTrace();
        }
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
            autoAdjust(copy1, imageProcessor1);
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
