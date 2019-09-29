import org.junit.Test;

import java.util.*;

/**
 * package PACKAGE_NAME;
 *
 * @auther txw
 * @create 2019-09-19  13:25
 * @description：
 */
public class TestMyself {

    class User {
        private String name;
        private Integer age;
        public User(String name, Integer age) {
            this.name = name;
            this.age = age;
        }
        public String getName() {
            return name;
        }
        public void setName(String name) {
            this.name = name;
        }

        public Integer getAge() {
            return age;
        }

        public void setAge(Integer age) {
            this.age = age;
        }

        @Override
        public String toString() {
            return "User{" +
                    "name='" + name + '\'' +
                    ", age=" + age +
                    '}';
        }
    }

    @Test
    public void test01() {
        HashMap<Integer, User> users = new HashMap<>();
        users.put(1, new User("张三", 25));
        users.put(3, new User("李四", 22));
        users.put(2, new User("王五", 28));
        HashMap<Integer, User> sortHashMap = sortHashMap(users);
        System.out.println(sortHashMap);
    }

    private HashMap<Integer, User> sortHashMap(HashMap<Integer, User> users) {
        Set<Map.Entry<Integer, User>> entries = users.entrySet();
        LinkedHashMap<Integer, User> linkedHashMap = new LinkedHashMap<>();
        List<Map.Entry<Integer, User>> list = new ArrayList<>(entries);
        Collections.sort(list,(entry1,entry2)->{
            //前减后倒叙
            return entry2.getValue().getAge()-entry1.getValue().getAge();
        });
        for (Map.Entry<Integer, User> entry : list) {
            linkedHashMap.put(entry.getKey(),entry.getValue());
        }
        return linkedHashMap;
    }
}
