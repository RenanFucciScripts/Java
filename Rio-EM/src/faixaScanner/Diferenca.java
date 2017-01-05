package faixaScanner;

import java.io.File;
import java.util.Stack;

import javax.sound.sampled.LineUnavailableException;

import metodos.MetodosRF;

public class Diferenca {

	public static void main(String[] args) throws LineUnavailableException {
		MetodosRF methods =  new MetodosRF();

		String dir =  "C:\\Users\\Renan Fucci\\Dropbox\\Estante Magica_Renan_Fucci\\Pasta_Renan_Fucci\\2015-08-17\\imagensFaixaScanner\\imagens_Sem_Faixas\\";
		File fl = new File(dir);
		Stack<File> pilhaArqs = new Stack<File>();
		pilhaArqs=methods.empilharArquivosDiretorio(fl, pilhaArqs);

		for (int i = 0; i < pilhaArqs.size(); i+=2) {
			int[][] red= methods.leImagem(dir+pilhaArqs.get(i).getName(), "Red");
			int[][] green= methods.leImagem(dir+pilhaArqs.get(i).getName(), "Green");
			int[][] blue= methods.leImagem(dir+pilhaArqs.get(i).getName(), "Blue");

			int[][] red1= methods.leImagem(dir+pilhaArqs.get(i+1).getName(), "Red");
			int[][] green1= methods.leImagem(dir+pilhaArqs.get(i+1).getName(), "Green");
			int[][] blue1= methods.leImagem(dir+pilhaArqs.get(i+1).getName(), "Blue");

			int[][] redF= new int[red.length][red[0].length];
			int[][] greenF=new int[red.length][red[0].length];
			int[][] blueF= new int[red.length][red[0].length];


			for (int j = 0; j < red.length; j++) {
				for (int k = 0; k < red[i].length; k++) {
					redF[j][k]= Math.abs(red1[j][k]-red[j][k]);
					greenF[j][k]=Math.abs(green1[j][k]-green[j][k]);
					blueF[j][k]=Math.abs(blue1[j][k]-blue[j][k]);
				}
			}
			methods.gravarImagemColorida(redF, greenF, blueF, dir, pilhaArqs.get(i+1).getName().substring(0, pilhaArqs.get(i+1).getName().length()-3)+" (dif).jpg");
		}
		MetodosRF.fazerSom();	
	}
}
