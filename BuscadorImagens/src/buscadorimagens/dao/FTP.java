package buscadorimagens.dao;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.net.ftp.FTPSClient;

/**
 * Classe DAO para acessar as informações de um Servidor FTP;
 *
 * @author RenanFucci
 */
public class FTP {

    /**
     * @author RenanFucci<br>
     * <strong>Método setUltimoDirFTP</strong><br>
     * Método setar o ultimo diretorio para download de arquivos do servidor
     * FTP.<br>
     * @param dir String do diretorio escolhido;
     */
    public static void setUltimoDirFTP(String dir) {
        final String dir_padrao = "ultimoDirFTP.json";
        try (PrintWriter fw = new PrintWriter(dir_padrao);
                BufferedWriter bw = new BufferedWriter(fw);) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("dir", dir);
            bw.write(jsonObject.toString());
        } catch (Exception ex) {
            Logger.getLogger(FTP.class.getName()).log(Level.SEVERE, ex.getLocalizedMessage(), ex);
        }
    }

    /**
     * @author RenanFucci<br>
     * <strong>Método getUltimoDirFTP</strong><br>
     * Método pegar o ultimo diretorio do FTP setado no método
     * {@link #setUltimoDirFTP(java.lang.String)  setUltimoDirFTP()}.<br>
     * @return String do diretorio setado;
     */
    public static String getUltimoDirFTP() {
        try {
            JsonParser parser = new JsonParser();
            Object obj = parser.parse(new FileReader("ultimoDirFTP.json"));
            JsonObject jsonObject = (JsonObject) obj;
            String str_dir = jsonObject.get("dir").getAsString();
            if (str_dir != null && new File(str_dir).exists() && new File(str_dir).isDirectory()) {
                return str_dir;
            }
        } catch (Exception ex) {
            System.err.println(ex.getLocalizedMessage());
        }
        return System.getProperty("user.home");
    }

    /**
     * @author RenanFucci<br>
     * <strong>Método downloadFiles</strong><br>
     * Método para fazer o download de um arquivo em um servidor FTP e salvá-lo
     * no diretório do método
     * {@link #setUltimoDirFTP(java.lang.String) setUltimoDirFTP()}.<br>
     * @param jsonObject JsonObject com as configurações de conexão do servidor
     * FTP;
     * @param url_out String com o diretório de onde está o arquivo no servidor
     * FTP;
     * @param folder_download File com o diretório de onde o arquivo será salvo
     * no SO;
     * @return Boolean se o arquivo foi baixado com sucesso ou não.
     */
    public static boolean downloadFiles(JsonObject jsonObject, String url_out, File folder_download) {
        try {

            String HOST = jsonObject.get("iphost").getAsString().trim(),
                    ROOT_FOLDER = jsonObject.get("root_folder").getAsString();
            String USER = jsonObject.get("user").getAsString().trim(),
                    PWD = jsonObject.get("pwd").getAsString(),
                    PORT = (jsonObject.get("port").getAsString()).trim();

//            System.out.println("HOST:\t[" + HOST + "]");
//            System.out.println("ROOT FOLDER:\t[" + ROOT_FOLDER + "]");
//            System.out.println("USER:\t[" + USER + "]");
//            System.out.println("PORT:\t[" + PORT + "]");
//            System.out.println("PWD:\t[" + PWD + "]");
            FTPSClient con = new FTPSClient();

            if (PORT == null || PORT.contentEquals("")) {
                con.connect(HOST, con.getDefaultPort());
            } else {
                con.connect(HOST, Integer.parseInt(PORT));
            }
//            System.out.println("connected:\t[" + con.getReplyString() + "]");
            boolean logged = con.login(USER, PWD);
            if (logged) {
                con.enterLocalPassiveMode();
                con.setFileType(org.apache.commons.net.ftp.FTP.BINARY_FILE_TYPE);
                File fl_out = new File((folder_download.getAbsolutePath() + "/" + (url_out.substring(url_out.lastIndexOf("/")))));
                System.out.println("\nfl_out:\t" + fl_out);
                folder_download.setWritable(true);
                folder_download.setReadable(true);
                OutputStream out = new FileOutputStream(fl_out);
                boolean result = con.retrieveFile(url_out, out);
                con.logout();
                con.disconnect();
                if (result) {
                    return true;
                }
            }
        } catch (Exception e) {
            System.err.println(e.getLocalizedMessage());
            e.printStackTrace();
        }
        return false;
    }

    /**
     * @author RenanFucci<br>
     * <strong>Método testFTP_con</strong><br>
     * Método para testar a conexão com o servidor FTP com as informações
     * setadas.<br>
     * @param jsonObject JsonObject com as configurações do servidor FTP;
     * @return Boolean se o arquivo foi baixado com sucesso ou não.
     */
    public static boolean testFTP_con(JsonObject jsonObject) {
        try {
            String HOST = jsonObject.get("iphost").getAsString().trim(),
                    ROOT_FOLDER = jsonObject.get("root_folder").getAsString();
            String USER = jsonObject.get("user").getAsString().trim(),
                    PWD = jsonObject.get("pwd").getAsString(),
                    PORT = (jsonObject.get("port").getAsString()).trim();
//            System.out.println("\n\nHOST:\t[" + HOST + "]");
//            System.out.println("ROOT FOLDER:\t[" + ROOT_FOLDER + "]");
//            System.out.println("USER:\t[" + USER + "]");
//            System.out.println("PORT:\t[" + PORT + "]");
//            System.out.println("PWD:\t[" + PWD + "]");

            FTPSClient con = new FTPSClient();
            con.enterLocalPassiveMode();
            if (PORT == null || PORT.contentEquals("")) {
//                System.out.println("wout/ port");
                con.connect(HOST);
            } else {
//                System.out.println("w/ port");
                con.connect(HOST, Integer.parseInt(PORT));
            }
            boolean b = con.login(USER, PWD);
//            System.out.println("connected:\t"+b);
            return b;
        } catch (Exception ex) {
            System.err.println(ex.getLocalizedMessage());
            ex.printStackTrace();
        }
        return false;
    }

    /**
     * @author RenanFucci<br>
     * <strong>Método getConfigFTP</strong><br>
     * Método para pegar a configuração de conexão com o servidor FTP setadas no
     * método
     * {@link #setConfigFTP(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String) #setConfigFTP()}
     * .<br>
     * @return JsonObject com as configurações de conexão do servidor FTP.
     */
    public static JsonObject getConfigFTP() {
        try {
            JsonParser parser = new JsonParser();
            Object obj = parser.parse(new FileReader("config/ftp.json"));
            JsonObject jsonObject = (JsonObject) obj;
            return jsonObject;
        } catch (Exception ex) {
            System.err.println("getJsonFTP:\tex:\t" + ex.getLocalizedMessage());
        }
        return null;
    }

    /**
     * @author RenanFucci<br>
     * <strong>Método setConfigFTP</strong><br>
     * Método setar as configurações de conexão com o servidor FTP.<br>
     * @param user String com o nome do usuário do FTP;
     * @param iphost String com o ip do host do FTP;
     * @param root_folder String com a pasta raiz do FTP;
     * @param pwd String com a senha do FTP;
     * @param port String com a porta de conexão do FTP;
     */
    public static void setConfigFTP(String user, String iphost,
            String root_folder, String pwd, String port) {
        File fl = new File("config/");
        if (!fl.exists()) {
            fl.mkdir();
        }
        final String dir_padrao = "config/ftp.json";
        try (PrintWriter fw = new PrintWriter(dir_padrao);
                BufferedWriter bw = new BufferedWriter(fw);) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("iphost", iphost);
            jsonObject.addProperty("root_folder", root_folder);
            jsonObject.addProperty("user", user);
            jsonObject.addProperty("pwd", pwd);
            jsonObject.addProperty("port", (port == null || port.contentEquals("")) ? ("") : (port));
            bw.write(jsonObject.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
