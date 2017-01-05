/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package teste_docx4j;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.docx4j.openpackaging.packages.PresentationMLPackage;
import org.docx4j.openpackaging.parts.PresentationML.MainPresentationPart;
import org.docx4j.openpackaging.parts.PresentationML.SlidePart;

/**
 *
 * @author _
 */
public class Teste_Docx4j {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            String dir = "C:/Users/_/Dropbox/Estante Magica_Renan_Fucci/Pasta_Renan_Fucci/2016-10-10/teste-docx4j/";
            String path = dir + "rf15 - Teste Renan PNG"
                    + ".pptx";
            File pptxFile = new File(path);

            // Create skeletal package, including a MainPresentationPart and a SlideLayoutPart
            PresentationMLPackage presentationMLPackage = PresentationMLPackage.load(pptxFile);
            MainPresentationPart mainPresentationPart = presentationMLPackage.getMainPresentationPart();

            File fl = null;
            FileOutputStream flOutStream = null;
            for (int i = 0; i < mainPresentationPart.getSlideCount(); i++) {
                Root root = new Root();
                Image image = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);
                root.setImage(image);

                SlidePart slidePart = mainPresentationPart.getSlide(i);
                JAXBContext jc = slidePart.getJAXBContext();

                Marshaller marshaller = jc.createMarshaller();
                marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
                marshaller.marshal(root, System.out);

                fl = new File(path.substring(0, path.lastIndexOf(".")) + "_" + i + ".jpg");
                ImageIO.write((RenderedImage) root.getImage(), "JPG", fl);
                
                //ResolvedLayout resolvedLayout = slidePart.getResolvedLayout();
                //resolvedLayout.getBg().getBgRef().
            }

            /**
             * Convert all images inside xml
             */
//            SvgExporter.setImageDirPath(dir);
//            Iterator partIterator = presentationMLPackage.getParts().getParts().entrySet().iterator();
//            while (partIterator.hasNext()) {
//
//                Map.Entry pairs = (Map.Entry) partIterator.next();
//
//                Part p = (Part) pairs.getValue();
//                if (p instanceof SlidePart) {
//
//                    System.out.println(
//                            SvgExporter.svg(presentationMLPackage, (SlidePart) p)
//                    );
//                }
//            }
        } catch (Docx4JException ex) {
            Logger.getLogger(Teste_Docx4j.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(Teste_Docx4j.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
