package com.qingbo.monk.base.baseview;

import android.content.Context;
import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.widget.AppCompatEditText;

import com.qingbo.monk.R;
import com.xunda.lib.common.common.utils.ListUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

/**
 * 内容中添加@消息  固定删除一整个名字
 */
public class AtEditText extends AppCompatEditText {
    ArrayList<Entity> atList;
//    ArrayList<ChatEmoji> emojiList;
    /**
     * key:表情名称
     * value:表情id
     */
    HashMap<String, Integer> emojiMap;
    private Context context;
    private OnAtInputListener mOnAtInputListener;

    public AtEditText(Context context) {
        super(context);
        initView(context);
    }

    public AtEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public AtEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        this.context = context;
        atList = new ArrayList<>();
//        emojiList = new ArrayList<>();
        emojiMap = new HashMap<>();
        setOnKeyListener(new MyOnKeyListener(this));
        addTextChangedListener(new MentionTextWatcher());
    }


    /**
     * 修改光标位置
     */
//    @Override
//    protected void onSelectionChanged(int selStart, int selEnd) {
//        super.onSelectionChanged(selStart, selEnd);
//        if (ListUtils.isEmpty(atList)){
//            setSelection();
//        }
//    }


    /**
     * 添加@的内容
     *
     * @param parm 最多传4个 ,分别对应 id,name,parm1,parm2
     */
    public void addAtContent(String... parm) {
        atList.add(new Entity(parm[0], parm[1], parm[2]));
        // 光标位置之前的字符是否是 @ , 如果不是 加上一个@
        int selectionStart = getSelectionStart();
//        int selectionStart = 0;
        // 获取当前内容
        String sss = Objects.requireNonNull(getText()).toString();
        // 获取光标前以为字符
        String s = selectionStart != 0 ? sss.toCharArray()[selectionStart - 1] + "" : "";
        // 将内容插入 , 改变文字颜色
        setText(changeTextColor(sss.substring(0, selectionStart) + (!s.equals("@") ? "@" : "") + parm[1] + sss.substring(selectionStart, sss.length()))); //字符串替换，删掉符合条件的字符串
        // 设置光标位置
        setSelection((sss.substring(0, selectionStart) + (!s.equals("@") ? "@" : "") + parm[1]).length());
    }

    /**
     * 添加聊天表情
     *
     * @param
     */
