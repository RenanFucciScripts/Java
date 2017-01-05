package buscadorimagens.dao;

import java.util.HashMap;
import java.util.Map;

/**
 * Classe DAO para acessar as informações da tabela escola (EM_CHILD) do um
 * banco de dados;
 *
 * @author RenanFucci
 */
public class Escola {

    private String nomeEscola;
    private String dataRegistro;
    private int quantidadeRegistros = 0;
    private int quantidadeArqsZeroByte = 0;
    private Map<String, Integer> map_extensao_qntd = new HashMap<>();

    public Escola(String nomeEscola, String dataRegistro) {
        this.nomeEscola = nomeEscola;
        this.dataRegistro = dataRegistro;
    }

    /**
     * @author RenanFucci<br>
     * <strong>Método addExtensaoArq</strong><br>
     * Método incrementar a quantidade de registros de arquivos.<br>
     */
    void addRegistro() {
        this.quantidadeRegistros += 1;
    }

    /**
     * @author RenanFucci<br>
     * <strong>Método addExtensaoArq</strong><br>
     * Método incrementar a quantidade de registros de arquivos com zero bytes.<br>
     */
    void addArqsZeroByte() {
        this.quantidadeArqsZeroByte += 1;
    }

    /**
     * @author RenanFucci<br>
     * <strong>Método addExtensaoArq</strong><br>
     * Método adicionar uma nova de extensão de arquivo ao HashMap desta classe.
     * Obs.: Se essa extensão já existir, o método incremente a quantidade de
     * registros.<br>
     * @param type_File tipo do arquivo que será inserido;
     */
    void addExtensaoArq(String type_File) {
        int aux = (map_extensao_qntd.containsKey(type_File) ? (map_extensao_qntd.get(type_File)) : (0));
        map_extensao_qntd.put(type_File, aux + 1);
    }

    /**
     * @author RenanFucci<br>
     * <strong>Método toString</strong><br>
     * Sobrescrita do Método toString para retornar uma String com as
     * informações do objeto desta classe.<br>
     * @return String com as informações;
     */
    @Override
    public String toString() {
        String str = "";
        str += "Escola " + nomeEscola + ":\n";
        str += "\t com " + quantidadeRegistros + " registro" + ((quantidadeRegistros > 1) ? ("s") : ("")) + ";\n";
        str += "\t com data de registro " + dataRegistro + ";\n";

        str += "\t com " + quantidadeArqsZeroByte + " arquivo" + ((quantidadeRegistros > 1) ? ("s") : ("")) + " com zero byte;\n";
        str += "\t dos quais:\n";
        for (Map.Entry<String, Integer> entry : map_extensao_qntd.entrySet()) {
            String key = entry.getKey();
            Integer value = entry.getValue();
            str += "\t\t" + value + ((value > 1) ? (" são ") : (" é ")) + key + "\n";
        }
        return str;
    }

    /* --------  Getters  ----------*/
    public String getNomeEscola() {
        return nomeEscola;
    }

    public int getQuantidadeRegistros() {
        return quantidadeRegistros;
    }

    public int getQuantidadeArqsZeroByte() {
        return quantidadeArqsZeroByte;
    }

    public Map<String, Integer> getMap_extensao_qntd() {
        return map_extensao_qntd;
    }

    public String getDataRegistro() {
        return dataRegistro;
    }

}
