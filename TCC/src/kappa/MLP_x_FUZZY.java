package kappa;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

public class MLP_x_FUZZY {

	private int concordamEhAgua = 0;
	private int concordamEhVegetacao=0;
	private int mlpEhVegetacao=0;
	private int fuzzyEhAgua=0;
	private int fuzzyEhVegetacao=0;
	private int mlpEhAgua=0;
	public static void main(String[] args) {
		try{
			String dir =  "C:/Users/_/Dropbox/UFMS - CPAN/7º Semestre - 2016/TCC II/TCC I - results/";

			
			BufferedImage fuzzy =ImageIO.read(new File(dir+"finalFUZZY.jpg"));
			BufferedImage mlp =ImageIO.read(new File(dir+"finalMLP.jpg"));
			System.out.println("QNTDpxs:\t"+(fuzzy.getHeight()*fuzzy.getWidth()));
			System.out.println("QNTDpxs:\t"+(mlp.getHeight()*mlp.getWidth()));
			
			new MLP_x_FUZZY(fuzzy, mlp);
			
		}catch(Exception ex){
			System.err.println(ex.getMessage());
			ex.printStackTrace();
		}
	}

	public MLP_x_FUZZY(BufferedImage fuzzy, BufferedImage mlp ) {
		
		for (int i = 0; i < fuzzy.getHeight(); i++) {
			for (int j = 0; j < fuzzy.getWidth(); j++) {
				//System.out.println("\n\n\t\tLP\t\t\t\t\tMLP\n"
				//		+ "\tR\tG\tB\t\t\tR\tG\tB");
				int lp_r = new Color(fuzzy.getRGB(j, i)).getRed();
				int lp_g = new Color(fuzzy.getRGB(j, i)).getGreen();
				int lp_b = new Color(fuzzy.getRGB(j, i)).getBlue();
				
				int mlp_r = new Color(mlp.getRGB(j, i)).getRed();
				int mlp_g = new Color(mlp.getRGB(j, i)).getGreen();
				int mlp_b = new Color(mlp.getRGB(j, i)).getBlue();
				//System.out.println("\t"+lp_r+"\t"+lp_g+"\t"+lp_b+"\t\t\t"+mlp_r+"\t"+mlp_g+"\t"+mlp_b);
				if(ehAgua(fuzzy.getRGB(j, i)) && ehAgua(mlp.getRGB(j, i))){
					concordamEhAgua+=1;
				}
				else if(ehVegetacao(fuzzy.getRGB(j, i)) && ehVegetacao(mlp.getRGB(j, i))){
					concordamEhVegetacao+=1;
				}
				else if(ehAgua(fuzzy.getRGB(j, i)) && ehVegetacao(mlp.getRGB(j, i))){
					fuzzyEhAgua +=1;
					mlpEhVegetacao+=1;
				}	
				else if(ehAgua(mlp.getRGB(j, i)) && ehVegetacao(fuzzy.getRGB(j, i))){
					fuzzyEhVegetacao +=1;
					mlpEhAgua +=1;
				}
			}
		}
		System.out.println("concordamEhAgua:\t"+concordamEhAgua);
		System.out.println("concordamEhVegetacao:\t"+concordamEhVegetacao);
		System.out.println("fuzzyEhAgua:\t"+fuzzyEhAgua);
		System.out.println("fuzzyEhVegetacao:\t"+fuzzyEhVegetacao);
		System.out.println("mlpEhAgua:\t"+mlpEhAgua);
		System.out.println("mlpEhVegetacao:\t"+mlpEhVegetacao);
	}

	public static void printMaior(BufferedImage buf){
		int maior = 0;
		for (int i = 0; i < buf.getHeight(); i++) {
			for (int j = 0; j < buf.getWidth(); j++) {
				int r = new Color(buf.getRGB(j, i)).getRed();
				int g = new Color(buf.getRGB(j, i)).getGreen();
				int b = new Color(buf.getRGB(j, i)).getBlue();
				if(r> maior){
					maior = r;
				}
				if(g> maior){
					maior = g;
				}
				if(b> maior){
					maior = b;
				}
			}	
		}
		System.out.println("maior:\t"+maior);
	}

	public static void printMenor(BufferedImage buf){
		int menor = 0;
		for (int i = 0; i < buf.getHeight(); i++) {
			for (int j = 0; j < buf.getWidth(); j++) {
				int r = new Color(buf.getRGB(j, i)).getRed();
				int g = new Color(buf.getRGB(j, i)).getGreen();
				int b = new Color(buf.getRGB(j, i)).getBlue();
				if(r< menor){
					menor = r;
				}
				if(g< menor){
					menor = g;
				}
				if(b< menor){
					menor = b;
				}
			}	
		}
		System.out.println("menor:\t"+menor);
	}

	public boolean ehAgua(int rgb){
		int r = new Color(rgb).getRed();
		int g = new Color(rgb).getGreen();
		int b = new Color(rgb).getBlue();
		if(b>r && b>g){
			return true;
		} else
			return false;
	}
	public boolean ehVegetacao(int rgb){
		int r = new Color(rgb).getRed();
		int g = new Color(rgb).getGreen();
		int b = new Color(rgb).getBlue();
		if(g>r && g>b){
			return true;
		} else
			return false;
	}
	
	/*public boolean ehSolo(int rgb){
		int r = new Color(rgb).getRed();
		int g = new Color(rgb).getGreen();
		int b = new Color(rgb).getBlue();
		return (r>=254 && g==0 && b==0) ? true: false;
	}*/
}
