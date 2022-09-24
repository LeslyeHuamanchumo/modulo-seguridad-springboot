package pe.isil.seguridad;

import com.mysql.cj.x.protobuf.MysqlxPrepare;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.sql.*;

@SpringBootApplication
public class Application {

    public static void main(String[] args) throws Exception{
        SpringApplication.run(Application.class, args);

        // Load driver
        Class.forName("com.mysql.cj.jdbc.Driver");

        // Crear conexión
        Connection conexion = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/moduloseg", "root", "123jtm91." );

        // Crear Statement
        //Statement st = conexion.createStatement();
        //PreparedStatement pt = conexion.prepareStatement("select * from Users where id=?");
        //PreparedStatement pt = conexion.prepareStatement("select * from Users where id=? and username=?");
        //pt.setInt(1, 2); // recibe como parámetro el index y el valor (en este caso estamos mandando 3 porque es el id de José)
        //pt.setString(2,"DNI25631258");

        CallableStatement callSp = conexion.prepareCall("{call validarLogin(?,?,?)}");
        callSp.setString(1, "DNI25631258");
        callSp.setString(2, "123456");
        callSp.registerOutParameter(3,Types.INTEGER);

        // Ejecutar sentencia
        //ResultSet resultado = st.executeQuery("select * from Users");
        //ResultSet resultado = pt.executeQuery();
        callSp.executeQuery();

        // En este no vamos a tener un resultSet, solamente un parámetro de salida.
        int resultado = callSp.getInt(3);

        if(resultado == 1){
            System.out.println("Login Exitoso");
        }else{
            System.out.println("Usuario o clave incorrecto");
        }

        // Recorrer el resultado
        /*
            while (resultado.next()){
                System.out.println(resultado.getString("name"));
            }
        */

        // Creamos un nuevo CallableStatement

        CallableStatement callSp2 = conexion.prepareCall("{call listarAllUsers()}"); // no le estamos pasando nungún parámetro
        ResultSet resultadoSp2 = callSp2.executeQuery();

        while (resultadoSp2.next()){
            System.out.println(resultadoSp2.getString("username"));
        }

        // Creamos otro nuevo CallableStatement

        CallableStatement callSp3 = conexion.prepareCall("{call listarUserPerId(?)}");
        callSp3.setInt(1, 2); // le pasamos como parámetro el id = 2
        ResultSet resultadoSp3 = callSp3.executeQuery();

        while (resultadoSp3.next()){
            System.out.println(resultadoSp3.getString("name"));
        }

    }
}
