package Proyecto_BancoXYZ;

import java.io.FileReader;

public class Hola_Mundo {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		FileReader rs = new FileReader("Hola.txt");
		int valor=rs.read();
		while(valor!=-1) {
			System.out.println((char)valor);
			valor=rs.read();
		}
	}
}