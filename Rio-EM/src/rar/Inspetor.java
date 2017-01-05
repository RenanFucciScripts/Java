package rar;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.SystemUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.util.PDFImageWriter;

import com.github.junrar.Archive;
import com.github.junrar.exception.RarException;
import com.github.junrar.impl.FileVolumeManager;
import com.github.junrar.rarfile.FileHeader;

public class Inspetor {


	public static void main(String[] args) {	
		String dirEntrada= "F:/Users/Renan Fucci/Dropbox/Estante Magica_Renan_Fucci/Pasta_Renan_Fucci/2016-02-29/";

		//Se quiser desativar o Log
		/*		
		 * System.setProperty("org.apache.commons.logging.Log",
		 * "org.apache.commons.logging.impl.NoOpLog");
		 */
		Stack<File> pilhaArqs= new Stack<File>();
		pilhaArqs=empilharArquivosDiretorio(new File(dirEntrada), pilhaArqs);	
		System.out.println(map);
		System.out.println(pilhaArqs.size());
	}

	/**
	 * <b>@author Renan Fucci<br></b>
	 * <b>Metodo empilharArquivosDiretorio</b><br>
	 * Metodo para fazer pilha de todos os arquivos de um diretorio, com inspecao de extensao de arquivos.<br>
	 * Se houver espacos no nome do arquivo, o mesmo sera renomeado, substituindo os espacos por <i>underline</i>.<br>
	 * Se existirem arquivos pdf, sera chamada uma funcao para converter.<br>
	 * Se existirem arquivos compactados (RAR ou ZIP), sera chamado funcao para extrair todos os arquivos.<br>
	 * Se existirem arquivos em formatos de imagens (BMP, GIF e PNG), sera chamada uma funcao que ira converter pra JPG.<br>
	 * Se exsitirem jpg, nao havera alteracao.<br>
	 * Se existirem arquivos em qualq	uer formato diferente dos anteriores, o mesmo sera copiado pra uma pasta (aler) e sera exibido uma mensagem de erro. <br>
	 * @param folder arquivo a ser empilhado;
	 * @param pilhaArqs varialvel na qual serao empilhados os arquivos.
	 * @return pilhaArqs variavel de arquivos empilhadas.
	 */
	public Stack<File> empilharArquivosDiretorio(String diretorio,  Stack<File> pilhaArqs){
		empilharArquivosDiretorio(new File(diretorio), pilhaArqs);
		return pilhaArqs;	
	}

	/**
	 * <b>@author Renan Fucci<br></b>
	 * <b>Metodo unRAR<br></b><br>
	 * <b><i>FUNCIONA SOMENTO NO LINUX</i></b><br>
	 * Metodo para chamar por linha de comando os arquivos rar e descomcapta-los.
	 * @param dirEntrada  string que deve conter o somente o diretorio onde esta localizado o arquivo.
	 * @param nomeArq_Extens  nome do arquivo com a extens�o.
	 */
	private static void unRAR(String dirEntrada, String nomeArq_Extens) {
		File fl;//Arquivo de entrada
		String command;//Comando de extração de arquivo a ser executado
		try {
			fl = new File(dirEntrada+nomeArq_Extens);
			if(fl.exists()){
				command="unrar e "+fl.getPath()+" "+fl.getParent()+File.separator;
				Runtime.getRuntime().exec(new String[]{"bash","-c",command});
			}
			else{
				System.err.println("Nao foi possivel acessar o diretorio: "+fl.getPath());
			}
		} catch (Exception e) {
			System.err.println(e.getLocalizedMessage());
			e.printStackTrace();
		}
	}

	private static Map<String, Integer> map = new HashMap<String, Integer>();
	
