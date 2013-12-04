package com.dwr.jdbcConnection;


import java.util.ArrayList;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.text.SimpleDateFormat; 
import java.util.Date;

	public class puntoventas {
	//aqui el main
	public static void main(String args[]){
		puntoventas pv = new puntoventas();
		pv.getMostrarProducto();
		pv.InsertarDatos("","1", "Pale-Lim", "Paleta de Limon", "50", "10-01-2013", "2.5");
		//pv.InsertarDatos("4","1", "Pale-Lim", "Paleta de Limon", "50", "10-01-2013", "2.5");
		//pv.InsertarDatos("6","1", "Pale-Man", "Paleta de Mango", "50", "10-01-2013", "2.5");
		//pv.EditarDatos("6", "2", "Pale-Tama", "Paleta de Tamarindo", "10", "6-5-2013", "5");
		System.out.print(pv.getHola());
		//pv.getHola();
	}
	//luego agoa tus funciones 
	///...
	
	public String getHola(){
		return "Servicio Web con DWR funcionando!!";
	}
	
	// donde va carga de plaza unidad lo cambias por el nombre q le quieras poner al metodo
	public ArrayList<ArrayList<String>> getMostrarProducto(){
		ArrayList<ArrayList<String>> consulta =new ArrayList<ArrayList<String>>();
		//donde esta  sp_getPlazaconsul ahi pones tu proseso almasenado "SP"
		String spQuery="sp_mostrarProducto";
		// adentro de las llaves de values pones los parametros q va resibir tu funcion 
		Object[ ] values={};   
		//en donde esta columsn ahi va lo q te va regresar la tabla cada columna con ese orden
		String[] columns={"idProducto","idProvedor","DescrCorta","DescrLarga","CantExistencia","UltCompra","Precio"};
		consulta=JdbcConexion.executeSP(spQuery, values.length, columns.length, values);
		//el ssitem d eabajo es para saber q entro bien te imprime es emensaje y sabras q todo fu e bien 
		System.out.print("\n\n"+consulta+"\n\n");
		return consulta;
	}
	
	public String InsertarDatos(String idProducto,  String idProvedor,String DescrCorta
			,String DescrLarga,String CantExistencia,String UltCompra,String Precio){
		String datos ="";
		 String spQuery="sp_altaproducto";
		 Object[ ] values={idProducto, idProvedor, DescrCorta, DescrLarga, CantExistencia, UltCompra, Precio};   
		 String[] columns={"respuesta"};
		 datos=JdbcConexion.executeUpdateSP(spQuery, values.length, values,columns.length).toString();
		 System.out.print(datos);
		 return datos;

		}
	
	public String EditarDatos( String idProducto,  String idProvedor,String DescrCorta
			,String DescrLarga,String CantExistencia,String UltCompra,String Precio){
		String datos ="";
		 String spQuery="sp_modificaproducto";
		 Object[ ] values={ idProducto, idProvedor, DescrCorta, DescrLarga, CantExistencia, UltCompra, Precio};   
		 String[] columns={"respuesta"};
		 datos=JdbcConexion.executeUpdateSP(spQuery, values.length, values,columns.length).toString();
		 System.out.print(datos);
		 return datos;

		}
	
}  
	
