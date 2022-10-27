package com.evan.scanenhancer.ui;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.brother.sdk.common.IConnector;
import com.brother.sdk.common.device.MediaSize;
import com.brother.sdk.scan.ScanJob;
import com.brother.sdk.scan.ScanJobController;
import com.brother.sdk.scan.ScanParameters;
import com.evan.scanenhancer.Constants;
import com.evan.scanenhancer.R;
import com.evan.scanenhancer.adapters.RecentActionsAdapter;
import com.evan.scanenhancer.data.model.Result;
import com.evan.scanenhancer.util.GlideEngine;
import com.evan.scanenhancer.util.ScannerManager;
import com.evan.scanenhancer.viewmodels.MainViewModel;
import com.evan.scanenhancer.viewmodels.SharedViewModel;
import com.luck.picture.lib.basic.PictureSelector;
import com.luck.picture.lib.config.SelectMimeType;
import com.luck.picture.lib.entity.LocalMedia;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dagger.hilt.android.AndroidEntryPoint;
import permissions.dispatcher.NeedsPermission;
import timber.log.Timber;

@AndroidEntryPoint
public class MainActivity extends AppCompatActivity implements
        RecentActionsAdapter.RecentActionItemsCallback {

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    private MainViewModel mainViewModel;
    private RecentActionsAdapter adapter;
    private AlertDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        SharedViewModel sharedViewModel = new ViewModelProvider(this).get(SharedViewModel.class);
        mainViewModel = new ViewModelProvider(this).get(MainViewModel.class);

        adapter = new RecentActionsAdapter(mainViewModel.getRecentActions(), this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        sharedViewModel.getLiveData().observe(this, isRecentActionsUpdated -> {
            mainViewModel.getData();
        });

        mainViewModel.getLiveData().observe(this, isSuccessful -> {
            if (isSuccessful) {
                adapter.notifyDataSetChanged();
            }
        });
    }

    @OnClick(R.id.btn_scan)
    void onScan() {
        scanItem();
    }

    @OnClick(R.id.btn_select_photo)
    void onSelectPhoto() {
        selectPhoto();
    }

    @NeedsPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
    void selectPhoto() {
        PictureSelector.create(this)
                .openGallery(SelectMimeType.ofImage())
                .setImageEngine(GlideEngine.createGlideEngine())
                .setMaxSelectNum(1)
                .isDisplayCamera(false)
                .forResult(Constants.REQUEST_CODE_SELECT_PHOTO);
    }

    @Override
    public void onItemClicked(Result result) {
        ProcessedImageFragment imageFragment = new ProcessedImageFragment(result.getData().getUrl());
        imageFragment.show(getSupportFragmentManager(), Constants.PROCESSED_IMAGE_FRAGMENT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null) {
            return;
        }
        if (requestCode == Constants.REQUEST_CODE_SELECT_PHOTO) {
            ArrayList<LocalMedia> list = PictureSelector.obtainSelectorList(data);
            String path = list.get(0).getRealPath();

            FilterFragment filterFragment = new FilterFragment(path);
            filterFragment.show(getSupportFragmentManager(), Constants.FILTER_FRAGMENT);
        }
    }

    private void scanItem() {
        showProgressBar();
        new Thread(() -> {
            ScannerManager.connect(ScannerManager.CONNECTION.WIFI, getApplicationContext());

            while (!ScannerManager.isConnected()) {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("Still Connecting...");
            }
            System.out.println("Connected");
            System.out.println("Attempting scan...");

            executeImageScan(ScannerManager.createIConnector(getApplicationContext()));
        }).start();
    }

    public void executeImageScan(IConnector connector) {
        ScanJob scanJob = null;
        try {
            ScanParameters scanParameters = new ScanParameters();
            scanParameters.documentSize = MediaSize.Photo2L;
            scanParameters.autoDocumentSizeScan = true;
            scanJob = new ScanJob(scanParameters, this, new ScanJobController(this.getFilesDir()) {
                public void onUpdateProcessProgress(int value) {
                }

                public void onNotifyProcessAlive() {
                }

                public void onImageReadToFile(String scannedImagePath, int pageIndex) {
                    runOnUiThread(() -> hideProgressBar());
                    FilterFragment filterFragment = new FilterFragment(scannedImagePath);
                    filterFragment.show(getSupportFragmentManager(), Constants.FILTER_FRAGMENT);
                }
            });
            connector.submit(scanJob);
        } catch (Exception e) {
            Timber.e("Error scanning: " + e);
            scanJob.cancel();
        } finally {
            scanJob = null;
        }
    }

    private void hideProgressBar() {
        progressDialog.dismiss();
    }

    private void showProgressBar() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.alertDialogTheme);
        builder.setView(R.layout.progress_scanning_image);
        progressDialog = builder.create();
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
    }
}