package br.com.rrf.harpia_em_mv;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DB_DAO {

    private Connection con; //Conexao com BD
    private Statement st; //Requisicao de conexao
    private int last_id_inserted;//Ultimo id inserido no banco
    private PreparedStatement prepStmt_insert;//Obj de Requisicao para INSERT
    private PreparedStatement prepStmt_update;//Obj de Requisicao para UPDATE

    //Informacoes de conexao - Localhost
    private static final String DB_URL = "jdbc:mysql://localhost:3306/em_harpia?autoReconnect=true&useSSL=false";
    private static final String DB_USER = "root";
    private static final String DB_PWD = "";

    /**
     * <b>@author Renan Fucci<br></b>
     * <b>Metodo connect</b><br>
     * Metodo para fazer a conexao com o BD .
     *
     * @return o resultado booleano da conexao.
     */
    public boolean connect() {
        try {
            //System.out.println("DB_Dao.connect");
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection(DB_URL, DB_USER, DB_PWD);
            st = con.createStatement();
            return true;
        } catch (SQLException e) {
            System.err.println("sql con: " + e.getErrorCode() + "\n" + e.getMessage());
            e.printStackTrace();
        } catch (Exception ex) {
            System.err.println("exce: " + ex.getLocalizedMessage());
            ex.printStackTrace();
        }
        return false;
    }

    /**
     * <b>@author Renan Fucci<br></b>
     * <b>Metodo isConnected</b><br>
     * Metodo para testar se esta connectado com o BD .
     * @return o resultado booleano da teste.
     */
    public boolean isConnected() {
        try {
            if (!con.isClosed()) {
                return true;
            }
        } catch (Exception ex){
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
     * <b>@author Renan Fucci<br></b>
     * <b>Metodo createStmt_Insert</b><br>
     * Metodo para criar um objeto de requisicao INSERT no BD.
     *
     * @return o resultado booleano da criacao do Obj.
     */
    public boolean createStmt_Insert() {
        //System.out.println("DB_DAO.createStmt_Insert");
        try {
            String sql = "INSERT INTO `em_harpia_imagesin`(`url_in`, `date_in`,`type_file`, `converted_file`, `id_parent`, `status_file`) "
                    + "VALUES"
                    + " (?,?,?,?,?,?)";

            prepStmt_insert = con.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * <b>@author Renan Fucci<br></b>
     * <b>Metodo executeStmt_Insert</b><br>
     * Metodo para executar o Obj de requisicao INSERT criado no metodo
     * {@link #createStmt_Insert()}.<br>
     *
     * @param url_in diretorio do arquivo;
     * @param type_file extensao do arquivo;
     * @param converted_file se o arquivo (!='.jpg'), foi convertido;
     * @param id_parent id do pai deste arquivo no BD, se tiver. Se não, 0;
     * @return se verdadeiro, o id do INSERT e caso contrario, -1.
     */
    public int executeStmt_Insert(String url_in, String type_file, String converted_file, int id_parent) {
        //System.out.println("DB_DAO.executeStmt_Insert");
        int id_inserted = -1;
        if (prepStmt_insert == null) {
            System.err.println("Favor criar o Statement utilizando o metodo 'createStmt_Insert' antes de chamar este metodo.");
            return id_inserted;
        } else {
            try {
                String date_in = (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(new Date());
                String status = "01-WaitingProcessing";
                prepStmt_insert.setString(1, url_in);
                prepStmt_insert.setString(2, date_in);
                prepStmt_insert.setString(3, type_file);
                prepStmt_insert.setString(4, converted_file);
                prepStmt_insert.setInt(5, id_parent);
                prepStmt_insert.setString(6, status);
                prepStmt_insert.executeUpdate();
                ResultSet keys = prepStmt_insert.getGeneratedKeys();
                keys.next();
                id_inserted = keys.getInt(1);
                last_id_inserted = id_inserted;
                return id_inserted;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return id_inserted;
    }

    /**
     * <b>@author Renan Fucci<br></b>
     * <b>Metodo createStmt_Update</b><br>
     * Metodo para criar um objeto de requisicao UPDATE no BD.
     *
     * @return o resultado booleano da criacao do Obj.
     */
    public boolean createStmt_Update() {
        //System.out.println("DB_DAO.createStmt_Update");
        try {
            String sql = "UPDATE `em_harpia_imagesin` SET `converted_file`=? WHERE `id`=?";

            prepStmt_update = con.prepareStatement(sql);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * <b>@author Renan Fucci<br></b>
     * <b>Metodo executeStmt_Update</b><br>
     * Metodo para executar o Obj de requisicao INSERT criado no metodo
     * {@link #createStmt_Update()}.<br>
     *
     * @param id id do arquivo no banco;
     * @param converted_file se o arquivo (!='.jpg'), foi convertido;
     * @return o resultado booleano da requisicao.
     */
    public boolean executeStmt_Update(int id, String converted_file) {
        if (prepStmt_update == null) {
            System.err.println("Favor criar o Statement utilizando o metodo 'createStmt_Update' antes de chamar este metodo.");
        } else {
            try {
                prepStmt_update.setString(1, converted_file);
                prepStmt_update.setInt(2, id);
                boolean executed = prepStmt_update.execute();
                return executed;
            } catch (SQLException e) {
                System.err.println(e.getMessage());
                e.printStackTrace();
            }
        }

        return false;
    }

    public int getQuantidadeStatus_01_WaitingProcessing() {
        int rows = 0;
        if (con != null) {
            try {
                String sql = "SELECT * FROM `em_harpia_imagesin` WHERE `status` LIKE '01-WaitingProcessing'";
                PreparedStatement stmt = con.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery();
                if (rs.last()) {
                    rows = rs.getRow();
                    rs.beforeFirst();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            System.err.println("Nao ha conexao com o bd\nFavor utilizar o metodo connect()");
        }
        return rows;

    }

    public boolean hasWaintingProcessItems() {
        //System.out.println("DB_DAO.hasWaintingProcessItems");
        int rows = 0;
        if (con != null) {
            try {
                String sql = "SELECT `id` FROM `em_harpia_imagesin` "
                        + "WHERE `status_file` LIKE '01-WaitingProcessing'";
                PreparedStatement stmt = con.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery();
                if (rs.last()) {
                    rows = rs.getRow();
                    rs.beforeFirst();
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            System.err.println("Nao ha conexao com o bd\nFavor utilizar o metodo connect()");
        }

        return ((rows > 0) ? true : false);
    }

    /* --------  Getters  ----------*/
    /**
     * @return the con
     */
    public Connection getCon() {
        return con;
    }

    /**
     * @return the st
     */
    public Statement getSt() {
        return st;
    }

    /**
     * @return the last_id_inserted
     */
    public int getLast_id_inserted() {
        return last_id_inserted;
    }

}
