package Proyecto_BancoXYZ;
//Importar libreria de transferencia de datos Stream
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.*;
import java.net.Socket;
//leer por pantalla
import java.util.Scanner;
//leer por ventanas
import javax.swing.JOptionPane;
public class cliente {
		final String HOST ="127.0.0.1";//Direccion del localhost
		final int PUERTO = 5000;//Puerto de comunicacion 
		DataInputStream ingresar;
		DataOutputStream salida;
		String mensaje;
	public void iniciarCliente(){
		// TODO Auto-generated method stub
		Scanner leer = new Scanner(System.in);
		try {
			Socket sc = new Socket(HOST,PUERTO);//Enviar peticion al servidor
			ingresar = new DataInputStream(sc.getInputStream());
			salida = new DataOutputStream(sc.getOutputStream());
			String teclado = "";
			System.out.println("BANCOXYZ, Digita tu usuario por favor");//Leer datos del usuario 
			while(!teclado.equals("x")){
					teclado = leer.nextLine();
					salida.writeUTF(teclado);
					mensaje = ingresar.readUTF();
					System.out.println(mensaje);
			}
			sc.close();//Cerrar socket cliente a servidor
		} catch (IOException ex) {
			//TODO: handle exception
			
		}
	}
	public static void main(String[] args) {
		cliente cliente = new cliente();
		cliente.iniciarCliente();
	}
}