package cc.chenghong.vkagetorder.test;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;

/**
 * Created by 何成龙 on 2016/7/21.
 */
public class Sign {
    public static void main(String[] args) {
//        String str = 用户ID：626
//        API 密钥：8c61ff8e4d1b6ed9930f6cb21029f67df630f92a
//        终端号：521
//        密钥：111111;
        String str1 = "8c61ff8e4d1b6ed9930f6cb21029f67df630f92amachine_code521partner626time1469517600111111";
        System.out.println(new Md5FileNameGenerator().generate(str1));
    }
}
