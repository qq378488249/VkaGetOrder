package cc.chenghong.vkagetorder.test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;

/**
 * Created by hcl on 2016/6/17.
 */
public class SortComparator implements Comparator {
    public int compare(Object arg0,Object arg1){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String str0 = arg0 + "";
        String str1 = arg1 + "";
        int result = 0;
        Date d1;
        Date d2;
        try {
            d1 = sdf.parse(str0);
            d2 = sdf.parse(str1);
            result = (int)(d1.getTime()-d2.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
//        int flag  = DataUtils.data1_compare_data2(user0.getCreated(), user1.getCreated(), "");
//        int flag = user0.getBirthday().compareTo(user1.getBirthday());
        return result;
    }

}
