package com.qingbo.monk.question.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.text.Editable;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.text.style.URLSpan;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.qingbo.monk.HttpSender;
import com.qingbo.monk.R;
import com.qingbo.monk.base.BaseCameraAndGalleryActivity_More;
import com.qingbo.monk.base.rich.bean.MyFontStyle;
import com.qingbo.monk.base.rich.handle.CustomHtml;
import com.qingbo.monk.base.rich.view.FontStylePanel;
import com.qingbo.monk.base.rich.view.RichEditText;
import com.qingbo.monk.bean.UploadPictureBean;
import com.qingbo.monk.dialog.LinkDialog;
import com.qingbo.monk.question.adapter.ChooseImageAdapter;
import com.xunda.lib.common.common.Constants;
import com.xunda.lib.common.common.fileprovider.FileProvider7;
import com.xunda.lib.common.common.http.HttpUrl;
import com.xunda.lib.common.common.http.MyOnHttpResListener;
import com.xunda.lib.common.common.itemdecoration.GridDividerItemDecoration;
import com.xunda.lib.common.common.utils.DisplayUtil;
import com.xunda.lib.common.common.utils.StringUtil;
import com.xunda.lib.common.common.utils.T;
import com.xunda.lib.common.dialog.TwoButtonDialogBlue;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 向别人提问
 */
public class PublisherAskQuestionToPeopleActivity extends BaseCameraAndGalleryActivity_More implements FontStylePanel.OnFontPanelListener, RichEditText.OnSelectChangeListener{
    @BindView(R.id.et_content)
    RichEditText et_content;
    @BindView(R.id.tv_to_name)
    TextView tv_to_name;
    @BindView(R.id.tv_remains_text)
    TextView tv_remains_text;
    @BindView(R.id.tv_remains_image)
    TextView tv_remains_image;
    @BindView(R.id.recycleView_image)
    RecyclerView recycleView_image;

    @BindView(R.id.tv_tag)
    TextView tvTag;
    @BindView(R.id.ll_tag)
    LinearLayout llTag;

    @BindView(R.id.fontStylePanel)
    FontStylePanel fontStylePanel;
    private List<UploadPictureBean> imageList = new ArrayList<>();
    private List<String> imageStringList = new ArrayList<>();
    private ChooseImageAdapter mAdapter;
    private TwoButtonDialogBlue mDialog;
    private String mContent, images;
    private String to_name, to_id,shequn_id;


    public static void actionStart(Context context, String to_name, String to_id, String shequn_id) {
        Intent intent = new Intent(context, PublisherAskQuestionToPeopleActivity.class);
        intent.putExtra("to_name", to_name);
        intent.putExtra("to_id", to_id);
        intent.putExtra("shequn_id", shequn_id);
        context.startActivity(intent);
    }


    @Override
    protected int getLayoutId() {
        return R.layout.activity_ask_question_to_people;
    }

    @Override
    protected void initLocalData() {
        initFirstAddData();
        initImageRecyclerViewAndAdapter();
        to_name = getIntent().getStringExtra("to_name");
        to_id = getIntent().getStringExtra("to_id");
        shequn_id = getIntent().getStringExtra("shequn_id");
        tv_to_name.setText(StringUtil.getStringValue(to_name));
    }


    /**
     * 添加添加图片这个对象
     */
    private void initFirstAddData() {
        UploadPictureBean bean = new UploadPictureBean();
        bean.setType(1);
        imageList.add(bean);
    }


    private void initImageRecyclerViewAndAdapter() {
        GridLayoutManager layoutManager = new GridLayoutManager(mContext, 3);
        GridDividerItemDecoration mItemDecoration = new GridDividerItemDecoration(false, DisplayUtil.sp2px(mContext, 32), getResources().getColor(R.color.white));
        recycleView_image.addItemDecoration(mItemDecoration);
        recycleView_image.setLayoutManager(layoutManager);
        mAdapter = new ChooseImageAdapter(imageList);
        recycleView_image.setAdapter(mAdapter);
    }


