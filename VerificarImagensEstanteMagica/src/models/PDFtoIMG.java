
package models;

import java.awt.Color;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.RenderedImage;
import java.awt.image.WritableRaster;
import java.io.File;
import java.lang.reflect.Field;
import java.util.Hashtable;
import java.util.List;


import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.ghost4j.document.PDFDocument;
import org.ghost4j.renderer.SimpleRenderer;

/**
 *
 * @author _
 */
public class PDFtoIMG {

    private final int resolution =200;
    
    public int getResolution(){
        return this.resolution;
    }
    
    static {
        Logger.getRootLogger().setLevel(Level.OFF);
        initGhost4j();
    }

    private static void initGhost4j() {
        try {
            setLibraryPath();
            System.loadLibrary("win32-x86-64/gsdll64");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private static void setLibraryPath() {

        try {
            System.setProperty("java.library.path", "bin/");
            System.out.println(System.getProperty("java.library.path"));
            Field fieldSysPath = ClassLoader.class.getDeclaredField("sys_paths");
            fieldSysPath.setAccessible(true);
            fieldSysPath.set(null, null);

        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        }

    }

    public List<Image> pdfToImgs(File fl) {
        try {
            File file = fl;
            PDFDocument document = new PDFDocument();
            document.load(file);

            SimpleRenderer renderer = new SimpleRenderer();
            // set resolution (in DPI)
            renderer.setResolution(resolution);

            List<Image> images = renderer.render(document);

            return images;

        } catch (Exception e) {
            System.err.println(e.getLocalizedMessage());
            e.printStackTrace();
        }
        return null;
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private BufferedImage toBufferedImage(RenderedImage img) {
        if (img instanceof BufferedImage) {
            return (BufferedImage) img;
        }
        ColorModel cm = img.getColorModel();
        int width = img.getWidth();
        int height = img.getHeight();
        WritableRaster raster = cm.createCompatibleWritableRaster(width, height);
        boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
        Hashtable properties = new Hashtable();
        String[] keys = img.getPropertyNames();
        if (keys != null) {
            for (int i = 0; i < keys.length; i++) {
                properties.put(keys[i], img.getProperty(keys[i]));
            }
        }
        BufferedImage result = new BufferedImage(cm, raster, isAlphaPremultiplied, properties);
        img.copyData(raster);
        return result;
    }

    /**
     * <b>@author Renan Fucci</b><br>
     * <b>Metodo toJPG</b><br>
     * Metodo para converter qualquer tipo de extensao de imagem para jpg.
     *
     * @param bufferedImage imagem a ser convertida.
     * @return imagem no padrao jpg.
     */
    private BufferedImage toJPG(BufferedImage bufferedImage) {
        BufferedImage newBufferedImage;//Buffer da img com o aplha
        newBufferedImage = new BufferedImage(bufferedImage.getWidth(),
                bufferedImage.getHeight(), BufferedImage.TYPE_INT_RGB);
        newBufferedImage.createGraphics().drawImage(bufferedImage, 0, 0, Color.WHITE, null);
        return newBufferedImage;
    }
}
