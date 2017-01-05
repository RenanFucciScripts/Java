package config;

import java.lang.reflect.Field;
import org.opencv.core.Core;

/**
 *
 * @author _
 */
public class ConfigurarOpenCV {
 
    
    static {
        initOpenCv();
    }

    private static void initOpenCv(){
        try {
            setLibraryPath_OpenCV();
        } catch (Error ex) {
            ex.printStackTrace();
        }
    }

    private static void setLibraryPath_OpenCV(){

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
                ex.printStackTrace();
                throw new Error(ex2);
            } catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException ex1) {
                ex1.printStackTrace();
            }
        } catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException ex0) {
            ex0.printStackTrace();
        }

    }
}
