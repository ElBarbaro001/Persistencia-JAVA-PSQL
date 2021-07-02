package Proyecto_BancoXYZ;

//Importar libreria de transferencia de datos Stream
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.*;
import java.util.logging.Logger;
import java.util.logging.Level;
import java.sql.*;
import java.util.Scanner;

public class servidor {
	static int PUERTO = 5000;
	ServerSocket server;
	Socket sc;
	
	final String dbURL = "pervasive://localhost:1583/bancoxyz";
	final String dbusuario = "xroot";
	final String dbcontrasena = "root";
	final String controlador = "com.pervasive.jdbc.v2.Driver";
	
	// Declarar clases para la transmision de flujo de datos
	// Enviar datos
	DataOutputStream salida;
	// Recibir datos
	DataInputStream entrada;
	// Declarar variables entre el cliente y el servidor: mensaje=msg
	String msgUsuario, msgContrasena, msgOpciones, msgRetirar, msgConsignar, msgResultado;
	// Menu Opciones Cliente
	String opcion1 = "1 Consultar Saldo -> $";
	String opcion2 = "2 Retirar Dinero -> $";
	String opcion3 = "3 Cosnignar Dinero -> $";
	String opcion4 = "4 Cliente nuevo -> ";
	// Submenu Retirar Dinero
	String retirar1 = "1 $ 20.000";
	String retirar2 = "2 $ 50.000";
	String retirar3 = "3 $ 100.000";
	String retirar4 = "4 $ 200.000";
	// Variables Cliente
	String nombre;
	String apellido;
	int idCliente;
	int numCuenta;
	int saldo;
	String tipo;
	

	// Clase para inciar el servidor
	public void iniciarServidor() {
		try {
			// Iniciamos el servidor
			server = new ServerSocket(PUERTO);
			sc = new Socket();
			System.out.println("--------------------------------> Servidor iniciado <--------------------------------");
			sc = server.accept();
			System.out.println("--------------------------------> Cliente conectado <--------------------------------");
			entrada = new DataInputStream(sc.getInputStream());
			salida = new DataOutputStream(sc.getOutputStream());

			// Inicializar variables Menu
			String msn = "";
			String usuario = "";
			String contrasena = "";
			String opcion = "";

			while (msn.equals("")) {
				// Validar usuario del sistema
				//Primera validacion de usuario
				if (usuario.equals("")) {
					msgUsuario = entrada.readUTF();// Leer datos que llegan
					System.out.println("--------------------------------> Cliente " + msgUsuario +" ingresando contraseña <--------------");
					salida.writeUTF("Bienvenido " + msgUsuario + " Ingresar contrasena ->");// recibir contraseña desde el cliente
					usuario = msgUsuario.toString();
				} else {
					usuario = msgUsuario.toString();
				}
				//segunda validacion de contraseña
				if (!usuario.equals("") && contrasena.equals("")) {
					msgContrasena = entrada.readUTF();
					System.out.println("Sesion iniciada");
					salida.writeUTF("Seleccionar una de las siguientes opciones\n" + opcion1 + "\n " + opcion2 + " \n"
							+ opcion3);
					contrasena = msgContrasena.toString();
				} else {
					contrasena = msgContrasena.toString();
				}
				//tercera validacion usuario y contraseña
				if (!usuario.equals("") && !contrasena.equals("")) {
					msgOpciones = entrada.readUTF();
					opcion = msgOpciones.toString();
					//Caso
					switch (opcion) {
						case "1":
							consultarpsql(usuario);
							//Imprimir por pantalla
							salida.writeUTF("Bienvenido " + nombre + " " + apellido + "\nCuenta de: " + tipo + " numero:" + numCuenta + "\nSaldo actual: $"+ saldo);
							break;
						case "2":
							salida.writeUTF("Valor a retirar \n" + retirar1 + "\n" + retirar2 + "\n"+  retirar3 +"\n" +retirar4);
							msgRetirar = entrada.readUTF();//Leer datos que llegan al servidor
							String opcionRetiro = "";
							opcionRetiro = msgRetirar.toString();
							//retirarpsql(usuario, opcionRetiro);
							salida.writeUTF(msgResultado);
							break;
						case "3":
							salida.writeUTF("Valor a depositar $ -> ");
							msgConsignar = entrada.readUTF();
							int valorDeposito = Integer.parseInt(msgConsignar);
							//depositarDB(usuario, valorDeposito);
							break;
					}// switch
					msn = usuario + ", " + contrasena;
				} else {
					opcion = msgOpciones.toString();
				}
			} // while
			sc.close();
		} catch (Exception e) {
			// TODO: handle exception
		}
	}// Iniciar servidor
	
	//Clase para consultar datos 
	public void consultarpsql(String usuarioAlias) {
		final String usuario = usuarioAlias;
		try {
			Class.forName("org.postgresql.Driver");//conector
			java.sql.Connection con = DriverManager.getConnection(dbURL,dbusuario,dbcontrasena);

			// Crear sentencias sql con el objeto Statement
			// https://docs.oracle.com/javase/8/docs/api/java/beans/Statement.html
			Statement statement = con.createStatement();
			ResultSet resultSet = statement.executeQuery("select cod_cliente,nombres,apellidos from clientes where usuario='" + usuario + "'");

			if (resultSet.next() == true) {
				nombre = resultSet.getString("nombres");
				apellido = resultSet.getString("apellidos");
				idCliente = resultSet.getInt("cod_cliente");
				ResultSet resultSet2 = statement.executeQuery("select num_cuenta,saldo,tipo from cuentas where cod_cliente"+idCliente);
				//Almacenar datos				
				if (resultSet2.next() == true) {
					numCuenta = resultSet2.getInt("num_cuenta");
					saldo = resultSet2.getInt("saldo");
					tipo = resultSet2.getString("tipo");
				}
			}
			con.close();
		} catch (SQLException e) {
			System.out.println("SQL Exception: " + e.toString());
		} catch (ClassNotFoundException cE) {
			System.out.println("Class Not Found Exception" + cE.toString());
		}
	}// Consultarpsql
	public static void main(String[] args) {
		servidor ser = new servidor();
		ser.iniciarServidor();
	}
}// Clase ppal