package org.example;
import java.sql.*;
import java.util.ArrayList;

public class DatabaseManager {
    private Connection connection;

    // Constructor: inicializa la conexión a la base de datos
    public DatabaseManager(String url, String username, String password) throws SQLException {
        connection = DriverManager.getConnection(url, username, password);
    }

    // Método para insertar datos en la base de datos
    public void insertData(empresas e) {
        String sql = "INSERT INTO empresas (importe, fecha_adjudicacion, adjudicatario, nif, objeto, objeto_generico, proveedores_consultados, tipo_contrato ) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setFloat(1,e.getImporte());
            statement.setDate(2,new java.sql.Date(e.getFechaAdjudicacion().getTime()));
            statement.setString(3,e.getAdjudicatario());
            statement.setString(4,e.getNIF());
            statement.setString(5,e.getObjeto());
            statement.setString(6,e.getObjetoGenerico());
            statement.setString(7, e.getProveedoresConsultados());
            statement.setString(8, e.getTipoContrato());
            statement.executeUpdate();
        } catch (SQLException err) {
            err.printStackTrace();
        }
    }

    //Metodo para recuperar los datos de la bbdd
    public ArrayList<empresas> selectData(){
        ArrayList<empresas> empresasList = new ArrayList<>();

        String sql = "SELECT * FROM `EMPRESAS`";


        try (Statement st= connection.createStatement();
             ResultSet rs=st.executeQuery(sql)){

            while(rs.next()){
                empresas empresa = new empresas();
                empresa.setNIF(rs.getString("nif"));
                empresa.setAdjudicatario(rs.getString("adjudicatario"));
                empresa.setObjetoGenerico(rs.getString("objeto_generico"));
                empresa.setObjeto(rs.getString("objeto"));
                empresa.setFechaAdjudicacion(rs.getDate("fecha_adjudicacion"));
                empresa.setImporte(rs.getFloat("importe"));
                empresa.setProveedoresConsultados(rs.getString("proveedores_consultados"));
                empresasList.add(empresa);
            }


            return empresasList;
        }catch (Exception e){
            e.getMessage();
            return null;
        }
    }



    // Método para cerrar la conexión a la base de datos
    public void closeConnection() {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
