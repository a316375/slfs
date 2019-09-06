package com.xyx.slfs;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class ReadActivity extends AppCompatActivity {
    int p = 0;
    private PDFView pdfView;
    private Toolbar toolbar;
    private InterstitialAd mInterstitialAd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        MobileAds.initialize(this,
                "ca-app-pub-7420611722821229~4555695674");
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-7420611722821229/8957975111");
        mInterstitialAd.loadAd(new AdRequest.Builder().build());

        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                // Load the next interstitial.
                mInterstitialAd.loadAd(new AdRequest.Builder().build());
            }

        });

        pdfView = findViewById(R.id.read);
        loadPDF(pdfView, 0);





        Button save = findViewById(R.id.bt_save);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                save();
                showAD();//广告
            }
        });

        Button button = findViewById(R.id.bt_AD);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sharedPreferences = getSharedPreferences("data", Context.MODE_PRIVATE);
                int userpage = sharedPreferences.getInt("page", 0);
                loadPDF(pdfView, userpage);
                showAD();//广告
            }
        });




    }



    private void showAD() {
        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        } else {
            Log.d("TAG", "The interstitial wasn't loaded yet.");
        }
    }

    private void loadPDF(PDFView pdfView, int page) {

        pdfView.fromAsset("slfs.pdf")   //设置pdf文件地址
//                .pages(0, 2, 1, 3, 3, 3)//只加载某页面
                .enableSwipe(true) // allows to block changing pages using swipe
                .swipeHorizontal(false)
                .enableDoubletap(true)
                .defaultPage(page)
                // allows to draw something on the current page, usually visible in the middle of the screen
                .onDraw(null)
                // allows to draw something on all pages, separately for every page. Called only for visible pages
                .onDrawAll(null)
                .onLoad(null) // called after document is loaded and starts to be rendered
                .onPageChange(new OnPageChangeListener() {
                    @Override
                    public void onPageChanged(final int page, final int pageCount) {
                        toolbar.setSubtitle(String.format(" %s /%s", page + 1, pageCount));
                        p = page;
                        Log.v("-->", "" + page + "/" + pageCount);
                    }
                })
                .onPageScroll(null)
                .onError(null)
                .onPageError(null)
                .onRender(null) // called after document is rendered for the first time
                // called on single tap, return true if handled, false to toggle scroll handle visibility
                .onTap(null)
//                .onLongPress(onLongPressListener)
                .enableAnnotationRendering(false) // render annotations (such as comments, colors or forms)
                .password(null)
                .scrollHandle(null)
                .enableAntialiasing(true) // improve rendering a little bit on low-res screens
                // spacing between pages in dp. To define spacing color, set view background
                .spacing(0)
//                .autoSpacing(false) // add dynamic spacing to fit each page on its own on the screen
//                .linkHandler(DefaultLinkHandler)
//                .pageFitPolicy(FitPolicy.WIDTH) // mode to fit pages in the view
//                .fitEachPage(false) // fit each page to the view, else smaller pages are scaled relative to largest page.
//                .pageSnap(false) // snap pages to screen boundaries
//                .pageFling(false) // make a fling change only a single page like ViewPager
//                .nightMode(false) // toggle night mode
                .load();
    }



    private void save() {
        //步骤1：创建一个SharedPreferences对象
        SharedPreferences sharedPreferences = getSharedPreferences("data", Context.MODE_PRIVATE);
        //步骤2： 实例化SharedPreferences.Editor对象
        SharedPreferences.Editor editor = sharedPreferences.edit();
        //步骤3：将获取过来的值放入文件
        // editor.putString("name", "Tom");
        editor.putInt("page", p);
        //editor.putBoolean("marrid",false);
        //步骤4：提交
        editor.commit();
    }



}