	/**
	 * <b>@author Renan Fucci<br></b>
	 * <b>Metodo empilharArquivosDiretorio</b><br>
	 * Metodo para fazer pilha de todos os arquivos de um diretorio, com inspecao de extensao de arquivos.<br>
	 * Se houver espacos no nome do arquivo, o mesmo sera renomeado, substituindo os espacos por <i>underline</i>.<br>
	 * Se existirem arquivos pdf, sera chamada uma funcao para converter.<br>
	 * Se existirem arquivos compactados (RAR ou ZIP), sera chamado funcao para extrair todos os arquivos.<br>
	 * Se existirem arquivos em formatos de imagens (BMP, GIF e PNG), sera chamada uma funcao que ira converter pra JPG.<br>
	 * Se exsitirem jpg, nao havera alteracao.<br>
	 * Se existirem arquivos em qualquer formato diferente dos anteriores, o mesmo sera copiado pra uma pasta (aler) e sera exibido uma mensagem de erro. <br>
	 * @param folder arquivo a ser empilhado;
	 * @param pilhaArqs varialvel na qual serao empilhados os arquivos.
	 * @return pilhaArqs variavel de arquivos empilhadas.
	 */
	private static Stack<File> empilharArquivosDiretorio(final File folder, Stack<File> pilhaArqs) {
		for (final File fileEntry : folder.listFiles()) {
			if (fileEntry.isDirectory()) {
				empilharArquivosDiretorio(fileEntry, pilhaArqs);
			} else {
				if(temEspacoBranco(fileEntry)){
					renomearArqEspacosBrancos(fileEntry);
				}
				else{
					String arq_ext  =FilenameUtils.getExtension(fileEntry.getName());//extensao do arquivo
					int aux = map.containsKey(arq_ext) ? map.get(arq_ext) : 0;
					map.put(arq_ext, aux+1);
					if (arq_ext.contentEquals("zip") || arq_ext.contentEquals("rar")) {
						pilhaArqs.push(fileEntry);

						String dirEntrada = fileEntry.getParent() + File.separator;
						String nomeArq_Extens = fileEntry.getName();
						if (SystemUtils.IS_OS_LINUX) {
							unRAR(dirEntrada, nomeArq_Extens);
						} else {
							junRAR(dirEntrada, nomeArq_Extens);
						}
					} else if (arq_ext.contentEquals("jpg")) {
						pilhaArqs.push(fileEntry);

					} else if( arq_ext.contentEquals("bmp") || arq_ext.contentEquals("png") || arq_ext.contentEquals("gif")){
						pilhaArqs.push(fileEntry);
						BufferedImage bufferedImage;//Buffer da img original
						BufferedImage newBufferedImage ;//Buffer da img com o aplha
						bufferedImage = readImage(fileEntry.getAbsolutePath());
						newBufferedImage = new BufferedImage(bufferedImage.getWidth(),
								bufferedImage.getHeight(), BufferedImage.TYPE_INT_RGB);
						newBufferedImage.createGraphics().drawImage(bufferedImage, 0, 0, Color.WHITE, null);
						writeImage(newBufferedImage, fileEntry.getParent()+ fileEntry.getPath().substring(fileEntry.getPath().lastIndexOf(File.separator), (fileEntry.getPath().length()-4))+".jpg");
					} else if (arq_ext.contentEquals("pdf")) {
						pilhaArqs.push(fileEntry);
						String dirEntrada = (fileEntry.getParent()) + File.separator;
						String nomeArq_Extens = fileEntry.getName();
						conversorPDF2JPG(dirEntrada, nomeArq_Extens);
					}
					else{
						pilhaArqs.push(fileEntry);
						if(!arq_ext.contentEquals("ftpquota"))
							moverParaPasta_Alerta(fileEntry);

					}

				}
			}
		}

		return pilhaArqs;
	}

	/**
	 * <b>@author Renan Fucci<br></b>
	 * <b>Metodo renomearArqEspacosBrancos</b><br>
	 * Metodo para renomear um arquivo com espaco em branco.
	 * @param fileEntry arquivo de entrada que sera renomeado.
	 */
	private static void renomearArqEspacosBrancos(File fileEntry){
		String fileName; //nome do arquivo
		File flrenomeado;//arquivo renomeado

		fileName = fileEntry.getName().replaceAll("\\s", "_");
		flrenomeado = new File(fileEntry.getParent()+File.separator+fileName);
		try {
			FileUtils.copyFile(fileEntry, flrenomeado);
			System.err.println("\nArquivo com espacos em branco, renomeado para: "+flrenomeado);
			fileEntry.delete();
		} catch (IOException e) {
			System.err.println(e.getLocalizedMessage());
		}
	}

	/**
	 * <b>@author Renan Fucci<br></b>
	 * <b>Metodo temEspacoBranco<br></b><br>
	 * Metodo para testar se o nome de um arquivo possui espaco em branco.
	 * @param fileEntry arquivo de entrada que sera testado o espaco em branco.
	 * @return booleano com o teste de escpaco em branco no nome do arquivo.
	 */
	private static boolean temEspacoBranco(File fileEntry){
		String fileName = fileEntry.getName();//Nome do Arquivo de entrada
		Pattern pattern = Pattern.compile("\\s");//Padrao de compilacao, neste caso espaco em branco
		Matcher matcher = pattern.matcher(fileName);//Combina o espaco em branco com o padrao de compilacao
		return matcher.find();//Busca o espaco em branco no nome do arquivo
	}

	/**
	 * <b>@author Renan Fucci<br></b>
	 * <b>Metodo moverParaPasta_Alerta<br></b><br>
	 * Metodo para mover arquivo para uma pasta "Alerta" dentro do mesmo diretorio.
	 * @param fileEntry arquivo de entrada que sera movido.
	 */
	private static void moverParaPasta_Alerta(File fileEntry){
		File fl;//arquivo da pasta 'alerta'
		boolean pastaExiste;//booleano para testar se a pasta ja existe ou foi criada
		boolean jaEstaPastAlerta;//booleano para testar se o arquivo ja esta na pasta Alerta, pq senao ele fica criando pasta 'aleta' dentro de pasta 'alerta'
		String auxStr;//String auxiliar pra nome da pasta 'alerta'
		jaEstaPastAlerta=fileEntry.getParent().substring(fileEntry.getParent().lastIndexOf(File.separator)+1, fileEntry.getParent().length()).contentEquals("alerta");
		if(jaEstaPastAlerta){
			return;
		}
		else{
			try {
				auxStr=fileEntry.getParent()+File.separator+"alerta"+File.separator;
				fl = new File(auxStr);
				pastaExiste = ((!fl.isDirectory())? fl.mkdir(): fl.exists());
				if(pastaExiste){
					File flOut = new File(fileEntry.getParent()+File.separator+"alerta"+File.separator+fileEntry.getName());
					FileUtils.copyFile(fileEntry, flOut);
					System.err.println("\nArquivo com extensao diferente\nCopiado para "+flOut.getPath());
				}
				else{
					System.err.println("\nNao foi possivel criar diretorio: "+auxStr);
				}
			} catch (IOException e) {
				System.err.println(e.getLocalizedMessage());	
			}
		}
	} 