    @Override
    protected void initEvent() {
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                clickPhoto(position);
            }
        });

        mAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                deleteImageNew(position);
            }
        });

        et_content.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                tv_remains_text.setText(String.format("%s/120",StringUtil.getEditText(et_content).length()));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        fontStylePanel.setOnFontPanelListener(this);
        et_content.setOnSelectChangeListener(this);

    }

    @OnClick(R.id.ll_tag)
    public void onClick() {
        String tag = (String) llTag.getTag();
        if ("0".equals(tag)) {
            tvTag.setText("匿名");
            setDrawableLeft(R.mipmap.niming);
            llTag.setTag("1");
        } else {
            tvTag.setText("公开");
            setDrawableLeft(R.mipmap.gongkai);
            llTag.setTag("0");
        }
    }

    private void setDrawableLeft(int mipmap) {
        Drawable drawableLeft = getResources().getDrawable(mipmap);
        tvTag.setCompoundDrawablesWithIntrinsicBounds(drawableLeft,
                null, null, null);
    }




    private void showBackDialog() {
        getPramsValue();

        if (!StringUtil.isBlank(mContent) || !StringUtil.isBlank(images)) {
            if (mDialog == null) {
                mDialog = new TwoButtonDialogBlue(this, "返回将丢失填写的内容，确定返回吗", "取消", "确定",
                        new TwoButtonDialogBlue.ConfirmListener() {

                            @Override
                            public void onClickRight() {
                                finish();
                            }

                            @Override
                            public void onClickLeft() {

                            }
                        });
            }

            if (!mDialog.isShowing()) {
                mDialog.show();
            }
        } else {
            finish();
        }

    }


    private void getPramsValue() {
        mContent = StringUtil.getEditText(et_content);
        images = StringUtil.listToString(imageStringList);
    }

    /**
     * 创建提问
     */
    private void createAskQuestion() {
        HashMap<String, String> baseMap = new HashMap<>();
        baseMap.put("type", "2");
        baseMap.put("id", to_id);
        baseMap.put("shequn_id", shequn_id);
        baseMap.put("content", mContent);
        baseMap.put("images", images);
        baseMap.put("action", "1");//1是社群
        baseMap.put("optype", "0");//默认是0,0是发布,1是保存
        baseMap.put("is_anonymous", (String) llTag.getTag());//默认是0,0是发布,1是保存
        String s = CustomHtml.toHtml(et_content.getEditableText());
        baseMap.put("html_content", s);

        HttpSender sender = new HttpSender(HttpUrl.createTopic, "创建提问", baseMap,
                new MyOnHttpResListener() {

                    @Override
                    public void onComplete(String json, int status, String description, String data) {
                        if (status == Constants.REQUEST_SUCCESS_CODE) {
                            showToastDialog("提交成功！");
                        }
                    }
                }, true);
        sender.setContext(mActivity);
        sender.sendPost();
    }




    /**
     * 点击图片
     */
    private void clickPhoto(int position) {
        int all_size = imageList.size();
        if (all_size < 3) {
            if (position == all_size - 1 && imageList.get(position).getType() == 1) {//添加照片
                checkGalleryPermission(1);
            } else {
                jumpToPhotoShowActivity(position, imageStringList);
            }
        }
    }


    /**
     * 展示选择的图片
     */
    private void showImageListImages(List<UploadPictureBean> mTempList) {
        imageList.addAll(imageList.size()-1, mTempList);
        deleteLastOne();
        tv_remains_image.setText(String.format("%s/1",imageStringList.size()));
        mAdapter.notifyDataSetChanged();
    }


    private void deleteLastOne() {
        if (imageList.size() > 1) {
            imageList.remove(imageList.size() - 1);
        }
    }


    private void deleteImageNew(int position) {
        imageStringList.remove(position);
        imageList.remove(position);
        UploadPictureBean addBean = new UploadPictureBean();
        addBean.setType(1);
        imageList.add(addBean);
        tv_remains_image.setText(String.format("%s/1",imageStringList.size()));
        mAdapter.notifyDataSetChanged();
    }


    @Override
    protected void onUploadSuccess(List<String> urlList,List<File> fileList) {
        List<UploadPictureBean> uriList = new ArrayList<>();
        for (File mFile:fileList) {
            Uri filePath = FileProvider7.getUriForFile(mContext,mFile);
            UploadPictureBean obj = new UploadPictureBean();
            obj.setImageUri(filePath);
            obj.setType(2);
            uriList.add(obj);
        }
        imageStringList.addAll(urlList);
        showImageListImages(uriList);
    }

    @Override
    protected void onUploadFailure(String error_info) {

    }

    @Override
    public void onBackPressed() {
        showBackDialog();
    }


    @OnClick({R.id.rl_back, R.id.rl_publish})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_back:
                showBackDialog();
                break;
            case R.id.rl_publish:
                getPramsValue();

                if (StringUtil.isBlank(mContent) && StringUtil.isBlank(images)) {
                    T.ss("内容、图片必须填写一项");
                    return;
                }

                createAskQuestion();
                break;
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    public void setBold(boolean isBold) {
        et_content.setBold(isBold);
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    public void setItalic(boolean isItalic) {
        et_content.setItalic(isItalic);
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    public void setUnderline(boolean isUnderline) {

//
//        et_content.setUnderline(isUnderline);
        linkDialog();

    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    public void setStreak(boolean isStreak) {
//        et_content.setStreak(isStreak);
        int end = et_content.getSelectionEnd();
        Editable editableText = et_content.getEditableText();
        editableText.insert(end, "\n");
    }

    @Override
    public void insertImg() {

    }

    @Override
    public void setFontSize(int size) {
        et_content.setFontSize(14);
    }

    @Override
    public void setFontColor(int color) {

    }

    /**
     * 样式改变
     *
     * @param fontStyle
     */
    @Override
    public void onFontStyleChang(MyFontStyle fontStyle) {
        fontStylePanel.initFontStyle(fontStyle);
    }

    @Override
    public void onSelect(int start, int end) {

    }

    private void linkDialog() {
        new LinkDialog(mActivity, "", "取消", "确定", new LinkDialog.ConfirmListener() {
            @Override
            public void onClickRight(String name, String Url) {
                setLink(name,Url);
            }

            @Override
            public void onClickLeft() {
            }
        }).show();
    }

    private void setLink(String name ,String url){
        Editable edit = et_content.getEditableText();//获取EditText的文字
        int start = et_content.getSelectionStart();
        String text = name;
        int end = et_content.getSelectionEnd() + text.length();
        if (start < 0 || start >= edit.length()) {
            edit.append(text);
        } else {
            edit.insert(start, text);//光标所在位置插入文字
        }
        et_content.getEditableText().setSpan(new URLSpan(url), start, end,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        et_content.setMovementMethod(LinkMovementMethod.getInstance());//可点击
    }


}
