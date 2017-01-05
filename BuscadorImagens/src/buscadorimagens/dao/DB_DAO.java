package buscadorimagens.dao;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.control.Alert;

/**
 * Classe DAO para acessar as informações de um banco de dados;
 *
 * @author RenanFucci
 */
public class DB_DAO {

    private Connection con; //Conexao com BD
    private Statement st; //Requisicao de conexao
    private int last_id_inserted;//Ultimo id inserido no banco
    private PreparedStatement prepStmt_insert;//Obj de Requisicao para INSERT
    private PreparedStatement prepStmt_update;//Obj de Requisicao para UPDATE

    //Informacoes de conexao - Localhost
    private static String DB_URL;
    private static String DB_USER;
    private static String DB_PWD;
    private boolean erroEhConfig = false;
    private boolean erroEhIdInvalido = false;
    public String task_alerta_title;
    public String task_alerta_headertext;
    public String task_alerta_contentText;
    public Alert.AlertType task_alerta_alertType;

    public boolean isErroEhConfig() {
        return erroEhConfig;
    }

    public boolean isErroEhIdInvalido() {
        return erroEhIdInvalido;
    }

    public static JsonObject getJsonBD() {
        try {
            JsonParser parser = new JsonParser();
            Object obj = parser.parse(new FileReader("config/bd.json"));
            JsonObject jsonObject = (JsonObject) obj;
            return jsonObject;
        } catch (Exception ex) {
            System.err.println("getJsonBD:\tex:\t" + ex.getLocalizedMessage());
        }
        return null;
    }

    public boolean getConfigBD() {
        try {
            JsonObject jsonObject = getJsonBD();

            String nomeBD = jsonObject.get("nomeBD").getAsString();
            String iphost = jsonObject.get("iphost").getAsString();
            String user = jsonObject.get("user").getAsString();
            String pwd = jsonObject.get("pwd").getAsString();

            if ((!nomeBD.contentEquals("")) && (!user.contentEquals("") && (!iphost.contentEquals("")))) {
                DB_URL = "jdbc:mysql://" + iphost + ":3306/" + nomeBD + "?autoReconnect=true&useSSL=false";
                DB_USER = user;
                DB_PWD = pwd;
                return true;
            }
        } catch (Exception ex) {
            System.err.println(ex.getMessage());
            ex.printStackTrace();

        }
        return false;
    }

    /**
     * <b>@author Renan Fucci<br></b>
     * <b>Metodo connect</b><br>
     * Metodo para fazer a conexao com o BD .
     *
     * @return o resultado booleano da conexao.
     */
    public boolean connect() {

        String aux_task_alerta_title;
        String aux_task_alerta_headertext;
        String aux_task_alerta_contentText;
        String axu_task_alerta_alertType;

        try {
            if (getConfigBD() == false) {
                task_alerta_title = "Banco de Dados";
                task_alerta_headertext = "Nenhuma configuração de conexão com o banco."
                        + "\nFavor ir nas configuraçõa e setar os dados de conexão com o banco de dados";
                task_alerta_contentText = null;
                task_alerta_alertType = Alert.AlertType.ERROR;
                return false;
            }
            //System.out.println("DB_Dao.connect");
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection(DB_URL, DB_USER, DB_PWD);
            st = con.createStatement();
            return true;
        } catch (SQLException e) {

            task_alerta_headertext = "Tentativa de conexão com o bando de dados falhou.";
            task_alerta_contentText = "Possíveis problemas:"
                    + "\n\t1) Configurações não são válidas e o teste de conexão falhou."
                    + "\n\t2) O banco de dados está desativado.";
            System.err.println("e.: " + e.getLocalizedMessage());
        } catch (Exception ex) {
            task_alerta_headertext = "Tentativa de conexão com o bando de dados falhou.";
            task_alerta_contentText = "Possíveis problemas:"
                    + "\n\t1)Configurações não são válidas e o teste de conexão falhou."
                    + "\n\t2)O banco de dados está desativado.";
            System.err.println("ex: " + ex.getLocalizedMessage());

        }
        task_alerta_title = "Banco de Dados";
        task_alerta_alertType = Alert.AlertType.ERROR;

        return false;
    }

