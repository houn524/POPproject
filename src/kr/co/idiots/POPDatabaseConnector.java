package kr.co.idiots;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import kr.co.idiots.model.POPProblem;
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
//            Class.forName("com.mysql.jdbc.Driver");
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
	
	public void saveFlowchart(String content) {
		PreparedStatement st = null;
		
		String sql = "select * from flowchart where id=1;";
		
		boolean result = false;
		
		try {
			
			
            st = connection.prepareStatement(sql);//mainApp.getConnector().getConnection().createStatement();
            ResultSet rs = st.executeQuery();
            
            if(rs.next()) {
            	sql = "update flowchart set content=? where id=1;";
            } else {
            	sql = "insert into flowchart(id, content) values(1, ?)";
            }
            
            st = connection.prepareStatement(sql);
            st.setString(1, content);
            
            st.executeUpdate();
 
 
            rs.close();
            st.close();
        } catch (SQLException se1) {
            se1.printStackTrace();
        }
	}
	
	public String loadFlowchart(int id) {
		PreparedStatement st = null;
		String content = "";
		String sql = "select content from flowchart where id=?;";
		
		boolean result = false;
		
		try {
            st = connection.prepareStatement(sql);
            st.setInt(1, id);
            ResultSet rs = st.executeQuery();
            
            if(rs.next()) {
            	content = rs.getString(1);
            }
            rs.close();
            st.close();
        } catch (SQLException se1) {
            se1.printStackTrace();
        }
		
		System.out.println(content);
		return content;
	}
	
	public ArrayList<POPProblem> loadProblems(String difficulty) {
		PreparedStatement st = null;
		String sql = "select * from problem where difficulty=?;";
		
		ArrayList<POPProblem> list = new ArrayList<>();
		
		boolean result = false;
		
		try {
            st = connection.prepareStatement(sql);
            st.setString(1, difficulty);
            ResultSet rs = st.executeQuery();
            if(rs.next()) {
            	POPProblem problem = new POPProblem(rs.getInt("number"), rs.getString("title"), rs.getString("content"), rs.getString("input"), rs.getString("output"), rs.getString("difficulty"));
            	System.out.println(problem.getTitle());
            	list.add(problem);
            }
            rs.close();
            st.close();
        } catch (SQLException se1) {
            se1.printStackTrace();
        }
		
		return list;
	}
}
