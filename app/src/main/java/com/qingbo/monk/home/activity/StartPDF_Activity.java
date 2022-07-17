package com.qingbo.monk.home.activity;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.qingbo.monk.R;
import com.qingbo.monk.base.BaseActivity;
import com.wdeo3601.pdfview.PDFView;
import com.xunda.lib.common.common.titlebar.CustomTitleBar;

import butterknife.BindView;

/**
 * 打开PDF
 */
public class StartPDF_Activity extends BaseActivity {

    @BindView(R.id.title_bar)
    CustomTitleBar title_bar;
    @BindView(R.id.pdf_view)
    PDFView pdfView;
    private String titleName;
    private String pdfUrl;

    /**
     * @param context
     * @param title
     */
    public static void startActivity(Context context, String title, String pdfUrl) {
        Intent intent = new Intent(context, StartPDF_Activity.class);
        intent.putExtra("title", title);
        intent.putExtra("pdfUrl", pdfUrl);
        context.startActivity(intent);
    }


    @Override
    protected int getLayoutId() {
        return R.layout.activity_start_pdf;
    }

    @Override
    protected void initLocalData() {
        titleName =  getIntent().getStringExtra("title");
        pdfUrl =  getIntent().getStringExtra("pdfUrl");
    }

    @Override
    protected void initView() {
        if (!TextUtils.isEmpty(titleName)){

        title_bar.setTitle(titleName);
        }
        addPDF();
    }

    private void addPDF() {
        pdfView.setOffscreenPageLimit(2);// 设置当前显示页的前后缓存个数，效果类似 ViewPager 的这个属性
        pdfView.isCanZoom(true);// 是否支持缩放
        pdfView.setMaxScale(10f); // 设置最大缩放倍数,最大支持20倍
//        pdfView.setWatermark(R.mipmap.app_logo);// 添加水印
        // 设置当前页变化的回调监听
        pdfView.setOnPageChangedListener(new PDFView.OnPageChangedListener() {
            @Override
            public void onPageChanged(int currentPageIndex, int totalPageCount) {
                // show current page number
            }
        });

//        pdfView.showPdfFromPath(filePath)// 从本地文件打开 pdf

        if (!TextUtils.isEmpty(pdfUrl)){
        pdfView.showPdfFromUrl(pdfUrl);// 从网络打开 pdf
        }

    }
}