/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

/**
 *
 * @author _
 */
public abstract class V_e_V_Constantes {

    public static final double DPI_LIMIT = 190;
    public static final long PORCENT_DIFF_MANCHAS = 5;
    public static final int MARGEM_ERRO = 10;
    public static final double LIMIT_DESVIO_PADRAO = 1.1;
    public static final double LIMIT_ANGLE = 5.0;
    public static final double LIMIAR_BRANCO = 244;
    public static final int KSIZE = 3;

    
    /* =================================================== Mensagens de Erro ===================================================*/
    public static final String MSG_ERRO_DESCONHECIDO
            = "Oops! Tivemos um problema estranho que não conseguimos identificar."
            + "\nÉ necessário entrar em contato com a Estante Mágica enviando esse arquivo de imagem e um txt chamado \"Erro-Desconhecido.txt\" que está na mesma pasta desse aplicativo, tudo bem? "
            + "\nÉ importante enviar essas informações para que possamos ajudar na resolução definitiva do problema.";
    public static final String MSG_ERRO_SO_PDF
            = "Oops! Os arquivos de imagens precisam ser em JPG, PNG, BMP ou PDF. Outros formatos não funcionam."
            + "\nRedigitalize o material dos alunos utilizando um desses tipos de arquivos para salvar as imagens.";
    public static final String MSG_ERRO_DPI_MIN
            = "Humm... Percebemos que a resolução das imagens está baixa."
            + "\nElas não vão ficar legais no livro se forem dessa maneira."
            + "\nEscaneie novamente o material usando uma resolução de " + DPI_LIMIT + "dpi. Ok?";
    public static final String MSG_ERRO_MANCHAS_FORTES
            = "Humm... Não estamos conseguindo visualizar corretamente essa imagem. "
            + "\nEla está com muitas manchas e interferências."
            + "\nFaz uma nova digitalização e volta aqui para testar, ok? :)";
    public static final String MSG_ERRO_ASPECT_RADIO
            = "Humm... Essa imagem não foi escaneada por completo."
            + "\nPelo jeito faltou algum pedaço dela ou foi utilizada uma folha com tamanho diferente de A4 para impressão da folha. Isso não pode. "
            + "\nDigitalize novamente a folha e teste novamente por favor! =)";
    public static final String MSG_ERRO_IMG_TORTA
            = "Oops! Ficamos confusos! A imagem está torta."
            + "\nConsegue fazer a digitalização de novo tomando cuidado para colocar a imagem bem ao centro do scanner? =)";
    public static final String MSG_ERRO_QRCODE_ILEGIVEL
            = "Ih! Temos um problema! O QR Code está ilegível. "
            + "\nEle é bem importante, pois é o código que usamos para identificar os materiais dos nossos alunos. "
            + "\nVocê precisa fazer uma nova digitalização para não perdermos esse material, ok? :)";

    /* =================================================== Mensagens de Erro ===================================================*/
}
