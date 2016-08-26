package cc.chenghong.vkagetorder.test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by hcl on 2016/6/17.
 */
public class Test1 {
    public static void main(String[] args) {
        List<String> list = new ArrayList<>();
        String str1 = "2016-06-16 16:16:00";
        String str2 = "2016-06-17 16:16:00";
        String str3 = "2016-06-18 16:16:00";
        list.add(str2);
        list.add(str3);
        list.add(str1);
        String br = "<br />";
        String br1 = "<br/>";
        String nb = "&nbsp;";
        String s = br+"asdfsadf"+nb+"sadf"+br+"65465";
        String s1 = s.replace(br,"\n");
        String s2 = s1.replace(nb,"  ");
        String s3 = s2.replace(br1,"\n");
        System.out.println(s);
        System.out.println(s2);
//        list.add(str3);
//        list.add(str2);
//        list.add(str1);
        Collections.sort(list, new SortComparator());
        System.out.println(list.toString());
    }

    /**
     * 把html代码转化为安卓代码
     * @param str
     * @return
     */
    String html2Android(String str){
        String result="";
        String br = "<br />";
        String br1 = "<br/>";
        String nb = "&nbsp;";

        String s1 = str.replace(br,"\n");
        String s2 = s1.replace(nb,"  ");
        result = s2.replace(br1,"\n");
        return result;
    }

}
