package buscadorimagens.dao;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Classe DAO para guardar as informações de escolas a partir de um arquivo XML.
 * Estas informações das escolas serão agrupadas por nome de escola pelo padrão
 * de igualdade {@link Object#equals(java.lang.Object) Object.equals(object)}.
 *
 * @author RenanFucci
 *
 * Padrão da Tag RemotePath do XML gerado pelo FileZilla Server:<br>
 * "RemotePath" Num1 Num2 Num3 ParentFolder Num4 ChildFolder "RemotePath"<br>
 * Num1 = Server Type. List in "src/include/server.h". <br>
 * Num2 = Prefix Length of Num1 (Server Type). <br>
 * Num3 = Length of the ParentFolder String. <br>
 * Num4 = Length of the ChildFolder String. <br>
 *
 * Como usar: <br>
 * String filePath_XML = "" + "FileZilla.xml211020116.xml"; <br>
 * Path path = Paths.get(filePath_XML); <br>
 * System.out.println(path.getFileName().toAbsolutePath().toString()); <br>
 * AgruparPorEscolaXML porEscolaXML = new AgruparPorEscolaXML(filePath_XML);
 * <br>
 * porEscolaXML.imprimirPorEscola(); <br>
 * System.out.println(porEscolaXML.getJsonPorEscola().toString()); <br>
 *
 */
public class AgruparPorEscolaXML {

    private Map<String, Escola> map_escolas = new HashMap<>();

    /**
     * @author RenanFucci<br>
     * <strong>Método AgruparPorEscolaXML</strong><br>
     * Método construtor para agrupar por nome as escolas que estão num arquivo
     * XML.<br>
     * @param filePath_XML String do diretorio do arquivo XML;
     */
    public AgruparPorEscolaXML(String filePath_XML) {
        try {
            File file = new File(filePath_XML);
            DocumentBuilder dBuilder = DocumentBuilderFactory.newInstance()
                    .newDocumentBuilder();
            Document doc = dBuilder.parse(file);
            doc.getDocumentElement().normalize();
            NodeList nList = doc.getElementsByTagName("File");
            for (int temp = 0; temp < nList.getLength(); temp++) {
                Node nNode = nList.item(temp);
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;
                    String str_RemotePath = (eElement.getElementsByTagName("RemotePath").item(0).getTextContent());

                    String[] aux = str_RemotePath.split("\\s");
                    String str_DataRegistro = aux[3];
                    str_DataRegistro = ""
                            + "" + str_DataRegistro.substring(0, 2)
                            + "-" + str_DataRegistro.substring(2, 4)
                            + "-" + str_DataRegistro.substring(4, 8);

                    String str_nomeEscola = "";
                    for (int i = 5; i < aux.length; i++) {
                        str_nomeEscola += ((i > 5) ? (" ") : ("")) + aux[i];
                    }
                    Escola escola = ((map_escolas.containsKey(str_nomeEscola)) ? (map_escolas.get(str_nomeEscola)) : (new Escola(str_nomeEscola, str_DataRegistro)));
                    escola.addRegistro();
                    int sizeFile = Integer.parseInt(eElement.getElementsByTagName("Size").item(0).getTextContent());
                    if (sizeFile == 0) {
                        escola.addArqsZeroByte();
                    }
                    String str_RemoteFile = (eElement.getElementsByTagName("RemoteFile").item(0).getTextContent());
                    String type_File = str_RemoteFile.substring(str_RemoteFile.lastIndexOf("."));
                    escola.addExtensaoArq(type_File);
                    map_escolas.put(str_nomeEscola, escola);
                }
            }
        } catch (Exception ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, ex.getLocalizedMessage(), ex);
        }
    }

    /**
     * @author RenanFucci<br>
     * <strong>Método imprimirPorEscola</strong><br>
     * Método para imprimir alfabéticamente as escolas no console da JVM.<br>
     */
    public void imprimirPorEscola() {
        List<String> escolas_asc = new ArrayList<>(map_escolas.keySet());
        escolas_asc.sort(String::compareTo);
        escolas_asc.stream().forEach((string) -> {
            System.out.println(map_escolas.get(string).toString());
        });
    }

    /**
     * @author RenanFucci<br>
     * <strong>Método getJsonPorEscola</strong><br>
     * Método para pegar o arquivo JSON com a lista de escolas.
     * <br>
     * @return JsonArray com a lista de escolas do JSON;
     */
    public JsonArray getJsonPorEscola() {
        JsonArray jsonArray_porEscola = new JsonArray();
        List<String> escolas_asc = new ArrayList<>(map_escolas.keySet());
        escolas_asc.sort(String::compareTo);
        escolas_asc.stream().forEach((string) -> {
            JsonObject jsonObject_porEscola = new JsonObject();
            jsonObject_porEscola.addProperty("NomeEscola", map_escolas.get(string).getNomeEscola());
            jsonObject_porEscola.addProperty("DataRegistro", map_escolas.get(string).getDataRegistro());
            jsonObject_porEscola.addProperty("QntdRegistros", map_escolas.get(string).getQuantidadeRegistros());
            jsonObject_porEscola.addProperty("QntdArqsZeroByte", map_escolas.get(string).getQuantidadeArqsZeroByte());
            JsonArray jsonArray_map_ext_qntd = new JsonArray();
            map_escolas.get(string).getMap_extensao_qntd().entrySet().stream().map((entry) -> {
                JsonObject jsonObject_ext_qntd = new JsonObject();
                jsonObject_ext_qntd.addProperty(entry.getKey(), entry.getValue());
                return jsonObject_ext_qntd;
            }).forEach((jsonObject_ext_qntd) -> {
                jsonArray_map_ext_qntd.add(jsonObject_ext_qntd);
            });
            jsonObject_porEscola.add("Map_Ext_Qntd", jsonArray_map_ext_qntd);
            jsonArray_porEscola.add(jsonObject_porEscola);
        });
        return jsonArray_porEscola;
    }
}
