package centro;

import java.awt.Image;

import javax.swing.ImageIcon;

import centro.interfaz.Interfaz;

public class Inicio {

	public static void main(String[] args) {

		ImageIcon icon = new ImageIcon("Icono.png");

		Image image = icon.getImage();

		image = image.getScaledInstance(38, 56,  Image.SCALE_SMOOTH);

		icon = new ImageIcon(image);

		new Interfaz(icon);

	}

}