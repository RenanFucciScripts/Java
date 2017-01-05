package rar;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Stack;

import javax.imageio.ImageIO;

import org.apache.commons.io.FilenameUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.util.PDFImageWriter;

public class Unrar {

	public static void main(String[] args) {
		String dirEntrada= "C:\\Users\\Renan Fucci\\Dropbox\\Estante Magica_Renan_Fucci\\Pasta_Renan_Fucci\\2016-02-17\\";
		Stack<File> pilhaArqs= new Stack<File>();
		pilhaArqs=empilharArquivosDiretorio(new File(dirEntrada), pilhaArqs);
		//String nomeArq_Extens = "teste.rar";
		//unRAR(dirEntrada, nomeArq_Extens);
	}
	
	/**
	 * <b>@author Renan Fucci<br></b>
	 * <b>Metodo unRAR<br></b>
	 * <b>So pode ser usado no linux</b><br>
	 * Metodo para chamar por linha de comando os arquivos rar e descomcapta-los.
	 * @param dirEntrada  string que deve conter o somente o diretorio onde esta localizado o arquivo.
	 * @param nomeArq_Extens  nome do arquivo com a extensão.
	 */
	public static void unRAR(String dirEntrada, String nomeArq_Extens) {
		File fl = new File(dirEntrada+nomeArq_Extens);
		String auxStr;
		File pasta;
		String command;
		/*		String auxStr =fl.getAbsolutePath().substring((fl.getAbsolutePath().lastIndexOf("/")), fl.getAbsolutePath().lastIndexOf("."));
		auxStr =fl.getAbsolutePath().substring(0,fl.getAbsolutePath().lastIndexOf("/"))+(auxStr.replaceAll("\\s", ""));
		 */		
		auxStr =fl.getAbsolutePath().substring((fl.getAbsolutePath().lastIndexOf("\\")), fl.getAbsolutePath().lastIndexOf("."));
		 auxStr =fl.getAbsolutePath().substring(0,fl.getAbsolutePath().lastIndexOf("\\"))+(auxStr.replaceAll("\\s", ""));

		 pasta =new File(auxStr+"\\");
		 pasta.mkdir();

		 command="unrar e "+fl.getAbsolutePath()+" "+pasta.getAbsolutePath();
		 try {
			 @SuppressWarnings("unused")
			 Process p2 = Runtime.getRuntime().exec(new String[]{"bash","-c",command+"/"});
		 } catch (Exception e) {
			 System.err.println(e.getLocalizedMessage());
		 }
	}

	/**
	 * <b>@author Renan Fucci<br></b>
	 * <b>Metodo empilharArquivosDiretorio</b>
	 * Metodo para fazer pilha de arquivos de um diretorio, todos os arquivos na extensao JPG.
	 * Se existeram arquivos pdf, sera chamada uma funcao para converter.
	 * <p> dentro das pastas e subpastas do diretorio.  </p>
	 * @param folder diretorio a ser empilhado;
	 * @param pilhaArqs varialvel na qual serao empilhados os arquivos.
	 * @return pilhaArqs variavel de arquivos empilhadas.
	 */
	public static Stack<File> empilharArquivosDiretorio(final File folder, Stack<File> pilhaArqs) {
		for (final File fileEntry : folder.listFiles()) {
			if (fileEntry.isDirectory()) {
				empilharArquivosDiretorio(fileEntry, pilhaArqs);
			} else {
				String arq_ext  =FilenameUtils.getExtension(fileEntry.getName());
				if(arq_ext.contentEquals("zip") || arq_ext.contentEquals("rar")){
					pilhaArqs.push(fileEntry);
					String dirEntrada=fileEntry.getParent()+"\\";
					System.out.println("dirin: "+dirEntrada);
					String nomeArq_Extens=fileEntry.getName();
					System.out.println("nome_arq: "+nomeArq_Extens);
					unRAR(dirEntrada, nomeArq_Extens);
				}
				else if(arq_ext.contentEquals("jpg")){
					pilhaArqs.push(fileEntry);
				}
				else if(arq_ext.contentEquals("bmp") || arq_ext.contentEquals("png") || arq_ext.contentEquals("gif")){
					BufferedImage buff = readImage(fileEntry.getAbsolutePath());
					String dirfinal = fileEntry.getAbsolutePath().substring(fileEntry.getAbsolutePath().lastIndexOf("/")+1, fileEntry.getAbsolutePath().length()-4)+".jpg";
					System.out.println(dirfinal);
					writeImage(buff,dirfinal);

				}
				else if(arq_ext.contentEquals("pdf")){
					pilhaArqs.push(fileEntry);
					String dirEntrada = (fileEntry.getParent())+"/";
					String subPastaSaida = (fileEntry.getAbsolutePath().substring(fileEntry.getAbsolutePath().lastIndexOf("/")+1, fileEntry.getAbsolutePath().length()-4))+"/";
					subPastaSaida= subPastaSaida.replaceAll("\\s", "");
					String nomeArq_Extens=fileEntry.getName();
					conversorPDF2JPG(dirEntrada, subPastaSaida, nomeArq_Extens);
				}
				else{
					File fl = new File(fileEntry.getParent()+"/alerta/");
					if(!fl.exists()){
						fl.mkdir();
						File flOut = new File(fileEntry.getParent()+"/alerta/"+fileEntry.getName());
						System.out.println(flOut.isFile());
					}
				}

			}
		}
		return pilhaArqs;
	}


	/**
	 * <b>@author Renan Fucci<br></b>
	 * <b>Metodo conversorPDF2JPG<br></b>
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
							System.exit(1); //mata todo o harpia 
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

	/**
	 * <b>@author Renan Fucci</b><br>
	 * <b>Metodo readImage</b>
	 * Metodo para ler uma imagem em um diretorio e transforma-la num {@link BufferedImage}.
	 * @param dirCompletoComExtens diretorio completo com a extensao do arquivo.
	 * @return um {@link BufferedImage}.
	 *
	 */
	public static BufferedImage readImage(String dirCompletoComExtens){ 
		try {
			return ImageIO.read(new File(dirCompletoComExtens));
		} catch (IOException e) {
			System.err.println(e.getLocalizedMessage()
					+ "\nCertifique-se que o diretorio esta correto e com a mesma extensao do arquivo.");
		}
		return null;
	}

	/**
	 * <b>@author Renan Fucci</b><br>
	 * <b>Metodo writeImage</b>
	 * Metodo para gravar uma imagem em um diretorio.
	 * @param dirCompletoComExtens diretorio completo com a extensao do arquivo.
	 * @return boolean com o resultado da gravacao do arquivo.
	 *
	 */
	public static boolean writeImage(BufferedImage image, String dirCompletoComExtens ){ 
		try {
			return ImageIO.write(image, FilenameUtils.getExtension(dirCompletoComExtens), new File(dirCompletoComExtens));
		} catch (IOException e) {
			System.err.println(e.getLocalizedMessage()
					+ "\nCertifique-se que o diretorio esta correto e com a uma extensao de arquivo.");
		}
		return false;
	}


}
