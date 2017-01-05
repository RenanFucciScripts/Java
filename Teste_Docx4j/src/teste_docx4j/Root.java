/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package teste_docx4j;

import java.awt.Image;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Root {

    private Image image;

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

}