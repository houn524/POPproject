package kr.co.idiots;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class POPDatabaseConnector {
	
	private Connection connection;
	
	public POPDatabaseConnector() {
		connection = null;
        Statement st = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://popmysqlinstance.cbmkycbel6rd.ap-northeast-2.rds.amazonaws.com:3306/popdb" , "houn524", "tmdgns12");
            st = connection.createStatement();
 
            String sql;
            sql = "show databases;";
 
            ResultSet rs = st.executeQuery(sql);
 
            while (rs.next()) {
                System.out.println(rs.getString("Database"));
            }
 
            rs.close();
            st.close();
        } catch (SQLException se1) {
            se1.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
	}
}
