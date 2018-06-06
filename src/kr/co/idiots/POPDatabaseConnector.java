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
	
	public void saveFlowchartByUserIdAndProblemNumber(String user_id, int problem_number, String content) {
		PreparedStatement st = null;
		
		String sql = "select flowchart_id from solving where user_id=? and problem_number=?;";
		int flowchart_id;
		
		int autoincrement = 0;
		
		boolean result = false;
		
		try {
            st = connection.prepareStatement(sql);//mainApp.getConnector().getConnection().createStatement();
            st.setString(1, user_id);
            st.setInt(2, problem_number);
            ResultSet rs = st.executeQuery();
            
            if(rs.next()) {
            	flowchart_id = rs.getInt(1);
            	saveFlowchart(flowchart_id, content);
            } else {
            	saveFlowchart(-1, content);
            	st = connection.prepareStatement("select auto_increment from information_schema.TABLES where TABLE_SCHEMA='popdb' and TABLE_NAME='flowchart';");
            	rs = st.executeQuery();
            	if(rs.next()) 
            		autoincrement = rs.getInt(1) - 1;
            	
            	sql = "insert into solving values(?, ?, ?);";
            	
            	st = connection.prepareStatement(sql);
            	st.setString(1, user_id);
            	st.setInt(2, problem_number);
            	st.setInt(3, autoincrement);
            	
            	st.executeUpdate();
            }
 
            rs.close();
            st.close();
        } catch (SQLException se1) {
            se1.printStackTrace();
        }
	}
	
	public void saveFlowchart(int id, String content) {
		PreparedStatement st = null;
		String sql = "";
		boolean result = false;
		
		try {
//            st = connection.prepareStatement(sql);//mainApp.getConnector().getConnection().createStatement();
//            st.setInt(1, id);
//            ResultSet rs = st.executeQuery();
            
            if(id > -1) {
            	sql = "update flowchart set content=? where id=?;";
            	st = connection.prepareStatement(sql);
                st.setString(1, content);
                st.setInt(2, id);
            } else {
            	sql = "insert into flowchart(content) values(?)";
            	st = connection.prepareStatement(sql);
                st.setString(1, content);
            }
            
            st.executeUpdate();
 
            st.close();
        } catch (SQLException se1) {
            se1.printStackTrace();
        }
	}
	
	public String loadFlowchartByUserIdAndProblemNumber(String user_id, int problem_number) {
		PreparedStatement st = null;
		int flowchart_id;
		String sql = "select flowchart_id from solving where user_id=? and problem_number=?;";
		String content = "";
		boolean result = false;
		
		try {
            st = connection.prepareStatement(sql);
            st.setString(1, user_id);
            st.setInt(2, problem_number);
            ResultSet rs = st.executeQuery();
            
            if(rs.next()) {
            	flowchart_id = rs.getInt(1);
            	content = loadFlowchart(flowchart_id);
            } else {
            	content = "Start::::::::::Stop";
            }
            rs.close();
            st.close();
        } catch (SQLException se1) {
            se1.printStackTrace();
        }
		return content;
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
            while(rs.next()) {
            	POPProblem problem = new POPProblem(rs.getInt("number"), rs.getString("title"), rs.getString("content"), rs.getString("input_example"), rs.getString("output_example"), 
            			rs.getString("input_case"), rs.getString("output_case"), rs.getString("difficulty"));
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