	/**
	 * <b>@author Renan Fucci<br></b>
	 * <b>Metodo junRAR<br></b><br>
	 * Metodo para descompacter arquivos somento com java.
	 * @param dirEntrada  string que deve conter o somente o diretorio onde esta localizado o arquivo.
	 * @param nomeArq_Extens  nome do arquivo com a extensao.
	 */
	private static void junRAR(String dirEntrada, String nomeArq_Extens) {
		Archive a = null;// variavel de lib para descompactacao
		String filename = dirEntrada+nomeArq_Extens;//String com diretorio do nome do arquivo de entrada
		File fl = new File(filename);//Arquivo de entrada
		try {
			a = new Archive(new FileVolumeManager(fl));

		} catch (RarException e) {
			System.err.println(e.getLocalizedMessage());
		} catch (IOException e) {
			System.err.println(e.getLocalizedMessage());
		}
		if (a != null) {
			if(fl.exists()){

				FileHeader fh = a.nextFileHeader();

				while (fh != null) {
					try {
						File out = new File(fl.getParent() +File.separator
								+ fh.getFileNameString().trim());
						FileOutputStream os = new FileOutputStream(out);
						a.extractFile(fh, os);
						os.close();
					} catch (FileNotFoundException e) {
						System.err.println(e.getLocalizedMessage());
					} catch (RarException e) {
						System.err.println(e.getLocalizedMessage());

					} catch (IOException e) {
						System.err.println(e.getLocalizedMessage());
					}
					fh = a.nextFileHeader();
				}
			}
			else{
				System.err.println("\nNao foi possivel acessar o arquivo: "+fl.getPath());
			}
		}
	}

	/**
	 * <b>@author Renan Fucci<br></b>
	 * <b>Metodo conversorPDF2JPG<br></b>
	 * Metodo para converter cada pagina de um PDF em uma imagem no formato JPG.
	 * @param dirEntrada  string que deve conter o somente o diretorio onde esta localizado o arquivo.
	 * @param subPastaSaida  inserir se quiser que os arquivos sejam salvos em uma subpasta do dirEntrada, 
	 * se naoo, setar o valor da string em branco (String subPastaSaida = "";), e neste caso, 
	 * as imagens serao convertidas no mesmo diretorio de dirEntrada.
	 * @param nomeArq_Extens  nome do arquivo com a extens�o.
	 */ 
	private static void conversorPDF2JPG(String dirEntrada, String nomeArq_Extens) {
		final int resolution = 200;//resolucao da guideline da lib
		String password = "-password";//atribuito padrao da guideline da lib
		File pdf = new File(dirEntrada + nomeArq_Extens);//arquivo de entrada
		if (pdf.exists()) {
			String outputPrefix = dirEntrada + nomeArq_Extens.substring(0, nomeArq_Extens.lastIndexOf("."));
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
						}
					}

				}
				document.close();
			} catch (Exception ex) {
				System.err.println(ex.getMessage());
				ex.printStackTrace();
			}

		}
		else{
			System.err.println("Abertura/Leitura do arquivo interrompida.");
		}
	}

	/**
	 * <b>@author Renan Fucci</b><br>
	 * <b>Metodo readImage</b><br>
	 * Metodo para ler uma imagem em um diretorio e transforma-la num {@link BufferedImage}.
	 * @param dirCompletoComExtens diretorio completo com a extensao do arquivo.
	 * @return um {@link BufferedImage}.
	 *
	 */
	public static  BufferedImage readImage(String dirCompletoComExtens){ 
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
	 * <b>Metodo writeImage</b><br>
	 * Metodo para gravar uma imagem em um diretorio.
	 * @param dirCompletoComExtens diretorio completo com a extensao do arquivo.
	 * @return boolean com o resultado da gravacao do arquivo.
	 *
	 */
	public static  boolean writeImage(BufferedImage image, String dirCompletoComExtens ){ 
		try {	
			return ImageIO.write(image, FilenameUtils.getExtension(dirCompletoComExtens), new File(dirCompletoComExtens));
		} catch (IOException e) {
			System.err.println(e.getLocalizedMessage()
					+ "\nCertifique-se que o diretorio esta correto e com a uma extensao de arquivo.");
		}
		return false;
	}


}