//    public void addChatEmoji(ChatEmoji emoji) {
//        emojiList.add(emoji);
//        emojiMap.put(emoji.getCharacter(), emoji.getId());
//        // 获取光标当前位置
//        int selectionStart = getSelectionStart();
//        // 获取当前内容
//        String sss = getText().toString();
//        // 将内容插入 , 改变文字颜色
//        setText(changeTextColor(sss.substring(0, selectionStart) + emoji.getCharacter() + sss.substring(selectionStart, sss.length()))); //字符串替换，删掉符合条件的字符串
//        // 设置光标位置
//        setSelection((sss.substring(0, selectionStart) + emoji.getCharacter()).length());
//    }
    private SpannableString changeTextColor(String sText) {
        int startIndex = 0;
        List<Integer> spanIndexes = getSpanIndexes(sText);

        SpannableString spanText = new SpannableString(sText);

        if (spanIndexes != null && spanIndexes.size() != 0) {
            for (int i = 0; i < spanIndexes.size(); i++) {
                if (i % 2 == 0) {  // 开始位置
                    startIndex = spanIndexes.get(i);
                } else {  // 结束位置
                    spanText.setSpan(new ForegroundColorSpan(context.getResources().getColor(R.color.text_color_1F8FE5)), startIndex, spanIndexes.get(i), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
            }
        }

        return spanText;
//        return changeChatEmoji(sText, spanText);
    }

    /**
     * 添加表情
     *
     * @param sText 文本
     */
//    private SpannableString changeChatEmoji(String sText,SpannableString spanText) {
//        int startIndex = 0;
//        List<Integer> spanIndexes = getEmojiIndexes(sText);
//
//        if (spanIndexes != null && spanIndexes.size() != 0) {
//            for (int i = 0; i < spanIndexes.size(); i++) {
//                if (i % 2 == 0) {  // 开始位置
//                    startIndex = spanIndexes.get(i);
//                } else {  // 结束位置
//                    Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), emojiMap.get(sText.substring(startIndex, spanIndexes.get(i))));
//                    bitmap = Bitmap.createScaledBitmap(bitmap, 50, 50, true);
//                    ImageSpan imageSpan = new ImageSpan(context, bitmap);
//                    spanText.setSpan(imageSpan, startIndex,  spanIndexes.get(i), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//                }
//            }
//        }
//        return spanText;
//    }
//
//    public List<Integer> getEmojiIndexes(String sText) {
//        int endIndex = 0;
//        int startIndex = 0;
//        List<Integer> spanIndexes = new ArrayList<Integer>();
//        for (int i = 0; i < emojiList.size(); i++) {
//            String tempname = emojiList.get(i).getCharacter();
//            if ((startIndex = sText.indexOf(tempname, endIndex)) != -1) {
//                endIndex = startIndex + tempname.length();
//                spanIndexes.add(startIndex);//name 的开始索引，键值为偶数，从0开始
//                spanIndexes.add(startIndex + tempname.length()); //name 的结束索引，键值为奇数，从1开始
//            }
//        }
//        return spanIndexes;
//    }
    public List<Integer> getSpanIndexes(String sText) {
        int endIndex = 0;
        int startIndex = 0;
        List<Integer> spanIndexes = new ArrayList<Integer>();
        for (int i = 0; i < atList.size(); i++) {
            String tempname = "@" + atList.get(i).getName();
            if ((startIndex = sText.indexOf(tempname, endIndex)) != -1) {
                endIndex = startIndex + tempname.length();
                spanIndexes.add(startIndex);//name 的开始索引，键值为偶数，从0开始
                spanIndexes.add(startIndex + tempname.length()); //name 的结束索引，键值为奇数，从1开始
            }
        }
        return spanIndexes;
    }


    private class MyOnKeyListener implements OnKeyListener {
        private EditText editText;

        public MyOnKeyListener(EditText editText) {
            this.editText = editText;
        }

        @Override
        public boolean onKey(View view, int keyCode, KeyEvent event) {
            if (keyCode == KeyEvent.KEYCODE_DEL && event.getAction() == KeyEvent.ACTION_DOWN) { //当为删除键并且是按下动作时执行
                return myDelete(1) || myDelete(2);
            }
            return false;
        }

        /**
         * 删除方法
         *
         * @param type [1] @ [2] 表情
         * @return
         */
        private boolean myDelete(int type) {
            String content = editText.getText().toString();
            int selectionStart = editText.getSelectionStart();
            int endIndex = 0;
            int startIndex;
            int deleteNum = 0;
            List<Integer> spanIndexes = new ArrayList<Integer>();
//            int size = (type == 1) ? atList.size() : emojiList.size();
            int size = atList.size();
            for (int i = 0; i < size; i++) {
//                String name = type == 1 ? "@" + atList.get(i).getName() : emojiList.get(i).getCharacter();
                String name = "@" + atList.get(i).getName();
                if ((startIndex = content.indexOf(name, endIndex)) != -1) {
                    if (startIndex > selectionStart) break; // 如果开始索引值,大于光标位置,那么退出遍历
                    endIndex = startIndex + name.length();
                    deleteNum = i;
                    spanIndexes.add(startIndex);//name 的开始索引，键值为偶数，从0开始
                    spanIndexes.add(startIndex + name.length()); //name 的结束索引，键值为奇数，从1开始
                    if (endIndex > selectionStart) break; // 如果结束索引值,大于光标位置,那么退出遍历
                }
            }
            // spanIndexes 必须大于0 且 光标位置不能大于 结束索引位置
            if (spanIndexes.size() > 0 && spanIndexes.get(spanIndexes.size() - 2) < selectionStart && spanIndexes.get(spanIndexes.size() - 1) >= selectionStart) {
                editText.setText(changeTextColor(content.substring(0, spanIndexes.get(spanIndexes.size() - 2)) + content.substring(spanIndexes.get(spanIndexes.size() - 1)))); //字符串替换，删掉符合条件的字符串
                editText.setSelection(spanIndexes.get(spanIndexes.size() - 2));  // 设置光标位置
                if (type == 1)
                    atList.remove(deleteNum);
//                else
//                    emojiList.remove(deleteNum); //删除对应实体
                return true;
            } else {
                return false;
            }
        }

    }


    private class MentionTextWatcher implements TextWatcher {
        //若从整串string中间插入字符，需要将插入位置后面的range相应地挪位
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int index, int i1, int count) {
            if (count == 1 && !TextUtils.isEmpty(charSequence)) {
                char mentionChar = charSequence.toString().charAt(index);
                if ('@' == mentionChar && mOnAtInputListener != null) {
                    mOnAtInputListener.OnAtCharacterInput();
                }
            }
        }

        @Override
        public void afterTextChanged(Editable editable) {
        }
    }

    /**
     * 刷新@列表
     */
    public void refreshAtList(String... parm) {
        atList.add(new Entity(parm[0], parm[1], parm[2]));
        setText(changeTextColor(getText().toString()));
    }

    public ArrayList<Entity> getAtList() {
        return atList;
    }

    public void setOnAtInputListener(OnAtInputListener OnAtInputListener) {
        mOnAtInputListener = OnAtInputListener;
    }

    public interface OnAtInputListener {
        void OnAtCharacterInput();
    }


    /**
     * @*** 格式的实体类
     */
    public class Entity {
        String id;
        String name;
        String parm1;
        String parm2;

        /**
         * @param parm 最多传4个 ,分别对应 id,name,parm1,parm2
         */
        public Entity(String... parm) {
            if (parm.length >= 1)
                this.id = parm[0];
            if (parm.length >= 2)
                this.name = parm[1];
            if (parm.length >= 3)
                this.parm1 = parm[2];
            if (parm.length >= 4)
                this.parm2 = parm[3];
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getParm1() {
            return parm1;
        }

        public void setParm1(String parm1) {
            this.parm1 = parm1;
        }

        public String getParm2() {
            return parm2;
        }

        public void setParm2(String parm2) {
            this.parm2 = parm2;
        }
    }
}
