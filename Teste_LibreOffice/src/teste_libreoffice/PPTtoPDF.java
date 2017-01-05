/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package teste_libreoffice;

import com.sun.star.beans.PropertyValue;
import com.sun.star.comp.helper.Bootstrap;
import com.sun.star.frame.XComponentLoader;
import com.sun.star.frame.XController;
import com.sun.star.frame.XModel;
import com.sun.star.frame.XStorable;
import com.sun.star.lang.XComponent;
import com.sun.star.lang.XMultiComponentFactory;
import com.sun.star.uno.UnoRuntime;
import com.sun.star.uno.XComponentContext;
import com.sun.star.uno.XInterface;
import com.sun.star.util.CloseVetoException;
import com.sun.star.util.XCloseable;
import com.sun.star.view.XSelectionSupplier;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author _
 */
public class PPTtoPDF {

    private PropertyValue[] propertyValues;

    public static void main(String[] args) {
        String path = "C:/Users/_/Dropbox/Estante Magica_Renan_Fucci/Pasta_Renan_Fucci/2016-10-09/"
                + "rf15 - Teste Renan PNG"
                + ".pptx";
        new PPTtoPDF(path);
    }

    public PPTtoPDF(String path) {
        XComponent oDocToStore = null;
        Object desktop = null;
        XComponentContext xContext = null;
        try {
            String dir = "file:///" + path;
            String loadUrl = dir;
            String convertUrl = loadUrl.substring(0, loadUrl.lastIndexOf(".")) + ".pdf";

            desktop = createDesktop(xContext);
            oDocToStore = loadExistingDocument(desktop, loadUrl);

            XStorable xStorable = UnoRuntime.queryInterface(XStorable.class, oDocToStore);
            export(xStorable, convertUrl, desktop);
        } catch (Exception ex) {
            Logger.getLogger(PPTtoPDF.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (oDocToStore != null) {
                try {
                    XCloseable xCloseable = (XCloseable) UnoRuntime.queryInterface(
                            XCloseable.class, oDocToStore);
                    xCloseable.close(true);
                    com.sun.star.lang.XComponent xComp
                            = UnoRuntime.queryInterface(
                                    com.sun.star.lang.XComponent.class, oDocToStore);

                    xComp.dispose();
                } catch (CloseVetoException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    private Object createDesktop(XComponentContext xContext) {
        try {
            // get the remote office component context
            xContext = Bootstrap.bootstrap();
            System.out.println("Connected to a running office ...");

            // get the remote office service manager
            XMultiComponentFactory xMCF = xContext.getServiceManager();

            Object oDesktop = xMCF.createInstanceWithContext(
                    "com.sun.star.frame.Desktop", xContext);
            return oDesktop;
        } catch (com.sun.star.uno.Exception ex) {
            Logger.getLogger((this.getClass().getName())).log(Level.SEVERE, ex.getMessage(), ex);
        } catch (Exception ex) {
            Logger.getLogger((this.getClass().getName())).log(Level.SEVERE, ex.getMessage(), ex);
        }
        return null;
    }

    private XComponent loadExistingDocument(Object desktop, String loadUrl) {
        try {  // query the XComponentLoader interface from the Desktop service
            XComponentLoader xComponentLoader = (XComponentLoader) UnoRuntime.queryInterface(
                    XComponentLoader.class, desktop);

            // define load properties according to com.sun.star.document.MediaDescriptor
            /* or simply create an empty array of com.sun.star.beans.PropertyValue structs:
             PropertyValue[] loadProps = new PropertyValue[0]
             */
            // the boolean property Hidden tells the office to open a file in hidden mode
            propertyValues = new com.sun.star.beans.PropertyValue[2];

            propertyValues[0] = new PropertyValue();
            propertyValues[0].Name = "Hidden";
            propertyValues[0].Value = true;

            // load
            return xComponentLoader.loadComponentFromURL(loadUrl, "_blank", 0, propertyValues);
        } catch (Exception ex) {
            Logger.getLogger((this.getClass().getName())).log(Level.SEVERE, ex.getMessage(), ex);
        }
        return null;
    }

    private void export(XStorable xStorable, String sStoreUrl, Object desktop) {
        try {

            propertyValues = new com.sun.star.beans.PropertyValue[3];

            propertyValues[0] = new com.sun.star.beans.PropertyValue();
            propertyValues[0].Name = "Selection";
            propertyValues[0].Value = select(xStorable, desktop);

            propertyValues[1] = new com.sun.star.beans.PropertyValue();
            propertyValues[1].Name = "Quality";
            propertyValues[1].Value = new Long(100);

            propertyValues[2] = new PropertyValue();
            propertyValues[2].Name = "DisplayPDFDocumentTitle";
            propertyValues[2].Value = Boolean.FALSE;

            PropertyValue[] aFilterData = propertyValues;
            // Preparing properties for converting the document
            propertyValues = new com.sun.star.beans.PropertyValue[3];
            // Setting the filter name
            propertyValues[0] = new com.sun.star.beans.PropertyValue();
            propertyValues[0].Name = "Overwrite";
            propertyValues[0].Value = Boolean.TRUE;

            propertyValues[1] = new com.sun.star.beans.PropertyValue();
            propertyValues[1].Name = "FilterName";
            propertyValues[1].Value = "writer_pdf_Export";
            // Setting the filter name
            propertyValues[2] = new com.sun.star.beans.PropertyValue();
            propertyValues[2].Name = "FilterData";
            propertyValues[2].Value = aFilterData;

            xStorable.storeToURL(sStoreUrl, propertyValues);
            // Closing the converted document. Use XCloseable.close if the
            // interface is supported, otherwise use XComponent.dispose
        } catch (Exception ex) {
            Logger.getLogger(PPTtoPDF.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (xStorable != null) {
                try {
                    XCloseable xCloseable = (XCloseable) UnoRuntime.queryInterface(
                            XCloseable.class, xStorable);
                    xCloseable.close(true);
                } catch (CloseVetoException ex) {
                    ex.printStackTrace();
                }
            } else {
                XComponent xComp
                        = UnoRuntime.queryInterface(
                                com.sun.star.lang.XComponent.class, xStorable);

                xComp.dispose();
            }
        }
    }

    private Object select(XInterface xInterface, Object aAny) {
        Object oSelection = null;
        try {
            XModel xModel = (XModel) UnoRuntime.queryInterface(
                    XModel.class, xInterface);
            if (xModel != null) {
                XController xController = xModel.getCurrentController();
                XSelectionSupplier xSelectionSupplier
                        = (XSelectionSupplier) UnoRuntime.queryInterface(
                                XSelectionSupplier.class, xController);
                xSelectionSupplier.select(aAny);
                // there is really no need to retrieve the selection
                oSelection = xSelectionSupplier.getSelection();
            }
        } finally {
            return oSelection;
        }
    }
}
