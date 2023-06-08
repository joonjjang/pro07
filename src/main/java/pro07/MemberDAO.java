package pro07;

import java.sql.Connection; // 특정 데이터베이스에 연결
import java.sql.Date;
import java.sql.DriverManager; //JDBC 드라이버를 관리하기 위한 기본 클래스 
import java.sql.PreparedStatement; //SQL문을 미리 컴파일하기 위한 클래스
import java.sql.ResultSet;
import java.sql.Statement; //The object used for executing a static SQL statementand returning the results it produces
import java.util.ArrayList;
import java.util.List;

public class MemberDAO {

	// 필
	private static final String driver = "oracle.jdbc.driver.OracleDriver";
	private static final String url = "jdbc:oracle:thin:@localhost:1521:orcl";
	private static final String user = "scott";
	private static final String pwd = "12341234";

	Statement stmt; //
	PreparedStatement pstmt; // SQL 문을 미리 만들어 두는 방식
	ResultSet rs;
	Connection conn;

	// 생
	public List<MemberVO> listMembers() {
		List<MemberVO> list = new ArrayList<MemberVO>();

		connDB();

		String query = "SELECT * FROM t_member where id='hong' ";
		System.out.println(query);

		try {
			pstmt = conn.prepareStatement(query);
			rs = pstmt.executeQuery(); // Executes the given SQL statement, which returns a single ResultSet object.

			// ResultSet은 객체이므로 다시 풀어주는 과정을 코딩
			// ResultSet은 데이터베이스 내부적으로 수행된 SQL문의 처리 결과를 JDBC에서 쉽게 관리 할수 있도록 해줌
			// ResultSet은 next() 메서드를 이용해서 다음 로우(row, 행) 으로 이동할 수 있다.
			// 커서를 최초 데이터 위치로 이동시키려면, ResultSet을 사용하기 전에 rs.next() 메소드를 한 번 이용해 줄 필요가 있다.
			// 대부분의 경우 executeQuery() 메서드를 수행한 후 while(rs.next())와 같이 더 이상 로우가 없을 때까지 루프를
			// 돌면서 데이터를 처리하는 방법이 유용함.
//			rs.nest()  //한 줄 씩 커서(결과가 여러줄이므로 맨 위에 줄부터 접근 하게 하는 포인터) 를 옮김

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
			conn.close(); // 데이터 베이스와의 연결을 관리하는 Connection 인스턴스는 사용한 뒤 반납하지 않으면 개속해서 연결을 유지하므로, 어느 시점에서는
			//데이터 베이스 연결이 부족한 상황이 발생할 수 있따. 따라서 사용자가 많은 시스템일수록 커넥션 관리가 중요한데, 이를 위해 커넥션 풀(Connection Pool)을 이용함
			//커넥션 풀을 사용여부와는 상관없이 데이터베이스 연결은 해당 연결을 통한 직업이 모두 끝나는 시점에서 close()메서드를 이용해 해제하는 것이 좋다.
			
//			Connection Pool : 미리 데이터베이스와 연결시켜놓는 기술 

		} catch (Exception e) {
			System.out.println("sql 문장을 돌리는데 문제가 생김");
		}

		return list;

	}

	// 메
	private void connDB() {
		try {
			Class.forName(driver); // 해당드라이버 클래스를 불러옴
			System.out.println("DB연결");
			conn = DriverManager.getConnection(url, user, pwd);
			pstmt = (PreparedStatement) conn.createStatement(); // Creates a Statement object for sendingSQL statements
																// to the database
		} catch (Exception e) {
			System.out.println("DB연결에 문제가 생김");

		}
	}

}
