package rar;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.sound.sampled.LineUnavailableException;

import com.github.junrar.Archive;
import com.github.junrar.exception.RarException;
import com.github.junrar.impl.FileVolumeManager;
import com.github.junrar.rarfile.FileHeader;

import metodos.MetodosEM;


public class DescompactarRAR {

	public static void main(String[] args) throws LineUnavailableException {
		String dirEntrada = "C:\\Users\\Renan Fucci\\Dropbox\\Estante Magica_Renan_Fucci\\";
		String subPastaSaida="descompactadas\\"; 
		//ou, se quiser mesma diretorio 
		// String subPastaSaida="";
		String nomeArq_Extens="copia teste 2015.rar";
		//descompactarRAR(dirEntrada, subPastaSaida, nomeArq_Extens);
		unRAR(dirEntrada,nomeArq_Extens);
		MetodosEM.fazerSom();

	}

	public static void unRAR(String dirEntrada, String nomeArq_Extens) {
		File fl = new File(dirEntrada+nomeArq_Extens);
		String auxStr =fl.getAbsolutePath().substring((fl.getAbsolutePath().lastIndexOf("\\")), fl.getAbsolutePath().lastIndexOf("."));
		auxStr =fl.getAbsolutePath().substring(0,fl.getAbsolutePath().lastIndexOf("\\"))+(auxStr.replaceAll("\\s", ""));
		File pasta =new File(auxStr+"\\");
		pasta.mkdir();
		String command="unrar "+fl.getAbsolutePath()+" "+pasta.getAbsolutePath();
		try{
			Process p = Runtime.getRuntime().exec(new String[]{"csh","-c",command});
		}catch(Exception ex){
			System.err.println(ex.getLocalizedMessage());
			try {
				Process p2 = Runtime.getRuntime().exec(new String[]{"bash","-c",command});
			} catch (Exception e) {
				System.err.println(e.getLocalizedMessage());
			}
			

		}

	}

	/**
	 * <b>@author Renan Fucci<br></b>
	 * <b>Metodo descompactarRAR<br></b>
	 * Metodo para descompactar todos os arquivos que estiveram em um arquivo .rar.
	 * @param dirEntrada string que deve conter o somente o diretorio onde está localizado o arquivo.
	 * @param subPastaSaida inserir se quiser que os arquivos sejam salvos em uma subpasta do dirEntrada, 
	 * se não, setar o valor da string em branco (String subPastaSaida = "";), e neste caso, 
	 * as imagens serão descompactadas no mesmo diretorio de dirEntrada.
	 * @param nomeArq_Extens nome do arquivo com a extensão.
	 */
	public static void descompactarRAR(String dirEntrada,String subPastaSaida, String nomeArq_Extens){ 
		if(!(new File(dirEntrada+subPastaSaida).exists()))
			new File(dirEntrada+subPastaSaida).mkdir();
		//System.out.println(new File(dirEntrada+subPastaSaida+nomeArq_Extens).getAbsolutePath());
		File f = new File(dirEntrada+nomeArq_Extens);
		Archive a = null;
		try {
			a = new Archive(new FileVolumeManager(f));
		} catch (RarException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (a != null) {
			//a.getMainHeader().print();
			FileHeader fh = a.nextFileHeader();
			while (fh != null) {
				try {
					String arqNome =  dirEntrada+subPastaSaida
							+ fh.getFileNameString().trim();
					System.out.println("---------- "+arqNome+" ---------------");
					File fl = new File(arqNome);
					System.out.println("---------- "+fl.getParent()+" ---------------");
					if(!(new File(fl.getParent()).exists())){
						new File(fl.getParent()).mkdirs();
					}
					File out = new File(arqNome);
					//System.out.println(out.getAbsolutePath());
					FileOutputStream os = new FileOutputStream(out);
					a.extractFile(fh, os);
					os.close();
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (RarException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				fh = a.nextFileHeader();
			}
		}
	}

}