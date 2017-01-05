package manchas;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;


public class Manchas {

	static{
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
	}
	private double limiarBranco = 244;
	private static int margemErro = 10;
	private double limitDesvioPadrao = 1.1;
	private int margemLado = 0;
	private static String pathname;
	private int ksize = 3;

	public static void main(String[] args) {
		pathname = "C:/Users/_/Dropbox/Estante Magica_Renan_Fucci/Pasta_Renan_Fucci/2016-06-22/Imagens_Manchas/"
				+ "img097";
		Mat src = Imgcodecs.imread(pathname+".jpg");

		//		System.out.println(src.toString());
		System.out.println((new Manchas()).hasManchas(src));
		//		
		//new Manchas().applyManchasAlgImgInteira(src);
	}

	public boolean hasManchas(Mat img){
		Mat src = img;
		//Filtro na margem do topo
		int lnIni=5;
		int lnFin=85;
		Mat subMat =  src.submat(lnIni, lnFin, 0, src.width());
		Mat dst_subMat = new Mat();
		Imgproc.blur(subMat, dst_subMat, new Size(ksize,ksize));

		//Filtro na imagem inteira
		Mat dst = new Mat();
		src = src.submat(lnIni, lnFin, 0, src.width());
		Imgproc.blur(src, dst, new Size(ksize, ksize));
		src = new Mat();
		src = dst;

		// FAZER O FILTRO DA MEDIA NA IMG ORIG
		double[] src_px;
		for (int i = ksize-1; i < src.rows()-ksize; i+=ksize) {
			for (int j = ksize-1; j < src.cols()-ksize; j+=ksize) {
				src_px 	= src.get(i, j);
				//desconsiderar pixels Brancos e TODO: se o desvio padrão entre as tres bandas for muito alto(50-100)
				if((!(src_px[0]>=limiarBranco && src_px[1]>=limiarBranco && src_px[2]>=limiarBranco)) && desvioPadraoEhAlto(src_px, limitDesvioPadrao)){
					if(possuiPixelIgual(src_px, dst_subMat, margemErro)){
						paintVizinhos(i, j, src, ksize);
					}
				}
			}
		}
		img = img.submat(0, src.rows(), 0, src.cols());
		long countDif=0;
		for (int i = ksize-1; i < src.rows()-ksize; i+=ksize) {
			for (int j = ksize-1; j < src.cols()-ksize; j+=ksize) {
				double[] vet1 = src.get(i, j);
				double[] vet2 = img.get(i, j);
				if(!(vetoresSaoIguais(vet1, vet2, margemErro))){
					countDif+=1;
				}
			}
		}
		long max = src.rows() *src.cols();
		long x =  ((countDif*100)/max);
		if(x>=5){
			return true;
		}
		return false;
	}

