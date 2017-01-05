package projCameraLID;

import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.Box;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.github.sarxos.webcam.*;
import com.github.sarxos.webcam.ds.ipcam.IpCamDevice;
import com.github.sarxos.webcam.ds.ipcam.IpCamDeviceRegistry;
import com.github.sarxos.webcam.ds.ipcam.IpCamDriver;
import com.github.sarxos.webcam.ds.ipcam.IpCamMode;

public class testeCamereIp {
	static {
		Webcam.setDriver(new IpCamDriver());
	}

	public static void main(String[] args) throws IOException {
		System.out.println("0");
		
		IpCamDeviceRegistry.register(new IpCamDevice("Lignano", "http://172.19.2.90/jmpeg", IpCamMode.PULL));
		System.out.println("1");
		JFrame f = new JFrame("Live Views From Lignano Beach");
		System.out.println("2");
		System.out.println(""+Webcam.getDefault());
		f.add(new WebcamPanel(Webcam.getDefault()));
		System.out.println("3");
		
		f.pack();
		f.setVisible(true);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

}
