package co.touchlab.dogify;

import co.touchlab.dogify.models.ImageResult;
import co.touchlab.dogify.models.NameResult;
import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface DogService {
    @GET("breeds/list")
    Observable<NameResult> getBreeds();

    @GET("breed/{name}/images/random")
    Observable<ImageResult> getImage(@Path("name") String name);
}
