package harpia_em;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.RenderedImage;
import java.awt.image.WritableRaster;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.lang.reflect.Field;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.zip.ZipEntry;

import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

import com.github.junrar.Archive;
import com.github.junrar.impl.FileVolumeManager;
import com.github.junrar.rarfile.FileHeader;
import java.io.FileInputStream;
import java.util.zip.ZipInputStream;
import org.apache.commons.lang3.SystemUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.apache.pdfbox.tools.imageio.ImageIOUtil;

public class EM_preparar_lista_imagens_BRANCH_JBIG2 {

    public static void main(String[] args) {
        String dir = "C:/Users/_/Dropbox/Estante Magica_Renan_Fucci/"
                + "Pasta_Renan_Fucci/2016-11-07/teste_pdf_branco/";
        new EM_preparar_lista_imagens_BRANCH_JBIG2(dir);
    }

    public EM_preparar_lista_imagens_BRANCH_JBIG2(String dirIN) {
        DB_DAO db_dao = new DB_DAO();
        boolean isPrepared = prepareDAO_BD(db_dao);
        if (isPrepared) {
            if (!(db_dao.hasWaintingProcessItems())) {
                ArrayList<File> listaArq = new ArrayList<>();
                criarListaArqs(dirIN, listaArq);
                if (listaArq.size() > 0) {
                    inserirListaArqs_noBD(listaArq, db_dao);
                    db_dao.disconnect();
                } else {
                    System.err.println("Diretorio nao possui arquivos.");
                }
            } else {
                System.err.println("Ainda existem arquivos a serem processados no BD.");
            }
        }
        db_dao.disconnect();
    }

    /**
     * <b>@author Renan Fucci</b><br>
     * <b>Objeto map<b><br>
     * Objeto para contar a quantidade de arquivo por extensao.
     */
    private Map<String, Integer> map = new HashMap<>();

    /**
     * <b>@author Renan Fucci</b><br>
     * <b>Metodo criarListaArqs</b><br>
     * Metodo para criar uma lista de arquivos a partir de um diretorio.
     *
     * @param dir diretorio dos arquivos.
     * @param listaArq lista onde serao armazenados os arquivos.
     */
    private void criarListaArqs(String dir, ArrayList<File> listaArq) {
        criarListaArqs(new File(dir), listaArq);

    }

    /**
     * <b>@author Renan Fucci<br></b>
     * <b>Metodo criarListaArqs</b><br>
     * Metodo para criar uma lista de arquivos a partir de um diretorio.
     *
     * @param folder diretorio dos arquivos.
     * @param listaArq lista onde serao armazenados os arquivos.
     */
    private void criarListaArqs(final File folder, ArrayList<File> listaArq) {
        if (FileUtils.sizeOf(folder) > 0) {
            for (File fileEntry : folder.listFiles()) {
                if (fileEntry.isDirectory()) {
                    if (!fileEntry.getName().equalsIgnoreCase(".dropbox.cache")) {
                        criarListaArqs(fileEntry, listaArq);
                    }
                } else if (FileUtils.sizeOf(fileEntry) > 0 && fileIsComplete(fileEntry)) {
                    listaArq.add(fileEntry);
                }
            }
        }
    }

    /**
     * <b>@author Renan Fucci<br></b><br>
     * <b>Metodo inserirListaArqs_noBD</b><br>
     * Metodo para inserir num BD uma lista de arquivos, criado no metodo
     * {@link #criarListaArqs(String, ArrayList)}.<br>
     *
     * @param listaArq lista de arquivo a ser inserida no BD;
     * @param db_dao objeto de conexao com o banco.
     */
    private void inserirListaArqs_noBD(ArrayList<File> listaArq, DB_DAO db_dao) {
        //System.out.println("inserirListaArqs_noBD( arraylist<file>, db_dao)");
        if (!db_dao.isConnected()) {
            prepareDAO_BD(db_dao);
        }
        inserirListaArqs_noBD(db_dao, listaArq);
    }

    /**
     * <b>@author Renan Fucci<br></b><br>
     * <b>Metodo prepareDAO_BD</b><br>
     * Metodo para preparar a classe de controle de objeto de conexao do banco
     * de dados.
     *
     * @param db_dao objeto de conexao com o banco.
     * @return boolean se a classe esta preparada ou nao.
     */
    private boolean prepareDAO_BD(DB_DAO db_dao) {
        if (db_dao.connect()) {
            if (db_dao.createStmt_Insert() && db_dao.createStmt_Update()) {
                return true;
            } else {
                System.err.println("Nao conseguiu criar requisicao ao banco");
            }
        } else {
            System.err.println("Nao conseguiu conexao com banco");
        }
        return false;
    }

