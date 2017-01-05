package buscadorimagens.dao;

/**
 * Classe para manter os padrões de listas de extensões de arquivos (file_type), as informações da Imagens do campo
 * HARPIA_IMAGESIN do banco de dados, agrupadas por Categoria (last_stage);
 *
 * @author RenanFucci
 */
public class Listas_Padrao {

    public static final String[] LIST_EXTENSAO = {"JPG", "JPEG", "BMP", "PNG", "GIF", "RAR", "ZIP", "PDF", "PPT", "PPTX", "Outros..."};

    public static final String[] LIST_LAST_STAGE_SELECT = {"notRecognized", "outOfBounds", "ilustraSaved",
        "childIsNotActive", "whiteImage", "fileNotExists", "ownerChanged"};
    public static final String[] LIST_LAST_STAGES_NOMECATEGORIAS = {"Não Reconhecidas", "Fora do Limite", "Reconhecidas",
        "Crianças Inativas", "Em Branco", "Arquivos Deletados", "Mudança de Dono"};

}
