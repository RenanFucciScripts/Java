package config;

import java.lang.reflect.Field;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.opencv.core.Core;

/**
 *
 * @author _
 */
public class ConfigurarOpenCV {

    static {
        initOpenCv();
    }

    private static void initOpenCv() {
        try {
            setLibraryPath_OpenCV();
        } catch (Error ex) {
            Logger.getLogger(ConfigurarOpenCV.class.getName()).log(Level.SEVERE, ex.getLocalizedMessage(), ex);
        }
    }

    private static void setLibraryPath_OpenCV() {

        try {
            System.setProperty("java.library.path", "bin/x" + "86");
            System.out.println(System.getProperty("java.library.path"));
            Field fieldSysPath = ClassLoader.class.getDeclaredField("sys_paths");
            fieldSysPath.setAccessible(true);
            fieldSysPath.set(null, null);
            System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
            System.out.println("OpenCV loaded. Version: " + Core.VERSION);
        } catch (Error ex) {
            try {
                System.setProperty("java.library.path", "bin/x" + "64");
                System.out.println(System.getProperty("java.library.path"));
                Field fieldSysPath = ClassLoader.class.getDeclaredField("sys_paths");
                fieldSysPath.setAccessible(true);
                fieldSysPath.set(null, null);
                System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
                System.out.println("OpenCV loaded. Version: " + Core.VERSION);

            } catch (Error ex2) {
                Logger.getLogger(ConfigurarOpenCV.class.getName()).log(Level.SEVERE, ex2.getLocalizedMessage(), ex2);
                throw new Error(ex2);
            } catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException ex1) {
                Logger.getLogger(ConfigurarOpenCV.class.getName()).log(Level.SEVERE, ex1.getLocalizedMessage(), ex1);
            }
        } catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException ex0) {
            Logger.getLogger(ConfigurarOpenCV.class.getName()).log(Level.SEVERE, ex0.getLocalizedMessage(), ex0);
        }

    }
}
