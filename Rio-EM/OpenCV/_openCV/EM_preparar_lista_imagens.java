package _openCV;

import java.awt.Color;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.RenderedImage;
import java.awt.image.WritableRaster;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.lang.reflect.Field;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import javax.imageio.ImageIO;
import javax.swing.JComboBox;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.ghost4j.document.PDFDocument;
import org.ghost4j.renderer.SimpleRenderer;

import com.github.junrar.Archive;
import com.github.junrar.exception.RarException;
import com.github.junrar.impl.FileVolumeManager;
import com.github.junrar.rarfile.FileHeader;

import db_dao.DB_DAO;


public class EM_preparar_lista_imagens {

	
	
	static {
		Logger.getRootLogger().setLevel(Level.OFF);
		initGhost4j();
	}

	private static void initGhost4j() {
		try {
			setLibraryPath();
			System.loadLibrary("win32-x86-64/gsdll64");
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	private static void setLibraryPath() {

		try {
			System.setProperty("java.library.path", "bin/");
			System.out.println(System.getProperty("java.library.path"));
			Field fieldSysPath = ClassLoader.class.getDeclaredField("sys_paths");
			fieldSysPath.setAccessible(true);
			fieldSysPath.set(null, null);

		} catch (Exception ex) {
			ex.printStackTrace();
			throw new RuntimeException(ex);
		}

	}

	public EM_preparar_lista_imagens(String dirIN) {
		DB_DAO db_dao = new DB_DAO();
		boolean isPrepared = prepareDAO_BD(db_dao);
		if(isPrepared){
			if(!(db_dao.hasWaintingProcessItems())){
				ArrayList<File> listaArq =  new ArrayList<File>();
				criarListaArqs(dirIN, listaArq);
				if(listaArq.size()>0){
					inserirListaArqs_noBD(listaArq, db_dao);
					db_dao.disconnect();
				}else{
					System.err.println("Diretorio nao possui arquivos.");
				}
			}else{
				System.err.println("Ainda existem arquivos a serem processados no BD.");
			}
		}
	}



	/**
	 * <b>@author Renan Fucci</b><br>
	 * <b>Objeto map<b><br>
	 * Objeto que conta a quantidade de arquivo por extensao.
	 */
	private Map<String, Integer> map = new HashMap<String, Integer>();

	/** 
	 * <b>@author Renan Fucci</b><br>
	 * <b>Metodo criarListaArqs</b><br>
	 * Metodo para criar uma lista de arquivos a partir de um diretorio.
	 * @param dir diretorio dos arquivos.
	 * @param listaArq lista onde serao armazenados os arquivos.
	 */
	private void criarListaArqs(String dir, ArrayList<File> listaArq){
		criarListaArqs(new File(dir), listaArq);

	}

	/**
	 * <b>@author Renan Fucci<br></b>
	 * <b>Metodo criarListaArqs</b><br>
	 * Metodo para criar uma lista de arquivos a partir de um diretorio.
	 * @param folder diretorio dos arquivos.
	 * @param listaArq lista onde serao armazenados os arquivos.
	 */
	private  void criarListaArqs(final File folder, ArrayList<File> listaArq) {
		if(FileUtils.sizeOf(folder)>0){
			for (File fileEntry : folder.listFiles()) {
				if (fileEntry.isDirectory()) {
					criarListaArqs(fileEntry, listaArq);
				} 
				else {
					if(FileUtils.sizeOf(fileEntry) >0  && fileIsComplete(fileEntry)){
						listaArq.add(fileEntry);
					}
				}
			}
		}
	}


	/**
	 * <b>@author Renan Fucci<br></b><br>
	 * <b>Metodo inserirListaArqs_noBD</b><br>
	 * Metodo para inserir num BD uma lista de arquivos, criado no metodo {@link #criarListaArqs(String, ArrayList)}.<br>
	 * @param listaArq lista de arquivo a ser inserida no BD;
	 * @param db_dao objeto de conexao com o banco.
	 */
	private void inserirListaArqs_noBD(ArrayList<File> listaArq, DB_DAO db_dao){
		if(prepareDAO_BD(db_dao)){
			inserirListaArqs_noBD(db_dao, listaArq);
		}
	}

	/**
	 * <b>@author Renan Fucci<br></b><br>
	 * <b>Metodo prepareDAO_BD</b><br>
	 * Metodo para preparar a classe de controle de objeto de conexao do banco de dados.
	 * @param db_dao objeto de conexao com o banco.
	 * @return boolean se a classe esta preparada ou nao.
	 */
	private boolean prepareDAO_BD(DB_DAO db_dao){
		if(db_dao.connect()){
			if(db_dao.createStmt_Insert() && db_dao.createStmt_Update()){
				return true;
			}
			else{
				System.err.println("Nao conseguiu criar requisicao ao banco");
			}
		}
		else{
			System.err.println("Nao conseguiu conexao com banco");
		}
		return false;
	}

	/**
	 * <b>@author Renan Fucci<br></b>
	 * <b>Metodo inserirListaArqs_noBD</b><br>
	 * Metodo para inserir num BD uma lista de arquivos, criado no metodo {@link #criarListaArqs(String, ArrayList)}.<br>
	 * @param db_dao objeto de conexao com o banco.
	 * @param listaArq lista de arquivo a ser inserida no BD;
	 */
	private void inserirListaArqs_noBD(DB_DAO db_dao, ArrayList<File> listaArq){
		Iterator<File> iter = listaArq.iterator();

		while (iter.hasNext()) {
			File fileEntry = iter.next();
			String arq_ext  =FilenameUtils.getExtension(fileEntry.getName());//extensao do arquivo
			int aux = map.containsKey(arq_ext) ? map.get(arq_ext) : 0;
			map.put(arq_ext, aux+1);
			if (arq_ext.contentEquals("rar")) {
				String dirEntrada = fileEntry.getParent() + File.separator;
				String nomeArq_Extens = fileEntry.getName();
				junRAR(dirEntrada, nomeArq_Extens, arq_ext, 0, db_dao);
			}
			else if(arq_ext.contentEquals("zip")){
				String dirEntrada = fileEntry.getParent() + File.separator;
				String nomeArq_Extens = fileEntry.getName();
				unZIP(dirEntrada, nomeArq_Extens, arq_ext, 0, db_dao);	
			}
			else if (arq_ext.contentEquals("jpg")) {
				db_dao.executeStmt_Insert(fileEntry.getPath(), arq_ext, "No", 0);
			}
			else if(arq_ext.contentEquals("bmp") || arq_ext.contentEquals("png") 
					|| arq_ext.contentEquals("gif") || arq_ext.contentEquals("jpeg")){
				BufferedImage bufferedImage;
				BufferedImage newBufferedImage ;
				String strNewDir; 
				bufferedImage = readImage(fileEntry.getAbsolutePath());
				newBufferedImage = toJPG(bufferedImage);
				strNewDir = fileEntry.getParent()+ fileEntry.getPath().substring(fileEntry.getPath().lastIndexOf(File.separator), (fileEntry.getPath().length()-4))+".jpg";
				if(writeImage(newBufferedImage, strNewDir)){
					int id_inserted = db_dao.executeStmt_Insert(fileEntry.getPath(), arq_ext, "Success", 0);
					db_dao.executeStmt_Insert(strNewDir, "jpg", "No", id_inserted);
				}
				else{
					db_dao.executeStmt_Insert(fileEntry.getPath(), arq_ext, "Fail", 0);		
				}
			} 
			else if (arq_ext.contentEquals("pdf")) {
				String dirEntrada = (fileEntry.getParent()) + File.separator;
				String nomeArq_Extens = fileEntry.getName();
				pdfToImgs(dirEntrada, nomeArq_Extens, arq_ext, 0, db_dao);
			}
			else if(arq_ext.contentEquals("dropbox") || arq_ext.contentEquals("ftpquota")){

			}else{
				db_dao.executeStmt_Insert(fileEntry.getPath(), arq_ext, "Fail", 0);

			}			
			iter.remove();
		}
	}

	/**
	 * <b>@author Renan Fucci<br></b>
	 * <b>Metodo inspetorArqs_Parent</b><br>
	 * Metodo para fazer a inspecao da extensao dos arquivos e a insercao no BD.<br>
	 * Se a extensao do arquivo for "zip" ou "rar", sera feita a descompactacao utilizando o metodo {@link #junRAR(String, String, String, int, DB_DAO)}.<br>
	 * Se for "jpg", só insere no BD.<br>
	 * Se for uma extensao de imagem ("bmp", "png", "gif", "jpeg"), é feita a conversao pra jpg antes da insercao no BD.<br>
	 * Se for "pdf", faz a conversao para imagem com o metodo {@link #pdfToImgs(String, String, String, int, DB_DAO)}.<br>
	 * Caso contrario, nao eh feito nenhum tratamento antes da insercao no BD.<br>
	 * @param fileEntry arquivo de entrada;
	 * @param db_dao objeto de conexao com BD;
	 * @param id_inserted id do pai deste arquivo no BD, se tiver. Se não, 0;
	 * @param listaArq lista de arquivo a ser inserida no BD;
	 */
	private void inspetorArqs_Parent(File fileEntry, DB_DAO db_dao, int id_inserted){
		String arq_ext  =FilenameUtils.getExtension(fileEntry.getName());//extensao do arquivo
		int aux = map.containsKey(arq_ext) ? map.get(arq_ext) : 0;
		map.put(arq_ext, aux+1);
		if (arq_ext.contentEquals("rar")) {
			String dirEntrada = fileEntry.getParent() + File.separator;
			String nomeArq_Extens = fileEntry.getName();
			junRAR(dirEntrada, nomeArq_Extens, arq_ext, id_inserted, db_dao);
		}else if(arq_ext.contentEquals("zip")){
			String dirEntrada = fileEntry.getParent() + File.separator;
			String nomeArq_Extens = fileEntry.getName();
			unZIP(dirEntrada, nomeArq_Extens, arq_ext, id_inserted, db_dao);
		}
		else if (arq_ext.contentEquals("jpg")) {
			db_dao.executeStmt_Insert(fileEntry.getPath(), arq_ext, "No", id_inserted);

		}
		else if( arq_ext.contentEquals("bmp") || arq_ext.contentEquals("png") || arq_ext.contentEquals("gif") || arq_ext.contentEquals("jpeg") ){
			BufferedImage bufferedImage;//Buffer da img original
			BufferedImage newBufferedImage ;//Buffer da img com o aplha
			String strNewDir ; //Novo dir 
			bufferedImage = readImage(fileEntry.getAbsolutePath());
			newBufferedImage = toJPG(bufferedImage);
			strNewDir = fileEntry.getParent()+ fileEntry.getPath().substring(fileEntry.getPath().lastIndexOf(File.separator), (fileEntry.getPath().length()-4))+".jpg";
			if(writeImage(newBufferedImage, strNewDir)){
				int id_parent = db_dao.executeStmt_Insert(fileEntry.getPath(), arq_ext, "Success",id_inserted );
				db_dao.executeStmt_Insert(strNewDir, "jpg", "No", id_parent);
			}
			else{
				db_dao.executeStmt_Insert(fileEntry.getPath(), arq_ext, "Fail", id_inserted);		
			}
		} 
		else if (arq_ext.contentEquals("pdf")) {
			String dirEntrada = (fileEntry.getParent()) + File.separator;
			String nomeArq_Extens = fileEntry.getName();				
			pdfToImgs(dirEntrada, nomeArq_Extens, arq_ext, id_inserted, db_dao);

		}
		else if(arq_ext.contentEquals("dropbox") || arq_ext.contentEquals("ftpquota")){

		}
		else{
			db_dao.executeStmt_Insert(fileEntry.getPath(), arq_ext, "Fail", id_inserted);

		}

	}

	/**
	 * <b>@author Renan Fucci<br></b>
	 * <b>Metodo fileIsComplete<br></b>
	 * Metodo para testar se o arquivo esta completamente escrito.<br>
	 * @param file arquivo que sera testado.
	 * @return resulato booleano se o arquivo esta completo.
	 */
	private boolean fileIsComplete(File file) {
		RandomAccessFile stream = null;
		try {
			stream = new RandomAccessFile(file, "rw");
			return true;
		} catch (Exception e) {
			System.err.println("Arquivo " + file.getName() + " nao esta completamente escrito.");
			e.printStackTrace();
		} finally {
			if (stream != null) {
				try {
					stream.close();
				} catch (IOException e) {
					System.err.println(e.getLocalizedMessage());
					e.printStackTrace();
				}
			}
		}
		return false;
	}

	/**
	 * <b>@author Renan Fucci</b><br>
	 * <b>Metodo unZIP.</b><br>
	 * Metodo para descompactar arquivos ZIP somente com java.
	 * @param dirEntrada string que deve conter o somente o diretorio onde esta localizado o arquivo.
	 * @param nomeArq_Extens  nome do arquivo com a extensao.
	 * @param type_file extensao do arquivo;
	 * @param id_parent id do pai deste arquivo no BD, se tiver. Se não, 0;
	 * @param db_dao objeto de conexao com BD.
	 * @return resultado booleano da descompactacao.
	 */
	private boolean unZIP(String dirEntrada, String nomeArq_Extens, String type_file, int id_parent, DB_DAO db_dao) {
		String filename = dirEntrada+nomeArq_Extens;//String com diretorio do nome do arquivo de entrada
		File fl = new File(filename);
		File newDir = new File(fl.getParent()+File.separator+fl.getName().substring(0, fl.getName().lastIndexOf("."))+File.separator);
		newDir.mkdirs();

		int id_inserted_ZIP=-1; //id do documento com extensao de compactacao no BD

		try{
			Charset charset = Charset.forName("IBM00858");
			ZipFile zipFile = new ZipFile(fl.getAbsolutePath(), charset);
			@SuppressWarnings("rawtypes")
			Enumeration e = zipFile.entries();
			String converted_file = "Fail";
			id_inserted_ZIP = db_dao.executeStmt_Insert(fl.getPath(), type_file, converted_file, id_parent);
			while (e.hasMoreElements()) {
				ZipEntry entry = (ZipEntry) e.nextElement();
				File out = new File(newDir.getPath()+File.separator+entry.getName());
				if (entry.isDirectory()) {
					out.mkdirs();
				} 
				else {
					File pastas = new File(out.getParent()+File.separator);
					pastas.mkdirs();
					BufferedInputStream bis = new BufferedInputStream(zipFile.getInputStream(entry));
					int b;
					byte buffer[] = new byte[2048];
					FileOutputStream fos = new FileOutputStream(
							out);
					BufferedOutputStream bos = new BufferedOutputStream(fos,
							1024);
					while ((b = bis.read(buffer, 0, 1024)) != -1) {
						bos.write(buffer, 0, b);
					}
					bos.flush();
					bos.close();
					bis.close();
					inspetorArqs_Parent(out, db_dao, id_inserted_ZIP);
				}
			}
			db_dao.executeStmt_Update(id_inserted_ZIP, "Success");
			zipFile.close();
			return true;
		} catch (IOException ex) {
			System.err.println(ex.getLocalizedMessage());
			ex.printStackTrace();
		}

		return false;
	}

	/**
	 * <b>@author Renan Fucci</b><br>
	 * <b>Metodo junRAR.</b><br>
	 * Metodo para descompacter arquivos RAR somente com java.
	 * @param dirEntrada string que deve conter o somente o diretorio onde esta localizado o arquivo.
	 * @param nomeArq_Extens  nome do arquivo com a extensao.
	 * @param type_file extensao do arquivo;
	 * @param id_parent id do pai deste arquivo no BD, se tiver. Se não, 0;
	 * @param db_dao objeto de conexao com BD.
	 * @return resultado booleano da descompactacao.
	 */
	private boolean junRAR(String dirEntrada, String nomeArq_Extens, String type_file, int id_parent, DB_DAO db_dao) {
		Archive a = null;// variavel de lib para descompactacao
		String filename = dirEntrada+nomeArq_Extens;//String com diretorio do nome do arquivo de entrada
		File fl = new File(filename);//Arquivo de entrada
		File newDir = new File(fl.getParent()+File.separator+fl.getName().substring(0, fl.getName().lastIndexOf("."))+File.separator);
		newDir.mkdirs();
		int id_inserted_RAR=-1; //id do documento com extensao de compactacao no BD
		try {
			a = new Archive(new FileVolumeManager(fl));

		} catch (RarException e) {
			System.err.println("rarEx: "+e.getLocalizedMessage());
		} catch (IOException e) {
			System.err.println("rarEx: IOex: "+e.getLocalizedMessage());
		}
		if (a != null) {
			if(fl.exists()){
				String converted_file = "Fail";
				id_inserted_RAR = db_dao.executeStmt_Insert(fl.getPath(), type_file, converted_file, id_parent);
				FileHeader fh = a.nextFileHeader();
				while (fh != null) {
					try {
						File out = new File(newDir.getPath() +File.separator
								+ fh.getFileNameString().trim());
						if(fh.isDirectory()){
							out.mkdirs();
						}
						else{
							File pastas = new File(out.getParent()+File.separator);
							pastas.mkdirs();
							FileOutputStream os = new FileOutputStream(out);
							a.extractFile(fh, os);
							os.close();
							inspetorArqs_Parent(out, db_dao, id_inserted_RAR);
						}
					} catch (FileNotFoundException e) {
						System.err.println("FlNoFnd: "+e.getLocalizedMessage());
						e.printStackTrace();
					} catch (RarException e) {
						System.err.println("FlNoFnd: RarEx: "+e.getLocalizedMessage());
						e.printStackTrace();
					} catch (IOException e) {
						System.err.println("FlNoFnd: RarEx: IOex: "+e.getLocalizedMessage());
						e.printStackTrace();
					}
					fh = a.nextFileHeader();
				}
				db_dao.executeStmt_Update(id_inserted_RAR, "Success");
				return true;
			}
			else{
				System.err.println("\nNao foi possivel acessar o arquivo: "+fl.getPath());
			}
		}
		return false;
	}




	/**
	 * <b>@author Renan Fucci</b><br>
	 * <b>Metodo pdfToImgs</b><br>
	 * Metodo para converter um pdf em arquivos de imgs.
	 * @param dirEntrada string que deve conter o somente o diretorio onde esta localizado o arquivo.
	 * @param nomeArq_Extens  nome do arquivo com a extensao.
	 * @param type_file extensao do arquivo;
	 * @param id_parent id do pai deste arquivo no BD, se tiver. Se não, 0;
	 * @param db_dao objeto de conexao com BD.
	 * @return resultado booleano da conversao.
	 */
	private void pdfToImgs(String dirEntrada, String nomeArq_Extens, String type_file, int id_parent, DB_DAO db_dao) {
		int id_inserted_PDF=-1; //id do documento com extensao de compactacao no BD

		try {
			File file = new File(dirEntrada+nomeArq_Extens);

			PDFDocument document = new PDFDocument();
			document.load(file);

			File newDir = new File(file.getParent()+File.separator+file.getName().substring(0, file.getName().lastIndexOf("."))+File.separator);
			newDir.mkdirs();
			String converted_file = "Fail";
			id_inserted_PDF = db_dao.executeStmt_Insert(file.getPath(), type_file, converted_file, id_parent);

			SimpleRenderer renderer = new SimpleRenderer();
			// set resolution (in DPI)
			renderer.setResolution(200);

			List<Image> images = renderer.render(document);

			for (int i = 0; i < images.size(); i++) {
				RenderedImage x = (RenderedImage) images.get(i);
				BufferedImage img =  toJPG((toBufferedImage(x)));
				File yourImageFile = new File(newDir.getPath()+File.separator+file.getName().substring(0,file.getName().lastIndexOf("."))+"_" + i + ".jpg");
				ImageIO.write(img, "JPG", yourImageFile);
				inspetorArqs_Parent(yourImageFile, db_dao, id_inserted_PDF);
			}

		} catch (Exception e) {
			System.err.println(e.getLocalizedMessage());
			e.printStackTrace();
		}
		db_dao.executeStmt_Update(id_inserted_PDF, "Success");
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
	/**
	 * <b>@author Renan Fucci</b><br>
	 * <b>Metodo readImage</b><br>
	 * Metodo para ler uma imagem em um diretorio e transforma-la num {@link BufferedImage}.
	 * @param dirCompletoComExtens diretorio completo com a extensao do arquivo.
	 * @return um {@link BufferedImage}.
	 *
	 */
	private BufferedImage readImage(String dirCompletoComExtens){ 
		try {
			return ImageIO.read(new File(dirCompletoComExtens));
		} catch (IOException e) {
			System.err.println(e.getLocalizedMessage()
					+ "\nCertifique-se que o diretorio esta correto e com a mesma extensao do arquivo.");
			e.printStackTrace();
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
	private boolean writeImage(BufferedImage image, String dirCompletoComExtens ){ 
		try {	
			return ImageIO.write(image, FilenameUtils.getExtension(dirCompletoComExtens), new File(dirCompletoComExtens));
		} catch (IOException e) {
			System.err.println(e.getLocalizedMessage()
					+ "\nCertifique-se que o diretorio esta correto e com a uma extensao de arquivo.");
			e.printStackTrace();
		}
		return false;
	}

	static {//hack pra mudar o default charset da JVM
		try{
			System.setProperty("file.encoding","IBM00858");	
			Field charset = Charset.class.getDeclaredField("defaultCharset");
			charset.setAccessible(true);
			charset.set(null,null);
		}catch(Exception ex){
			System.err.println(ex.getMessage());
			ex.printStackTrace();
		}
	}
}
