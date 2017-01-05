/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

/**
 *
 * @author _
 */
public class IO {

    public static void criarTxtdoTempo(String fileName, String tempo) {
        try {
            String str_tempo = tempo + "\n";
            PrintWriter writer = new PrintWriter(fileName + ".txt", "UTF-8");
            writer.println(str_tempo);
            writer.close();
        } catch (FileNotFoundException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
}
