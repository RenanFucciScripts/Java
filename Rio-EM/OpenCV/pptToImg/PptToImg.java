package pptToImg;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import javax.imageio.ImageIO;

import org.apache.poi.xslf.usermodel.XMLSlideShow;
import org.apache.poi.xslf.usermodel.XSLFSlide;

public class PptToImg {
	

	private double tamDesejado;

	
	
	public PptToImg(File fl, double tamDesejado_emPX) {
		this.tamDesejado = tamDesejado_emPX;
		init(fl);
	}
	
	public PptToImg(String dirIN, double tamDesejado_emPX) {
		this.tamDesejado = tamDesejado_emPX;
		init(new File(dirIN));
	}
	
	/**
	 * <b>@author Renan Fucci</b><br>
	 * <b>Metodo init</b><br>
	 * Metodo para iniciar o processo de convercao de arquivos PPT para JPG.
	 * @param fl arquivo no formato ppt ou pptx.
	 */
	private void init(File fl){
		HashMap<String, BufferedImage> list = slides_pptToImgs(fl); 
		String dir = fl.getParent();
		String newDir = fl.getName().substring(0, fl.getName().lastIndexOf("."));
		File dirFinal = new File(dir+File.separator+newDir+File.separator);
		dirFinal.mkdirs();
		try {
			for (String key : list.keySet()) {
				ImageIO.write(list.get(key), "jpg", new File(dirFinal+File.separator+key));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	/**
	 * <b>@author Renan Fucci</b><br>
	 * <b>Metodo slides_pptToImgs</b><br>
	 * Metodo para converter, dentro de um documento de apresentacao, cada slide em uma imagem no formato jpg.
	 * @param fl arquivo no formato ppt ou pptx.
	 * @return lista {@link HashMap},no qual as chaves(key) sao os nomes dos arq e os valores(values) sao as imagens no formato jpg.
	 */
	protected HashMap<String, BufferedImage> slides_pptToImgs(File fl){
		HashMap<String, BufferedImage> listImgs = new HashMap<String, BufferedImage>();
		try {
			File file=fl;
			XMLSlideShow ppt = new XMLSlideShow(new FileInputStream(file));
			Dimension pgsize = ppt.getPageSize();

			double zoom = (tamDesejado<pgsize.getHeight())? (1) : (tamDesejado/ pgsize.getHeight()); 	
			
			AffineTransform at = new AffineTransform();
			at.setToScale(zoom, zoom);

			List<XSLFSlide> slide = ppt.getSlides();

			for (int i = 0; i < slide.size(); i++) {	
				BufferedImage img = new BufferedImage((int)Math.floor(pgsize.width*zoom), (int)Math.floor(pgsize.height*zoom), BufferedImage.TYPE_INT_RGB);
				Graphics2D graphics = img.createGraphics();
				graphics.setTransform(at);
				graphics.setPaint(Color.white);
				graphics.fill(new Rectangle2D.Float(0, 0, pgsize.width, pgsize.height));
				slide.get(i).draw(graphics);
				String key = (i==0)?("capa.jpg"):("ilustracao-"+i+".jpg");
				listImgs.put(key, toJPG(img));
			}
			ppt.close();
		} catch (IOException e) {
			System.err.println(e.getMessage());
			e.printStackTrace();
		}
		return listImgs;
	}

	/**
	 * <b>@author Renan Fucci</b><br>
	 * <b>Metodo toJPG</b><br>
	 * Metodo para converter qualquer tipo de extensao de imagem para jpg.
	 * @param bufferedImage imagem a ser convertida.
	 * @return imagem no padrao jpg.
	 */
	private static BufferedImage toJPG(BufferedImage bufferedImage){
		BufferedImage newBufferedImage ;//Buffer da img com o aplha
		newBufferedImage = new BufferedImage(bufferedImage.getWidth(),
				bufferedImage.getHeight(), BufferedImage.TYPE_INT_RGB);
		newBufferedImage.createGraphics().drawImage(bufferedImage, 0, 0, Color.WHITE, null);

		return newBufferedImage;
	}
}
