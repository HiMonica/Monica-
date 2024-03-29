package com.monica.seckilldemo.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.monica.seckilldemo.pojo.User;
import com.monica.seckilldemo.vo.RespBean;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 生成用户
 */
public class UserUtils {

    private static void createUser(int count) throws Exception {
        List<User> users = new ArrayList<>(count);
        // 生成用户
        for (int i=0; i < count; i++){
            User user = new User();
            user.setPhone(17000000000L+i);
            user.setNickname("user"+i);
            user.setSalt("1a2b3c");
            user.setPassword(MD5Utils.inputPassToDBPass("123456", user.getSalt()));
            user.setRegisterDate(LocalDateTime.now());
            user.setLoginCount(1);
            users.add(user);
        }
        System.out.println("create user");
        // 插入数据库
//        Connection conn = getConn();
//        String sql = "insert into user(phone, nickname, salt, password, register_date, login_count) values(?,?,?,?,?,?)";
//        PreparedStatement pstat = conn.prepareStatement(sql);
//        for (int i = 0; i < count; i++) {
//            User user = users.get(i);
//            pstat.setLong(1, user.getPhone());
//            pstat.setString(2, user.getNickname());
//            pstat.setString(3, user.getSalt());
//            pstat.setString(4, user.getPassword());
//            pstat.setTimestamp(5, new Timestamp(user.getRegisterDate().getNano()));
//            pstat.setInt(6, user.getLoginCount());
//            pstat.addBatch();
//        }
//        pstat.executeBatch();
//        pstat.close();
//        conn.close();
//        System.out.println("insert to db");
        // 登录，生成token
        String uslString = "http://localhost:8080/login/doLogin";
        File file = new File( "/Users/liuyuyang/Documents/centos/config.txt");
        if (file.exists()){
            file.delete();
        }
        RandomAccessFile raf = new RandomAccessFile(file, "rw");
        file.createNewFile();
        raf.seek(0);
        for (int i = 0; i < users.size(); i++) {
            User user = users.get(i);
            URL url = new URL(uslString);
            HttpURLConnection huc = (HttpURLConnection) url.openConnection();
            huc.setRequestMethod("POST");
            huc.setDoOutput(true);
            OutputStream os = huc.getOutputStream();
            String params = "mobile=" + user.getPhone() + "&password=" + MD5Utils.inputPassToFromPass("123456");
            os.write(params.getBytes());
            os.flush();
            os.close();
            InputStream is = huc.getInputStream();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] buff = new byte[1024];
            int len = 0;
            while((len = is.read(buff)) >= 0){
                baos.write(buff, 0, len);
            }
            is.close();
            baos.close();
            String response = new String(baos.toByteArray());
            ObjectMapper objectMapper = new ObjectMapper();
            RespBean respBean = objectMapper.readValue(response, RespBean.class);
            String userTicket = (String) respBean.getObj();
            System.out.println("create userTicket : " + userTicket);

            String row = user.getPhone() + "," + userTicket + "\r\n";
            raf.seek(raf.length());
            raf.write(row.getBytes());
            System.out.println("write to file : " + user.getPhone());
        }
        raf.close();
        System.out.println("over");
    }

        private static Connection getConn() throws Exception {
        String driver = "com.mysql.cj.jdbc.Driver";
        String url = "jdbc:mysql://localhost:3306/seckill?useUnicaode=true&characterEncoding=UTF-8&serverTimezone=Asia/Shanghai";
        String username = "root";
        String password = "lyy18281235812";
        Class.forName(driver);
        return DriverManager.getConnection(url,username,password);
    }

    public static void main(String[] args) throws Exception {
        createUser(5000);
    }
}
