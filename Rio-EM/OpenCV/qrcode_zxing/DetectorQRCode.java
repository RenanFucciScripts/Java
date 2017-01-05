package qrcode_zxing;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.sql.Connection;
import java.util.EnumMap;

import javax.imageio.ImageIO;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.ChecksumException;
import com.google.zxing.DecodeHintType;
import com.google.zxing.FormatException;
import com.google.zxing.LuminanceSource;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.ResultPoint;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.DetectorResult;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.multi.qrcode.QRCodeMultiReader;
import com.google.zxing.multi.qrcode.detector.MultiDetector;

import javafx.util.converter.ByteStringConverter;


/**
 * @author Renan Fucci
 * @deprecated
 */
public class DetectorQRCode {

	public static void main(String[] args) throws NotFoundException, ChecksumException, FormatException {
		try {

			String filePath = "C:/Users/Renan Fucci/Dropbox/Estante Magica_Renan_Fucci/Pasta_Renan_Fucci/2016-06-10/capa";
			File f1 =new File(filePath+".jpg");

			DetectorQRCode detectorQRCode = new DetectorQRCode();
			BufferedImage imagem;
			imagem = ImageIO.read(f1);

			Connection conn = null;
			DetectorResult detectorResult = detectorQRCode.leQRCode(conn, imagem);

			ResultPoint[] resultPoints = detectorResult.getPoints();
			BufferedImage image2 = detectorQRCode.copiaImagem(imagem);

			for (ResultPoint rspoint : resultPoints) {
				System.out.println((int) rspoint.getX()+"\t"+ (int) rspoint.getY() );
				image2 = detectorQRCode.paintPoint( (int) rspoint.getX(), (int) rspoint.getY(), image2);
			}
			ImageIO.write(image2, "jpg", new File(filePath+"_pint.jpg"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * @deprecated
	 * */
	protected BufferedImage paintPoint(int x, int y, BufferedImage image) {
		int[] janela = new int[17];
		for (int k = -(int) Math.floor(janela.length/2); k <= (int) Math.floor(janela.length/2); k++) {
			for (int l = -(int) Math.floor(janela.length/2); l <= (int) Math.floor(janela.length/2); l++) {
				image.setRGB(l+x, k+y, (new Color(6, 166, 226)).getRGB());
			}
		}
		return image;
	}


	/**

	 * @deprecated
	
	 * Método que recebe uma imagem, verifica se existe pelo menos UM QRCode
	 * nela e o retorna. Caso nenhum QRCode seja encontrado retorna NULL
	 *
	 * @param conn : objeto com a conexão ao banco de dados
	 * @param imagem : imagem de entrada
	 * @return
	 * @throws IOException 
	 * @throws NotFoundException 
	 * @throws FormatException 
	 * @throws ChecksumException 
	 */
	@SuppressWarnings({ "unused", "unchecked", "rawtypes" })
	public DetectorResult leQRCode(Connection conn, BufferedImage imagem) throws IOException, NotFoundException, ChecksumException, FormatException {
		BufferedImage imagemAux; // buffer usado para auxiliar na leitura do QRCode
		BitMatrix bitMatrixData, // objeto matriz de dados lidos do QRCode
		bitMatrixData2Teste; // objeto para armazenar os dados lidos do QRCode temporariamente
		DetectorResult[] resultadoDecodificacao; // objeto que armazena o objeto QRCode
		MultiDetector multiDetect; // objeto usado para detectar QRCode em imagens com mais de um QRCode
		EnumMap<DecodeHintType, Object> hintMap; // objeto usado para mapear o QRCode a partir da imagem
		DetectorResult retorno; // objeto que armazena o resultado a ser retornado
		// tenta encontrar o QRCode sem aplicar o método de realce
		// inicializar as variáveis
		retorno = null;
		imagemAux = copiaImagem(imagem);
		// converte o bufferedImage para bitMatrixData
		bitMatrixData = emReadImage2BitMatrix(conn, imagem);
		// busca por um dos QRCodes
		multiDetect = new MultiDetector(bitMatrixData);
		hintMap = new EnumMap(DecodeHintType.class);
		hintMap.put(DecodeHintType.TRY_HARDER, Boolean.TRUE);
		hintMap.put(DecodeHintType.PURE_BARCODE, Boolean.FALSE);
		hintMap.put(DecodeHintType.POSSIBLE_FORMATS, BarcodeFormat.QR_CODE);
		// tenta encontrar os QRCodes na imagem sem realce
		resultadoDecodificacao = buscaQRCodes(multiDetect, hintMap);

		// se não conseguir encontrar o QRCode sem realce

		if (resultadoDecodificacao == null) {
			retorno = null;

		} else {
			// percorre o vetor de QRCodes para retornar um que seja diferente de nulo
			for (int numeroQRCodes = 0; numeroQRCodes < resultadoDecodificacao.length; numeroQRCodes++) {
				if (resultadoDecodificacao[numeroQRCodes] != null) {
					retorno = resultadoDecodificacao[numeroQRCodes];
				}
			}
			try{
				System.out.println("nao null 1");
				BinaryBitmap binImage = emReadImage2Binary(conn, imagem);
				System.out.println("nao null 2");
				QRCodeMultiReader reader = new QRCodeMultiReader();
				
				System.out.println("nao null 3");
				Result result = reader.decode(binImage, hintMap);
				System.out.println("nao null 4");

				if(result !=null){
					System.out.println("nao null 4");
					Charset charset =Charset.forName("ISO-8859");
					byte[] d = result.getText().getBytes(charset.name());
					String str_rs="";
					for (byte e : d) {
						ByteStringConverter byteStringConverter = new ByteStringConverter();
						str_rs+=byteStringConverter.toString(e);
					}
					System.out.println("rs:\t"+str_rs);
				}
			}catch (FormatException e) {
				System.err.println(e.getLocalizedMessage());
				e.printStackTrace();// TODO: handle exception

			}		
		}

		return retorno;
	}

	/**
	 * @deprecated
	 * */
	
	protected BufferedImage copiaImagem(BufferedImage imagem) {
		ColorModel cm = imagem.getColorModel();
		boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
		WritableRaster raster = imagem.copyData(null);
		return new BufferedImage(cm, raster, isAlphaPremultiplied, null);
	}

	/**
	 * Método que armazena um bufferedImage em um objeto BitMatrix
	 *
	 * @param imagem : imagem de entrada
	 * @return
	 * @deprecated
	 * */
	
	protected BinaryBitmap emReadImage2Binary(Connection conn, BufferedImage imagem) {
		BinaryBitmap binImage; // objeto que armazenará a imagem em uma matriz de bits
		LuminanceSource lumSource;
		try {
			lumSource = new BufferedImageLuminanceSource(imagem);
			binImage = new BinaryBitmap(new HybridBinarizer(lumSource));
		} catch (Exception ex) {
			// - Corrigir AQUI - this.mensagemLog.escreverMensagem(conn, "ERRO: O QRCode não foi decodificado.");
			return null;
		}
		return binImage;
	}


	/**
	 * @deprecated
	 * Método que armazena um bufferedImage em um objeto BitMatrix
	 *
	 * @param imagem : imagem de entrada
	 * @return
	 */
	protected BitMatrix emReadImage2BitMatrix(Connection conn, BufferedImage imagem) {
		BitMatrix bImage; // objeto que armazenará a imagem em uma matriz de bits
		LuminanceSource lumSource;
		try {
			lumSource = new BufferedImageLuminanceSource(imagem);
			bImage = new HybridBinarizer(lumSource).getBlackMatrix();
		} catch (NotFoundException ex) {
			// - Corrigir AQUI - this.mensagemLog.escreverMensagem(conn, "ERRO: O QRCode não foi decodificado.");
			return null;
		}
		return bImage;
	}


	/**
	 * @deprecated
	 * Método que detecta os QRCodes em uma imagem. Dá suporta para detectar
	 * multiplos QRCodes
	 *
	 * @param multiDetect : objeto com o detector a partir da matriz de bits da
	 * imagem
	 * @param hintMap : objeto com os atributos usados durante a detecção do
	 * QRCode
	 * @return
	 */
	protected DetectorResult[] buscaQRCodes(MultiDetector multiDetect, EnumMap<DecodeHintType, Object> hintMap) {
		DetectorResult[] resultado; // objeto que armazena o resultado do método, contendo os QRCodes identificados, ou nulo se não encontrar nada
		// inicializa as variáveis
		resultado = null;
		try {
			// procura os QRCodes
			resultado = multiDetect.detectMulti(hintMap);

		} catch (NotFoundException ex) {
			return null;
		}
		return resultado;
	}
}