package buscadorimagens.dao;

import java.util.HashMap;
import java.util.Map;

/**
 * Classe MODEL para controlar as informações da Imagens do campo
 * HARPIA_IMAGESIN do banco de dados, agrupadas por Categoria (last_stage);
 *
 * @author RenanFucci
 */
public class ImgsObjsAbaTotal_PorCategoria {

    private String NomeCategoria;
    private int TotalCategoria;
    private boolean isNull;

    private Map<String, Integer> map_urlin_qntd = new HashMap<>();
    private Map<String, Integer> map_dateout_qntd = new HashMap<>();

    public ImgsObjsAbaTotal_PorCategoria() {
        this.isNull = true;
    }

    public ImgsObjsAbaTotal_PorCategoria(String NomeCategoria, int TotalCategoria) {
        this.NomeCategoria = NomeCategoria;
        this.TotalCategoria = TotalCategoria;
        this.isNull = false;
    }

    /**
     * @author RenanFucci<br>
     * <strong>Método add_Url_in</strong><br>
     * Método adicionar um url_in num HashMap. Se a url_in já existir na
     * estrutura, será incrementado a quantidade de registros.<br>
     * @param url_in String da url_in que será adicionado;
     */
    public void add_Url_in(String url_in) {
        int aux = map_urlin_qntd.containsKey(url_in) ? map_urlin_qntd.get(url_in) : 0;
        map_urlin_qntd.put(url_in, aux + 1);
    }

    /**
     * @author RenanFucci<br>
     * <strong>Método add_date_out</strong><br>
     * Método adicionar um date_out num HashMap. Se a date_out já existir na
     * estrutura, será incrementado a quantidade de registros.<br>
     * @param date_out String da date_out que será adicionado;
     */
    public void add_date_out(String date_out) {
        String date_out_w_time = date_out.split("\\s")[0];
        int aux = map_dateout_qntd.containsKey(date_out_w_time) ? map_dateout_qntd.get(date_out_w_time) : 0;
        map_dateout_qntd.put(date_out_w_time, aux + 1);
    }

    /*----------- GETTERS -----------*/
    public int getTotalCategoria() {
        return TotalCategoria;
    }

    public boolean isIsNull() {
        return isNull;
    }

    public String getNomeCategoria() {
        return NomeCategoria;
    }

    public Map<String, Integer> getMap_urlin_qntd() {
        return map_urlin_qntd;
    }

    public Map<String, Integer> getMap_dateout_qntd() {
        return map_dateout_qntd;
    }

}
