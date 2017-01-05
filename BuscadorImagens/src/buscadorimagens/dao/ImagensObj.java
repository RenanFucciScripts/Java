package buscadorimagens.dao;

/**
 * Classe MODEL para controlar as informações da Imagens do campo
 * HARPIA_IMAGESIN do banco de dados;
 *
 * @author RenanFucci
 */
public class ImagensObj {

    private String url_in;
    private String url_out;
    private String date_out;
    private String last_stage;
    private int id_school;

    public ImagensObj(String url_in, String url_out, String date_out, int id_school) {
        this.url_in = url_in;
        this.url_out = url_out;
        this.date_out = date_out;
        this.id_school = id_school;
    }

    public ImagensObj(String url_in, String url_out, String date_out, String last_stage) {
        this.url_in = url_in;
        this.url_out = url_out;
        this.date_out = date_out;
        this.last_stage = last_stage;
    }

    public ImagensObj(String url_in, String url_out, String date_out) {
        this.url_in = url_in;
        this.url_out = url_out;
        this.date_out = date_out;
    }

    /*----------- GETTERS -----------*/
    
    public String getUrl_in() {
        return url_in;
    }

    public String getUrl_out() {
        return url_out;
    }

    public String getDate_out() {
        return date_out;
    }

    public String getLast_stage() {
        return last_stage;
    }

    public int getId_school() {
        return id_school;
    }

}
