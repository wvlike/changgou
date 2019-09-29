import com.alibaba.druid.sql.visitor.functions.Char;
import org.junit.Test;

import java.io.*;
import java.util.*;

/**
 * package PACKAGE_NAME;
 *
 * @auther txw
 * @create 2019-09-19  14:00
 * @description：
 */
public class MyTest {

    @Test
    public void test01() throws Exception {
        Scanner sc = new Scanner(System.in);
        System.out.println("请输入用户名");
        String username = sc.nextLine();
        System.out.println("请输入密码");
        String password = sc.nextLine();

        Random random = new Random();
        String str = "";
        for (int i = 48; i <= 57; i++) {
            str += (char) i;
        }
        for (int i = 65; i <= 90; i++) {
            str += (char) i;
        }
        for (int i = 97; i <= 122; i++) {
            str += (char) i;
        }
        int index;
        String code = "";
        for (int i = 0; i < 4; i++) {
            index = random.nextInt(62);
            code += str.charAt(index);
        }
//        System.out.println(code);

        System.out.println("验证码为：" + code);
        System.out.println("请输入验证码");
        String userCode = sc.nextLine();
        if (userCode.equalsIgnoreCase(code)) {

            FileInputStream fis = new FileInputStream("E:\\user.txt");
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);
            List<String> list = new ArrayList<>();
            String buffer;
            while ((buffer = br.readLine()) != null) {
                list.add(buffer);
            }
            br.close();
            isr.close();
            fis.close();
            if (list.size() > 0) {
                for (String s : list) {
                    String[] split = s.split(",");
                    if (split[0].equals(userCode)) {
                        System.out.println("用户名重复");
                        return;
                    }
                }
            }
            String userInfo = username + "," + password;
            list.add(userInfo);
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("E:\\user.txt")));
            for (String info : list) {
                bw.write(info);
                bw.newLine();
            }
            bw.close();

        } else {
            System.out.println("验证码有误");
        }


    }

    @Test
    public void test02() {
        String str = "";
        for (int i = 48; i <= 57; i++) {
            str += (char) i;
        }
        for (int i = 65; i <= 90; i++) {
            str += (char) i;
        }
        for (int i = 97; i <= 122; i++) {
            str += (char) i;
        }
        System.out.println(str);


    }

    @Test
    public void test03() {
        Scanner sc = new Scanner(System.in);
        System.out.println("请输入用户名");
        String username = sc.nextLine();
        System.out.println("请输入密码");
        String password = sc.nextLine();
        System.out.println(username + password);
    }

    @Test
    public void test04() {
        Map map = new HashMap();
        map.put(null, null);
        System.out.println(map.get(null));
    }

    @Test
    public void test05() {

        char str = 'a';
        System.out.println(Integer.valueOf(str));
        int num = 111;
        System.out.println((char) 111);

    }
}
