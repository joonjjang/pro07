package sec07_01;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

//import sec07_01.MemberVO;

public class MemberDAO {

	private DataSource dataFactory;
	private Connection conn;
	private PreparedStatement pstmt;
//	private ResultSet rs;

	public MemberDAO() {
		// JNDI방식의 연결로 Member DAO 객체를 초기
		try {
		Context ctx = new InitialContext(); //컨텍스트 작업을 위한 객체
		Context envContext = (Context) ctx.lookup("java:/comp/env");
		dataFactory = (DataSource) envContext.lookup("jdbc/oracle");
		} catch (Exception e) {
			System.out.println("톰캣의 context.xml에 정의 되어있는 이름부분에서 정확한");

		}
	}
	
	
	public List<MemberVO> listMembers() {
		List<MemberVO> list = new ArrayList<MemberVO>();
		try {
			conn = dataFactory.getConnection();
			String query = "SELECT * FROM t_member ";
					
			pstmt = conn.prepareStatement(query);
			ResultSet rs = pstmt.executeQuery(); // Executes the given SQL statement, which returns a single ResultSet object.


			while (rs.next()) {

				String id = rs.getString("id"); // 칼럼 이름(ID)에 해당하는 값을 가져옴
				String pwd = rs.getString("pwd");
				String name = rs.getString("name");
				String email = rs.getString("email");
				Date joinDate = rs.getDate("joinDate");
				System.out.println(id + pwd + name + email + joinDate);

				MemberVO vo = new MemberVO();

				vo.setId(id);
				vo.setPwd(pwd);
				vo.setName(name);
				vo.setEmail(email);
				vo.setJoinDate(joinDate);

				list.add(vo);
				System.out.println("==========================");
			}

			rs.close(); // ResultSet의 사용이 끝났으면 close() 메소드를 이용해 ResultSet을 닫아주도록 함
			pstmt.close();
			conn.close(); 
		} catch (Exception e) {
			// TODO: handle exception
		}

		return list;
	}

}
