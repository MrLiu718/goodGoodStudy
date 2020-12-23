package cn;

import java.io.File;
import java.io.IOException;
import java.sql.*;

public class HubbletpJdbcTest {
	//数据库连接对象
    private static Connection conn = null;
     
    private static String driver = ""; //驱动
     
    private static String url = ""; //连接字符串
     
    private static String username = ""; //用户名
     
    private static String password = ""; //密码
    
    String ENTER = "\r\n";
     
     
    // 获得连接对象
    private static synchronized Connection getConn(){
        if(conn == null){
            try {
                Class.forName(driver);
                conn = DriverManager.getConnection(url, username, password);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return conn;
    }
     
    //执行查询语句
    public void query(String sql, boolean isSelect) throws SQLException{
        PreparedStatement pstmt;
         
        try {
            pstmt = getConn().prepareStatement(sql);
            //建立一个结果集，用来保存查询出来的结果
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                String name = rs.getString("name");
                System.out.println(name);
            }
            rs.close();
            pstmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
  //执行查询语句
    public ResultSet query(String sql) throws SQLException{
        PreparedStatement pstmt;
         
        try {
            pstmt = getConn().prepareStatement(sql);
            //建立一个结果集，用来保存查询出来的结果
            ResultSet rs = pstmt.executeQuery();
            return rs;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
     
/*    public void query(String sql) throws SQLException{
        PreparedStatement pstmt;
        pstmt = getConn().prepareStatement(sql);
        pstmt.execute();
        pstmt.close();
    }*/
     
     
    //关闭连接
    public void close(){
        try {
            getConn().close();
             
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    @SuppressWarnings("resource")
	public static void main(String[] args) throws SQLException, IOException {
    	
    	String driver = "org.postgresql.Driver";    //驱动标识符
        String url = "jdbc:postgresql://50.2.66.27:21080/temp?sslmode=require"; //链接字符串

        String user = "hubble";         //数据库的用户名
        String password = "hubble";     //数据库的密码
        Connection con = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;
        boolean flag = false;
        
        String sql = "select cust_id,open_acct_dt,cert_type_cd,gender_cd,start_dt  from sum_data_t98_cust_base_info where cust_id in('80874058','90874058')";
        

        try {
            Class.forName(driver);
            con = DriverManager.getConnection(url,user, password);
            
            //查询记录
            pstm =con.prepareStatement(sql);
            rs = pstm.executeQuery();
            
            while(rs.next()) {
                String cust_id =rs.getString("CUST_ID");
                String gender_cd =rs.getString("GENDER_CD");
                
                System.out.println("更新操作前： cust_id=" + cust_id + " and gender_cd=" + gender_cd);
            }
            
            con.setAutoCommit(false);
            
            //业务逻辑：将两个用户的gender_cd进行调换
            String update_1 = "UPDATE sum_data_t98_cust_base_info SET gender_cd = '1' WHERE cust_id = '80874058'";
            pstm =con.prepareStatement(update_1);
            pstm.execute();
            
            //业务逻辑
            String update_2 = "UPDATE sum_data_t98_cust_base_info SET gender_cd = '0' WHERE cust_id = '90874058'";
            pstm =con.prepareStatement(update_2);
            pstm.execute();
            
            
            con.commit();
            flag = true;
            
            //查询记录
            pstm =con.prepareStatement(sql);
            rs = pstm.executeQuery();
            
            System.out.println("=============================================================");
            
            while(rs.next()) {
                String cust_id =rs.getString("CUST_ID");
                String gender_cd =rs.getString("GENDER_CD");
                
                System.out.println("更新操作后： cust_id=" + cust_id + " and gender_cd=" + gender_cd);
            }
            

        } catch(ClassNotFoundException e) {
            e.printStackTrace();
        }
        catch(SQLException e) {
        	//如果程序异常则回滚
        	con.rollback();
            e.printStackTrace();
            
          //查询记录
            pstm =con.prepareStatement(sql);
            rs = pstm.executeQuery();
            
            System.out.println("====================异常，进行回滚=================================");
            
            while(rs.next()) {
                String cust_id =rs.getString("CUST_ID");
                String gender_cd =rs.getString("GENDER_CD");
                
                System.out.println("查看数据： cust_id=" + cust_id + " and gender_cd=" + gender_cd);
            }
        }
        finally {
            if(rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

            // 关闭执行通道
            if(pstm !=null) {
                try {
                    pstm.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

            // 关闭连接通道
            try {
                if(con != null &&(!con.isClosed())) {
                    try {
                        con.close();
                    } catch (SQLException e) {
                       e.printStackTrace();
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        if(flag) {
            //System.out.println("执行成功！");
        } else {
            //System.out.println("执行失败！");
        }
    }
}