    /**
     * <b>@author Renan Fucci<br></b>
     * <b>Metodo inserirListaArqs_noBD</b><br>
     * Metodo para inserir num BD uma lista de arquivos, criado no metodo
     * {@link #criarListaArqs(String, ArrayList)}.<br>
     *
     * @param db_dao objeto de conexao com o banco.
     * @param listaArq lista de arquivo a ser inserida no BD;
     */
    private void inserirListaArqs_noBD(DB_DAO db_dao, ArrayList<File> listaArq) {
        //System.out.println("inserirListaArqs_noBD(db_dao, arraylist<file>)");
        Iterator<File> iter = listaArq.iterator();

        while (iter.hasNext()) {
            File fileEntry = iter.next();
            String arq_ext = FilenameUtils.getExtension(fileEntry.getName());//extensao do arquivo
            int aux = map.containsKey(arq_ext) ? map.get(arq_ext) : 0;
            map.put(arq_ext, aux + 1);
            System.out.println("fl:\t" + fileEntry.getName());
            if (arq_ext.equalsIgnoreCase("rar")) {
                //System.out.println("if rar");
                String dirEntrada = fileEntry.getParent() + File.separator;
                String nomeArq_Extens = fileEntry.getName();
                junRAR(dirEntrada, nomeArq_Extens, arq_ext, 0, db_dao);
            } else if (arq_ext.equalsIgnoreCase("zip")) {
                //System.out.println("if zip");
                String dirEntrada = fileEntry.getParent() + File.separator;
                String nomeArq_Extens = fileEntry.getName();
                unZIP(dirEntrada, nomeArq_Extens, arq_ext, 0, db_dao);
            } else if (arq_ext.equalsIgnoreCase("jpg")) {
                //System.out.println("if jpg");
                db_dao.executeStmt_Insert(fileEntry.getPath(), arq_ext, "No", 0);
            } else if (arq_ext.equalsIgnoreCase("bmp") || arq_ext.equalsIgnoreCase("png")
                    || arq_ext.equalsIgnoreCase("gif") || arq_ext.equalsIgnoreCase("jpeg")) {
                //System.out.println("if imgs");
                BufferedImage bufferedImage;
                BufferedImage newBufferedImage;
                String strNewDir;
                bufferedImage = readImage(fileEntry.getAbsolutePath());
                newBufferedImage = toJPG(bufferedImage);
                strNewDir = fileEntry.getParent()
                        + fileEntry.getPath().substring(
                                fileEntry.getPath().lastIndexOf(File.separator),
                                (fileEntry.getPath().length() - ((arq_ext.length() + 1))))
                        + ".jpg";
                if (new File(strNewDir).exists()) {
                    strNewDir = colocarPrefixoEM(fileEntry);
                    File newFfl = new File(strNewDir);
                }
                if (writeImage(newBufferedImage, strNewDir)) {
                    int id_inserted = db_dao.executeStmt_Insert(fileEntry.getPath(), arq_ext, "Success", 0);
                    db_dao.executeStmt_Insert(strNewDir, "jpg", "No", id_inserted);
                } else {
                    db_dao.executeStmt_Insert(fileEntry.getPath(), arq_ext, "Fail", 0);
                }
            } else if (arq_ext.equalsIgnoreCase("pdf")) {
                String dirEntrada = (fileEntry.getParent()) + File.separator;
                String nomeArq_Extens = fileEntry.getName();
                pdfToImgs(dirEntrada, nomeArq_Extens, arq_ext, 0, db_dao);
            } else if (arq_ext.equalsIgnoreCase("dropbox") || arq_ext.equalsIgnoreCase("ftpquota")) {

            } else if (arq_ext.equalsIgnoreCase("ppt") || arq_ext.equalsIgnoreCase("pptx")) {
                db_dao.executeStmt_Insert(fileEntry.getPath(), arq_ext, "No", 0);

            } else {
                db_dao.executeStmt_Insert(fileEntry.getPath(), arq_ext, "Fail", 0);

            }
            iter.remove();
        }
    }