	public void applyManchasAlgImgInteira(Mat img) {
		try{
			Mat src = img;
			long start = System.currentTimeMillis();
			//Filtro na margem do topo

			int lnIni=5;
			int lnFin=11;
			Mat subMat =  src.submat(lnIni, lnFin, 0, src.width());
			Mat dst_subMat = new Mat();
			Imgproc.blur(subMat, dst_subMat, new Size(ksize,ksize));
			//Map<Integer, Integer> map = getLUT_sRGB(dst_subMat);
			//System.out.println(map.size());

			//			int z;
			//			for (z = 0; z < 7; z++) {
			//				subMat = dst_subMat;
			//				dst_subMat = new Mat();
			//				Imgproc.pyrDown(subMat, dst_subMat);
			//				System.out.println(dst_subMat.size().toString());
			//			}


			//Filtro na imagem inteira
			Mat dst = new Mat();
			Imgproc.blur(src, dst, new Size(ksize, ksize));
			src = new Mat();
			src = dst;


			// FAZER O FILTRO DA MEDIA NA IMG ORIG
			double[] src_px;
			for (int i = ksize-1; i < src.rows()-ksize; i+=ksize) {
				for (int j = ksize-1; j < src.cols()-ksize; j+=ksize) {
					src_px 	= src.get(i, j);
					//desconsiderar pixels Brancos e TODO: se o desvio padrão entre as tres bandas for muito alto(50-100)
					if((!(src_px[0]>=limiarBranco && src_px[1]>=limiarBranco && src_px[2]>=limiarBranco)) && desvioPadraoEhAlto(src_px, limitDesvioPadrao)){
						//if(possuiPixelIgual_LUTsRGB(src_px, map)){
						if(possuiPixelIgual(src_px, dst_subMat, margemErro)){
							//if(possuiPixelIgual_margemLados(src_px, j, dst_subMat, margemErro)){
							paintVizinhos(i, j, src, ksize);
						}
					}
				}
			}
			double tempo = (System.currentTimeMillis()-start)/1000.0;
			String str_tempo ="";
			if(tempo>60.0){
				str_tempo+= ((tempo/60)+"\tminutos");
			}
			else{
				str_tempo+= ((tempo)+"\tsegundos");				
			}
			System.out.println(str_tempo);
			String algo= "_manchas_dp("+limitDesvioPadrao+")_cabc("+(lnFin-lnIni)+")_magemLado("+margemLado+")";
			Imgcodecs.imwrite(pathname+algo+".jpg", src);
			Imgcodecs.imwrite(pathname+"dst.jpg", src);

			//MetodosEM.criarTxtdoTempo(pathname+algo, str_tempo);
			//MetodosEM.fazerSom();
			//System.out.println("countTrue=\t"+countTrue);
		}catch(Exception ex){
			System.err.println(ex.getMessage());
			ex.printStackTrace();
		}
	}



	private boolean desvioPadraoEhAlto(double[] src_px, double limitDesvioPadrao) {
		double somat = (src_px[0]+src_px[1]+src_px[2]);
		double media=(somat/3);
		double var=(((int)(somat-media))^2)/255.0;
		double dp= Math.sqrt(var);
		if(dp>limitDesvioPadrao){
			return true;
		}
		return false;

	}

	private boolean possuiPixelIgual_margemLados(double[] px, int j_atual,  Mat img, int margemErro) {
		margemLado = 7000;
		int aux = j_atual - margemLado;
		int j_ini = (aux <= 0) ? (0) : (aux);
		aux  = j_atual + margemLado;
		int j_fin = (aux >= img.cols())? (img.cols()) : aux; 
		for (int i = 0; i < img.rows(); i++) {
			for (int j = j_ini; j < j_fin; j++) {
				double[] src_px = img.get(i, j);
				if(vetoresSaoIguais(src_px, px, margemErro)){
					return true;
				}
			}
		}
		return false;
	}

	private boolean possuiPixelIgual(double[] px, Mat img, int margemErro) {
		for (int i = 0; i < img.rows(); i++) {
			for (int j = 0; j < img.cols(); j++) {
				double[] src_px = img.get(i, j);
				if(vetoresSaoIguais(src_px, px, margemErro)){
					return true;
				}
			}

		}
		return false;
	}

	public static boolean vetoresSaoIguais(double[] vet1, double[] vet2, int margemErro) {
		for (int i = 0; i < vet1.length; i++) {
			if(vet1[i]-vet2[i]>margemErro || vet2[i]-vet1[i]>margemErro){
				return false;
			}
		}
		return true;
	}

	public static void paintVizinhos(int i, int j, Mat img, int ksize){
		int floorJanela = (int) Math.floor(ksize/2);
		for (int k = -floorJanela; k <= floorJanela; k++) {
			for (int l = -floorJanela; l <= floorJanela; l++) {
				img.put(k+i, l+j, new double[]{255,0,0}); 
			}
		}
	}
	
