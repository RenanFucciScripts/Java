package models;

import boofcv.abst.filter.blur.BlurFilter;
import boofcv.core.image.ConvertBufferedImage;
import boofcv.factory.filter.blur.FactoryBlurFilter;
import boofcv.struct.image.ImageFloat32;
import boofcv.struct.image.ImageUInt8;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.ChecksumException;
import com.google.zxing.DecodeHintType;
import com.google.zxing.FormatException;
import com.google.zxing.LuminanceSource;
import com.google.zxing.NotFoundException;
import com.google.zxing.ResultPoint;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.DetectorResult;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.multi.qrcode.detector.MultiDetector;
import com.google.zxing.qrcode.decoder.Decoder;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.util.Arrays;
import java.util.EnumMap;

/**
 *
 * @author Alex Araujo
 */
public class QRCodeDetector_AA {

    /**
     * <b>@author Renan Fucci<br></b>
     * <b>Metodo filtroGaussianBoofCV<br></b>
     * Metodo para aplicar o filtro da media da biblioteca BoofCV em uma imagem.
     *
     * @param img imagem a ser processada;
     * @param radius tamanho do raio.
     * @return saida imagem com o filtro.
     */
    private static BufferedImage filtroMediaBoofCV(BufferedImage imagem, int radius) {
        ImageFloat32 input = ConvertBufferedImage.convertFromSingle(imagem, null, ImageFloat32.class);
        ImageFloat32 output = new ImageFloat32(input.width, input.height);

        BlurFilter<ImageFloat32> filter = FactoryBlurFilter.mean(ImageFloat32.class, radius);
        filter.process(input, output);
        BufferedImage saida = ConvertBufferedImage.convertTo(output, new BufferedImage(imagem.getWidth(), imagem.getHeight(), 5));

        return saida;
    }

    protected BufferedImage copiaImagem(BufferedImage imagem) {
        try {
            ColorModel cm = imagem.getColorModel();
            boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
            WritableRaster raster = imagem.copyData(null);
            return new BufferedImage(cm, raster, isAlphaPremultiplied, null);
        } catch (NullPointerException nullex) {
            System.err.println("copiaImagem:\t" + nullex.getMessage());
        } catch (Exception ex) {
            System.err.println("copiaImagem:\t" + ex.getLocalizedMessage());
            ex.printStackTrace();
        }
        return null;
    }

    public ResultPoint[] encontrarQRCode(BufferedImage imagemOriginal) {
        DetectorResult resultadoDeteccao;
        BufferedImage imagemOriginalCinza;
        String conteudoQRCode;

        imagemOriginalCinza = converteImagemParaCinzento(imagemOriginal);
        // pega o primeiro QRCode que conseguir na imagem
        resultadoDeteccao = getQRCode(imagemOriginalCinza);
        // valida o QRCode detectado
        if (resultadoDeteccao == null) {
            // tenta realçar a imagem original uma vez
            imagemOriginalCinza = converteImagemParaCinzento(realceCabecalho(imagemOriginal));
            resultadoDeteccao = getQRCode(imagemOriginalCinza);
            if (resultadoDeteccao == null) {
                // tenta realçar a imagem original duas vezes seguidas em uma tentativa quase desesperada... :)
                imagemOriginalCinza = converteImagemParaCinzento(realceCabecalho(realceCabecalho(imagemOriginal)));
                resultadoDeteccao = getQRCode(imagemOriginalCinza);
                if (resultadoDeteccao == null) {
////////                    this.mensagemLog.escreverMensagem(conn, "ERRO: Nenhum QRCode foi identificado nesta imagem.");
                    return null;
                }
            }
        }
        // decodifica o QRCode encontrado, extraindo seu conteudo
        conteudoQRCode = decodeQR(resultadoDeteccao.getBits());
        //valida o conteúdo do QRCode
        if (conteudoQRCode == null) {
            System.out.println("ERRO: O QRCode não teve seu conteúdo decodificado.");
            return null;
        }

        // armazena os pontos de controle do QRCode no vetor
        ResultPoint[] pontosControleQRCode = resultadoDeteccao.getPoints();
        return pontosControleQRCode;
    }

