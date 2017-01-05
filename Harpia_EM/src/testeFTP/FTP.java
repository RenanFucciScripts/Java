/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package testeFTP;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import org.apache.commons.net.ftp.FTPClient;

/**
 *
 * @author _
 */
public class FTP {

    public static void main(String[] args) {
        FTP ftp = new FTP();
        JsonObject jsonObject = new JsonObject();
        String iphost = "ftp.inoxtrading.com.br";//"127.0.0.0";
        String user = "inoxtrading.com.br";
        String pwd = "@MeuEnzo1107";
        String port = "";
        String url_out = "";

        FTP.setConfigFTP(user, iphost, pwd, port);
        FTP.downloadFiles(FTP.getJsonFTP(), url_out);
    }

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

    public static boolean downloadFiles(JsonObject jsonObject, String url_out) {
        try {
            DirectoryChooser directoryChooser = new DirectoryChooser();
            directoryChooser.setTitle("Selecionar pastas com arquivos somente no formato PDF ou de imagens(BMP, JPG, JPEG ou PNG)");
            directoryChooser.setInitialDirectory(new File(FTP.getUltimoDirFTP()));
            File file = directoryChooser.showDialog(null);

            String iphost = jsonObject.get("iphost").getAsString();
            String str_aux = "";
            String USER = jsonObject.get("user").getAsString(),
                    PWD = jsonObject.get("pwd").getAsString(),
                    PORT = (jsonObject.get("port").getAsString());
            if(USER.contentEquals("")||PWD.contentEquals("")){
                
            }
            FTPClient con = new FTPClient();
            con.login(USER, PWD);

            for (String split : iphost.split("\\.")) {
                str_aux += split;
            }
            if (str_aux.matches("\\d+")) { // so numero
                System.out.println("numero");
                InetAddress HOST = InetAddress.getByName(iphost);
                System.out.println("HOST:\t" + HOST.getHostAddress());
                if (PORT != null || PORT.contentEquals("")) {
                    con.connect(HOST);
                } else {
                    con.connect(HOST, Integer.parseInt(PORT));
                }
            } else if (str_aux.matches("[a-zA-Z]+")) { // so letra
                System.out.println("letra");
                String HOST = iphost;
                System.out.println("HOST:\t" + HOST);
                if (PORT != null || PORT.contentEquals("")) {
                    con.connect(HOST);
                } else {
                    con.connect(HOST, Integer.parseInt(PORT));
                }
            } else {
                System.err.println("IP");
            }
            if ((file.exists()) ? (true) : (false)) {
                con.enterLocalPassiveMode(); // importante !!!!
                OutputStream out = new FileOutputStream(file);
                boolean result = con.retrieveFile(url_out, out);
                con.logout();
                con.disconnect();
                if (result) {
                    return true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static JsonObject getJsonFTP() {
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

    public static void setConfigFTP(String user, String iphost, String pwd, String port) {
        File fl = new File("config/");
        if (!fl.exists()) {
            fl.mkdir();
        }
        final String dir_padrao = "config/ftp.json";
        try (PrintWriter fw = new PrintWriter(dir_padrao);
                BufferedWriter bw = new BufferedWriter(fw);) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("iphost", iphost);
            jsonObject.addProperty("user", user);
            jsonObject.addProperty("pwd", pwd);
            jsonObject.addProperty("port", (port == null || port.contentEquals("")) ? ("") : (port));
            bw.write(jsonObject.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
