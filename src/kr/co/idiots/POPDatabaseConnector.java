package kr.co.idiots;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class POPDatabaseConnector {
	
	public POPDatabaseConnector() {
		Connection connection = null;
        Statement st = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection("popmysqlinstance.cbmkycbel6rd.ap-northeast-2.rds.amazonaws.com:3306/popdb" , "houn524", "tmdgns12");
            st = connection.createStatement();
 
            String sql;
            sql = "show databases;";
 
            ResultSet rs = st.executeQuery(sql);
 
//            while (rs.next()) {
//                System.out.println(rs.);
//            }
 
            rs.close();
            st.close();
            connection.close();
        } catch (SQLException se1) {
            se1.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (st != null)
                    st.close();
            } catch (SQLException se2) {
            }
            try {
                if (connection != null)
                    connection.close();
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
	}
}
