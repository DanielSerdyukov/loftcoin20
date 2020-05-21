package com.lofschool.loftcoin.ui.wallets;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import com.lofschool.loftcoin.BaseComponent;
import com.lofschool.loftcoin.R;
import com.lofschool.loftcoin.databinding.FragmentWalletsBinding;
import com.lofschool.loftcoin.widget.RecyclerViews;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;

public class WalletsFragment extends Fragment {

    private final CompositeDisposable disposable = new CompositeDisposable();

    private final WalletsComponent component;

    private FragmentWalletsBinding binding;

    private WalletsViewModel viewModel;

    private SnapHelper walletsSnapHelper;

    private WalletsAdapter walletsAdapter;

    private TransactionsAdapter transactionsAdapter;

    @Inject
    public WalletsFragment(BaseComponent baseComponent) {
        component = DaggerWalletsComponent.builder()
            .baseComponent(baseComponent)
            .build();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(this, component.viewModelFactory())
            .get(WalletsViewModel.class);
        walletsAdapter = component.walletsAdapter();
        transactionsAdapter = component.transactionsAdapter();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_wallets, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setHasOptionsMenu(true);

        binding = FragmentWalletsBinding.bind(view);
        walletsSnapHelper = new PagerSnapHelper();
        walletsSnapHelper.attachToRecyclerView(binding.recycler);

        final TypedValue value = new TypedValue();
        view.getContext().getTheme().resolveAttribute(R.attr.walletCardWidth, value, true);
        final DisplayMetrics displayMetrics = view.getContext().getResources().getDisplayMetrics();
        final int padding = (int) (displayMetrics.widthPixels - value.getDimension(displayMetrics)) / 2;
        binding.recycler.setPadding(padding, 0, padding, 0);
        binding.recycler.setClipToPadding(false);
        binding.recycler.setHasFixedSize(true);

        binding.recycler.setLayoutManager(new LinearLayoutManager(view.getContext(), RecyclerView.HORIZONTAL, false));
        binding.recycler.addOnScrollListener(new CarouselScroller());
        disposable.add(RecyclerViews
            .onSnap(binding.recycler, walletsSnapHelper)
            .subscribe(viewModel::changeWallet));

        binding.recycler.setAdapter(walletsAdapter);

        disposable.add(viewModel.wallets().subscribe(walletsAdapter::submitList));
        disposable.add(viewModel.wallets().map(List::isEmpty).subscribe((isEmpty) -> {
            binding.walletCard.setVisibility(isEmpty ? View.VISIBLE : View.GONE);
            binding.recycler.setVisibility(isEmpty ? View.GONE : View.VISIBLE);
        }));

        binding.transactions.setLayoutManager(new LinearLayoutManager(view.getContext()));
        binding.transactions.setAdapter(transactionsAdapter);
        binding.transactions.setHasFixedSize(true);

        disposable.add(viewModel.transactions().subscribe(transactionsAdapter::submitList));
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.wallets, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (R.id.add == item.getItemId()) {
            disposable.add(viewModel.addWallet().subscribe());
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDestroyView() {
        walletsSnapHelper.attachToRecyclerView(null);
        binding.recycler.setAdapter(null);
        binding.transactions.setAdapter(null);
        disposable.clear();
        super.onDestroyView();
    }

    private static class CarouselScroller extends RecyclerView.OnScrollListener {
        @Override
        public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
            final int centerX = (recyclerView.getLeft() + recyclerView.getRight()) / 2;
            for (int i = 0; i < recyclerView.getChildCount(); ++i) {
                final View child = recyclerView.getChildAt(i);
                final int childCenterX = (child.getLeft() + child.getRight()) / 2;
                final float childOffset = Math.abs(centerX - childCenterX) / (float) centerX;
                float factor = (float) (Math.pow(0.85, childOffset));
                child.setScaleX(factor);
                child.setScaleY(factor);
            }
        }
    }

}