    /**
     * <b>@author Renan Fucci<br></b>
     * <b> Metodo realceCabecalho</b>
     * Metodo para fazer realce nos cabecalhos que estejam muito claros.
     *
     * @param buff arquivo imagem que sera feito o realce.
     * @return buff bufferedImage da imagem realcada.
     *
     */
    private BufferedImage realceCabecalho(BufferedImage buff) {
        BufferedImage buff1;
        buff1 = null;
        try {
            int[][] red = buffToMatriz(buff, "Red");
            int[][] green = buffToMatriz(buff, "Green");
            int[][] blue = buffToMatriz(buff, "Blue");

            int[][] medR = filtroMedianaRF(red, JanelasConv.tres);
            int[][] medG = filtroMedianaRF(green, JanelasConv.tres);
            int[][] medB = filtroMedianaRF(blue, JanelasConv.tres);

            int[] janela = new int[JanelasConv.tres.getValue()];
            int limiarBranco = 245;

            for (int i = 0; i < red.length; i++) {
                for (int j = 0; j < red[i].length; j++) {
                    if (red[i][j] > limiarBranco && green[i][j] > limiarBranco && blue[i][j] > limiarBranco) {
                        red[i][j] = medR[i][j];
                        green[i][j] = medG[i][j];
                        blue[i][j] = medB[i][j];
                    }
                }
            }
            int[][] entradaR = eliminarPontosOrfaos(red, janela.length);
            int[][] entradaG = eliminarPontosOrfaos(green, janela.length);
            int[][] entradaB = eliminarPontosOrfaos(blue, janela.length);

            int[][] saidaR = new int[entradaR.length][entradaR[0].length];
            int[][] saidaG = new int[entradaR.length][entradaR[0].length];
            int[][] saidaB = new int[entradaR.length][entradaR[0].length];

            for (int i = 0; i < saidaB.length; i++) {
                for (int j = 0; j < saidaB[i].length; j++) {
                    saidaR[i][j] = entradaR[i][j];
                    saidaG[i][j] = entradaG[i][j];
                    saidaB[i][j] = entradaB[i][j];
                }
            }
            int limiarShadow = 93;
            int limiarMiddleTone = 245;

            for (int i = 0; i < saidaB.length; i++) {
                for (int j = 0; j < saidaB[i].length; j++) {
                    if (entradaR[i][j] < limiarShadow) {
                        saidaR[i][j] = 0;
                    } else if (entradaR[i][j] < limiarMiddleTone) {
                        saidaR[i][j] = (int) (0.03 * entradaR[i][j]);
                    } else {
                        saidaR[i][j] = entradaR[i][j];
                    }
                    if (entradaG[i][j] < limiarShadow) {
                        saidaG[i][j] = 0;
                    } else if (entradaG[i][j] < limiarMiddleTone) {
                        saidaG[i][j] = (int) (0.03 * entradaG[i][j]);
                    } else {
                        saidaG[i][j] = entradaG[i][j];
                    }
                    if (entradaB[i][j] < limiarShadow) {
                        saidaB[i][j] = 0;
                    } else if (entradaB[i][j] < limiarMiddleTone) {
                        saidaB[i][j] = (int) (0.03 * entradaB[i][j]);
                    } else {
                        saidaB[i][j] = entradaB[i][j];
                    }
                }

            }

            for (int i = 0; i < saidaB.length; i++) {
                for (int j = 0; j < saidaB[i].length; j++) {
                    saidaR[i][j] = (saidaR[i][j] + entradaR[i][j]) / 2;
                    saidaG[i][j] = (saidaG[i][j] + entradaG[i][j]) / 2;
                    saidaB[i][j] = (saidaB[i][j] + entradaB[i][j]) / 2;

                }
            }

            buff1 = matrizColorToBufferImage(saidaR, saidaG, saidaB);
        } catch (Exception ex) {
            return null;
        }
        return buff1;
    }

