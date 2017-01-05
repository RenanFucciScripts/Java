/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package testeHTTP;

import java.net.URL;
import java.net.URLConnection;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author _
 */
public class TesteHTTP {

    public static void main(String[] args) {
        URL url = null;
        URLConnection con = null;
        int i;
        try {
            url = new URL("https://localhost//");
            con = url.openConnection();
            System.out.println("url.getFile:\t" + url.getPath());
//            File file = new File("C:\\Foldername\\Address.txt");
//            try (BufferedInputStream bis = new BufferedInputStream(
//                    con.getInputStream());
//                    BufferedOutputStream bos = new BufferedOutputStream(
//                            new FileOutputStream(file.getName()));) {
//                while ((i = bis.read()) != -1) {
//                    bos.write(i);
//                }
//                bos.flush();
//            }
        } catch (Exception ex) {
            Logger.getLogger(TesteHTTP.class.getName()).log(Level.SEVERE, ex.getLocalizedMessage(), ex);
        }
    }
}
