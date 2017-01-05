package manchas;

import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

public class Comparacao_Manchas {
	private final static String pathname1 = "C:/Users/Renan Fucci/Dropbox/Estante Magica_Renan_Fucci/Pasta_Renan_Fucci"
			+ "/2016-06-17/Imagens_Manchas/img096_manchas";

	public static void main(String[] args) {

		try{

			BufferedImage buff1 = ImageIO.read(new File(pathname1+".jpg"));

			String pathname2 = "C:/Users/Renan Fucci/Dropbox/Estante Magica_Renan_Fucci/Pasta_Renan_Fucci"
					+ "/2016-06-17/Imagens_Manchas/";
			pathname2+="img096_manchas_dp(1.2)_cabc(6)_magemLado(700)";
			BufferedImage buff2 = ImageIO.read(new File(pathname2+".jpg"));
			long pixeldif=0;

			for (int i = 0; i < buff1.getHeight(); i++) {
				for (int j = 0; j < buff1.getWidth(); j++) {
					if(buff1.getRGB(j, i) != buff2.getRGB(j, i)){
						pixeldif+=1;
					}
				}
			}
			System.out.println(pixeldif);
			double max = buff1.getHeight() * buff1.getWidth();
			System.out.println(max);
			double x = (100 -  ((100.0*pixeldif)/max));
			
			System.out.println(x);
		}catch(Exception ex){
			System.err.println(ex.getMessage());
			ex.printStackTrace();
		}

	}

}
