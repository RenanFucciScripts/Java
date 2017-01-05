package metodos;

import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.Result;
import com.google.zxing.ResultPoint;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;

public class QRCode {

	public static void main(String[] args){

		String filePath = "C:/Users/Renan Fucci/Dropbox/Estante Magica_Renan_Fucci/Pasta_Renan_Fucci/2016-06-10/capa.jpg";
		Result result = readQRCode(filePath);
		if(result!=null){
			System.out.println(result.getText());
			ResultPoint[] resultPoints = result.getResultPoints();
			for (ResultPoint rsPoint : resultPoints) {
				System.out.println("x:\t"+rsPoint.getX()+"\ty:\t"+rsPoint.getY());
			}
		}
		else{
			System.err.println("Não encontrou nenhum QRCode");
		}
	}


	public static Result readQRCode(String filePath) {
		try{
			
			Map<DecodeHintType, Object> hintMap = new HashMap<DecodeHintType, Object>();
			hintMap.put(DecodeHintType.TRY_HARDER, Boolean.TRUE);
			hintMap.put(DecodeHintType.PURE_BARCODE, Boolean.FALSE);
			hintMap.put(DecodeHintType.POSSIBLE_FORMATS, BarcodeFormat.QR_CODE);
			BinaryBitmap binaryBitmap = new BinaryBitmap(new HybridBinarizer(
					new BufferedImageLuminanceSource(							ImageIO.read(new FileInputStream(filePath)))));
			Result qrCodeResult = new MultiFormatReader().decode(binaryBitmap,hintMap);
			return qrCodeResult;
		}catch(Exception ex){
			System.err.println(ex.getLocalizedMessage());
			ex.printStackTrace();
		}
		return null;
	}
	
	
}