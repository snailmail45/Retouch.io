package com.evan.scanenhancer.ui;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.evan.scanenhancer.Constants;
import com.evan.scanenhancer.R;
import com.evan.scanenhancer.adapters.FilterAdapter;
import com.evan.scanenhancer.data.model.FilterItem;
import com.evan.scanenhancer.di.FilterViewModelFactory;
import com.evan.scanenhancer.util.Status;
import com.evan.scanenhancer.viewmodels.FilterViewModel;
import com.evan.scanenhancer.viewmodels.SharedViewModel;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class FilterFragment extends DialogFragment implements FilterAdapter.FilterAdapterCallback {

    @BindView(R.id.imageview)
    ImageView imageView;

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    @Inject
    FilterViewModelFactory viewModelFactory;

    private Context context;
    private final String imagePath;
    private FilterAdapter adapter;
    private FilterViewModel viewModel;
    private SharedViewModel sharedViewModel;
    private AlertDialog progressDialog;

    public FilterFragment(String imagePath) {
        this.imagePath = imagePath;
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
        View view = inflater.inflate(R.layout.fragment_filter, container, false);
        ButterKnife.bind(this, view);

        Glide.with(this)
                .load(imagePath)
                .apply(RequestOptions.centerInsideTransform())
                .into(imageView);

        viewModel = new ViewModelProvider(
                this,
                FilterViewModel.provideFactory(viewModelFactory, imagePath)
        ).get(FilterViewModel.class);

        sharedViewModel = new ViewModelProvider((ViewModelStoreOwner) context).get(SharedViewModel.class);

        viewModel.getLiveData().observe(this, serviceResult -> {
            switch (serviceResult.getStatus()) {
                case IN_PROGRESS:
                    showProgressDialog();
                    break;

                case SUCCESS:
                    hideProgressDialog();
                    sharedViewModel.updateRecycler();
                    break;

                case FAILED:
                    hideProgressDialog();
                    break;
            }
        });


        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        adapter = new FilterAdapter(viewModel.getList(), this);
        recyclerView.setLayoutManager(new LinearLayoutManager(
                requireContext(), RecyclerView.HORIZONTAL, false
        ));
        recyclerView.setItemAnimator(null);
        recyclerView.setAdapter(adapter);

        viewModel.getLiveData().observe(this, result -> {
            if (result.getStatus() == Status.SUCCESS) {
                ProcessedImageFragment processedImageFragment = new ProcessedImageFragment(result.getResult().getData().getUrl());
                processedImageFragment.show(getParentFragmentManager(), Constants.PROCESSED_IMAGE_FRAGMENT);
                this.dismiss();
            }
        });

    }


    @OnClick(R.id.btn_close)
    void onCloseBtnClick() {
        this.dismiss();
    }

    @Override
    public void onItemClick(FilterItem filterItem) {
        viewModel.onItemClick(filterItem);
    }

    private void hideProgressDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }

    private void showProgressDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.alertDialogTheme);
        builder.setView(R.layout.progress_dialog_uploading_image);
        progressDialog = builder.create();
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
    }
}