    /**
     * <b>@author Renan Fucci<br></b>
     * <b>Metodo inspetorArqs_Parent</b><br>
     * Metodo para fazer a inspecao da extensao dos arquivos e a insercao no
     * BD.<br>
     * Se a extensao do arquivo for "zip" ou "rar", sera feita a descompactacao
     * utilizando o metodo
     * {@link #junRAR(String, String, String, int, DB_DAO)}.<br>
     * Se for "jpg", só insere no BD.<br>
     * Se for uma extensao de imagem ("bmp", "png", "gif", "jpeg"), é feita a
     * conversao pra jpg antes da insercao no BD.<br>
     * Se for "pdf", faz a conversao para imagem com o metodo
     * {@link #pdfToImgs(String, String, String, int, DB_DAO)}.<br>
     * Caso contrario, nao eh feito nenhum tratamento antes da insercao no
     * BD.<br>
     *
     * @param fileEntry arquivo de entrada;
     * @param db_dao objeto de conexao com BD;
     * @param id_inserted id do pai deste arquivo no BD, se tiver. Se não, 0;
     * @param listaArq lista de arquivo a ser inserida no BD;
     */
    private void inspetorArqs_Parent(File fileEntry, DB_DAO db_dao, int id_inserted) {
        //System.out.println("inspetorArqs_Parent");
        String arq_ext = FilenameUtils.getExtension(fileEntry.getName());//extensao do arquivo
        int aux = map.containsKey(arq_ext) ? map.get(arq_ext) : 0;
        map.put(arq_ext, aux + 1);
        if (arq_ext.equalsIgnoreCase("rar")) {
            String dirEntrada = fileEntry.getParent() + File.separator;
            String nomeArq_Extens = fileEntry.getName();
            junRAR(dirEntrada, nomeArq_Extens, arq_ext, id_inserted, db_dao);
        } else if (arq_ext.equalsIgnoreCase("zip")) {
            String dirEntrada = fileEntry.getParent() + File.separator;
            String nomeArq_Extens = fileEntry.getName();
            unZIP(dirEntrada, nomeArq_Extens, arq_ext, id_inserted, db_dao);
        } else if (arq_ext.equalsIgnoreCase("jpg")) {
            db_dao.executeStmt_Insert(fileEntry.getPath(), arq_ext, "No", id_inserted);

        } else if (arq_ext.equalsIgnoreCase("bmp") || arq_ext.equalsIgnoreCase("png") || arq_ext.equalsIgnoreCase("gif") || arq_ext.equalsIgnoreCase("jpeg")) {
            BufferedImage bufferedImage;//Buffer da img original
            BufferedImage newBufferedImage;//Buffer da img com o aplha
            String strNewDir; //Novo dir 
            bufferedImage = readImage(fileEntry.getAbsolutePath());
            newBufferedImage = toJPG(bufferedImage);
            strNewDir = fileEntry.getParent()
                    + fileEntry.getPath().substring(
                            fileEntry.getPath().lastIndexOf(File.separator),
                            (fileEntry.getPath().length() - ((arq_ext.length() + 1))))
                    + ".jpg";
            if (new File(strNewDir).exists()) {
                strNewDir = colocarPrefixoEM(fileEntry);
                File newFfl = new File(strNewDir);
            }
            if (writeImage(newBufferedImage, strNewDir)) {
                int id_parent = db_dao.executeStmt_Insert(fileEntry.getPath(), arq_ext, "Success", id_inserted);
                db_dao.executeStmt_Insert(strNewDir, "jpg", "No", id_parent);
            } else {
                db_dao.executeStmt_Insert(fileEntry.getPath(), arq_ext, "Fail", id_inserted);
            }
        } else if (arq_ext.equalsIgnoreCase("pdf")) {
            String dirEntrada = (fileEntry.getParent()) + File.separator;
            String nomeArq_Extens = fileEntry.getName();
            pdfToImgs(dirEntrada, nomeArq_Extens, arq_ext, id_inserted, db_dao);

        } else if (arq_ext.equalsIgnoreCase("dropbox") || arq_ext.equalsIgnoreCase("ftpquota")) {

        } else if (arq_ext.equalsIgnoreCase("ppt") || arq_ext.equalsIgnoreCase("pptx")) {
            db_dao.executeStmt_Insert(fileEntry.getPath(), arq_ext, "No", id_inserted);
        } else {
            db_dao.executeStmt_Insert(fileEntry.getPath(), arq_ext, "Fail", id_inserted);
        }
    }

