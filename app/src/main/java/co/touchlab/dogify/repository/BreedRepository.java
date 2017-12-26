package co.touchlab.dogify.repository;

import android.util.Log;

import javax.inject.Inject;
import javax.inject.Singleton;

import co.touchlab.dogify.DogService;
import co.touchlab.dogify.models.Breed;
import co.touchlab.dogify.models.ImageResult;
import io.reactivex.Observable;

/**
 * Created by Gauri Gadkari on 12/21/17.
 */

@Singleton
public class BreedRepository {
    private DogService dogService;

    @Inject
    BreedRepository(DogService dogService) {
        this.dogService = dogService;
    }

    public Observable<Breed> loadBreed() {
        return dogService.getBreeds()
                .flatMap(nameResult -> {
                    return Observable.just(nameResult.getMessage());
                })
                .flatMapIterable(listOfBreeds -> listOfBreeds)
                .flatMap(breedName -> {
                    return dogService.getImage(breedName);
                })
                .flatMap(imageResult -> {
                    Breed breed = new Breed();
                    breed.setImageURL(imageResult.getMessage());
                    String[] urlParts = imageResult.getMessage().split("/");
                    String breedName = urlParts[5];
                    breed.setName(breedName);
                    return Observable.just(breed);
                });
    }
}

