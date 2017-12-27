package co.touchlab.dogify.view;

import android.arch.lifecycle.ViewModel;

import javax.inject.Inject;

import co.touchlab.dogify.models.Breed;
import co.touchlab.dogify.repository.BreedRepository;
import io.reactivex.Observable;

/**
 * Created by Gauri Gadkari on 10/28/17.
 */

public class BreedListViewModel extends ViewModel {

    private Observable<Breed> breedObservable;

    @Inject
    public BreedListViewModel(BreedRepository breedRepository) {
        breedObservable = breedRepository.loadBreed()
                .cache();
    }

    public Observable<Breed> getBreedObservable() {
        return breedObservable;
    }
}