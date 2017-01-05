package fuzzyLogic;

import net.sourceforge.jFuzzyLogic.FIS;

/**
 * Test parsing an FCL file
 * @author pcingola@users.sourceforge.net
 */
public class TestTipper {
    public static void main(String[] args) throws Exception {
        // Load from 'FCL' file
    	String dir="C:\\Users\\Renan Fucci\\Dropbox\\UFMS - CPAN\\5º Semestre - 2015\\Inteligência Artificial\\Fuzzy\\";
    	String fileName = dir+"tipper.fcl";
        FIS fis = FIS.load(fileName,true);
        // Error while loading?
        if( fis == null ) { 
            System.err.println("Can't load file: '" 
                                   + fileName + "'");
            return;
        }

        // Show 
        //fis.chart();
        // Set inputs
        fis.setVariable("service", 3);
        fis.setVariable("food", 7);

        // Evaluate
        fis.evaluate();

        // Show output variable's chart 
        // Print ruleSet
        System.out.println(fis.getVariable("tip").getDefuzzifier());
    }
}