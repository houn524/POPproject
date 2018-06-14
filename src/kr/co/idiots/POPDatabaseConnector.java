package kr.co.idiots;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import kr.co.idiots.model.POPLoggedInMember;
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

	public void saveImageByUserIdAndProblemNumber(String user_id, int problem_number, File file) {
		PreparedStatement st = null;

		String sql = "select flowchart_id from solving where user_id=? and problem_number=?;";
		int flowchart_id;

		int autoincrement = 0;

		try {
			st = connection.prepareStatement(sql);//mainApp.getConnector().getConnection().createStatement();
			st.setString(1, user_id);
			st.setInt(2, problem_number);
			ResultSet rs = st.executeQuery();

			if(rs.next()) {
				flowchart_id = rs.getInt(1);
				saveImage(flowchart_id, file);
			}

			rs.close();
			st.close();
		} catch (SQLException se1) {
			se1.printStackTrace();
		}
	}

	public void saveImage(int id, File file) {
		PreparedStatement st = null;
		String sql = "";
		boolean result = false;

		try {
//            st = connection.prepareStatement(sql);//mainApp.getConnector().getConnection().createStatement();
//            st.setInt(1, id);
//            ResultSet rs = st.executeQuery();
			FileInputStream fin = null;
			if(id > -1) {
				fin = new FileInputStream(file);
				sql = "update flowchart set image=? where id=?;";
				st = connection.prepareStatement(sql);
				st.setBinaryStream(1, fin, (int) file.length());
				st.setInt(2, id);


			}

			st.executeUpdate();
			fin.close();
			st.close();
		} catch (SQLException | IOException se1) {
			se1.printStackTrace();
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
            	
            	sql = "insert into solving(user_id, problem_number, flowchart_id) values(?, ?, ?);";
            	
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
            	boolean solved = checkSolved(POPLoggedInMember.getInstance().getMember().getId(), rs.getInt("number"));
            	POPProblem problem = new POPProblem(rs.getInt("number"), rs.getString("title"), rs.getString("content"), rs.getString("input_example"), rs.getString("output_example"), 
            			rs.getString("input_case"), rs.getString("output_case"), rs.getString("difficulty"), solved);
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
	
	public boolean checkSolved(String id, int number) {
		PreparedStatement st = null;
		String sql = "select solved from solving where user_id=? and problem_number=?;";
		
		boolean result = false;
		
		try {
            st = connection.prepareStatement(sql);
            st.setString(1, id);
            st.setInt(2, number);
            ResultSet rs = st.executeQuery();
            if(rs.next()) {
            	result = rs.getBoolean(1);
            } else {
            	result = false;
            }
            rs.close();
            st.close();
        } catch (SQLException se1) {
            se1.printStackTrace();
        }
		
		return result;
	}
	
	public void setSolved(String id, int number, boolean solved) {
		PreparedStatement st = null;
		String sql = "update solving set solved=? where user_id=? and problem_number=?";
		boolean result = false;
		
		try {
            
        	st = connection.prepareStatement(sql);
            st.setBoolean(1, solved);
            st.setString(2, id);
            st.setInt(3, number);
            
            st.executeUpdate();
            System.out.println("set");
            st.close();
        } catch (SQLException se1) {
            se1.printStackTrace();
        }
	}
	
	public boolean checkOverlapID(String id) {
		PreparedStatement st = null;
		String sql = "select * from member where id=?;";
		
		boolean result = false;
		
		try {
            st = connection.prepareStatement(sql);
            st.setString(1, id);
            ResultSet rs = st.executeQuery();
            if(rs.next()) {
            	result = true;
            } else {
            	result = false;
            }
            rs.close();
            st.close();
        } catch (SQLException se1) {
            se1.printStackTrace();
        }
		
		return result;
	}
	
	public void insertMember(String id, String pw) {
		PreparedStatement st = null;
		String sql = "insert into member values(?, password(?));";
		String mp5 = "";
		boolean result = false;
		
		try {
//			MessageDigest md = MessageDigest.getInstance("MD5");
//			md.update(pw.getBytes());
//			byte byteData[] = md.digest();
//			StringBuffer sb = new StringBuffer();
//			for (int i = 0; i < byteData.length; i++) {
//	            sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
//	        }
//			mp5 = sb.toString();
			
            st = connection.prepareStatement(sql);
            st.setString(1, id);
            st.setString(2, pw);
//            System.out.println(mp5);
            st.executeUpdate();
            
            st.close();
        } catch (SQLException se1) {
            se1.printStackTrace();
        }
	}
	
	public boolean verifyMember(String id, String pw) {
//		String inputId = emailField.getText();
//		String inputPw = pwField.getText();
		
		PreparedStatement st = null;
		
		String sql = "select * from member where id=?";
		String actualPw = "";
		String encPw = "";
		boolean result = false;
		
		try {
            st = connection.prepareStatement(sql);//mainApp.getConnector().getConnection().createStatement();
            st.setString(1, id);
            
            ResultSet rs = st.executeQuery();
            if(!rs.next()) {
            	result = false;
            } else { 
            	actualPw = rs.getString("pw");
            	sql = "select password(?)";
            	st = connection.prepareStatement(sql);
            	st.setString(1, pw);
            	rs = st.executeQuery();
            	if(rs.next()) {
            		encPw = rs.getString(1);
            		if(encPw.equals(actualPw)) {
                		result = true;
                	} else {
                		result = false;
                	}
            	}
            }
 
            rs.close();
            st.close();
            
            
        } catch (SQLException se1) {
            se1.printStackTrace();
        }
		
		return result;
	}
}
