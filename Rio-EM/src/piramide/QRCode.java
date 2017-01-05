package piramide;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.util.EnumMap;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.DecodeHintType;
import com.google.zxing.LuminanceSource;
import com.google.zxing.NotFoundException;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.DetectorResult;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.multi.qrcode.detector.MultiDetector;
import com.sun.corba.se.pept.transport.Connection;

public class QRCode {

	/**
     * Método que recebe uma imagem, verifica se existe pelo menos UM QRCode
     * nela e o retorna. Caso nenhum QRCode seja encontrado retorna NULL
     *
     * @param conn : objeto com a conexão ao banco de dados
     * @param imagem : imagem de entrada
     * @return
     */
    public DetectorResult leQRCode(Connection conn, BufferedImage imagem) {
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
        imagemAux = copiaImagem(imagem, 0, 0, imagem.getWidth(), imagem.getHeight());

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
            System.out.println("ERRO: Ocorreu um erro ao carregar os dados do QR Code.");
            retorno = null;
        } else {
            // percorre o vetor de QRCodes para retornar um que seja diferente de nulo
            for (int numeroQRCodes = 0; numeroQRCodes < resultadoDecodificacao.length; numeroQRCodes++) {
                if (resultadoDecodificacao[numeroQRCodes] != null) {
                    retorno = resultadoDecodificacao[numeroQRCodes];
                }
            }
        }
        return retorno;
    }
    
    /**
     * Método que detecta os QRCodes em uma imagem. Dá suporta para detectar
     * multiplos QRCodes
     *
     * @param multiDetect : objeto com o detector a partir da matriz de bits da
     * imagem
     * @param hintMap : objeto com os atributos usados durante a detecção do
     * QRCode
     * @return
     */
    public DetectorResult[] buscaQRCodes(MultiDetector multiDetect, EnumMap<DecodeHintType, Object> hintMap) {
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
    
    /**
     * Método que recebe uma imagem como entrada e copia uma parte ou a imagem
     * inteira para um novo buffer de retorno
     *
     * @param imagem : imagem recebida como parâmetro
     * @param xInicio : coordenada x do ponto de início para cópia
     * @param yInicio : coordenada y do ponto de início para cópia
     * @param largura : largura da porção a ser copiada
     * @param altura : altura da porção a ser copiada
     * @return
     */
    public static BufferedImage copiaImagem(BufferedImage imagem, int xInicio, int yInicio, int largura, int altura) {
        int larguraImagem, // variável que armazena a largura da imagem/ou parte da imagem a ser copiada
                alturaImagem; // variável que armazena a altura da imagem/ou parte da imagem a ser copiada
        Raster raster; // variável usada para auxiliar na cópia
        BufferedImage imagemRetorno; // buffer para a imagem de retorno

        // cria o buffer de retorno com as mesmas características da imagem recebida como parâmetro @param imagem
        imagemRetorno = new BufferedImage(largura, altura, imagem.getType());
        raster = imagem.getData(new Rectangle(xInicio, yInicio, largura, altura));
        imagemRetorno.setData(raster);

        return imagemRetorno;
    }
    
    /**
     * Método que armazena um bufferedImage em um objeto BitMatrix
     *
     * @param imagem : imagem de entrada
     * @return
     */
    public BitMatrix emReadImage2BitMatrix(Connection conn, BufferedImage imagem) {
        BitMatrix bImage; // objeto que armazenará a imagem em uma matriz de bits
        LuminanceSource lumSource;
        try {
            lumSource = new BufferedImageLuminanceSource(imagem);
            bImage = new HybridBinarizer(lumSource).getBlackMatrix();
        } catch (NotFoundException ex) {
            System.out.println("ERRO: O QRCode não foi decodificado.");
            return null;
        }
        return bImage;
    }

}
