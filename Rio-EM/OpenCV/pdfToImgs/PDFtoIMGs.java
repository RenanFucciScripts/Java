package pdfToImgs;

import java.awt.Color;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.RenderedImage;
import java.awt.image.WritableRaster;
import java.io.File;
import java.util.Hashtable;
import java.util.List;

import javax.imageio.ImageIO;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.ghost4j.document.PDFDocument;
import org.ghost4j.renderer.SimpleRenderer;

import db_dao.DB_DAO;
import metodos.MetodosEM;

public class PDFtoIMGs {

	public static void main(String[] args) {
		PDFtoIMGs pdFtoIMGs = new PDFtoIMGs();
		DB_DAO db_dao  = null;
		int id_parent = 0;

		String type_file = "pptx";
		String nomeArq_Extens = "testeAlex.pptx";
		String dirEntrada = "C:/Users/_/Dropbox/Estante Magica_Renan_Fucci/Pasta_Renan_Fucci/2016-08-03/";;


		pdFtoIMGs.pdfToImgs(dirEntrada, nomeArq_Extens, type_file, id_parent, db_dao);
	}

	static{
		Logger.getRootLogger().setLevel(Level.OFF);

	}
	private void pdfToImgs(String dirEntrada, String nomeArq_Extens, String type_file, int id_parent, DB_DAO db_dao) {
		try {
			File file = new File(dirEntrada+nomeArq_Extens);

			PDFDocument document = new PDFDocument();
			document.load(file);
		
			File newDir = new File(file.getParent()+File.separator+file.getName().substring(0, file.getName().lastIndexOf("."))+File.separator);
			newDir.mkdirs();

			SimpleRenderer renderer = new SimpleRenderer();
			// set resolution (in DPI)
			renderer.setResolution(200);

			List<Image> images = renderer.render(document);

			for (int i = 0; i < images.size(); i++) {
				RenderedImage x = (RenderedImage) images.get(i);
				BufferedImage img =  toJPG((toBufferedImage(x)));
				File yourImageFile = new File(newDir.getPath()+File.separator+file.getName().substring(0,file.getName().lastIndexOf("."))+"_" + i + ".jpg");
				ImageIO.write(img, "JPG", yourImageFile);
			}

		} catch (Exception e) {
			System.err.println(e.getLocalizedMessage());
			e.printStackTrace();
			MetodosEM.fazerSom();
		}
		MetodosEM.fazerSom();

	}
	

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private BufferedImage toBufferedImage(RenderedImage img) {
		if (img instanceof BufferedImage) {
			return (BufferedImage)img;  
		}   
		ColorModel cm = img.getColorModel();
		int width = img.getWidth();
		int height = img.getHeight();
		WritableRaster raster = cm.createCompatibleWritableRaster(width, height);
		boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
		Hashtable properties = new Hashtable();
		String[] keys = img.getPropertyNames();
		if (keys!=null) {
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
	 * @param bufferedImage imagem a ser convertida.
	 * @return imagem no padrao jpg.
	 */
	private BufferedImage toJPG(BufferedImage bufferedImage){
		BufferedImage newBufferedImage ;//Buffer da img com o aplha
		newBufferedImage = new BufferedImage(bufferedImage.getWidth(),
				bufferedImage.getHeight(), BufferedImage.TYPE_INT_RGB);
		newBufferedImage.createGraphics().drawImage(bufferedImage, 0, 0, Color.WHITE, null);
		return newBufferedImage;
	}
}
