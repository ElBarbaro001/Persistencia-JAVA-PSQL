package Proyecto_BancoXYZ;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
public class conexion_BD {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//Inicializamos Connection con null
		Connection conectar = null;
		String host = "localhost";
		String port = "5432";
		String db_name = "bancoxyz";
		String usuario = "xroot";
		String contrasena ="root";
		try {
			//Cargamos el controlador psql
			Class.forName("com.pervasive.jdbc.v2.Driver");
			conectar = DriverManager.getConnection("jdbc:postgresql://"+host+":"+port+"/"+db_name+"", ""+usuario+"", ""+contrasena+"");
			if(conectar != null) {
				System.out.println("Conexion exitosa!");
			}else {
				System.out.println("Error!!! no existe conexion");
			}
			
		}catch (Exception e) {
			e.printStackTrace();
		}	
	}
}
