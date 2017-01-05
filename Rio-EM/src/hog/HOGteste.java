package hog;

import java.awt.image.BufferedImage;
import java.io.File;

import boofcv.alg.filter.convolve.GConvolveImageOps;
import boofcv.core.image.ConvertBufferedImage;
import boofcv.core.image.border.ImageBorder_F32;
import boofcv.gui.image.ShowImages;
import boofcv.io.image.UtilImageIO;
import boofcv.struct.convolve.Kernel1D_F32;
import boofcv.struct.image.ImageFloat32;

public class HOGteste {

    public static void main(String[] args) {
        String IMAGE_LOCATION = "D:/dog_orig.jpg";
        File image_file = new File(IMAGE_LOCATION);

        // Load image
        BufferedImage pic  = UtilImageIO.loadImage(IMAGE_LOCATION   );

        // Initialize destinations images
        ImageFloat32 dest1 = new ImageFloat32(pic.getWidth(), pic.getHeight());
        ImageFloat32 dest2 = new ImageFloat32(pic.getWidth(), pic.getHeight());
        ImageFloat32 distortedImg = ConvertBufferedImage.convertFrom(pic, dest1);

        // Initialize kernel
        Kernel1D_F32 kx = new Kernel1D_F32(3);
        Kernel1D_F32 ky = new Kernel1D_F32(3);
        kx.data[0] = -1;
        kx.data[1] = 0;
        kx.data[2] = 1;

        ky.data[0] = 1;
        ky.data[1] = 0;
        ky.data[2] = -1;

        ImageBorder_F32 border = new ImageBorder_F32() {
            @Override
            public void setOutside(int arg0, int arg1, float arg2) {
                // TODO Auto-generated method stub   
            }
            @Override
            public float getOutside(int arg0, int arg1) {
                // TODO Auto-generated method stub
                return 0;
            }
        };

        // Apply 1-D mask horizontal
        GConvolveImageOps.horizontal(kx, distortedImg, dest2, border);
        // Apply 1-D mask vertical
        GConvolveImageOps.vertical(ky, distortedImg, dest2, border);

        BufferedImage destenation = new BufferedImage(dest2.getWidth(),dest2.getHeight(),BufferedImage.TYPE_BYTE_GRAY);
        destenation=ConvertBufferedImage.convertTo(distortedImg, destenation);
        ShowImages.showWindow( destenation,"image after apply kernel ");

        System.out.print("Done");
    }
}