    /**
     * <b>@author Renan Fucci<br></b>
     * <b>Metodo matrizToBufferImage </b>
     * Metodo para converter tres matrizes (RGB) em BufferedImage
     *
     * @param matriz[][](int) matriz a ser convertida.
     * @return saida(BufferedImage)
     *
     */
    private BufferedImage matrizColorToBufferImage(int[][] matrizR, int[][] matrizG, int[][] matrizB) throws Exception {
        BufferedImage saida = new BufferedImage(matrizR[0].length, matrizR.length, 5);
        for (int i = 0; i < matrizR.length; i++) {
            for (int j = 0; j < matrizR[i].length; j++) {
                saida.setRGB(j, i, new Color(matrizR[i][j], matrizG[i][j], matrizB[i][j]).getRGB());
            }
        }
        return saida;
    }

    /**
     * <b>@author Alex Araujo<br></b>
     * <b> Metodo eliminarPontosOrfaos</b>
     * Metodo para eliminar os pontos isolados na imagem.
     *
     * @param imagem uma das bandas RGB da imagem.
     * @param tamJanelaConv tamanho da janela de convolucao.
     * @return imagemSaida uma da bandas da imagem sem os pontos isolados.
     *
     */
    private static int[][] eliminarPontosOrfaos(int[][] imagem, int tamJanelaConv) throws Exception {
        int[][] imagemSaida = new int[imagem.length][imagem[0].length];
        int[] janela = new int[tamJanelaConv];

        int numeroVizinhosIguais = 0;
        for (int i = (int) Math.floor(janela.length / 2); i < imagem.length - (int) Math.floor(janela.length / 2); i++) {
            for (int j = (int) Math.floor(janela.length / 2); j < imagem[i].length - (int) Math.floor(janela.length / 2); j++) {
                numeroVizinhosIguais = 0;
                for (int k = -(int) Math.floor(janela.length / 2); k <= (int) Math.floor(janela.length / 2); k++) {
                    for (int l = -(int) Math.floor(janela.length / 2); l <= (int) Math.floor(janela.length / 2); l++) {
                        if (imagem[i + k][j + l] == imagem[i][j]) {
                            numeroVizinhosIguais += 1;
                        }
                    }
                }
                if (numeroVizinhosIguais >= janela.length) {
                    imagemSaida[i][j] = imagem[i][j];
                } else {
                    int media = 0;
                    for (int k = -(int) Math.floor(janela.length / 2); k <= (int) Math.floor(janela.length / 2); k++) {
                        for (int l = -(int) Math.floor(janela.length / 2); l <= (int) Math.floor(janela.length / 2); l++) {
                            media += imagem[i + k][j + l];
                        }
                    }
                    media = (int) Math.abs(media / ((janela.length * janela.length)));
                    imagemSaida[i][j] = media;
                }
            }
        }
        return imagemSaida;
    }

