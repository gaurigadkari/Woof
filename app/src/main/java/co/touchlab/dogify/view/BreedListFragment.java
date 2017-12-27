package co.touchlab.dogify.view;


import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import javax.inject.Inject;

import co.touchlab.dogify.R;
import co.touchlab.dogify.adapter.BreedAdapter;
import co.touchlab.dogify.dagger.utility.Injectable;
import co.touchlab.dogify.databinding.FragmentBreedListBinding;
import co.touchlab.dogify.models.Breed;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * A simple {@link Fragment} subclass.
 */
public class BreedListFragment extends Fragment implements Injectable {
    private RecyclerView breedRecyclerView;
    private BreedAdapter adapter = new BreedAdapter();
    private BreedListViewModel breedListViewModel;
    private ProgressBar spinner;
    FragmentBreedListBinding binding;
    @Inject
    ViewModelProvider.Factory viewModelFactory;
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();

    public BreedListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        breedListViewModel = ViewModelProviders.of(this, viewModelFactory).get(BreedListViewModel.class);
        breedListViewModel.getBreedObservable()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Breed>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        compositeDisposable.add(d);
                        adapter.clear();
                        showSpinner(true);
                    }

                    @Override
                    public void onNext(Breed breed) {
                        adapter.add(breed);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Snackbar.make(breedRecyclerView, "Oops! Something went wrong", Snackbar.LENGTH_LONG)
                                .show();
                        Log.d("TAG", e.getMessage());
                        showSpinner(false);
                    }

                    @Override
                    public void onComplete() {
                        showSpinner(false);
                    }
                });

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater,
                R.layout.fragment_breed_list, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        spinner = binding.spinner;
        breedRecyclerView = binding.breedList;
        breedRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        breedRecyclerView.setAdapter(adapter);
    }

    public static BreedListFragment newInstance() {
        return new BreedListFragment();
    }

    private void showSpinner(Boolean show) {
        spinner.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onDestroy() {
        //compositeDisposable.dispose();
        super.onDestroy();
    }
}
