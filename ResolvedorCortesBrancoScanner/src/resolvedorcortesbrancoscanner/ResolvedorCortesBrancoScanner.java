package resolvedorcortesbrancoscanner;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.opencv.core.Mat;

/**
 *
 * @author Renan Fucci
 * <a href="malito:renanfucci@hotmail.com">(renanfucci@hotmail.com)</a>
 */
public class ResolvedorCortesBrancoScanner {

    private Mat src;
    private Mat dst;
    private final boolean resolved;

    public ResolvedorCortesBrancoScanner(Mat src) {
        this.src = src;
        this.resolved = resolvedorCortesBrancoScanner(src);
    }

    private boolean resolvedorCortesBrancoScanner(Mat src) {        
        try {
            Mat src_aux = src.clone();
            
        } catch (Exception e) {
            LOG.log(Level.SEVERE, e.getLocalizedMessage());
        }
        return false;
    }
    private static final Logger LOG = Logger.getLogger(ResolvedorCortesBrancoScanner.class.getName());

    /*--------- GETTERS ---------*/
    public Mat getSrc() {
        return src;
    }

    public Mat getDst() {
        return dst;
    }

    public boolean isResolved() {
        return resolved;
    }

}
