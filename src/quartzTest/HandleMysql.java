package quartzTest;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.Reader;
import java.sql.*;

/**
 * Created by root on 15-7-16.
 */
public class HandleMysql {

    private static final HandleMysql single = new HandleMysql();

    public static HandleMysql getInstance() {
        return single;
    }
    public void init() {
        Connection conn = null;
        String sql;
        // MySQL的JDBC URL编写方式：jdbc:mysql://主机名称：连接端口/数据库的名称?参数=值
        // 避免中文乱码要指定useUnicode和characterEncoding
        // 执行数据库操作之前要在数据库管理系统上创建一个数据库，名字自己定，
        // 下面语句之前就要先创建javademo数据库
        String url = "jdbc:mysql://localhost/mysql?"
                + "user=root&password=root&useUnicode=true&characterEncoding=UTF8";

        try {
            // 之所以要使用下面这条语句，是因为要使用MySQL的驱动，所以我们要把它驱动起来，
            // 可以通过Class.forName把它加载进去，也可以通过初始化来驱动起来，下面三种形式都可以
            Class.forName("com.mysql.jdbc.Driver");// 动态加载mysql驱动
            // or:
            // com.mysql.jdbc.Driver driver = new com.mysql.jdbc.Driver();
            // or：
            // new com.mysql.jdbc.Driver();

            System.out.println("成功加载MySQL驱动程序");
            // 一个Connection代表一个数据库连接
            conn = DriverManager.getConnection(url);
            // Statement里面带有很多方法，比如executeUpdate可以实现插入，更新和删除等
            Statement stmt = conn.createStatement();
            sql = "create table student(NO char(20),name varchar(20),resume text,image longblob,primary key(NO))";
            int result = stmt.executeUpdate(sql);// executeUpdate语句会返回一个受影响的行数，如果返回-1就没有成功
            result = stmt.executeUpdate("alter table student change name name varchar(100) character set utf8 collate utf8_unicode_ci not null default ''");
            result = stmt.executeUpdate("alter table student change resume resume text character set utf8 collate utf8_unicode_ci");
            if (result != -1) {
                System.out.println("创建数据表成功");
                String test = "陶伟基";
                System.out.println(test);
                byte [] bytes = test.getBytes();
                for (int i = 0; i < bytes.length; i++){
                    System.out.println(bytes[i]);
                    System.out.printf("%x", bytes[i]);

                }
                sql = "insert into student(NO,name) values('2012001','陶伟基')";
                result = stmt.executeUpdate(sql);
                sql = "insert into student(NO,name) values('2012002','周小俊')";
                result = stmt.executeUpdate(sql);
                sql = "select * from student";
                ResultSet rs = stmt.executeQuery(sql);// executeQuery会返回结果的集合，否则返回空值
                System.out.println("学号\t姓名");
                while (rs.next()) {
                    System.out
                            .println(rs.getString(1) + "\t" + rs.getString(2));// 入如果返回的是int类型可以用getInt()
                }
            }
            sql = "insert into student(NO,resume) values('2012003',?)";
            PreparedStatement st = conn.prepareStatement(sql);
            System.out.println(HandleMysql.class.getClassLoader().getResource("/test.txt"));
            String path = HandleMysql.class.getResource("/test.txt").getPath();
            path = path.replaceAll("%20", " ");
            File file = new File(path);
            Reader reader = new FileReader(file);
            st.setCharacterStream(1, reader,(int) file.length());
            int num = st.executeUpdate();
            if(num>0){
                System.out.println("插入成功！！");
            }
            reader.close();

            sql = "insert into student(NO,image) values('2012004',?)";
            st = conn.prepareStatement(sql);
            path = HandleMysql.class.getResource("/test.txt").getPath();
            path = path.replaceAll("%20", " ");
            file = new File(path);
            FileInputStream fis = new FileInputStream(file);
            st.setBinaryStream(1, fis, (int)file.length());
            num = st.executeUpdate();
            if (num > 0){
                System.out.println("插入成功！！");
            }
            fis.close();
            conn.close();
        } catch (SQLException e) {
            System.out.println("MySQL操作错误");
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
