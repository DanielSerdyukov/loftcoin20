package com.lofschool.converter;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.lofschool.converter.databinding.DialogCurrencyBinding;
import com.lofschool.loftcoin.BaseComponent;
import com.lofschool.loftcoin.widget.RecyclerViews;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;

public class CoinsSheet extends BottomSheetDialogFragment {

    static final String KEY_MODE = "mode";

    static final int MODE_FROM = 1;

    static final int MODE_TO = 2;

    private final CompositeDisposable disposable = new CompositeDisposable();

    private final ConverterComponent component;

    private DialogCurrencyBinding binding;

    private ConverterViewModel viewModel;

    private CoinsSheetAdapter adapter;

    private int mode;

    @Inject
    CoinsSheet(BaseComponent baseComponent) {
        component = DaggerConverterComponent.builder()
            .baseComponent(baseComponent)
            .build();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(requireParentFragment(), component.viewModelFactory())
            .get(ConverterViewModel.class);
        adapter = component.coinsSheetAdapter();
        mode = requireArguments().getInt(KEY_MODE, MODE_FROM);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_currency, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding = DialogCurrencyBinding.bind(view);

        binding.recycler.setLayoutManager(new LinearLayoutManager(view.getContext()));
        binding.recycler.setAdapter(adapter);

        disposable.add(viewModel.topCoins().subscribe(adapter::submitList));
        disposable.add(RecyclerViews.onClick(binding.recycler)
            .map((position) -> adapter.getItem(position))
            .subscribe(coin -> {
                if (MODE_FROM == mode) {
                    viewModel.fromCoin(coin);
                } else {
                    viewModel.toCoin(coin);
                }
                dismissAllowingStateLoss();
            }));
    }

    @Override
    public void onDestroyView() {
        binding.recycler.setAdapter(null);
        disposable.dispose();
        super.onDestroyView();
    }
}
