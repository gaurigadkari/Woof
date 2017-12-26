package co.touchlab.dogify.view;

import android.arch.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import co.touchlab.dogify.models.Breed;
import co.touchlab.dogify.repository.BreedRepository;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Gauri Gadkari on 10/28/17.
 */

public class BreedListViewModel extends ViewModel {

    private Observable<Breed> breedObservable;
    private List<Breed> breedList = new ArrayList<>();

    @Inject
    public BreedListViewModel(BreedRepository breedRepository) {
        breedObservable = breedRepository.loadBreed();
        breedObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Breed>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        breedList.clear();
                    }

                    @Override
                    public void onNext(Breed breed) {
                        breedList.add(breed);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    public Observable<Breed> getBreedObservable() {
        return breedObservable;
    }
}