    /**
     * Método que recebe uma imagem, verifica se existe pelo menos UM QRCode
     * nela e o retorna. Caso nenhum QRCode seja encontrado retorna NULL
     *
     * @param imagem : imagem de entrada
     * @return
     */
    private DetectorResult getQRCode(BufferedImage imagem) {
        BufferedImage imagemAux; // buffer usado para auxiliar na leitura do QRCode
        DetectorResult retorno; // objeto que armazena o resultado a ser retornado
        BitMatrix bitMatrixData2Teste; // objeto para armazenar os dados lidos do QRCode temporariamente

        // inicializar as variáveis
        retorno = null;

        // tenta encontrar o QRCode sem aplicar o método de realce
        // cria um laço para iniciar as tentativas de pegar o QRCode corretamente
        // Tentativas:
        // 1: imagem original
        // 2: Filtro Gaussiano Janela de Convolução 3
        // 3: Filtro Gaussiano Janela de Convolução 5
        // 4: Filtro Media Janela de Convolução 3
        for (int tentativaNumero = 1; tentativaNumero <= 4; tentativaNumero++) { // laço para TENTATIVAS
            // reseta variável retorno
            retorno = null;

            // tenta encontrar um QRCode válido
            if (retorno == null && tentativaNumero == 1) {
                imagemAux = copiaImagem(imagem);
                retorno = leQRCode(imagemAux);
            } else if (retorno == null && tentativaNumero == 2) {
                //// AQUI this.mensagemLog.escreverMensagem(conn, "WARNING: Tentando encontrar QRCode com Filtro Gaussiano Janela de Convolução 3.");
                imagemAux = filtroGaussianBoofCV(copiaImagem(imagem), 3);
                retorno = leQRCode(imagemAux);
            } else if (retorno == null && tentativaNumero == 3) {
                //// AQUI this.mensagemLog.escreverMensagem(conn, "WARNING: Tentando encontrar QRCode com Filtro Gaussiano Janela de Convolução 5.");
                imagemAux = filtroGaussianBoofCV(copiaImagem(imagem), 5);
                retorno = leQRCode(imagemAux);
            } else if (retorno == null && tentativaNumero == 4) {
                //// AQUI this.mensagemLog.escreverMensagem(conn, "WARNING: Tentando encontrar QRCode com Filtro Media Janela de Convolução 3.");
                imagemAux = filtroMediaBoofCV(copiaImagem(imagem), 3);
                retorno = leQRCode(imagemAux);
            }

            // testar para ver se a decodificação do QRCode idenditificado funcionará bem
            // configura o matrixData para testar a decodificação do QR detectado
            if (retorno != null) {
                bitMatrixData2Teste = new BitMatrix(retorno.getBits().getWidth(), retorno.getBits().getHeight());
                for (int linha = 0; linha < retorno.getBits().getHeight(); linha++) {
                    for (int coluna = 0; coluna < retorno.getBits().getWidth(); coluna++) {
                        if (retorno.getBits().get(coluna, linha)) {
                            bitMatrixData2Teste.set(coluna, linha);
                        }
                    }
                }

                // verifica se o QR consegue ser decodificado. Se não conseguir, executa recursivamente esse método usando a imagem filtrada com um filtro gaussiana
                if (decodeQR(bitMatrixData2Teste) == null) {
                    System.out.println("************ Tentando encontrar número: " + tentativaNumero + " ***************");
                    // reseta o objeto de retorno para nulo
                    retorno = null;
                } else {
                    // interrompe o laço porque o QRCode foi encontradoe  decodificado corretamente
                    break; // laço para TENTATIVAS
                }
            }
        }
        return retorno;
    }

    /**
     * Método que recebe uma imagem como entrada e a converte para uma imagem de
     * retorno em níveis de cinzento
     *
     * Método já otimizado por Alex Araujo
     *
     * @param imagem : imagem de entrada
     * @return
     */
    private static BufferedImage converteImagemParaCinzento(BufferedImage imagem) {
        BufferedImage imagemRetorno; // buffer para armazenar a imagem de retorno

        // converte a imagem para niveis de cinza
        imagemRetorno = ConvertBufferedImage.convertTo(ConvertBufferedImage.convertFromSingle(imagem, null, ImageUInt8.class), null);

        return imagemRetorno;
    }

    /**
     * Método que recebe uma matriz construída a partir de um QRCode e o
     * decodifica
     *
     * @param QRBitmap
     * @return
     */
    private String decodeQR(BitMatrix QRBitmap) {
        String QRContent; // variável que armazenará o conteúdo lido do QRCode
        Decoder reader = new Decoder();
        try {
            // decodifica o QRCode
            QRContent = reader.decode(QRBitmap).getText();
        } catch (ChecksumException | FormatException ex) {
            //// AQUI this.mensagemLog.escreverMensagem(conn, "ERRO: O conteúdo do QRCode não foi decodificado corretamente.");
            return null;
        }

        // retorna o conteúdo do QRCode
        return QRContent;
    }

