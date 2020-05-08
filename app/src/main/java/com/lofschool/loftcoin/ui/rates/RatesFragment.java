package com.lofschool.loftcoin.ui.rates;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.lofschool.loftcoin.BaseComponent;
import com.lofschool.loftcoin.R;
import com.lofschool.loftcoin.databinding.FragmentRatesBinding;
import com.lofschool.loftcoin.util.PriceFormatter;

import javax.inject.Inject;

public class RatesFragment extends Fragment {

    private final RatesComponent component;

    private FragmentRatesBinding binding;

    private RatesAdapter adapter;

    private RatesViewModel viewModel;

    @Inject
    public RatesFragment(BaseComponent baseComponent) {
        component = DaggerRatesComponent.builder()
            .baseComponent(baseComponent)
            .build();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(this, component.viewModelFactory())
            .get(RatesViewModel.class);
        adapter = new RatesAdapter(new PriceFormatter());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_rates, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setHasOptionsMenu(true);
        binding = FragmentRatesBinding.bind(view);
        binding.recycler.setLayoutManager(new LinearLayoutManager(view.getContext()));
        binding.recycler.swapAdapter(adapter, false);
        binding.recycler.setHasFixedSize(true);
        viewModel.coins().observe(getViewLifecycleOwner(), adapter::submitList);
        viewModel.isRefreshing().observe(getViewLifecycleOwner(), binding.refresher::setRefreshing);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.rates, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (R.id.currency_dialog == item.getItemId()) {
            NavHostFragment
                .findNavController(this)
                .navigate(R.id.currency_dialog);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDestroyView() {
        binding.recycler.swapAdapter(null, false);
        super.onDestroyView();
    }

}