    /**
     * <b>@author Renan Fucci<br></b>
     * <b>Metodo fileIsComplete<br></b>
     * Metodo para testar se o arquivo esta completamente escrito.<br>
     *
     * @param file arquivo que sera testado.
     * @return resulato booleano se o arquivo esta completo.
     */
    private boolean fileIsComplete(File file) {
        try (RandomAccessFile stream = new RandomAccessFile(file, "rw");) {
            return true;
        } catch (Exception ex) {
            System.err.println("Arquivo " + file.getName() + " nao esta completamente escrito.");
            return false;
        }
    }

    /**
     * <b>@author Renan Fucci</b><br>
     * <b>Metodo unZIP.</b><br>
     * Metodo para descompactar arquivos ZIP somente com java.
     *
     * @param dirEntrada string que deve conter o somente o diretorio onde esta
     * localizado o arquivo.
     * @param nomeArq_Extens nome do arquivo com a extensao.
     * @param type_file extensao do arquivo;
     * @param id_parent id do pai deste arquivo no BD, se tiver. Se não, 0;
     * @param db_dao objeto de conexao com BD.
     * @return resultado booleano da descompactacao.
     */
    private boolean unZIP(String dirEntrada, String nomeArq_Extens, String type_file, int id_parent, DB_DAO db_dao) {
        try {
            int id_inserted_ZIP = -1; //id do documento com extensao de compactacao no BD
            String filename = dirEntrada + nomeArq_Extens;//String com diretorio do nome do arquivo de entrada
            File fl = new File(filename);
            String converted_file = "Fail";
            id_inserted_ZIP = db_dao.executeStmt_Insert(fl.getPath(), type_file, converted_file, id_parent);

            Charset charset = Charset.forName("IBM00858");
            try (ZipInputStream zipIn = new ZipInputStream(new FileInputStream(fl), charset);) {
                ZipEntry entry = zipIn.getNextEntry();

                File newDir = createNewDir(fl);
                if (!newDir.exists()) {
                    newDir.mkdir();
                }
                while (entry != null) {
                    String filePath = newDir.getAbsolutePath() + File.separator + entry.getName();
                    String strNewDir = "";
                    if (!entry.isDirectory()) {
                        File pastas = new File(filePath);
                        if (pastas.exists()) {
                            strNewDir = colocarPrefixoEM(pastas);
                            pastas = new File(strNewDir);
                        }
                        pastas.getParentFile().mkdirs();
                        extractFile(zipIn, pastas.getAbsolutePath());
                        inspetorArqs_Parent(pastas, db_dao, id_inserted_ZIP);
                    } else {
                        File dir = new File(filePath);
                        dir.mkdirs();
                    }
                    zipIn.closeEntry();
                    entry = zipIn.getNextEntry();
                }
                db_dao.executeStmt_Update(id_inserted_ZIP, "Success");
                return true;
            } catch (Exception ex) {
                System.err.println(ex.getLocalizedMessage());
            }
        } catch (Exception ex) {
            System.err.println(ex.getLocalizedMessage());
        }
        return false;
    }
    private static final int BUFFER_SIZE = 4096;