    /**
     * Método que recebe uma imagem, verifica se existe pelo menos UM QRCode
     * nela e o retorna. Caso nenhum QRCode seja encontrado retorna NULL
     *
     * @param imagem : imagem de entrada
     * @return
     */
    private DetectorResult leQRCode(BufferedImage imagem) {
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
        bitMatrixData = emReadImage2BitMatrix(imagem);

        // busca por um dos QRCodes
        multiDetect = new MultiDetector(bitMatrixData);

        hintMap
                = new EnumMap(DecodeHintType.class
                );
        hintMap.put(DecodeHintType.TRY_HARDER, Boolean.TRUE);
        hintMap.put(DecodeHintType.PURE_BARCODE, Boolean.FALSE);
        hintMap.put(DecodeHintType.POSSIBLE_FORMATS, BarcodeFormat.QR_CODE);

        // tenta encontrar os QRCodes na imagem sem realce
        resultadoDecodificacao = buscaQRCodes(multiDetect, hintMap);

        // se não conseguir encontrar o QRCode sem realce
        if (resultadoDecodificacao == null) {
            //// AQUI this.mensagemLog.escreverMensagem(conn, "ERRO: Ocorreu um erro ao carregar os dados do QR Code.");
            retorno = null;
        } else {
            // percorre o vetor de QRCodes para retornar um que seja diferente de nulo
            for (int numeroQRCodes = 0; numeroQRCodes < resultadoDecodificacao.length; numeroQRCodes++) {
                if (resultadoDecodificacao[numeroQRCodes] != null) {
                    retorno = resultadoDecodificacao[numeroQRCodes];
                }
            }
        }

        // testar para ver se a decodificação do QRCode idenditificado funcionará bem
        // configura o matrixData para testar a decodificação do QR detectado
        if (retorno != null) {
            bitMatrixData2Teste = new BitMatrix(retorno.getBits().getWidth(), retorno.getBits().getHeight());
            for (int linha = 0; linha < retorno.getBits().getHeight(); linha++) {
                for (int coluna = 0; coluna < retorno.getBits().getWidth(); coluna++) {
                    if (retorno.getBits().get(coluna, linha)) {
                        bitMatrixData2Teste.set(coluna, linha);
                    }
                }
            }

            // verifica se o QR consegue ser decodificado. Se não conseguir, executa recursivamente esse método usando a imagem filtrada com um filtro gaussiana
            if (decodeQR(bitMatrixData2Teste) == null) {
                retorno = null;
            }
        }

        return retorno;
    }

