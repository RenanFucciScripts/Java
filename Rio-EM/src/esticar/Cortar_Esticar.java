package esticar;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Stack;

import javax.imageio.ImageIO;

import comparacaoDesemp.GetRGBSeparado;
import metodos.MetodosEM;
import metodos.MetodosRF;

public class Cortar_Esticar {

	public static void main(String[] args) {
		String dir="C:\\Users\\Renan Fucci\\Dropbox\\Estante Magica_Renan_Fucci\\Pasta_Alex_Araujo\\2015-11-25\\Alex\\";
		MetodosRF methods =  new MetodosRF();
		Stack<File> pilhaArqs =  new Stack<File>();
		pilhaArqs =methods.empilharArquivosDiretorio(new File(dir), pilhaArqs );
		System.out.println(pilhaArqs.size());
	//	String dirOut="C:\\Users\\Renan Fucci\\Dropbox\\Estante Magica_Renan_Fucci\\Pasta_Renan_Fucci\\2015-11-25\\";
		
		try{
			for (File file : pilhaArqs) {
				System.out.println("------------ "+file.getName());
				BufferedImage entrada =  ImageIO.read(file);
				BufferedImage saida = cortar_esticar(entrada);
			/*	File mkdir= new File(dirOut+ (file.getParent().substring(file.getParent().lastIndexOf("\\"), file.getParent().length()))+"\\");
				if(mkdir.exists())
					mkdir.mkdir();
				//System.out.println(mkdir.getAbsolutePath()+"\\"+file.getName());
			*/	ImageIO.write(saida, "JPG", new File(file.getAbsolutePath()));
			}
			MetodosEM.fazerSom();

		}catch(Exception e){
			System.err.println(e.getLocalizedMessage());
			e.printStackTrace();
		}
		
	}

	public static BufferedImage cortar_esticar(BufferedImage entrada){
		try{

			int[][] red = GetRGBSeparado.getByteArrayRGB_Separado(entrada, "Red");
			int[][] green = GetRGBSeparado.getByteArrayRGB_Separado(entrada, "Green");
			int[][] blue = GetRGBSeparado.getByteArrayRGB_Separado(entrada, "Blue");

			int[][] redSaida = new int[red.length][red[0].length-15];
			int[][] greenSaida = new int[red.length][red[0].length-15];
			int[][] blueSaida =  new int[red.length][red[0].length-15];

			for (int i = 0; i < blue.length; i++) {
				for (int j = 15; j < blue[i].length; j++) {

					redSaida[i][j-15] = red[i][j];
					greenSaida[i][j-15] = green[i][j];
					blueSaida[i][j-15] = blue[i][j];

				}
			}
			BufferedImage crop = GetRGBSeparado.matrizColorToBufferImage(redSaida, greenSaida, blueSaida);

			BufferedImage saida =  new BufferedImage(entrada.getWidth(), entrada.getHeight(), 5);
			Image img = crop.getScaledInstance(entrada.getWidth(), entrada.getHeight(), Image.SCALE_DEFAULT);
			Graphics2D bgr =  saida.createGraphics();
			bgr.drawImage(img, 0, 0, null);
			bgr.dispose();

			return saida;
		}catch(Exception e){
			System.err.println(e.getLocalizedMessage());
			e.printStackTrace();
		}
		return null;

	}
}
