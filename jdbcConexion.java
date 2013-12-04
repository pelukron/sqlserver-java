package com.dwr.jdbcConnection;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
/** Hay que agregar la funcionalidad de pool de conexiones
import org.apache.commons.dbcp.BasicDataSource;
import org.apache.commons.pool.*;
**/
public class JdbcConexion {
	/*
	private static final String DATABASE_PRODUCCION="";
	private static final String DATABASE_PRUEBAS = "Gasolina";
	private static final String USER = "web"; 
	private static final String SERVER ="172.17.1.201";
	private static final String PASSWORD = "adminweb"; 
	private static final String  DRIVER ="com.microsoft.sqlserver.jdbc.SQLServerDriver";
	private static ResultSet rs = null;
	private stGasolinaatic PreparedStatement ps = null;
	private static Connection conn;
	*/
	private static final String DATABASE_PRODUCCION="";
	private static final String DATABASE_PRUEBAS = "prueba1";
	private static final String USER = "diego"; 
	private static final String SERVER ="localhost";
	private static final String PASSWORD = "toor"; 
	private static final String  DRIVER ="com.microsoft.sqlserver.jdbc.SQLServerDriver";
	private static ResultSet rs = null;
	private static PreparedStatement ps = null;
	private static Connection conn;
	
	static{
		try {
			try {
				Class.forName(DRIVER);
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//conn=DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName="+DATABASE_PRUEBAS,USER,PASSWORD);
			
			 conn=DriverManager.getConnection("jdbc:sqlserver://"+SERVER+":1433;databaseName="+DATABASE_PRUEBAS,USER,PASSWORD);
			 
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static Connection getConn(){
		return conn;
	}
	public static ArrayList<HashMap<String,Object>> executeQueryObject(String query,String[] columns){
		PreparedStatement ps=null;
		ResultSet rs=null;
		HashMap<String,Object> map=null;
		ArrayList<HashMap<String, Object>> array=new ArrayList<HashMap<String,Object>>();
		try {
			ps = conn.prepareStatement(query);
			rs= ps.executeQuery();
			while(rs.next()){
				map=new HashMap<String, Object>();
				for(String key:columns){
					Object value=rs.getObject(key);
					map.put(key,String.valueOf(value));
				}
				array.add(map);
			}
			rs.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			try {
				rs.close();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		return array;
	}	
	
	/**
	 * Metodo para ejecutar storedProcedure 
	 * @param queryName 	Nombre del Stored procedure a ejecutar
	 * @param numParamsIn 	Numero de parametros de entrada
	 * @param values		Arreglo de valores de objetos a enviar como parametros de entrada, debe ser la misma cantidad al numParamsIn y en respectivo orden
	 * @return Un arreglo de arreglo de String, que en javascript se interpretan como un arreglo de arreglos...
	 * */
	public static ArrayList<ArrayList<String>> executeSP(String queryName,int numParamsIn,int numExpectedColumns,Object[] values){
		String nameOfSp="{call "+queryName+" ";
		String interrogations="";
		ResultSet rs=null;
		ArrayList<ArrayList<String>> table=new ArrayList<ArrayList<String>>();;
		ArrayList<String> row=null;
		if(numParamsIn>0){
			nameOfSp+="(";
			for(int x=0;x<numParamsIn;x++){
				if(x==(numParamsIn-1))
					interrogations+="?";
				else
					interrogations+="?,";
			}
			nameOfSp+=interrogations+")}";
		}
		else{
			nameOfSp+="}";
		}
		CallableStatement call;
		try {
			call = conn.prepareCall(nameOfSp);
			if(numParamsIn>0){
				int index=1;
				for(Object value:values){
					call.setObject(index,value);
					index++;
				}
			}
			rs=call.executeQuery();
			while(rs.next()){
				row=new ArrayList<String>();
				for(int i=1;i<=numExpectedColumns;i++){
					row.add(String.valueOf(rs.getObject(i)));
				}
				table.add(row);
			}			
		
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			try {
				rs.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return table;
	}			
	
	public static ArrayList<ArrayList<String>> getEstados(String query){
		ArrayList<ArrayList<String>> estados = new ArrayList<ArrayList<String>>();
		try{
		ps = conn.prepareStatement(query);
		ResultSet  rs = ps.executeQuery();
			while(rs.next()){
				ArrayList<String> estado = new ArrayList<String>();
				estado.add(rs.getString("idEstado"));
				estado.add(rs.getString("nombre"));
				estados.add(estado);
			}
		}
		catch(SQLException e){
			e.printStackTrace();
		}
		return estados;
	}
	/**
	 * Obtiene un ArrayList con un HashMap que representan objetos.
	 * @param queryName Nombre del stored procedure
	 * @param numParamsIn Numero de parametros de entrada
	 * @param values Arreglo de valores correspondientes al numero de parametros de entrada
	 * @param columns Nombre las columnas que representaran el key del hashmap
	 * @return Una lista de objetos con keys de acuerdo a las columnas enviadas como parametros
	 */
	public static ArrayList<HashMap<String, Object>> getObjectSP(String queryName,int numParamsIn,Object[] values,String[] columns){
		String nameOfSp="{call "+queryName+" ";
		String interrogations="";
		ResultSet rs=null;
		ArrayList<HashMap<String, Object>> table=new ArrayList<HashMap<String, Object>>();;
		HashMap<String,Object> row=null;
		if(numParamsIn>0){
			nameOfSp+="(";
			for(int x=0;x<numParamsIn;x++){
				if(x==(numParamsIn-1))
					interrogations+="?";
				else
					interrogations+="?,";
			}
			nameOfSp+=interrogations+")}";
		}
		else{
			nameOfSp+="}";
		}
		CallableStatement call;
		try {
			call = conn.prepareCall(nameOfSp);
			if(numParamsIn>0){
				int index=1;
				for(Object value:values){
					call.setObject(index,value);
					index++;
				}
			}
			rs=call.executeQuery();
			while(rs.next()){
				row=new HashMap<String, Object>();
				for(String key:columns){
					row.put(key,String.valueOf(rs.getObject(key)));
				}
				table.add(row);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return table;
	}			
		
	public static int executeUpdateBatch(String queryName,int numParamsIn,ArrayList<ArrayList<String>> values){
		String nameOfSp="{call "+queryName+" ";
		String interrogations="";
		ResultSet rs=null;
		ArrayList<ArrayList<String>> table=new ArrayList<ArrayList<String>>();;
		ArrayList<String> row=null;
		CallableStatement call=null;
		int numAffected=-1;
		if(numParamsIn>0){
			nameOfSp+="(";
			for(int x=0;x<numParamsIn;x++){
				if(x==(numParamsIn-1))
					interrogations+="?";
				else
					interrogations+="?,";
			}
			nameOfSp+=interrogations+")}";
		}
		else{
			nameOfSp+="}";
		}
		try {
			call = conn.prepareCall(nameOfSp);
			for(ArrayList<String> object:values){
				for(int i=0;i<numParamsIn;i++){
					String val=object.get(i);
					call.setObject(i+1,val);
				}
				call.addBatch();
			}
			numAffected=call.executeBatch().length;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println(e.getCause());
		}finally{
			try {
				call.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return numAffected;
	}
	/**
	protected void finalize(){
		try{
			conn.close();
		}
		catch(SQLException e){
			e.printStackTrace();	
		}
	}**/
	public static int executeUpdate(String queryName,int numParamsIn,Object[] values){
		int affected=0;
		String nameOfSp="{call "+queryName+" ";
		String interrogations="";
		ResultSet rs=null;
		ArrayList<ArrayList<String>> table=new ArrayList<ArrayList<String>>();;
		ArrayList<String> row=null;
		if(numParamsIn>0){
			nameOfSp+="(";
			for(int x=0;x<numParamsIn;x++){
				if(x==(numParamsIn-1))
					interrogations+="?";
				else
					interrogations+="?,";
			}
			nameOfSp+=interrogations+")}";
		}
		else{
			nameOfSp+="}";
		}
		CallableStatement call=null;
		try {
			call = conn.prepareCall(nameOfSp);
			if(numParamsIn>0){
				int index=1;
				for(Object value:values){
					call.setObject(index,value);
					index++;
				}
			}
			affected=call.executeUpdate();			
		
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			try {
				call.clearBatch();
				call.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return affected;
	}		
	
	public static Object executeUpdateSP(String spName,int numParams,Object[] values,int numOutputs){	
		String nameOfSp="{call "+spName+" (";
		String interrogations="";
		String outputs="";
		Object id=null;
		for(int x=0;x<numParams;x++){
		if(x==(numParams-1))
		interrogations+="?";//si es el ultimo pone una variable
		else
		interrogations+="?,";
		}
		/*if(numOutputs>0 && numParams>0)
		interrogations+=",";
		for(int x=0;x<numOutputs;x++){	
		if(x==(numOutputs-1)){
		outputs+="@F"+x;
		}else
		outputs+="@F"+x+",";
		}*/
		nameOfSp+=interrogations+outputs+")}";
		PreparedStatement ps=null;
		ResultSet rs=null;
		try {
		ps=conn.prepareStatement(nameOfSp);
		int index=1;
		for(Object obj:values){
		if(obj instanceof Date){
		java.sql.Date d=new java.sql.Date(((Date) obj).getTime());
		ps.setObject(index,d);
		}else{
		ps.setObject(index,obj);
		}
		index++;
		}
		if(numOutputs>0){
		rs=ps.executeQuery();
		try {
		while(rs!=null && rs.next()){
		id=rs.getObject(1);
		}
		} catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		}
		}else{
		ps.executeUpdate();
		}
		} catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		}
		return id;
		}
	
	public static void executeQuery(String query){
		Statement st = null;
		try {
			st = conn.createStatement();
			st.execute(query);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}	
	
}
