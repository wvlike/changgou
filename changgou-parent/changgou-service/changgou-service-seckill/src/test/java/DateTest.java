import entity.DateUtil;

import java.util.Date;
import java.util.List;

/**
 * package PACKAGE_NAME;
 *
 * @auther txw
 * @create 2019-09-12  15:19
 * @description：
 */
public class DateTest {
    public static void main(String[] args) {

        List<Date> dates = DateUtil.getDateMenus();
        for (Date date : dates) {
            System.out.println(DateUtil.data2str(date, DateUtil.PATTERN_YYYYMMDDHH));

        }
    }
}