    /**
     * Método que armazena um bufferedImage em um objeto BitMatrix
     *
     * @param imagem : imagem de entrada
     * @return
     */
    private BitMatrix emReadImage2BitMatrix(BufferedImage imagem) {
        BitMatrix bImage; // objeto que armazenará a imagem em uma matriz de bits
        LuminanceSource lumSource;
        try {
            lumSource = new BufferedImageLuminanceSource(imagem);
            bImage = new HybridBinarizer(lumSource).getBlackMatrix();
        } catch (NotFoundException ex) {
            //// AQUI this.mensagemLog.escreverMensagem(conn, "ERRO: O QRCode não foi decodificado.");
            return null;
        }
        return bImage;
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
    private DetectorResult[] buscaQRCodes(MultiDetector multiDetect, EnumMap<DecodeHintType, Object> hintMap) {
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
     * <b>@author Renan Fucci<br></b>
     * <b>Metodo buffToMatriz<br></b>
     * Metodo para converter um BufferedImage para uma matriz;
     *
     * @param buff imagem a ser convertida.
     * @return imgSaida imagem no formado de matriz.
     *
     */
    private int[][] buffToMatriz(BufferedImage buff, String bandaRGB) throws Exception {
        int[][] imgOut = new int[buff.getHeight()][buff.getWidth()];
        for (int i = 0; i < buff.getHeight(); i++) {
            for (int j = 0; j < buff.getWidth(); j++) {
                if (bandaRGB.contentEquals("Red")) {
                    imgOut[i][j] = new Color(buff.getRGB(j, i)).getRed();
                } else if (bandaRGB.contentEquals("Green")) {
                    imgOut[i][j] = new Color(buff.getRGB(j, i)).getGreen();
                } else if (bandaRGB.contentEquals("Blue")) {
                    imgOut[i][j] = new Color(buff.getRGB(j, i)).getGreen();
                }
            }
        }
        return imgOut;
    }

    /**
     * <b>@author Renan Fucci<br></b>
     * <b>Enum {@link JanelasConv}</b>
     * Enumerador para limitar os tamanhos das janelas de convolucao.
     *
     */
    private enum JanelasConv {
        tres(3), cinco(5), sete(7), onze(11), all();

        private final int n;

        private JanelasConv() {
            this.n = 0;
        }

        private JanelasConv(int n) {
            this.n = n;
        }

        private int getValue() {
            return n;
        }
    }

    /**
     * <b>@author Renan Fucci<br></b>
     * <b>Metodo filtroGaussianBoofCV<br></b>
     * Metodo para aplicar o filtro da mediana com janela de convolucao em uma
     * imagem.
     *
     * @param matriz imagem a ser processada;
     * @param janelaConv tamanho da janela de convolucao.
     * @return matrizResultado imagem com o filtro.
     */
    private static int[][] filtroMedianaRF(int matriz[][], JanelasConv janelaConv) {
        int matrizAux[][] = new int[matriz.length][matriz[0].length];
        int matrizResultado[][] = new int[matriz.length][matriz[0].length];
        int janela[][] = new int[janelaConv.getValue()][janelaConv.getValue()];

        for (int g = 0; g < janela.length; g++) {
            for (int h = 0; h < janela[g].length; h++) {
                janela[g][h] = 1;
            }
        }
        for (int i = 0; i < matriz.length; i++) {
            for (int j = 0; j < matriz[i].length; j++) {
                matrizAux[i][j] = matriz[i][j];
            }
        }
        int vetor[] = new int[janelaConv.getValue() * janelaConv.getValue()];
        int mediana = 0;
        int x;
        int cont;
        for (int k = (int) Math.floor((janela.length / 2)); k < matrizAux.length - (int) Math.floor((janela.length / 2)); k++) {
            for (int l = (int) Math.floor((janela.length / 2)); l < matrizAux[0].length - (int) Math.floor((janela.length / 2)); l++) {
                cont = 0;
                for (int m = -(int) Math.floor((janela.length / 2)); m <= (int) Math.floor((janela.length / 2)); m++) {
                    for (int n = -(int) Math.floor((janela.length / 2)); n <= (int) Math.floor((janela.length / 2)); n++) {
                        vetor[cont] = janela[m + (int) Math.floor((janela.length / 2))][(int) Math.floor((janela.length / 2)) + n] * matrizAux[k + m][l + n];
                        cont += 1;
                        Arrays.sort(vetor);
                        x = (int) Math.ceil(vetor.length / 2);
                        mediana = vetor[x];
                    }
                }
                matrizResultado[k][l] = mediana;
            }
        }
        return matrizResultado;
    }

    /**
     * <b>@author Renan Fucci<br></b>
     * <b>Metodo filtroGaussianBoofCV<br></b>
     * Metodo para aplicar o filtro gaussiano da biblioteca BoofCV em uma
     * imagem.
     *
     * @param img imagem a ser processada;
     * @param radius tamanho do raio.
     * @return saida imagem com o filtro.
     */
    private static BufferedImage filtroGaussianBoofCV(BufferedImage imagem, int radius) {
        ImageFloat32 input = ConvertBufferedImage.convertFromSingle(imagem, null, ImageFloat32.class);
        ImageFloat32 output = new ImageFloat32(input.width, input.height);

        BlurFilter<ImageFloat32> filter = FactoryBlurFilter.gaussian(ImageFloat32.class, 1, radius);
        filter.process(input, output);
        BufferedImage saida = ConvertBufferedImage.convertTo(output, new BufferedImage(imagem.getWidth(), imagem.getHeight(), 5));

        return saida;
    }
}
