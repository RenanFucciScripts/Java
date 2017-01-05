/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package teste_libreoffice;

import com.sun.star.beans.PropertyValue;
import com.sun.star.beans.XPropertyAccess;
import com.sun.star.io.IOException;
import com.sun.star.uno.XComponentContext;
import com.sun.star.comp.helper.Bootstrap;
import com.sun.star.document.XExporter;
import com.sun.star.frame.XComponentLoader;
import com.sun.star.frame.XStorable;
import com.sun.star.lang.XComponent;
import com.sun.star.lang.XMultiComponentFactory;
import com.sun.star.ui.dialogs.ExecutableDialogResults;
import com.sun.star.ui.dialogs.XExecutableDialog;
import com.sun.star.uno.AnyConverter;
import com.sun.star.uno.UnoRuntime;
import com.sun.star.util.CloseVetoException;
import com.sun.star.util.XCloseable;
import com.sun.star.util.XStringSubstitution;

public class PDFExportDialog {

    private final XComponentContext m_xContext;

    public PDFExportDialog(XComponentContext xContext) {
        m_xContext = xContext;
    }

    public static void main(String[] args) {
        try {
            // get the remote office component context
            XComponentContext xContext = Bootstrap.bootstrap();

            PDFExportDialog demo = new PDFExportDialog(xContext);
            demo.run();

        } catch (java.lang.Exception e) {
            e.printStackTrace();
        } finally {
            System.exit(0);
        }
    }

