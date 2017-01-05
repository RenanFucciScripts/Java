
package models;

import java.awt.image.BufferedImage;

/**
 *
 * @author _
 */
public class CalcularProporcao {

//    public static void main(String[] args) throws IOException {
//        String str = "C:\\Users\\_\\Dropbox\\Estante Magica_Renan_Fucci\\Pasta_Renan_Fucci\\2016-08-03\\teste\\imagens_Testar_ValidadorImagens\\20-07\\";
//        File fl = new File(str + "Carolaine_Capa1.jpg");
//        boolean x = new CalcularProporcao().possuiProporcaoCorreta(ImageIO.read(fl));
//        System.out.println("x:\t"+x);
//    }

    private final double aspect_ratio_limit = 21 / 29.7;
    private final int porcent_aspect_ratio_limit = 5;

    public boolean possuiProporcaoCorreta(BufferedImage img) {
        double aspect_ratio = 0;
        System.out.println("w:\t" + img.getWidth());
        System.out.println("h:\t" + img.getHeight());

        if (img.getWidth() < img.getHeight()) {
            System.out.println("menor w");
            aspect_ratio = Double.valueOf(img.getWidth()) / Double.valueOf(img.getHeight());
        } else {
            System.out.println("menor h");
            aspect_ratio = Double.valueOf(img.getHeight()) / Double.valueOf(img.getWidth());
        }
        double margemErro = (porcent_aspect_ratio_limit * aspect_ratio_limit) / 100;
        double limitMin = aspect_ratio_limit - margemErro;
        double limitMax = aspect_ratio_limit + margemErro;
        
        System.out.println("limitMin:\t"+limitMin);
        System.out.println("limitMax:\t"+limitMax);
        System.out.println("aspect_ratio_limit:\t" + aspect_ratio_limit);
        System.out.println("margemErro:\t" + margemErro);
        System.out.println("aspectRadio:\t" + aspect_ratio);
        
        return ((aspect_ratio<limitMin || aspect_ratio>limitMax)? (false): (true));
    }
}