    /**
     * <b>@author Renan Fucci<br></b>
     * <b>Metodo isConnected</b><br>
     * Metodo para testar se esta connectado com o BD .
     *
     * @return o resultado booleano da teste.
     */
    public boolean isConnected() {
        try {
            if (!con.isClosed()) {
                return true;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }

    /**
     * <b>@author Renan Fucci<br></b>
     * <b>Metodo disconnect</b><br>
     * Metodo para fazer a desconexao com o BD .
     *
     * @return o resultado booleano da desconexao.
     */
    public boolean disconnect() {
        try {
            con.close();
            return true;
        } catch (SQLException e) {
            System.err.println("sql: " + e.getSQLState() + "\n" + e.getMessage());
        }
        return false;
    }

    /**
     * @author RenanFucci<br>
     * <strong>Método
     * getImagens_contaisString_dataRegistroBARRAnomeEscola_url_in</strong><br>
     * Método pegar informações do banco que possuam a mesma
     * dataderegistroBARRAnomeEscola_url_in.<br>
     * @param nomeEscola nome do escola que será buscado no banco de dados;
     * @param dataRegitro data do registro que será buscado no BD;
     * @param extensao_arq extensão do arquivo que será buscado no BD;
     * @param last_stage tag last_stage que será buscado no BD;
     * @return {@literal ArrayList<ImagensObj>} lista da busca no banco das
     * imagens que correspondam as informações de parâmetro;
     */
    public ArrayList<ImagensObj> getImagens_contaisString_dataRegistroBARRAnomeEscola_url_in(String nomeEscola,
            String dataRegitro, String extensao_arq, String last_stage) {
        //  System.out.print("\n\ngetImagens_contaisString_dataRegistroBARRAnomeEscola_url_in():\t");
        int rows = 0;
        PreparedStatement stmt;
        ResultSet rs;
        int id_escola = -1;
        ArrayList<ImagensObj> imagensObjs = new ArrayList<>();

        String dataRegistroBARRAnomeEscola = dataRegitro + "/" + nomeEscola;

        if (con != null) {
            try {
                String sql_get_id = "SELECT " + EM_HARPIA.EM_HARPIA_IMAGESIN.FIELD_URL_IN.getAsString_ComCrase() + ","
                        + " " + EM_HARPIA.EM_HARPIA_IMAGESIN.FIELD_URL_OUT.getAsString_ComCrase() + ","
                        + " " + EM_HARPIA.EM_HARPIA_IMAGESIN.FIELD_DATE_OUT.getAsString_ComCrase()
                        + " FROM " + EM_HARPIA.EM_HARPIA_IMAGESIN.TABLE_NAME.getAsString_ComCrase() + " "
                        + "WHERE " + EM_HARPIA.EM_HARPIA_IMAGESIN.FIELD_URL_IN.getAsString_ComCrase() + " LIKE '%" + dataRegistroBARRAnomeEscola + "%'"
                        + " AND " + EM_HARPIA.EM_HARPIA_IMAGESIN.FIELD_LAST_STAGE.getAsString_ComCrase() + " LIKE '" + last_stage + "'";

                if (!extensao_arq.contentEquals("Outros...")) {
                    sql_get_id += " AND" + EM_HARPIA.EM_HARPIA_IMAGESIN.FIELD_TYPE_FILE.getAsString_ComCrase() + " LIKE '" + extensao_arq + "'";
                }
                //      System.out.print("\n\t\t\t" + sql_get_id);
                stmt = con.prepareStatement(sql_get_id);
                rs = stmt.executeQuery();
                while (rs.next()) {
                    String url_in = (rs.getString(EM_HARPIA.EM_HARPIA_IMAGESIN.FIELD_URL_IN.getAsString_SemCrase()) != null) ? (rs.getString(EM_HARPIA.EM_HARPIA_IMAGESIN.FIELD_URL_IN.getAsString_SemCrase())) : ("");
                    String url_out = (rs.getString(EM_HARPIA.EM_HARPIA_IMAGESIN.FIELD_URL_OUT.getAsString_SemCrase()) != null) ? (rs.getString(EM_HARPIA.EM_HARPIA_IMAGESIN.FIELD_URL_OUT.getAsString_SemCrase())) : ("");
                    String date_out = (rs.getTimestamp(EM_HARPIA.EM_HARPIA_IMAGESIN.FIELD_DATE_OUT.getAsString_SemCrase()).toString() != null) ? (rs.getTimestamp(EM_HARPIA.EM_HARPIA_IMAGESIN.FIELD_DATE_OUT.getAsString_SemCrase()).toString()) : ("");
                    //System.out.println(url_in + "\t" + url_out + "\t" + date_out );
                    imagensObjs.add(new ImagensObj(url_in, url_out, date_out));

                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            System.err.println("Nao ha conexao com o bd\nFavor utilizar o metodo connect()");
        }
        return imagensObjs;
    }

    /**
     * @author RenanFucci<br>
     * <strong>Método getImagens_lastStage_like_codEscola</strong><br>
     * Método pegar informações do banco que possuam o mesmo código de
     * escola.<br>
     * @param like_str tag last_stage que será buscado no banco de dados;
     * @param codEscola código da escola que será buscado no BD;
     * @return {@literal ArrayList<ImagensObj>} lista da busca no banco das
     * imagens que correspondam as informações de parâmetro;
     */
    public ArrayList<ImagensObj> getImagens_lastStage_like_codEscola(String like_str, int codEscola) {
        //  System.out.print("\n\ngetImagens_lastStage_like_codEscola():\t");
        int rows = 0;
        ArrayList<ImagensObj> imagensObjs = new ArrayList<>();
        PreparedStatement stmt;
        ResultSet rs;
//        System.out.print("\n\tcon:\t" + con);

        if (con != null) {

            try {
//                System.out.print("\n\ttry\t");

                String sql_get_id = "SELECT " + EM_HARPIA.EM_SCHOOL.FIELD_ID_SCHOOL.getAsString_ComCrase() + ""
                        + " FROM " + EM_HARPIA.EM_SCHOOL.TABLE_NAME.getAsString_ComCrase() + " "
                        + "WHERE " + EM_HARPIA.EM_SCHOOL.FIELD_ID_SCHOOL.getAsString_ComCrase() + " = " + codEscola + ""
                        + " AND " + EM_HARPIA.EM_SCHOOL.FIELD_EM_SCHOOL_ATIVO.getAsString_ComCrase() + " LIKE 'Sim'";
                System.out.print("\n\t\t\t" + sql_get_id);
                stmt = con.prepareStatement(sql_get_id);
                rs = stmt.executeQuery();
                int id_escola = -1;
                while (rs.next()) {
//                    System.out.print("\n\t\t.");
                    id_escola = (rs.getInt(EM_HARPIA.EM_SCHOOL.FIELD_ID_SCHOOL.getAsString_SemCrase()) != 0)
                            ? (rs.getInt(EM_HARPIA.EM_SCHOOL.FIELD_ID_SCHOOL.getAsString_SemCrase())) : (id_escola);
                }
//                System.out.print("\n\tid_escola:\t" + id_escola);
                if (id_escola == -1) {
                    erroEhIdInvalido = true;
                    return null;
                }
                erroEhIdInvalido = false;
                List<String> sql_substrings_distinct = new ArrayList<>();
                String sql = "SELECT DISTINCT SUBSTRING_INDEX(" + EM_HARPIA.EM_HARPIA_IMAGESIN.FIELD_URL_IN.getAsString_ComCrase() + ""
                        + ", " + EM_HARPIA.EM_HARPIA_IMAGESIN.FIELD_FILE_IN_NAME.getAsString_ComCrase() + ", 1)"
                        + " FROM " + EM_HARPIA.EM_HARPIA_IMAGESIN.TABLE_NAME.getAsString_ComCrase() + " "
                        + "WHERE " + EM_HARPIA.EM_HARPIA_IMAGESIN.FIELD_ID_SCHOOL.getAsString_ComCrase() + " = " + id_escola + "";
                stmt = con.prepareStatement(sql);
                rs = stmt.executeQuery();
                //           System.out.print("\n\t\t\t\t" + sql);

                while (rs.next()) {
                    String s = rs.getString(1);
                    sql_substrings_distinct.add(s);
                }
                int cont_axu = 0;
                //       System.out.print("\n\t\t\tfor results sql acima:\t");
                for (String str_substr_distinct : sql_substrings_distinct) {
                    cont_axu += 1;
                    sql = "SELECT " + EM_HARPIA.EM_HARPIA_IMAGESIN.FIELD_URL_IN.getAsString_ComCrase() + ","
                            + " " + EM_HARPIA.EM_HARPIA_IMAGESIN.FIELD_URL_OUT.getAsString_ComCrase() + ","
                            + " " + EM_HARPIA.EM_HARPIA_IMAGESIN.FIELD_DATE_OUT.getAsString_ComCrase() + ""
                            + " FROM " + EM_HARPIA.EM_HARPIA_IMAGESIN.TABLE_NAME.getAsString_ComCrase() + ""
                            + " WHERE " + EM_HARPIA.EM_HARPIA_IMAGESIN.FIELD_URL_IN.getAsString_ComCrase() + " LIKE '%" + str_substr_distinct + "%'"
                            + " AND " + EM_HARPIA.EM_HARPIA_IMAGESIN.FIELD_LAST_STAGE.getAsString_ComCrase() + " LIKE '" + like_str + "'";
//                    if (cont_axu == 1) {
//                        System.out.print("\n\t\t\t\t\t" + sql);
//                    } else {
//                        System.out.print("\n\t\t\t\t.");
//                    }
                    stmt = con.prepareStatement(sql);
                    rs = stmt.executeQuery();
                    while (rs.next()) {
                        String url_in = (rs.getString(EM_HARPIA.EM_HARPIA_IMAGESIN.FIELD_URL_IN.getAsString_SemCrase()) != null) ? (rs.getString(EM_HARPIA.EM_HARPIA_IMAGESIN.FIELD_URL_IN.getAsString_SemCrase())) : ("");
                        String url_out = (rs.getString(EM_HARPIA.EM_HARPIA_IMAGESIN.FIELD_URL_OUT.getAsString_SemCrase()) != null) ? (rs.getString(EM_HARPIA.EM_HARPIA_IMAGESIN.FIELD_URL_OUT.getAsString_SemCrase())) : ("");
                        String date_out = (rs.getTimestamp(EM_HARPIA.EM_HARPIA_IMAGESIN.FIELD_DATE_OUT.getAsString_SemCrase()).toString() != null) ? (rs.getTimestamp(EM_HARPIA.EM_HARPIA_IMAGESIN.FIELD_DATE_OUT.getAsString_SemCrase()).toString()) : ("");
                        imagensObjs.add(new ImagensObj(url_in, url_out, date_out));
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            System.err.println("Nao ha conexao com o bd\nFavor utilizar o metodo connect()");
        }
        return imagensObjs;
    }

    /**
     * @author RenanFucci<br>
     * <strong>Método getImagens_lastStage_like_nomeEscola</strong><br>
     * Método pegar informações do banco que possuam o mesmo nome de escola.<br>
     * @param like_str tag last_stage que será buscado no banco de dados;
     * @param nomeEscola nome da escola que será buscado no BD;
     * @return {@literal ArrayList<ImagensObj>} lista da busca no banco das
     * imagens que correspondam as informações de parâmetro;
     */
    public ArrayList<ImagensObj> getImagens_lastStage_like_nomeEscola(String like_str, String nomeEscola) {
//        System.out.print("\n\ngetImagens_lastStage_like_nomeEscola():");
        int rows = 0;
        ArrayList<ImagensObj> imagensObjs = new ArrayList<>();
        PreparedStatement stmt;
        ResultSet rs;
        if (con != null) {
            try {
                String sql_get_id = "SELECT " + EM_HARPIA.EM_SCHOOL.FIELD_ID_SCHOOL.getAsString_ComCrase() + ""
                        + " FROM " + EM_HARPIA.EM_SCHOOL.TABLE_NAME.getAsString_ComCrase() + ""
                        + " WHERE " + EM_HARPIA.EM_SCHOOL.FIELD_EM_NAME_SCHOOL.getAsString_ComCrase() + ""
                        + " LIKE '" + nomeEscola + "'"
                        + " AND " + EM_HARPIA.EM_SCHOOL.FIELD_EM_SCHOOL_ATIVO.getAsString_ComCrase() + " LIKE 'Sim'";
                stmt = con.prepareStatement(sql_get_id);
                rs = stmt.executeQuery();
//                System.out.print("\n\t\t\t" + sql_get_id);
                int id_escola = -1;
                while (rs.next()) {
                    id_escola = (rs.getInt(EM_HARPIA.EM_SCHOOL.FIELD_ID_SCHOOL.getAsString_SemCrase()) != 0)
                            ? (rs.getInt(EM_HARPIA.EM_SCHOOL.FIELD_ID_SCHOOL.getAsString_SemCrase())) : (id_escola);
                }
                if (id_escola == -1) {
                    erroEhIdInvalido = true;
                    return null;
                }
                erroEhIdInvalido = false;
                imagensObjs = getImagens_lastStage_like_codEscola(like_str, id_escola);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            System.err.println("Nao ha conexao com o bd\nFavor utilizar o metodo connect()");
        }
        return imagensObjs;
    }

    /**
     * @author RenanFucci<br>
     * <strong>Método getImagens_lastStage_like_codCrianca</strong><br>
     * Método pegar informações do banco que possuam o mesmo código e ano de
     * crianca .<br>
     * @param idchild id da crianca que será buscado no banco de dados;
     * @param year ano da crianca que será buscado no banco de dados;
     * @return {@literal ArrayList<ImagensObj>} lista da busca no banco das
     * imagens que correspondam as informações de parâmetro;
     */
    public ArrayList<ImagensObj> getImagens_lastStage_like_codCrianca(int idchild, int year) {
//        System.out.print("\n\ngetImagens_lastStage_like_codCrianca:");
        int rows = 0;
        ArrayList<ImagensObj> imagensObjs = new ArrayList<>();
        PreparedStatement stmt;
        ResultSet rs;
        if (con != null) {
            try {
                String sql_get_id = "SELECT " + EM_HARPIA.EM_CHILD.FIELD_ID_CHILD.getAsString_ComCrase() + ""
                        + " FROM " + EM_HARPIA.EM_CHILD.TABLE_NAME.getAsString_ComCrase() + ""
                        + " WHERE " + EM_HARPIA.EM_CHILD.FIELD_ID_CHILD.getAsString_ComCrase() + " = " + idchild + ""
                        + " AND " + EM_HARPIA.EM_CHILD.FIELD_EM_CHILD_ATIVO.getAsString_ComCrase() + ""
                        + " LIKE 'Sim' AND " + EM_HARPIA.EM_CHILD.FIELD_EM_CHILD_ANO.getAsString_ComCrase() + " = " + year + "";
                stmt = con.prepareStatement(sql_get_id);
                rs = stmt.executeQuery();
                int id_crianca = -1;
//                System.out.print("\n\t\tsql_get_id:\t" + sql_get_id);
                while (rs.next()) {
                    id_crianca = (rs.getInt(EM_HARPIA.EM_CHILD.FIELD_ID_CHILD.getAsString_SemCrase()) != 0)
                            ? (rs.getInt(EM_HARPIA.EM_CHILD.FIELD_ID_CHILD.getAsString_SemCrase())) : (id_crianca);
                }
                if (id_crianca == -1) {
                    erroEhIdInvalido = true;
                    return null;
                }
                erroEhIdInvalido = false;
                String sql = "SELECT " + EM_HARPIA.EM_HARPIA_IMAGESIN.FIELD_URL_IN.getAsString_ComCrase() + ","
                        + " " + EM_HARPIA.EM_HARPIA_IMAGESIN.FIELD_URL_OUT.getAsString_ComCrase() + ","
                        + " " + EM_HARPIA.EM_HARPIA_IMAGESIN.FIELD_DATE_OUT.getAsString_ComCrase() + ","
                        + " " + EM_HARPIA.EM_HARPIA_IMAGESIN.FIELD_LAST_STAGE.getAsString_ComCrase() + ""
                        + " FROM " + EM_HARPIA.EM_HARPIA_IMAGESIN.TABLE_NAME.getAsString_ComCrase() + " "
                        + "WHERE " + EM_HARPIA.EM_HARPIA_IMAGESIN.FIELD_ID_CHILD.getAsString_ComCrase() + " = " + idchild + "";
                stmt = con.prepareStatement(sql);
                rs = stmt.executeQuery();
//                System.out.print("\n\t\tsql:\t" + sql);
                while (rs.next()) {
                    String url_in = (rs.getString(EM_HARPIA.EM_HARPIA_IMAGESIN.FIELD_URL_IN.getAsString_SemCrase()) != null) ? (rs.getString(EM_HARPIA.EM_HARPIA_IMAGESIN.FIELD_URL_IN.getAsString_SemCrase())) : ("");
                    String url_out = (rs.getString(EM_HARPIA.EM_HARPIA_IMAGESIN.FIELD_URL_OUT.getAsString_SemCrase()) != null) ? (rs.getString(EM_HARPIA.EM_HARPIA_IMAGESIN.FIELD_URL_OUT.getAsString_SemCrase())) : ("");
                    String date_out = (rs.getTimestamp(EM_HARPIA.EM_HARPIA_IMAGESIN.FIELD_DATE_OUT.getAsString_SemCrase()).toString() != null) ? (rs.getTimestamp(EM_HARPIA.EM_HARPIA_IMAGESIN.FIELD_DATE_OUT.getAsString_SemCrase()).toString()) : ("");
                    String last_stage = (rs.getString(EM_HARPIA.EM_HARPIA_IMAGESIN.FIELD_LAST_STAGE.getAsString_SemCrase()) != null) ? (rs.getString(EM_HARPIA.EM_HARPIA_IMAGESIN.FIELD_LAST_STAGE.getAsString_SemCrase())) : ("");
//                    System.out.println(url_in + "\t" + url_out + "\t" + date_out + "\t" + last_stage);
                    imagensObjs.add(new ImagensObj(url_in, url_out, date_out, last_stage));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            System.err.println("Nao ha conexao com o bd\nFavor utilizar o metodo connect()");
        }
        return imagensObjs;
    }

    /**
     * @author RenanFucci<br>
     * <strong>Método getImagens_lastStage_like_flName_flExtn</strong><br>
     * Método pegar informações do banco que possuam os mesmos last_stage,
     * file_in_name, type_file de crianca .<br>
     * @param lastStage last_stage da crianca que será buscado no banco de
     * dados;
     * @param flName file_in_name da crianca que será buscado no banco de dados;
     * @param flExtn type_file da crianca que será buscado no banco de dados;
     * @return {@literal ArrayList<ImagensObj>} lista da busca no banco das
     * imagens que correspondam as informações de parâmetro;
     */
    public ArrayList<ImagensObj> getImagens_lastStage_like_flName_flExtn(String lastStage, String flName, String flExtn) {
//        System.out.print("\n\ngetImagens_lastStage_like_flName_flExtn():\t");
        ArrayList<ImagensObj> imagensObjs = new ArrayList<>();
        PreparedStatement stmt;
        ResultSet rs;
        if (con != null) {
            try {
                String sql = "SELECT " + EM_HARPIA.EM_HARPIA_IMAGESIN.FIELD_URL_IN.getAsString_ComCrase() + ","
                        + " " + EM_HARPIA.EM_HARPIA_IMAGESIN.FIELD_URL_OUT.getAsString_ComCrase() + ","
                        + " " + EM_HARPIA.EM_HARPIA_IMAGESIN.FIELD_DATE_OUT.getAsString_ComCrase() + ","
                        + " " + EM_HARPIA.EM_HARPIA_IMAGESIN.FIELD_ID_SCHOOL.getAsString_ComCrase() + ""
                        + " FROM " + EM_HARPIA.EM_HARPIA_IMAGESIN.TABLE_NAME.getAsString_ComCrase() + ""
                        + " WHERE " + EM_HARPIA.EM_HARPIA_IMAGESIN.FIELD_FILE_IN_NAME.getAsString_ComCrase() + " LIKE '%" + flName + "%'"
                        + " AND " + EM_HARPIA.EM_HARPIA_IMAGESIN.FIELD_TYPE_FILE.getAsString_ComCrase() + " LIKE '" + flExtn + "'"
                        + " AND " + EM_HARPIA.EM_HARPIA_IMAGESIN.FIELD_LAST_STAGE.getAsString_ComCrase() + " LIKE '" + lastStage + "'";
//                System.out.print("\n\t\t" + sql);
                stmt = con.prepareStatement(sql);
                rs = stmt.executeQuery();
                while (rs.next()) {
                    String url_in = (rs.getString(EM_HARPIA.EM_HARPIA_IMAGESIN.FIELD_URL_IN.getAsString_SemCrase()) != null) ? (rs.getString(EM_HARPIA.EM_HARPIA_IMAGESIN.FIELD_URL_IN.getAsString_SemCrase())) : ("");
                    String url_out = (rs.getString(EM_HARPIA.EM_HARPIA_IMAGESIN.FIELD_URL_OUT.getAsString_SemCrase()) != null) ? (rs.getString(EM_HARPIA.EM_HARPIA_IMAGESIN.FIELD_URL_OUT.getAsString_SemCrase())) : ("");
                    String date_out = (rs.getTimestamp(EM_HARPIA.EM_HARPIA_IMAGESIN.FIELD_DATE_OUT.getAsString_SemCrase()).toString() != null) ? (rs.getTimestamp(EM_HARPIA.EM_HARPIA_IMAGESIN.FIELD_DATE_OUT.getAsString_SemCrase()).toString()) : ("");
                    int id_school = (rs.getInt(EM_HARPIA.EM_HARPIA_IMAGESIN.FIELD_ID_SCHOOL.getAsString_SemCrase()) != 0) ? (rs.getInt(EM_HARPIA.EM_HARPIA_IMAGESIN.FIELD_ID_SCHOOL.getAsString_SemCrase())) : -1;

                    imagensObjs.add(new ImagensObj(url_in, url_out, date_out, id_school));
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            System.err.println("Nao ha conexao com o bd\nFavor utilizar o metodo connect()");
        }
        return imagensObjs;
    }

    /**
     * @author RenanFucci<br>
     * <strong>Método getImagens_lastStage_like_nomeCrianca</strong><br>
     * Método pegar informações do banco que possuam os mesmos nome e ano de
     * crianca .<br>
     * @param nome_crianca nome da crianca que será buscado no banco de dados;
     * @param year ano da crianca que será buscado no banco de dados;
     * @return {@literal ArrayList<ImagensObj>} lista da busca no banco das
     * imagens que correspondam as informações de parâmetro;
     */
    public ArrayList<ImagensObj> getImagens_lastStage_like_nomeCrianca(String nome_crianca, int year) {
//        System.out.print("\n\ngetImagens_lastStage_like_nomeCrianca:");
        int rows = 0;
        ArrayList<ImagensObj> imagensObjs = new ArrayList<>();
        PreparedStatement stmt;
        ResultSet rs;
        if (con != null) {
            try {
                String sql_get_id = "SELECT " + EM_HARPIA.EM_CHILD.FIELD_ID_CHILD.getAsString_ComCrase() + ""
                        + " FROM " + EM_HARPIA.EM_CHILD.TABLE_NAME.getAsString_ComCrase() + ""
                        + " WHERE " + EM_HARPIA.EM_CHILD.FIELD_EM_NAME_CHILD.getAsString_ComCrase() + ""
                        + " LIKE '" + nome_crianca + "'"
                        + " AND " + EM_HARPIA.EM_CHILD.FIELD_EM_CHILD_ATIVO.getAsString_ComCrase() + " LIKE 'Sim'"
                        + " AND " + EM_HARPIA.EM_CHILD.FIELD_EM_CHILD_ANO.getAsString_ComCrase() + " = " + year + "";
                stmt = con.prepareStatement(sql_get_id);
                rs = stmt.executeQuery();
                int id_crianca = -1;
//                System.out.print("\n\t\tsql_get_id:" + sql_get_id);
                while (rs.next()) {
                    id_crianca = (rs.getInt(EM_HARPIA.EM_CHILD.FIELD_ID_CHILD.getAsString_SemCrase()) != 0)
                            ? (rs.getInt(EM_HARPIA.EM_CHILD.FIELD_ID_CHILD.getAsString_SemCrase())) : (id_crianca);
                }

                if (id_crianca == -1) {
                    erroEhIdInvalido = true;
                    return null;
                }
                erroEhIdInvalido = false;

                String sql = "SELECT " + EM_HARPIA.EM_HARPIA_IMAGESIN.FIELD_URL_IN.getAsString_ComCrase() + ","
                        + " " + EM_HARPIA.EM_HARPIA_IMAGESIN.FIELD_URL_OUT.getAsString_ComCrase() + ","
                        + " " + EM_HARPIA.EM_HARPIA_IMAGESIN.FIELD_DATE_OUT.getAsString_ComCrase() + ","
                        + " " + EM_HARPIA.EM_HARPIA_IMAGESIN.FIELD_LAST_STAGE.getAsString_ComCrase() + ""
                        + " FROM " + EM_HARPIA.EM_HARPIA_IMAGESIN.TABLE_NAME.getAsString_ComCrase() + ""
                        + " WHERE " + EM_HARPIA.EM_HARPIA_IMAGESIN.FIELD_ID_CHILD.getAsString_ComCrase() + " = " + id_crianca + "";
                stmt = con.prepareStatement(sql);
                rs = stmt.executeQuery();
//                System.out.print("\n\t\tsql:" + sql);
                while (rs.next()) {
                    String url_in = (rs.getString(EM_HARPIA.EM_HARPIA_IMAGESIN.FIELD_URL_IN.getAsString_SemCrase()) != null) ? (rs.getString(EM_HARPIA.EM_HARPIA_IMAGESIN.FIELD_URL_IN.getAsString_SemCrase())) : ("");
                    String url_out = (rs.getString(EM_HARPIA.EM_HARPIA_IMAGESIN.FIELD_URL_OUT.getAsString_SemCrase()) != null) ? (rs.getString(EM_HARPIA.EM_HARPIA_IMAGESIN.FIELD_URL_OUT.getAsString_SemCrase())) : ("");
                    String date_out = (rs.getTimestamp(EM_HARPIA.EM_HARPIA_IMAGESIN.FIELD_DATE_OUT.getAsString_SemCrase()).toString() != null) ? (rs.getTimestamp(EM_HARPIA.EM_HARPIA_IMAGESIN.FIELD_DATE_OUT.getAsString_SemCrase()).toString()) : ("");
                    String last_stage = (rs.getString(EM_HARPIA.EM_HARPIA_IMAGESIN.FIELD_LAST_STAGE.getAsString_SemCrase()) != null) ? (rs.getString(EM_HARPIA.EM_HARPIA_IMAGESIN.FIELD_LAST_STAGE.getAsString_SemCrase())) : ("");
//                    System.out.println(url_in + "\t" + url_out + "\t" + date_out + "\t" + last_stage);
                    imagensObjs.add(new ImagensObj(url_in, url_out, date_out, last_stage));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            System.err.println("Nao ha conexao com o bd\nFavor utilizar o metodo connect()");
        }
        return imagensObjs;
    }

    /* --------  Getters  ----------*/
    /**
     * @return o objeto da classe Connection
     */
    public Connection getCon() {
        return con;
    }

    /**
     * @return o objeto da classe Statement
     */
    public Statement getSt() {
        return st;
    }

    /**
     * @return o último id inserido (INSERT)
     */
    public int getLast_id_inserted() {
        return last_id_inserted;
    }

}
