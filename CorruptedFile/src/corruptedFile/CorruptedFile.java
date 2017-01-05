package corruptedFile;

import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import org.apache.commons.io.FilenameUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;

/**
 * Método para testar se os arquivos na extensão de imagem (JPG, JPEG, PNG, BMP)
 * e PDF estão corrompidos ou não. Os arquivos que não pertencerem as essas
 * extensão não serao testados e retornar o resultado de não corrompido.
 *
 * Como usar:
 *          CorruptedFile corruptedFile = new CorruptedFile(url_file);
 *          System.out.println("\ntypeFile:\t" + corruptedFile.getTypeFile());
 *          System.out.println("\ncorrupted:\t" + corruptedFile.isFileCorrupted());
 * 
 * 
 * @author Renan Fucci
 * <a href="malito:renanfucci@hotmail.com">(renanfucci@hotmail.com)</a>
 */
public class CorruptedFile {

    private boolean fileCorrupted;
    private final String typeFile;
    private final String[] relevantExtns = {"JPG", "JPEG", "PNG", "BMP", "PDF"};
    private static final Logger LOG = Logger.getLogger(CorruptedFile.class.getName());

    public CorruptedFile(String url_file) {
        this.typeFile = FilenameUtils.getExtension(Paths.get(url_file).getFileName().toString());
        testCorruptedFile(Paths.get(url_file));
    }

    /**
     * @author Renan Fucci
     * <a href="malito:renanfucci@hotmail.com">(renanfucci@hotmail.com)</a><br>
     * <strong>Método testCorruptedFile</strong>
     * Método para testar se o arquivo está corrompido ou não;
     * @param path Path com o arquivo;
     */
    private void testCorruptedFile(Path path) {
        try {
            if ((!fileIsRelevant())) {
                fileCorrupted = false;
            } else {
                fileCorrupted = (Files.isReadable(path)
                        && Files.isExecutable(path) && (!Files.isSymbolicLink(path))
                        && fileIsComplete(path) && (Files.size(path) > 0)
                        && fileCanBeOpened(path))
                        ? (false) : (true);
            }
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, ex.getLocalizedMessage());
        }
    }

    /**
     * @author Renan Fucci
     * <a href="malito:renanfucci@hotmail.com">(renanfucci@hotmail.com)</a><br>
     * <strong>Método fileCanBeOpened</strong>
     * Método para testar se o arquivo pode ser aberto ou não.
     * @param path Path com o arquivo;
     * @return boolean se o arquivo pode ser aberto ou não.
     */
    private boolean fileCanBeOpened(Path path) {
        try {
            if (typeFile.equalsIgnoreCase("PDF")) {
                try (PDDocument document = PDDocument.load(path.toFile());) {
                    PDFRenderer pdfRenderer = new PDFRenderer(document);
                    if (document.getNumberOfPages() > 0) {
                        for (int i = 0; i < document.getNumberOfPages(); i++) {
                            BufferedImage buff = pdfRenderer.renderImage(i);
                            buff.flush();
                        }
                        return true;
                    }
                }
            } else {
                InputStream is = Files.newInputStream(path);
                try {
                    final ImageInputStream imageInputStream = ImageIO
                            .createImageInputStream(is);
                    final Iterator<ImageReader> imageReaders = ImageIO
                            .getImageReaders(imageInputStream);
                    if (!imageReaders.hasNext()) {
                        return false;
                    }
                    final ImageReader imageReader = imageReaders.next();
                    imageReader.setInput(imageInputStream);
                    final BufferedImage image = imageReader.read(0);
                    if (image == null) {
                        return false;
                    }
                    image.flush();
                    if (imageReader.getFormatName().equals("JPEG")) {
                        imageInputStream.seek(imageInputStream.getStreamPosition() - 2);
                        final byte[] lastTwoBytes = new byte[2];
                        imageInputStream.read(lastTwoBytes);
                        if (lastTwoBytes[0] != (byte) 0xff || lastTwoBytes[1] != (byte) 0xd9) {
                        }
                    }
                    return true;
                } catch (Exception ex) {
                }
            }
        } catch (Exception ex) {
        }
        return false;
    }

    /**
     * @author Renan Fucci
     * <a href="malito:renanfucci@hotmail.com">(renanfucci@hotmail.com)</a><br>
     * <strong>Método fileIsRelevant</strong>
     * Método para testar se o arquivo é relevante ou não, isto é, pertence às
     * extensões JPG, JPEG, PNG, BMP e PDF.
     * @param path Path com o arquivo;
     * @return boolean se o arquivo é relevante ou não.
     */
    private boolean fileIsRelevant() {
        for (String relevantExtn : relevantExtns) {
            if (typeFile.equalsIgnoreCase(relevantExtn)) {
                return true;
            }
        }
        return false;
    }

    /**
     * @author Renan Fucci
     * <a href="malito:renanfucci@hotmail.com">(renanfucci@hotmail.com)</a><br>
     * <strong>Método fileIsComplete</strong>
     * Metodo para testar se o arquivo esta completamente escrito.<br>
     *
     * @param file arquivo que sera testado.
     * @return resulato booleano se o arquivo esta completo.
     */
    private boolean fileIsComplete(Path path) {
        try (RandomAccessFile stream = new RandomAccessFile(path.toFile(), "rw");) {
            return true;
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, ex.getLocalizedMessage());
        }
        return false;

    }

    /*---------------- GETTERS ----------------*/
    public boolean isFileCorrupted() {
        return fileCorrupted;
    }

    public String getTypeFile() {
        return typeFile;
    }

}