    private void extractFile(ZipInputStream zipIn, String filePath) throws Exception {
        try (BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(filePath));) {
            byte[] bytesIn = new byte[BUFFER_SIZE];
            int read = 0;
            while ((read = zipIn.read(bytesIn)) != -1) {
                bos.write(bytesIn, 0, read);
            }
        } catch (Exception ex) {
            System.err.println(ex.getLocalizedMessage());
            throw ex;
        }
    }

    /**
     * <b>@author Renan Fucci</b><br>
     * <b>Metodo junRAR.</b><br>
     * Metodo para descompactar arquivos RAR somente com java.
     *
     * @param dirEntrada string que deve conter o somente o diretorio onde esta
     * localizado o arquivo.
     * @param nomeArq_Extens nome do arquivo com a extensao.
     * @param type_file extensao do arquivo;
     * @param id_parent id do pai deste arquivo no BD, se tiver. Se não, 0;
     * @param db_dao objeto de conexao com BD.
     * @return resultado booleano da descompactacao.
     */
    private boolean junRAR(String dirEntrada, String nomeArq_Extens, String type_file, int id_parent, DB_DAO db_dao) {
        String filename = dirEntrada + nomeArq_Extens;//String com diretorio do nome do arquivo de entrada
        File fl = new File(filename);//Arquivo de entrada
        String strNewDir = "";
        int id_inserted_RAR = -1; //id do documento com extensao de compactacao no BD
        String converted_file = "Fail";
        id_inserted_RAR = db_dao.executeStmt_Insert(fl.getPath(), type_file, converted_file, id_parent);
        try (Archive a = new Archive(new FileVolumeManager(fl));) {
            if (fl.exists()) {
                FileHeader fh = a.nextFileHeader();
//                a.getFileHeaders().stream().forEach((a_temp)->{
//                    System.out.println(a_temp.getFileCRC());
//                    System.out.println(a_temp.getFullUnpackSize());
//                });
                File newDir = createNewDir(fl);
                while (fh != null) {
                    if (SystemUtils.IS_OS_LINUX) {
                        fh.setFileName(fh.getFileNameString().replaceAll("\\\\", File.separator));
                    }
                    File out = new File(newDir.getAbsoluteFile() + File.separator
                            + fh.getFileNameString().trim());
                    if (fh.isDirectory()) {
                        out.mkdirs();
                    } else {
                        File pastas = new File(out.getParent() + File.separator);
                        pastas.mkdirs();
                        if (out.exists()) {
                            strNewDir = colocarPrefixoEM(out);
                            out = new File(strNewDir);
                        }
                        try (FileOutputStream os = new FileOutputStream(out);) {
                            a.extractFile(fh, os);
                        }
                        inspetorArqs_Parent(out, db_dao, id_inserted_RAR);
                    }
                    fh = a.nextFileHeader();
                }
                db_dao.executeStmt_Update(id_inserted_RAR, "Success");
                return true;
            } else {
                System.err.println("\nNao foi possivel acessar o arquivo: " + fl.getPath());
            }
        } catch (Exception ex) {
            System.err.println(ex.getLocalizedMessage());
        }
        return false;
    }

    /**
     * <b>@author Renan Fucci</b><br>
     * <b>Metodo pdfToImgs</b><br>
     * Metodo para converter um pdf em arquivos de imgs.
     *
     * @param dirEntrada string que deve conter o somente o diretorio onde esta
     * localizado o arquivo.
     * @param nomeArq_Extens nome do arquivo com a extensao.
     * @param type_file extensao do arquivo;
     * @param id_parent id do pai deste arquivo no BD, se tiver. Se não, 0;
     * @param db_dao objeto de conexao com BD.
     * @return resultado booleano da conversao.
     */
    private void pdfToImgs(String dirEntrada, String nomeArq_Extens, String type_file, int id_parent, DB_DAO db_dao) {
        ImageIO.scanForPlugins();
        int id_inserted_PDF = -1; //id do documento com extensao de compactacao no BD
        File file = new File(dirEntrada + nomeArq_Extens);
        String converted_file = "Fail";
        id_inserted_PDF = db_dao.executeStmt_Insert(file.getPath(), type_file, converted_file, id_parent);
        try (PDDocument document = PDDocument.load(file);) {
            PDFRenderer pdfRenderer = new PDFRenderer(document);
            File newDir = createNewDir(file);
            for (int page = 0; page < document.getNumberOfPages(); ++page) {
                BufferedImage bim = pdfRenderer.renderImageWithDPI(page, 200, ImageType.RGB);
                String prefixoSaida = newDir.getPath() + File.separator + file.getName().substring(0, file.getName().lastIndexOf(".")).replaceAll("\\s", "\\-") + "_" + page + ".jpg";
                File yourImageFile = new File(prefixoSaida);
                if (yourImageFile.exists()) {
                    prefixoSaida = colocarPrefixoEM(yourImageFile);
                    yourImageFile = new File(prefixoSaida);
                }
                ImageIO.scanForPlugins();
                ImageIO.write(bim, "JPG", new File(prefixoSaida));
                ImageIOUtil.writeImage(bim, prefixoSaida, 200);
                inspetorArqs_Parent(yourImageFile, db_dao, id_inserted_PDF);
            }
            db_dao.executeStmt_Update(id_inserted_PDF, "Success");

        } catch (Exception ex) {
            System.err.println(ex.getLocalizedMessage());
        }
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private BufferedImage toBufferedImage(RenderedImage img) {
        try {
            if (img instanceof BufferedImage) {
                return (BufferedImage) img;
            }
            ColorModel cm = img.getColorModel();
            int width = img.getWidth();
            int height = img.getHeight();
            WritableRaster raster = cm.createCompatibleWritableRaster(width, height);
            boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
            Hashtable<String, Object> properties = new Hashtable<>();
            String[] keys = img.getPropertyNames();
            if (keys != null) {
                for (int i = 0; i < keys.length; i++) {
                    properties.put(keys[i], img.getProperty(keys[i]));
                }
            }
            BufferedImage result = new BufferedImage(cm, raster, isAlphaPremultiplied, properties);
            img.copyData(raster);
            return result;
        } catch (Exception ex) {
            System.err.println(ex.getLocalizedMessage());
        }
        return null;
    }

    /**
     * <b>@author Renan Fucci</b><br>
     * <b>Metodo criarNewDir</b><br>
     * Metodo para criar novo diretório com nome da pasta com o mesmo nome do
     * arquivo, e se houver espacos nesse nome, subistitui por "-" (sinal de
     * menos).
     *
     * @param file arquivo que sera usado para criar a pasta.
     * @return arquivo onde foi criada a pasta.
     */
    private File createNewDir(File file) throws Exception {
        try {
            File newDir = new File(file.getParent() + File.separator + file.getName().substring(0, file.getName().lastIndexOf(".")).replaceAll("\\s", "\\-") + File.separator);
            newDir.mkdirs();
            return newDir;
        } catch (Exception ex) {
            System.err.println(ex.getLocalizedMessage());
            throw new Exception(ex);
        }
    }

    private String colocarPrefixoEM(File fileEntry) {
        return fileEntry.getPath().substring(0,
                (fileEntry.getPath().lastIndexOf(File.separator)))
                + File.separator
                + "EM-"
                + fileEntry.getPath().substring(
                        (fileEntry.getPath().lastIndexOf(File.separator)) + 1,
                        ((fileEntry.getPath().length())));
    }

    /**
     * <b>@author Renan Fucci</b><br>
     * <b>Metodo toJPG</b><br>
     * Metodo para converter qualquer tipo de extensao de imagem para jpg.
     *
     * @param bufferedImage imagem a ser convertida.
     * @return imagem no padrao jpg.
     */
    private BufferedImage toJPG(BufferedImage bufferedImage) {
        try {
            BufferedImage newBufferedImage;//Buffer da img com o aplha
            newBufferedImage = new BufferedImage(bufferedImage.getWidth(),
                    bufferedImage.getHeight(), BufferedImage.TYPE_INT_RGB);
            newBufferedImage.createGraphics().drawImage(bufferedImage, 0, 0, Color.WHITE, null);
            return newBufferedImage;
        } catch (Exception ex) {
            System.err.println(ex.getLocalizedMessage());
        }
        return null;
    }

    /**
     * <b>@author Renan Fucci</b><br>
     * <b>Metodo readImage</b><br>
     * Metodo para ler uma imagem em um diretorio e transforma-la num
     * {@link BufferedImage}.
     *
     * @param dirCompletoComExtens diretorio completo com a extensao do arquivo.
     * @return um {@link BufferedImage}.
     *
     */
    private BufferedImage readImage(String dirCompletoComExtens) {
        try {
            return ImageIO.read(new File(dirCompletoComExtens));
        } catch (IOException ex) {
            System.err.println(ex.getLocalizedMessage()
                    + "\nCertifique-se que o diretorio esta correto e com a mesma extensao do arquivo.");
        } catch (Exception ex) {
            System.err.println(ex.getLocalizedMessage());
        }
        return null;
    }

    /**
     * <b>@author Renan Fucci</b><br>
     * <b>Metodo writeImage</b><br>
     * Metodo para gravar uma imagem em um diretorio.
     *
     * @param dirCompletoComExtens diretorio completo com a extensao do arquivo.
     * @return boolean com o resultado da gravacao do arquivo.
     *
     */
    private boolean writeImage(BufferedImage image, String dirCompletoComExtens) {
        try {
            return ImageIO.write(image, FilenameUtils.getExtension(dirCompletoComExtens), new File(dirCompletoComExtens));
        } catch (IOException ex) {
            System.err.println(ex.getLocalizedMessage()
                    + "\nCertifique-se que o diretorio esta correto e com a uma extensao de arquivo.");
        } catch (Exception ex) {
            System.err.println(ex.getLocalizedMessage());
        }
        return false;
    }

    static {//hack pra mudar o default charset da JVM
        try {
            System.setProperty("file.encoding", "IBM00858");
            Field charset = Charset.class
                    .getDeclaredField("defaultCharset");
            charset.setAccessible(true);
            charset.set(null, null);
        } catch (Exception ex) {
            System.err.println(ex.getLocalizedMessage());
        }
    }
}
