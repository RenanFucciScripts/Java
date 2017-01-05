/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rar;

import java.awt.image.BufferedImage;
import java.io.File;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.util.PDFImageWriter;

/**
 *
 * @author Renan Fucci
 */
public class NewPDFtoJPG {
    
    public static void main(String[] args) {
        String dirEntrada = "C:\\Users\\Renan Fucci\\Dropbox\\Estante Magica_Renan_Fucci\\Pasta_Renan_Fucci\\2015-11-10\\PDF_to_JPG\\";
        String subPastaSaida = "PDFtoJPG\\";
        String nomeArq_Extens = "imgs.pdf";
        conversorPDF2JPG(dirEntrada, subPastaSaida, nomeArq_Extens);
    }
    /**
	 * <b>@author Renan Fucci<br></b>
	 * <b>Metodo descompactarRAR<br></b>
	 * Metodo para converter cada pagina de um PDF em uma imagem no formato JPG.
	 * @param dirEntrada  string que deve conter o somente o diretorio onde está localizado o arquivo.
	 * @param subPastaSaida  inserir se quiser que os arquivos sejam salvos em uma subpasta do dirEntrada, 
	 * se não, setar o valor da string em branco (String subPastaSaida = "";), e neste caso, 
	 * as imagens serão convertidas no mesmo diretorio de dirEntrada.
	 * @param nomeArq_Extens  nome do arquivo com a extensão.
	 */ 
    public static void conversorPDF2JPG(String dirEntrada, String subPastaSaida, String nomeArq_Extens) {
        final int resolution = 200;
        String password = "-password";

        File pdf = new File(dirEntrada + nomeArq_Extens);
        if (pdf.exists()) {
            if (!(new File(dirEntrada + subPastaSaida).exists())) {
                new File(dirEntrada + subPastaSaida).mkdirs();
            }
            String outputPrefix = dirEntrada + subPastaSaida + nomeArq_Extens.substring(0, nomeArq_Extens.lastIndexOf("."));
            System.out.println(outputPrefix);
            PDDocument document = null;
            try {
                document = PDDocument.load(pdf);
                if (!(document.isEncrypted())) {

                    PDFImageWriter imageWriter = new PDFImageWriter();

                    boolean success = imageWriter.writeImage(document, "jpg", password, 1, document.getNumberOfPages(), outputPrefix, BufferedImage.TYPE_INT_RGB, resolution);
                    if (!success) {
                        success = imageWriter.writeImage(document, "jpg", password, 1, document.getNumberOfPages(), outputPrefix, BufferedImage.TYPE_INT_ARGB, resolution);
                        if (!success) {
                            System.err.println("Error: no writer found for image type");
                            System.exit(1);
                        }
                    }

                }
                document.close();

            } catch (Exception ex) {
                System.err.println(ex.getLocalizedMessage());
                ex.printStackTrace();
            }

        }
    }
}
