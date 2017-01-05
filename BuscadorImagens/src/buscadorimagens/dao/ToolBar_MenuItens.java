package buscadorimagens.dao;

import buscadorimagens.BuscadorImagens;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.stage.Stage;

/**
 * Classe as funções de todas as barras de menu em todas as telas;
 *
 * @author RenanFucci
 */
public class ToolBar_MenuItens {

    /**
     * @author RenanFucci<br>
     * <strong>Método onAction_ConfigBD</strong><br>
     * Método abrir a tela de Configuração de Banco de Dados.<br>
     * @param event Event do click do item no menu;
     */
    public void onAction_ConfigBD(ActionEvent event) {
        BuscadorImagens.setSceneEditBD((Stage) (((MenuItem) event.getSource()).getParentPopup().getOwnerWindow()));
    }

    /**
     * @author RenanFucci<br>
     * <strong>Método onAction_ConfigFTP</strong><br>
     * Método abrir a tela de Configuração de FTP.<br>
     * @param event Event do click do item no menu;
     */
    public void onAction_ConfigFTP(ActionEvent event) {
        BuscadorImagens.setSceneEditFTP((Stage) (((MenuItem) event.getSource()).getParentPopup().getOwnerWindow()));
    }

    /**
     * @author RenanFucci<br>
     * <strong>Método onAction_Crianca</strong><br>
     * Método abrir a tela de Busca por Criança.<br>
     * @param event Event do click do item no menu;
     */
    public void onAction_Crianca(ActionEvent event) {
        BuscadorImagens.setSceneCrianca((Stage) (((Button) event.getSource()).getScene().getWindow()));
    }

    /**
     * @author RenanFucci<br>
     * <strong>Método onAction_Escola</strong><br>
     * Método abrir a tela de Busca por Escola.<br>
     * @param event Event do click do item no menu;
     */
    public void onAction_Escola(ActionEvent event) {
        BuscadorImagens.setSceneEscola((Stage) (((Button) event.getSource()).getScene().getWindow()));
    }

    /**
     * @author RenanFucci<br>
     * <strong>Método onAction_XML</strong><br>
     * Método abrir a tela de Busca por XML.<br>
     * @param event Event do click do item no menu;
     */
    public void onAction_XML(ActionEvent event) {
        BuscadorImagens.setSceneXML((Stage) (((Button) event.getSource()).getScene().getWindow()));
    }

    /**
     * @author RenanFucci<br>
     * <strong>Método onAction_Arquivo</strong><br>
     * Método abrir a tela de Busca por Arquivo.<br>
     * @param event Event do click do item no menu;
     */
    public void onAction_Arquivo(ActionEvent event) {
        BuscadorImagens.setSceneArquivo((Stage) (((Button) event.getSource()).getScene().getWindow()));
    }
}
