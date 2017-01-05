package qrcode_zxing;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Map;

import javax.imageio.ImageIO;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.Result;
import com.google.zxing.ResultPoint;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;

public class QRCodeDetector_RF_Deprecated{

	private ResultPoint[] points;
	private String str_result;

	public static void main(String[] args){
		try{
			String filePath = "C:/Users/Renan Fucci/Dropbox/Estante Magica_Renan_Fucci/Pasta_Renan_Fucci/2016-06-10/capa";
			QRCodeDetector_RF_Deprecated qrcode = new QRCodeDetector_RF_Deprecated();

			File tmpFile = new File(filePath+".jpg");
			boolean result=qrcode.decode(ImageIO.read(tmpFile));

			if(result){
				BufferedImage image2 =ImageIO.read(tmpFile);
				System.out.println(qrcode.str_result);
				for (ResultPoint pt : qrcode.points) {
					image2 = qrcode.paintPoint( (int) pt.getX(), (int) pt.getY(), image2);
				}
				ImageIO.write(image2, "jpg", new File(filePath+"_paint.jpg"));
			}
			else if(result==false){
				System.out.println("Nao Achou nada");
			}

		} catch (Exception ex){
			System.err.println(ex.getMessage());
			ex.printStackTrace();
		}
	}

	/**
	 * @return the points
	 */
	public ResultPoint[] getPoints() {
		return points;
	}

	/**
	 * @return the str_result
	 */
	public String getStr_result() {
		return str_result;
	}

	public BufferedImage paintPoint(int x, int y, BufferedImage image) {
		int[] janela = new int[17];
		for (int k = -(int) Math.floor(janela.length/2); k <= (int) Math.floor(janela.length/2); k++) {
			for (int l = -(int) Math.floor(janela.length/2); l <= (int) Math.floor(janela.length/2); l++) {
				image.setRGB(l+x, k+y, (new Color(6, 166, 226)).getRGB());
			}
		}
		return image;
	}

	public boolean decode(BufferedImage img) {
		Map<DecodeHintType,Object> hintMap = new EnumMap<DecodeHintType, Object>(DecodeHintType.class);
		hintMap.put(DecodeHintType.TRY_HARDER, Boolean.TRUE);
		hintMap.put(DecodeHintType.PURE_BARCODE, Boolean.FALSE);
		hintMap.put(DecodeHintType.POSSIBLE_FORMATS, EnumSet.of(BarcodeFormat.QR_CODE));
		BufferedImage image = img;
		
		if (image == null)
			throw new IllegalArgumentException("Could not decode image.");
		LuminanceSource lumSRC = new BufferedImageLuminanceSource(image);
		BinaryBitmap binImage = new BinaryBitmap(new HybridBinarizer(lumSRC));
		MultiFormatReader reader = new MultiFormatReader();
		Result result;
		try{  
			result = reader.decode(binImage, hintMap);
			points = result.getResultPoints();
			str_result = String.valueOf(result.getText());
		} 
		catch (Exception ex){
			System.err.println(ex.getMessage());
			ex.printStackTrace();
			return false;
		}
		return true;
	}
}