package mvc.modelo;

import dto.Parametros;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.json.JsonArrayBuilder;
import javax.json.*;

/**
 *
 * @author User
 */
public class AcessoBD {
    ResultSet resultSet;
    Statement stmt;
    Connection con;
    ArrayList<Parametros> parametros_lista;
    Parametros par = new Parametros();
    ArrayList<Parametros> medidores_lista;
    Parametros med = new Parametros();
    

    public JsonArrayBuilder getParametros(String medidor, String periodo, String datahora) throws SQLException{
        
        con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/tempumidade", "tempumidade", "tempumidade");
        stmt=con.createStatement();
        
        
        String where = "";
        String ano = datahora.split("-")[0];
        String mes;
        String dia;
        
       
        switch(periodo) {
            case "ano":
                 where = "datahora >= '" + ano + "-01-01'::date " +
                          "AND datahora < ('" + ano +"-12-31 23:59:59');";
                 break;
            case "mes": 
                mes = datahora.split("-")[1];
                dia = datahora.split("-")[2];
                where = "datahora >= ('" + ano + "-"+ mes +"-"+ dia +"'::date - '1 month'::interval) " +
                        "AND datahora < ('" + ano +"-"+ mes +"-"+ dia +" 23:59:59');";
                break;
            case "dia": 
                mes = datahora.split("-")[1];
                dia = datahora.split("-")[2];
                where = "datahora >= '" + ano + "-"+ mes +"-"+ dia +"'::date " +
                          "AND datahora < ('" + ano +"-"+ mes +"-"+ dia +" 23:59:59');";
                break;
            case "semana": 
                mes = datahora.split("-")[1];
                dia = datahora.split("-")[2];
                where = "datahora >= ('" + ano + "-"+ mes +"-"+ dia +"'::date - '7 days'::interval) " +
                          "AND datahora < ('" + ano +"-"+ mes +"-"+ dia +" 23:59:59');";
                break;
        }
    
        
        String sql = "SELECT * FROM " + medidor + " where " + where;
        
        resultSet = stmt.executeQuery(sql);
        parametros_lista = new ArrayList();
        JsonArrayBuilder array= Json.createArrayBuilder() ;
            while(resultSet.next()){
                
            
                JsonObjectBuilder obj = Json.createObjectBuilder();
                obj.add("datahora",resultSet.getString("datahora"));
                obj.add("temperatura",resultSet.getString("temperatura"));
                obj.add("umidade",resultSet.getString("umidade"));
                array.add(obj);
        }
  
        return array;
    }
    
    public String guardar(
            String medidor,
            String temperatura,
            String umidade,
            String datahora,
            String serial
    ){
        try{
            double d_temperatura = Double.parseDouble(temperatura);
            double d_umidade = Double.parseDouble(umidade);
            
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            long l_datahora = df.parse(datahora).getTime();
            Timestamp t_datahora = new Timestamp(l_datahora);
            
            long l_serial = Long.parseLong(serial);

            con = DriverManager.getConnection(
                    "jdbc:postgresql://localhost:5432/tempumidade", //Database URL
                    "tempumidade",                                  //User
                    "tempumidade");                                 //Password
            PreparedStatement stmt = con.prepareStatement(
                    "INSERT INTO medidor001 "+
                    "(medidor,temperatura,umidade,datahora,serial) " +
                    " VALUES(?,?,?,?,?);");
            stmt.setString   (1, medidor);
            stmt.setDouble   (2, d_temperatura);
            stmt.setDouble   (3, d_umidade);
            stmt.setTimestamp(4, t_datahora);
            stmt.setLong     (5, l_serial);
            
            stmt.executeUpdate();
            stmt.close();
            con.close();
            
        } catch(Exception e){
            e.printStackTrace();
            return "NOK";
        }
        return "OK";
    }
}