	public static void paintVizinhos(int i, int j, BufferedImage img, int ksize){
		int floorJanela = (int) Math.floor(ksize/2);
		for (int k = -floorJanela; k <= floorJanela; k++) {
			for (int l = -floorJanela; l <= floorJanela; l++) {
				img.setRGB(k+i,l+j,new Color(255,0,0).getRGB()); 
			}
		}
	}






	/********************************************************************************/
	/**
	 * @deprecated
	 * */
	private static Map<Integer, Integer> getLUT_sRGB(Mat img){
		Map<Integer, Integer> map = new HashMap<Integer, Integer>();
		for (int i = 0; i < img.rows(); i++) {
			for (int j = 0; j < img.cols(); j++) {
				double[] data =	img.get(i, j);
				Color color = new Color((int)data[0],(int) data[1],(int) data[2]);
				Integer key = Integer.valueOf(color.getRGB());
				int aux = map.containsKey(key) ? map.get(key) : 0;
				map.put(key , aux+1);	
			}
		}
		return map;
	}


	/**
	 * @deprecated
	 * */
	private static boolean possuiPixelIgual_LUTsRGB(double[] px, Map<Integer, Integer> map ){
		double[] data =	px;
		Color color = new Color((int)data[0],(int) data[1],(int) data[2]);
		Integer key = Integer.valueOf(color.getRGB());

		if (map.containsKey(key)) {
			return true;
		}
		else if(temCorParecida(color, map)){
			return true;
		}

		return false;
	}

	/**
	 * @deprecated
	 * */
	private static boolean temCorParecida(Color color,  Map<Integer, Integer> map){
		Color c1;
		if(color.getRed()<(255-margemErro) 
				&& color.getGreen()<(255-margemErro) && color.getBlue()<(255-margemErro)){
			c1 = new Color(color.getRed() +margemErro, color.getGreen()+margemErro, color.getBlue()+margemErro);
			if (map.containsKey(c1.getRGB()) && map.get(c1.getRGB())>1) {
				return true;
			}
		}
		if(color.getRed()>(margemErro) 
				&& color.getGreen()>(margemErro) && color.getBlue()>(margemErro)){
			c1 = new Color(color.getRed() -margemErro, color.getGreen()-margemErro, color.getBlue()-margemErro);
			if (map.containsKey(c1.getRGB()) && map.get(c1.getRGB())>1) {
				return true;
			}
		}
		/***/

		/***/
		if(color.getRed()<(255-margemErro)){
			c1 = new Color(color.getRed() +margemErro, color.getGreen(), color.getBlue());
			if (map.containsKey(c1.getRGB()) && map.get(c1.getRGB())>1) {
				return true;
			}
		}
		if(color.getRed()>margemErro){
			c1 = new Color(color.getRed() -margemErro, color.getGreen(), color.getBlue());
			if (map.containsKey(c1.getRGB()) && map.get(c1.getRGB())>1) {
				return true;
			}
		}
		if(color.getGreen()<(255-margemErro)){
			c1 = new Color(color.getRed(), color.getGreen()+margemErro, color.getBlue());
			if (map.containsKey(c1.getRGB()) && map.get(c1.getRGB())>1) {
				return true;
			}
		}
		if(color.getGreen()>margemErro){
			c1 = new Color(color.getRed(), color.getGreen()-margemErro, color.getBlue());
			if (map.containsKey(c1.getRGB()) && map.get(c1.getRGB())>1) {
				return true;
			}
		}

		if(color.getBlue()<(255-margemErro)){
			c1 = new Color(color.getRed(), color.getGreen(), color.getBlue()+margemErro);
			if (map.containsKey(c1.getRGB()) && map.get(c1.getRGB())>1) {
				return true;
			}
		}
		if(color.getBlue()>margemErro){
			c1 = new Color(color.getRed(), color.getGreen(), color.getBlue()-margemErro);
			if (map.containsKey(c1.getRGB()) && map.get(c1.getRGB())>1) {
				return true;
			}
		}
		return false;
	}
}
