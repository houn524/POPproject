package kr.co.idiots;

import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;

import javafx.scene.image.Image;
import kr.co.idiots.model.POPComment;
import kr.co.idiots.model.POPLoggedInMember;
import kr.co.idiots.model.POPPost;
import kr.co.idiots.model.POPProblem;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class POPDatabaseConnector {
	
	private Connection connection;
	
	public POPDatabaseConnector() {
		connect();
	}

	public void connect() {
		connection = null;
		Statement st = null;
		try {
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

	public String loadProblemTitle(int number) {
		PreparedStatement st = null;
		String sql = "select title from problem where number=?;";
		String result = "";
		try {
			st = getConnection().prepareStatement(sql);
			st.setInt(1, number);
			ResultSet rs = st.executeQuery();
			if(rs.next()) {
				result = rs.getString("title");
			}
			rs.close();
			st.close();
		} catch (SQLException se1) {
			se1.printStackTrace();
		}
		return result;
	}

	public boolean checkProblemNumber(int number) {
		PreparedStatement st = null;
		String sql = "select * from problem where number=?;";

		boolean result = false;

		try {
			st = getConnection().prepareStatement(sql);
			st.setInt(1, number);
			ResultSet rs = st.executeQuery();
			if(!rs.next()) {
				result = false;
			} else {
				result = true;
			}
			rs.close();
			st.close();
		} catch (SQLException se1) {
			se1.printStackTrace();
		}

		return result;
	}

	public void insertPost(POPPost post) {
		PreparedStatement st = null;
		String sql = "";
		try {
			sql = "insert into post(title, content, author, date, problem_number, image)" +
					" values(?, ?, ?, ?, ?, ?);";
            st = getConnection().prepareStatement(sql);
            st.setString(1, post.getTitle());
            st.setString(2, post.getContent());
            st.setString(3, post.getAuthor());
            st.setString(4, post.getDate());
            st.setInt(5, post.getProblemNumber());
            if(post.getInputStream() == null) {
				st.setNull(6, Types.BINARY);
			} else {
				st.setBinaryStream(6, post.getInputStream());
			}
            st.executeUpdate();
			st.close();
		} catch (SQLException se1) {
			se1.printStackTrace();
		}
	}

	public void insertComment(POPComment comment) {
		PreparedStatement st = null;
		String sql = "";
		try {
			sql = "insert into comment(content, author, date, post_number, image)" +
					" values(?, ?, ?, ?, ?);";
			st = getConnection().prepareStatement(sql);
			st.setString(1, comment.getContent());
			st.setString(2, comment.getAuthor());
			st.setString(3, comment.getDate());
			st.setInt(4, comment.getPostNumber());
			if(comment.getInputStream() != null) {
				st.setBinaryStream(5, comment.getInputStream());
			} else {
				st.setNull(5, Types.BINARY);
			}
			st.executeUpdate();
			st.close();
		} catch (SQLException se1) {
			se1.printStackTrace();
		}
	}

	public void deletePost(int postNumber) {
		PreparedStatement st = null;
		String sql = "";
		try {
			sql = "delete from post where number=?;";
			st = getConnection().prepareStatement(sql);
			st.setInt(1, postNumber);
			st.executeUpdate();
			st.close();
		} catch (SQLException se1) {
			se1.printStackTrace();
		}
	}

	public void deleteComment(int commentNumber) {
		PreparedStatement st = null;
		String sql = "";
		try {
			sql = "delete from comment where number=?;";
			st = getConnection().prepareStatement(sql);
			st.setInt(1, commentNumber);
			st.executeUpdate();
			st.close();
		} catch (SQLException se1) {
			se1.printStackTrace();
		}
	}

	public void saveImageByUserIdAndProblemNumber(String user_id, int problem_number, InputStream is) {
		PreparedStatement st = null;

		String sql = "select flowchart_id from solving where user_id=? and problem_number=?;";
		int flowchart_id;

		try {
			st = getConnection().prepareStatement(sql);
			st.setString(1, user_id);
			st.setInt(2, problem_number);
			ResultSet rs = st.executeQuery();

			if(rs.next()) {
				flowchart_id = rs.getInt(1);
				saveImage(flowchart_id, is);
			}

			rs.close();
			st.close();
		} catch (SQLException se1) {
			se1.printStackTrace();
		}
	}

	public void saveImage(int id, InputStream is) {
		PreparedStatement st = null;
		String sql = "";
		boolean result = false;

		try {
			FileInputStream fin = null;
			if(id > -1) {
//				fin = new FileInputStream(file);
				sql = "update flowchart set image=? where id=?;";
				st = getConnection().prepareStatement(sql);
//				st.setBinaryStream(1, fin, (int) file.length());
				st.setBlob(1, is);
				st.setInt(2, id);


			}

			st.executeUpdate();
//			fin.close();
			st.close();
		} catch (SQLException se1) {
			se1.printStackTrace();
		}
	}
	
	public void saveFlowchartByUserIdAndProblemNumber(String user_id, int problem_number, String content) {
		PreparedStatement st = null;
		
		String sql = "select flowchart_id from solving where user_id=? and problem_number=?;";
		int flowchart_id;
		
		int autoincrement = 0;
		try {
            st = getConnection().prepareStatement(sql);
            st.setString(1, user_id);
            st.setInt(2, problem_number);
            ResultSet rs = st.executeQuery();
            
            if(rs.next()) {
            	flowchart_id = rs.getInt(1);
            	saveFlowchart(flowchart_id, content);
            } else {
            	saveFlowchart(-1, content);
            	st = getConnection().prepareStatement("select auto_increment from information_schema.TABLES where TABLE_SCHEMA='popdb' and TABLE_NAME='flowchart';");
            	rs = st.executeQuery();
            	if(rs.next()) 
            		autoincrement = rs.getInt(1) - 1;
            	
            	sql = "insert into solving(user_id, problem_number, flowchart_id) values(?, ?, ?);";
            	
            	st = getConnection().prepareStatement(sql);
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
		try {
            if(id > -1) {
            	sql = "update flowchart set content=? where id=?;";
            	st = getConnection().prepareStatement(sql);
                st.setString(1, content);
                st.setInt(2, id);
            } else {
            	sql = "insert into flowchart(content) values(?)";
            	st = getConnection().prepareStatement(sql);
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
		try {
            st = getConnection().prepareStatement(sql);
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
		try {
            st = getConnection().prepareStatement(sql);
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
		return content;
	}
	
	public ArrayList<POPProblem> loadProblems(String difficulty) {
		PreparedStatement st = null;
		String sql = "select * from problem where difficulty=?;";
		
		ArrayList<POPProblem> list = new ArrayList<>();
		try {
            st = getConnection().prepareStatement(sql);
            st.setString(1, difficulty);
            ResultSet rs = st.executeQuery();
            while(rs.next()) {
            	boolean solved = checkSolved(POPLoggedInMember.getInstance().getMember().getId(), rs.getInt("number"));
            	POPProblem problem = new POPProblem(rs.getInt("number"), rs.getString("title"), rs.getString("content"), rs.getString("input_example"), rs.getString("output_example"), 
            			rs.getString("input_case"), rs.getString("output_case"), rs.getString("difficulty"), solved);
            	list.add(problem);
            }
            rs.close();
            st.close();
        } catch (SQLException se1) {
            se1.printStackTrace();
        }
		
		return list;
	}

	public POPProblem loadProblemByNumber(int number) {
		PreparedStatement st = null;
		String sql = "select * from problem where number=?;";

		POPProblem result = null;

		try {
			st = getConnection().prepareStatement(sql);
			st.setInt(1, number);
			ResultSet rs = st.executeQuery();
			if(rs.next()) {
				boolean solved = checkSolved(POPLoggedInMember.getInstance().getMember().getId(), rs.getInt("number"));
				result = new POPProblem(rs.getInt("number"), rs.getString("title"), rs.getString("content"), rs.getString("input_example"), rs.getString("output_example"),
						rs.getString("input_case"), rs.getString("output_case"), rs.getString("difficulty"), solved);
			}
			rs.close();
			st.close();
		} catch (SQLException se1) {
			se1.printStackTrace();
		}

		return result;
	}

	public int loadFlowchartId(String user_id, int problem_number) {
		PreparedStatement st = null;
		String sql = "select flowchart_id from solving where user_id=? and problem_number=?;";
		int result = 0;
		try {
			st = getConnection().prepareStatement(sql);
			st.setString(1, user_id);
			st.setInt(2, problem_number);
			ResultSet rs = st.executeQuery();

			if(rs.next()) {
				result = rs.getInt("flowchart_id");
			}
			rs.close();
			st.close();
		} catch (SQLException se1) {
			se1.printStackTrace();
		}
		return result;
	}

	public ArrayList<Integer> loadFlowchartIds(String user_id) {
		ArrayList<Integer> list = new ArrayList<>();
		PreparedStatement st = null;
		String sql = "select flowchart_id from solving where user_id=? and solved=true;";
		try {
			st = getConnection().prepareStatement(sql);
			st.setString(1, user_id);
			ResultSet rs = st.executeQuery();

			while(rs.next()) {
				list.add(rs.getInt("flowchart_id"));
			}
			rs.close();
			st.close();
		} catch (SQLException se1) {
			se1.printStackTrace();
		}
		return list;
	}

	public ArrayList<Image> loadImages(String user_id) {
		ArrayList<Image> list = new ArrayList<>();
		PreparedStatement st = null;
		String sql = "select flowchart_id from solving where user_id=? and solved=true;";
		try {
			st = getConnection().prepareStatement(sql);
			st.setString(1, user_id);
			ResultSet rs = st.executeQuery();

			while(rs.next()) {
				Image image = null;
				image = loadImage(rs.getInt("flowchart_id"));
				if(image != null) {

					list.add(image);
				}
			}
			rs.close();
			st.close();
		} catch (SQLException se1) {
			se1.printStackTrace();
		}
		return list;
	}

	public Image loadImage(int flowchart_id) {
		PreparedStatement st = null;
		InputStream is = null;
		Image image = null;
		String sql = "select image from flowchart where id=?;";
		try {
			st = getConnection().prepareStatement(sql);
			st.setInt(1, flowchart_id);
			ResultSet rs = st.executeQuery();

			if(rs.next()) {
				is = rs.getBinaryStream("image");
				if(is != null)
					image = new Image(is);
			}
			rs.close();
			st.close();
		} catch (SQLException se1) {
			se1.printStackTrace();
		}

		return image;
	}
	
	public InputStream loadInputStream(int flowchart_id) {
		PreparedStatement st = null;
		InputStream is = null;
		String sql = "select image from flowchart where id=?;";
		try {
			st = getConnection().prepareStatement(sql);
			st.setInt(1, flowchart_id);
			ResultSet rs = st.executeQuery();

			if(rs.next()) {
				is = rs.getBinaryStream("image");
			}
			rs.close();
			st.close();
		} catch (SQLException se1) {
			se1.printStackTrace();
		}

		return is;
	}

	public ArrayList<POPPost> loadPosts() {
		PreparedStatement st = null;
		String sql = "select * from post order by date desc;";

		ArrayList<POPPost> list = new ArrayList<>();
		int commentCount = 0;
		try {
			st = getConnection().prepareStatement(sql);
			ResultSet rs = st.executeQuery();
			while(rs.next()) {
				InputStream is = null;
				is = rs.getBinaryStream("image");

				commentCount = countComment(rs.getInt("number"));
				POPPost post = new POPPost(rs.getInt("number"), rs.getString("title"), rs.getString("content"),
						rs.getString("author"), commentCount, rs.getString("date"), rs.getInt("problem_number"), is);
				list.add(post);
			}
			rs.close();
			st.close();
		} catch (SQLException se1) {
			se1.printStackTrace();
		}
		return list;
	}

	public ArrayList<POPComment> loadComments(int postNumber) {
		PreparedStatement st = null;
		String sql = "select * from comment where post_number=?";

		ArrayList<POPComment> list = new ArrayList<>();

		try {
			if(getConnection() == null)
				connect();
			st = getConnection().prepareStatement(sql);
			st.setInt(1, postNumber);
			ResultSet rs = st.executeQuery();
			while(rs.next()) {
				POPComment comment = new POPComment(
						rs.getInt("number"),
						rs.getString("content"),
						rs.getString("author"),
						rs.getString("date"),
						rs.getInt("post_number"),
						rs.getBinaryStream("image")
				);

				list.add(comment);
			}
			rs.close();
			st.close();
		} catch (SQLException se1) {
			se1.printStackTrace();
		}

		return list;
	}

	public int countComment(int post_number) {
		PreparedStatement st = null;
		String sql = "select * from comment where post_number=?;";

		int result = 0;

		try {
			st = getConnection().prepareStatement(sql);
			st.setInt(1, post_number);
			ResultSet rs = st.executeQuery();
			if(rs.next()) {
				rs.last();
				result = rs.getRow();
				rs.beforeFirst();
			}
			rs.close();
			st.close();
		} catch (SQLException se1) {
			se1.printStackTrace();
		}

		return result;
	}
	
	public boolean checkSolved(String id, int number) {
		PreparedStatement st = null;
		String sql = "select solved from solving where user_id=? and problem_number=?;";
		
		boolean result = false;
		
		try {
            st = getConnection().prepareStatement(sql);
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
		try {
            
        	st = getConnection().prepareStatement(sql);
            st.setBoolean(1, solved);
            st.setString(2, id);
            st.setInt(3, number);
            
            st.executeUpdate();
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
            st = getConnection().prepareStatement(sql);
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

		try {
            st = getConnection().prepareStatement(sql);
            st.setString(1, id);
            st.setString(2, pw);
            st.executeUpdate();
            
            st.close();
        } catch (SQLException se1) {
            se1.printStackTrace();
        }
	}
	
	public boolean verifyMember(String id, String pw) {
		PreparedStatement st = null;
		
		String sql = "select * from member where id=?";
		String actualPw = "";
		String encPw = "";
		boolean result = false;
		
		try {
            st = getConnection().prepareStatement(sql);
            st.setString(1, id);
            
            ResultSet rs = st.executeQuery();
            if(!rs.next()) {
            	result = false;
            } else { 
            	actualPw = rs.getString("pw");
            	sql = "select password(?)";
            	st = getConnection().prepareStatement(sql);
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
	
	public Connection getConnection() {
		try {
			if(connection == null || connection.isClosed()) {
				connect();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return connection;
	}
}