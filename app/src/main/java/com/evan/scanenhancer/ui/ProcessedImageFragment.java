package com.evan.scanenhancer.ui;

import android.app.Dialog;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.evan.scanenhancer.Constants;
import com.evan.scanenhancer.R;
import com.evan.scanenhancer.util.ClipboardUtil;
import com.evan.scanenhancer.util.NotificationUtil;
import com.evan.scanenhancer.util.SnackbarManager;
import com.google.android.material.snackbar.Snackbar;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import timber.log.Timber;

public class ProcessedImageFragment extends DialogFragment {

    @BindView(R.id.imageview)
    ImageView imageView;

    @BindView(R.id.parent_layout)
    ViewGroup parentLayout;

    private Context context;
    private final String imageUrl;

    public ProcessedImageFragment(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        setStyle(DialogFragment.STYLE_NORMAL, R.style.Theme_ScanEnhancer);
        final Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.getWindow().getAttributes().windowAnimations = R.style.FragmentDialogAnim;
        return dialog;
    }

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState
    ) {
        View view = inflater.inflate(R.layout.fragment_processed_image, container, false);
        ButterKnife.bind(this, view);

        Glide.with(this)
                .load(imageUrl)
                .apply(RequestOptions.centerInsideTransform())
                .into(imageView);


        return view;
    }

    @OnClick(R.id.btn_close)
    void onClose(){
        this.dismiss();
    }

    @OnClick(R.id.btn_download)
    void onDownload(){

    }

    @OnClick(R.id.btn_link)
    void onLinkBtnClick() {
        ClipboardUtil.copyText(context, imageUrl);
        SnackbarManager.createIndefinite(
                context, parentLayout, "Copied ".concat(imageUrl)
        ).show();
    }

    public Snackbar initViewDownloadsSnackbar(Context context, ViewGroup parentLayout, String msg) {
        return Snackbar.make(parentLayout, msg, Snackbar.LENGTH_LONG)
                .setAction("View", v -> {
                    Intent intent = new Intent(DownloadManager.ACTION_VIEW_DOWNLOADS);
                    startActivity(intent);
                })
                .setActionTextColor(
                        ContextCompat.getColor(context, R.color.gradientStartColor200)
                );
    }
}
