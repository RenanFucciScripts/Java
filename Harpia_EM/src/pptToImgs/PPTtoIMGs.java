/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pptToImgs;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.io.FilenameUtils;
import org.apache.poi.hslf.usermodel.HSLFSlide;
import org.apache.poi.hslf.usermodel.HSLFSlideShow;
import org.apache.poi.hslf.usermodel.HSLFSlideShowImpl;
import org.apache.poi.xslf.usermodel.XMLSlideShow;
import org.apache.poi.xslf.usermodel.XSLFSlide;

public class PPTtoIMGs {

    private double tamDesejado;
    private String retorno;
    private String codigoMagico;
    private HashMap<String, BufferedImage> list; // AQUI: Alterado por Alex Araujo

    public static void main(String[] args) {
        try {

            String dir = "C:\\Users\\_\\Dropbox\\Estante Magica_Renan_Fucci\\Pasta_Renan_Fucci\\2016-10-07\\";
            String filename = "a1to - A YANE STELLA SALVADOR ODA"
                    + ".ppt";
            File bffuim = new File(dir + "a1to - A YANE STELLA SALVADOR ODA\\pict_3.emf");

//            HSLFSlideShow ppt = new HSLFSlideShow(new HSLFSlideShowImpl(dir+filename));
//
//            int idx = 1;
//            for (HSLFPictureData pict : ppt.getPictureData()) {
//                // picture data
//                byte[] data = pict.getData();
//
//                PictureData.PictureType type = pict.getType();
//                String ext = type.extension;
//                FileOutputStream out = new FileOutputStream(dir+"\\a1to - A YANE STELLA SALVADOR ODA\\pict_" + idx + ext);
//                out.write(data);
//                out.close();
//                idx++;
//            }
//            PPTtoIMGs gs = new PPTtoIMGs(dir + filename, 2100);
//            System.out.println("gs.getCodigoMagico():\t" + gs.getCodigoMagico());
//            System.out.println("gs.getRetorno():\t" + gs.getRetorno());
//            System.out.println("+gs.getList():\t" + gs.getList().keySet());
//            for (String obj : gs.getList().keySet()) {
//                File fl = new File(dir + filename.substring(0, filename.lastIndexOf(".")) + "\\" + obj);
//                fl.mkdirs();
//                ImageIO.write(gs.getList().get(obj), "JPG", fl);
//            }
        } catch (Exception ex) {
            Logger.getLogger(PPTtoIMGs.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public PPTtoIMGs(File fl, double tamDesejado_emPX) {
        this.tamDesejado = tamDesejado_emPX;
        this.list = null;
        init(fl);
    }

    public PPTtoIMGs(String dirIN, double tamDesejado_emPX) {
        this.tamDesejado = tamDesejado_emPX;
        this.list = null;
        init(new File(dirIN));
    }

    /**
     * <b>@author Renan Fucci</b><br>
     * <b>Metodo init</b><br>
     * Metodo para iniciar o processo de convercao de arquivos PPT para JPG.
     *
     * @param fl arquivo no formato ppt ou pptx.
     */
    private void init(File fl) {
        String ext = FilenameUtils.getExtension(fl.getName());
        if (ext.equalsIgnoreCase("pptx")) {
            list = slides_pptxToImgs(fl);
        } else if (ext.equalsIgnoreCase("ppt")) {
            list = slides_pptToImgs(fl);
        } else {
            System.err.println("Não é um arquiuvo de Slide.");
            this.codigoMagico = "isNotValid";  // AQUI: Alterado por Alex Araujo
        }

        // AQUI: Alterado por Alex Araujo
////        String dir = fl.getParent();
////        String newDir = this.getCodigoMagico()+"/"; // AQUI Alterado por Alex Araujo
////        File dirFinal = new File(dir + File.separator + newDir + File.separator);
////        dirFinal.mkdirs();
////        try {
////            for (String key : list.keySet()) {
////                ImageIO.write(list.get(key), "jpg", new File(dirFinal + File.separator + key));
////            }
////        } catch (IOException e) {
////            e.printStackTrace();
////        }
    }

    /**
     * <b>@author Renan Fucci</b><br>
     * <b>Metodo slides_pptToImgs</b><br>
     * Metodo para converter, dentro de um documento de apresentacao, cada slide
     * em uma imagem no formato jpg.
     *
     * @param fl arquivo no formato ppt ou pptx.
     * @return lista {@link HashMap},no qual as chaves(key) sao os nomes dos arq
     * e os valores(values) sao as imagens no formato jpg.
     */
    protected HashMap<String, BufferedImage> slides_pptToImgs(File fl) {
        HashMap<String, BufferedImage> listImgs = new HashMap<>();
        File file = fl;

        try (HSLFSlideShow ppt = new HSLFSlideShow(new HSLFSlideShowImpl(fl.getAbsolutePath()));) {

            Dimension pgsize = ppt.getPageSize();
            double zoom = (tamDesejado < pgsize.getHeight()) ? (1) : (tamDesejado / pgsize.getHeight());

            AffineTransform at = new AffineTransform();
            at.setToScale(zoom, zoom);

            List<HSLFSlide> slide = ppt.getSlides();
            if (file.getName().contains("-")) {
                codigoMagico = file.getName().trim().split("-")[0];
            } else {
                codigoMagico = "failNameOfFile";
            }
            if (slide.size() == 14 || slide.size() == 15) {
                for (int i = 0; i < slide.size(); i++) {
                    BufferedImage img = new BufferedImage((int) Math.floor(pgsize.width * zoom), (int) Math.floor(pgsize.height * zoom), BufferedImage.TYPE_INT_RGB);
                    Graphics2D graphics = img.createGraphics();
                    graphics.setTransform(at);
                    graphics.setPaint(Color.white);
                    graphics.fill(new Rectangle2D.Float(0, 0, pgsize.width, pgsize.height));
                    slide.get(i).draw(graphics);
                    String key = ((slide.size() == 14) ? ("ilustracao_" + (i + 1) + ".jpg") : ("ilustracao_" + i + ".jpg"));
                    listImgs.put(key, toJPG(img));
                }
                retorno = ((slide.size() == 14) ? ("success14") : ("success15"));
            } else if (slide.size() > 15) {
                for (int i = 0; i < 15; i++) {
                    BufferedImage img = new BufferedImage((int) Math.floor(pgsize.width * zoom), (int) Math.floor(pgsize.height * zoom), BufferedImage.TYPE_INT_RGB);
                    Graphics2D graphics = img.createGraphics();
                    graphics.setTransform(at);
                    graphics.setPaint(Color.white);
                    graphics.fill(new Rectangle2D.Float(0, 0, pgsize.width, pgsize.height));
                    slide.get(i).draw(graphics);
                    String key = "ilustracao_" + (i) + ".jpg";
                    listImgs.put(key, toJPG(img));
                }
                retorno = "fail_" + slide.size();
            } else if (slide.size() < 14) {
                for (int i = 0; i < slide.size(); i++) {
                    BufferedImage img = new BufferedImage((int) Math.floor(pgsize.width * zoom), (int) Math.floor(pgsize.height * zoom), BufferedImage.TYPE_INT_RGB);
                    Graphics2D graphics = img.createGraphics();
                    graphics.setTransform(at);
                    graphics.setPaint(Color.white);
                    graphics.fill(new Rectangle2D.Float(0, 0, pgsize.width, pgsize.height));
                    slide.get(i).draw(graphics);
                    String key = "ilustracao_" + (i + 1) + ".jpg";
                    listImgs.put(key, toJPG(img));
                }
                retorno = "fail_" + slide.size();
            }
        } catch (Exception e) {
            retorno = "fail";
            System.err.println(e.getMessage());
            e.printStackTrace();
        }
        return listImgs;
    }

    /**
     * <b>@author Renan Fucci</b><br>
     * <b>Metodo slides_pptxToImgs</b><br>
     * Metodo para converter, dentro de um documento de apresentacao, cada slide
     * em uma imagem no formato jpg.
     *
     * @param fl arquivo no formato ppt ou pptx.
     * @return lista {@link HashMap},no qual as chaves(key) sao os nomes dos arq
     * e os valores(values) sao as imagens no formato jpg.
     */
    protected HashMap<String, BufferedImage> slides_pptxToImgs(File fl) {
        HashMap<String, BufferedImage> listImgs = new HashMap<String, BufferedImage>();
        try {
            File file = fl;
            XMLSlideShow ppt = new XMLSlideShow(new FileInputStream(file));
            Dimension pgsize = ppt.getPageSize();

            double zoom = (tamDesejado < pgsize.getHeight()) ? (1) : (tamDesejado / pgsize.getHeight());

            AffineTransform at = new AffineTransform();
            at.setToScale(zoom, zoom);

            List<XSLFSlide> slide = ppt.getSlides();
            if (file.getName().contains("-")) {
                codigoMagico = file.getName().trim().split("-")[0];
            } else {
                codigoMagico = "failNameOfFile";
            }
            if (slide.size() == 14 || slide.size() == 15) {
                for (int i = 0; i < slide.size(); i++) {
                    BufferedImage img = new BufferedImage((int) Math.floor(pgsize.width * zoom), (int) Math.floor(pgsize.height * zoom), BufferedImage.TYPE_INT_RGB);
                    img = toJPG(img);
                    Graphics2D graphics = img.createGraphics();
                    graphics.setTransform(at);
                    graphics.setPaint(Color.white);
                    graphics.fill(new Rectangle2D.Float(0, 0, pgsize.width, pgsize.height));
                    slide.get(i).draw(graphics);
                    String key = ((slide.size() == 14) ? ("ilustracao_" + (i + 1) + ".jpg") : ("ilustracao_" + i + ".jpg"));
                    listImgs.put(key, toJPG(img));
                }
                retorno = ((slide.size() == 14) ? ("success14") : ("success15"));
            } else if (slide.size() > 15) {
                for (int i = 0; i < 15; i++) {
                    BufferedImage img = new BufferedImage((int) Math.floor(pgsize.width * zoom), (int) Math.floor(pgsize.height * zoom), BufferedImage.TYPE_INT_RGB);
                    img = toJPG(img);
                    Graphics2D graphics = img.createGraphics();
                    graphics.setTransform(at);
                    graphics.setPaint(Color.white);
                    graphics.fill(new Rectangle2D.Float(0, 0, pgsize.width, pgsize.height));
                    slide.get(i).draw(graphics);
                    String key = "ilustracao_" + (i) + ".jpg";
                    listImgs.put(key, toJPG(img));
                }
                retorno = "fail_" + slide.size();
            } else if (slide.size() < 14) {
                for (int i = 0; i < slide.size(); i++) {
                    BufferedImage img = new BufferedImage((int) Math.floor(pgsize.width * zoom), (int) Math.floor(pgsize.height * zoom), BufferedImage.TYPE_INT_RGB);
                    img = toJPG(img);
                    Graphics2D graphics = img.createGraphics();
                    graphics.setTransform(at);
                    graphics.setPaint(Color.white);
                    graphics.fill(new Rectangle2D.Float(0, 0, pgsize.width, pgsize.height));
                    slide.get(i).draw(graphics);
                    String key = "ilustracao_" + (i + 1) + ".jpg";
                    listImgs.put(key, toJPG(img));
                }
                retorno = "fail_" + slide.size();
            }
            ppt.close();

        } catch (Exception e) {
            retorno = "fail";
            System.err.println(e.getMessage());
            e.printStackTrace();
        }
        return listImgs;
    }

    /**
     * <b>@author Renan Fucci</b><br>
     * <b>Metodo toJPG</b><br>
     * Metodo para converter qualquer tipo de extensao de imagem para jpg.
     *
     * @param bufferedImage imagem a ser convertida.
     * @return imagem no padrao jpg.
     */
    private static BufferedImage toJPG(BufferedImage bufferedImage) {
        BufferedImage newBufferedImage;//Buffer da img com o aplha
        newBufferedImage = new BufferedImage(bufferedImage.getWidth(),
                bufferedImage.getHeight(), BufferedImage.TYPE_INT_RGB);
        newBufferedImage.createGraphics().drawImage(bufferedImage, 0, 0, Color.WHITE, null);

        return newBufferedImage;
    }

    public String getRetorno() {
        return retorno;
    }

    public String getCodigoMagico() {
        return codigoMagico;
    }

    // AQUI: Alterado por Alex Araujo
    public HashMap<String, BufferedImage> getList() {
        return list;
    }

    // AQUI: Alterado por Alex Araujo
    public void setList(HashMap<String, BufferedImage> list) {
        this.list = list;
    }

}
