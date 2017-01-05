/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.DecodeHintType;
import com.google.zxing.LuminanceSource;
import com.google.zxing.ResultPoint;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.DetectorResult;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.multi.qrcode.detector.MultiDetector;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.sql.Connection;
import java.util.EnumMap;

/**
 *
 * @author _
 * @deprecated 
 */
public class QRCodeDetector_AA_deprecated {

//    public ResultPoint[] points;
//
//    protected BufferedImage paintPoint(int x, int y, BufferedImage image) {
//        int[] janela = new int[21];
//        for (int k = -(int) Math.floor(janela.length / 2); k <= (int) Math.floor(janela.length / 2); k++) {
//            for (int l = -(int) Math.floor(janela.length / 2); l <= (int) Math.floor(janela.length / 2); l++) {
//                image.setRGB(l + x, k + y, (new Color(6, 166, 226)).getRGB());
//            }
//        }
//        return image;
//    }
//
//    public boolean decode(BufferedImage imagem) {
//        try {
//            DetectorResult detectorResult = leQRCode(null, imagem);
//            if (detectorResult.getPoints() != null) {
//                points = detectorResult.getPoints();
//                return true;
//            }
//        } catch (NullPointerException nullex) {
//            System.err.println("getPoints() == null");
//        } catch (Exception ex) {
//            System.err.println(ex.getMessage());
//            ex.printStackTrace();
//        }
//        return false;
//    }
//
//    /**
//     * M�todo que recebe uma imagem, verifica se existe pelo menos UM QRCode
//     * nela e o retorna. Caso nenhum QRCode seja encontrado retorna NULL
//     *
//     * @param conn : objeto com a conex�o ao banco de dados
//     * @param imagem : imagem de entrada
//     * @return
//     */
//    @SuppressWarnings({"unused", "unchecked", "rawtypes"})
//    public DetectorResult leQRCode(Connection conn, BufferedImage imagem) {
//        BufferedImage imagemAux; // buffer usado para auxiliar na leitura do QRCode
//        BitMatrix bitMatrixData, // objeto matriz de dados lidos do QRCode
//                bitMatrixData2Teste; // objeto para armazenar os dados lidos do QRCode temporariamente
//        DetectorResult[] resultadoDecodificacao; // objeto que armazena o objeto QRCode
//        MultiDetector multiDetect; // objeto usado para detectar QRCode em imagens com mais de um QRCode
//        EnumMap<DecodeHintType, Object> hintMap; // objeto usado para mapear o QRCode a partir da imagem
//        DetectorResult retorno; // objeto que armazena o resultado a ser retornado
//        // tenta encontrar o QRCode sem aplicar o m�todo de realce
//        // inicializar as vari�veis
//        retorno = null;
//        try {
//
//            imagemAux = copiaImagem(imagem);
//            // converte o bufferedImage para bitMatrixData
//            bitMatrixData = emReadImage2BitMatrix(conn, imagem);
//            // busca por um dos QRCodes
//            multiDetect = new MultiDetector(bitMatrixData);
//            hintMap = new EnumMap(DecodeHintType.class);
//            hintMap.put(DecodeHintType.TRY_HARDER, Boolean.TRUE);
//            hintMap.put(DecodeHintType.PURE_BARCODE, Boolean.FALSE);
//            hintMap.put(DecodeHintType.POSSIBLE_FORMATS, BarcodeFormat.QR_CODE);
//            // tenta encontrar os QRCodes na imagem sem realce
//            resultadoDecodificacao = buscaQRCodes(multiDetect, hintMap);
//            // se n�o conseguir encontrar o QRCode sem realce
//            if (resultadoDecodificacao == null) {
//                retorno = null;
//            } else {
//                // percorre o vetor de QRCodes para retornar um que seja diferente de nulo
//                for (int numeroQRCodes = 0; numeroQRCodes < resultadoDecodificacao.length; numeroQRCodes++) {
//                    if (resultadoDecodificacao[numeroQRCodes] != null) {
//                        retorno = resultadoDecodificacao[numeroQRCodes];
//                    }
//                }
//                return retorno;
//            }
//        } catch (Exception ex) {
//            System.err.println("lerQrCode:\t" + ex.getMessage());
//            ex.printStackTrace();
//        }
//        return retorno;
//    }
//
//    /**
//     * M�todo que armazena um bufferedImage em um objeto BitMatrix
//     *
//     * @param imagem : imagem de entrada
//     * @return
//     */
//    public BitMatrix emReadImage2BitMatrix(Connection conn, BufferedImage imagem) {
//        BitMatrix bImage = null; // objeto que armazenar� a imagem em uma matriz de bits
//        LuminanceSource lumSource;
//        try {
//            lumSource = new BufferedImageLuminanceSource(imagem);
//            bImage = new HybridBinarizer(lumSource).getBlackMatrix();
//        } catch (Exception ex) {
//            System.err.println("emRead2Bitmatrix:\t" + ex.getMessage());
//            ex.printStackTrace();
//        }
//        return bImage;
//    }
//
//    /**
//     * M�todo que detecta os QRCodes em uma imagem. D� suporta para detectar
//     * multiplos QRCodes
//     *
//     * @param multiDetect : objeto com o detector a partir da matriz de bits da
//     * imagem
//     * @param hintMap : objeto com os atributos usados durante a detec��o do
//     * QRCode
//     * @return
//     */
//    public DetectorResult[] buscaQRCodes(MultiDetector multiDetect, EnumMap<DecodeHintType, Object> hintMap) {
//        DetectorResult[] resultado; // objeto que armazena o resultado do m�todo, contendo os QRCodes identificados, ou nulo se n�o encontrar nada
//        // inicializa as vari�veis
//        try {
//            // procura os QRCodes
//            resultado = multiDetect.detectMulti(hintMap);
//            return resultado;
//        } catch (Exception ex) {
//            System.err.println("buscaQRCodes:\t" + ex.getMessage());
//            ex.printStackTrace();
//        }
//        return null;
//    }
//
//    protected BufferedImage copiaImagem(BufferedImage imagem) {
//        try {
//            ColorModel cm = imagem.getColorModel();
//            boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
//            WritableRaster raster = imagem.copyData(null);
//            return new BufferedImage(cm, raster, isAlphaPremultiplied, null);
//        } catch (NullPointerException nullex) {
//            System.err.println("copiaImagem:\t"+nullex.getMessage());
//        } catch (Exception ex) {
//            System.err.println("copiaImagem:\t" + ex.getLocalizedMessage());
//            ex.printStackTrace();
//        }
//        return null;
//    }
//
}