    public void run() {

        XMultiComponentFactory xMCF = m_xContext.getServiceManager();

        Object oPDFDialog = null;

        try {
            oPDFDialog = xMCF.createInstanceWithContext(
                    "com.sun.star.document.PDFDialog", m_xContext);
        } catch (com.sun.star.uno.Exception ex) {
            ex.printStackTrace();
        }

        // get all interface references
        XExecutableDialog xExecutableDialog
                = (XExecutableDialog) UnoRuntime.queryInterface(
                        XExecutableDialog.class, oPDFDialog);

        XExporter xExporter = (XExporter) UnoRuntime.queryInterface(
                XExporter.class, xExecutableDialog);

        XPropertyAccess xPropertyAccess
                = (XPropertyAccess) UnoRuntime.queryInterface(
                        XPropertyAccess.class, xExporter);

        // check them all
        if (xExecutableDialog == null
                || xExporter == null || xPropertyAccess == null) {
            System.out.println("something went wrong with the PDFDialog!");
            return;
        }

        XComponent xComponent = null;
        try {
            // change the following to test with different doc. types
            // but remember to change the filter name below
            xComponent = createNewDoc(m_xContext, "swriter");

            // set the source document
            xExporter.setSourceDocument(xComponent);

            // set the title
            xExecutableDialog.setTitle("Demo Export PDF Dialog OOo API");

            // execute the dialog
            int nResult = xExecutableDialog.execute();

            if (nResult != ExecutableDialogResults.OK) {
                System.out.println("you didn't press OK! so do nothing...");
                return;
            }

            // get the options the user has choosen
            PropertyValue[] aFilterData = null;

            PropertyValue[] aPropertyValues = xPropertyAccess.getPropertyValues();
            for (int i = 0; i < aPropertyValues.length; i++) {
                PropertyValue propertyValue = aPropertyValues[i];
                if (propertyValue.Name.equals("FilterData")) {
                    try {
                        aFilterData = (PropertyValue[]) AnyConverter.toObject(
                                PropertyValue[].class, propertyValue.Value);

                    } catch (com.sun.star.lang.IllegalArgumentException ex) {
                        ex.printStackTrace();
                    }
                    break;
                }
            }

            if (aFilterData != null) {
                // print the properties and their values
                printPropertyValues(aFilterData);

                // finally export!                
                // location where to export the doc.
                String sURL = getHomeDir(m_xContext) + "/PDF_EXPORT_DIALOG_demo.pdf";
                // choose the proper filter            
                String sFilter = "writer_pdf_Export";

                boolean bExport = doExport(xComponent, sURL, sFilter, aFilterData);

                System.out.println((sURL)+((bExport) ? "\nExported!"
                        : "\nCouldn't export the document!"));
            }

        } catch (java.lang.Exception ex) {
            ex.printStackTrace();
        } finally {
            if (xComponent != null) {
                try {
                    XCloseable xCloseable = (XCloseable) UnoRuntime.queryInterface(
                            XCloseable.class, xComponent);
                    xCloseable.close(true);
                } catch (CloseVetoException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    private boolean doExport(
            XComponent xComponent,
            String aURL,
            String sFilterName,
            PropertyValue[] aFilterData) {
        XStorable xStorable = (XStorable) UnoRuntime.queryInterface(
                XStorable.class, xComponent);
        if (xStorable == null) {
            return false;
        } else {
            try {
                PropertyValue[] aMediaDescriptor = new PropertyValue[2];

                aMediaDescriptor[0] = new PropertyValue();
                aMediaDescriptor[0].Name = "FilterName";
                aMediaDescriptor[0].Value = sFilterName;

                aMediaDescriptor[1] = new PropertyValue();
                aMediaDescriptor[1].Name = "FilterData";
                aMediaDescriptor[1].Value = aFilterData;

                xStorable.storeToURL(aURL, aMediaDescriptor);
                return true;

            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return false;
    }

    public static void printPropertyValues(PropertyValue[] aPropertyValues) {
        System.out.println("\nProperties and values\n");

        for (PropertyValue property : aPropertyValues) {
            String sValue = "";
            try {
                sValue = String.valueOf(property.Value);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            System.out.println(property.Name + " = " + sValue);
        }
    }

    //*************************************************************************
    public static XComponent loadComponent(
            XComponentContext xContext,
            String sURL,
            PropertyValue[] aMediaDescriptor) {
        XComponent xComp = null;
        try {
            XComponentLoader xComponentLoader = (XComponentLoader) UnoRuntime.queryInterface(
                    XComponentLoader.class,
                    xContext.getServiceManager().createInstanceWithContext(
                            "com.sun.star.frame.Desktop", xContext));
            xComp = xComponentLoader.loadComponentFromURL(
                    sURL, "_default", 0, aMediaDescriptor);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return xComp;
    }

    public static XComponent createNewDoc(
            XComponentContext xContext,
            String sDocType,
            PropertyValue[] aMediaDescriptor) {
        XComponent xComp = null;
        try {
            xComp = loadComponent(xContext, "private:factory/"
                    + sDocType, aMediaDescriptor);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return xComp;
    }

    public static XComponent createNewDoc(
            XComponentContext xContext,
            String sDocType) {
        XComponent xComp = null;
        try {
            xComp = createNewDoc(xContext, sDocType, new PropertyValue[0]);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return xComp;
    }

    public static XComponent createNewHiddenDoc(
            XComponentContext xContext,
            String sDocType) {
        XComponent xComp = null;
        try {
            PropertyValue[] aMediaDescriptor = new PropertyValue[1];
            aMediaDescriptor[0] = new PropertyValue();
            aMediaDescriptor[0].Name = "Hidden";
            aMediaDescriptor[0].Value = Boolean.TRUE;

            xComp = createNewDoc(xContext, sDocType, aMediaDescriptor);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return xComp;
    }

    //************************************************************************
    //*************************************************************************
    public static String getHomeDir(XComponentContext xContext) {
        return getPathSubstitution(xContext, "$(home)");
    }

    public static String getWorkDir(XComponentContext xContext) {
        return getPathSubstitution(xContext, "$(work)");
    }

    public static String getUserDir(XComponentContext xContext) {
        return getPathSubstitution(xContext, "$(user)");
    }

    /**
     *
     * com.sun.star.util.PathSubstitution
     *
     * $(inst) Installation path of the Office. $(prog) Program path of the
     * Office. $(user) The user installation directory. $(work) The work
     * directory of the user. Under Windows this would be the "MyDocuments"
     * subdirectory. Under Unix this would be the home-directory $(home) The
     * home directory of the user. Under Unix this would be the home- directory.
     * Under Windows this would be the "Documents and Settings\ " subdirectory.
     * $(temp) The current temporary directory. $(path) The value of PATH
     * environment variable. $(lang) The country code used by the Office, like
     * 01=english, 49=german. $(langid) The language code used by the Office,
     * like 0x0009=english, 0x0409=english us. $(vlang) The language used by the
     * Office as a string. Like "german" for a german Office.
     *
     */
    public static String getPathSubstitution(
            XComponentContext xContext, String aVariable) {
        String variableSubst = null;
        try {
            XStringSubstitution xStringSubstitution = getPathSubstitution(xContext);
            variableSubst = xStringSubstitution.getSubstituteVariableValue(aVariable);

        } catch (com.sun.star.container.NoSuchElementException e) {
            e.printStackTrace();
        } catch (com.sun.star.uno.Exception ex) {
            ex.printStackTrace();
        } finally {
            return variableSubst;
        }
    }

    public static String getPathSubstitution(
            XComponentContext xContext, String aText, boolean bSubstRequired) {
        String variableSubst = null;
        try {
            XStringSubstitution xStringSubstitution = getPathSubstitution(xContext);
            variableSubst = xStringSubstitution.substituteVariables(
                    aText, bSubstRequired);

        } catch (com.sun.star.container.NoSuchElementException e) {
            e.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            return variableSubst;
        }
    }

    public static XStringSubstitution getPathSubstitution(
            XComponentContext xContext) {
        XStringSubstitution xPathSubstitution = null;
        XMultiComponentFactory xMCF = xContext.getServiceManager();
        try {
            xPathSubstitution = (XStringSubstitution) UnoRuntime.queryInterface(
                    XStringSubstitution.class,
                    xMCF.createInstanceWithContext(
                            "com.sun.star.util.PathSubstitution", xContext));
        } catch (com.sun.star.uno.Exception ex) {
            ex.printStackTrace();
        } finally {
            return xPathSubstitution;
        }
    }

}
