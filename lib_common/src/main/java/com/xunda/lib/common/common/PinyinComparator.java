package com.xunda.lib.common.common;


import com.xunda.lib.common.bean.FriendBean;
import com.xunda.lib.common.common.utils.StringUtil;

import java.util.Comparator;

/**
 *
 * @author
 *
 */
public class PinyinComparator implements Comparator<FriendBean> {


    public static PinyinComparator instance = null;

    public static PinyinComparator getInstance() {
        if (instance == null) {
            instance = new PinyinComparator();
        }
        return instance;
    }

    public int compare(FriendBean o1, FriendBean o2) {
        String letter1 = o1.getLetter();
        String letter2 = o2.getLetter();
        if(!StringUtil.isBlank(letter1) && !StringUtil.isBlank(letter2)){

        }

        if (letter1.equals("@")|| letter2.equals("#")) {
            return -1;
        } else if (letter1.equals("#")|| letter2.equals("@")) {
            return 1;
        } else {
            return letter1.compareTo(letter2);
        }
    }

}
