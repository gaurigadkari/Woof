package co.touchlab.dogify.dagger.component;

/*
  Created by Gauri Gadkari on 12/21/17.
 */

import co.touchlab.dogify.view.BreedListViewModel;
import dagger.Subcomponent;

/**
 * A sub component to create ViewModels. It is called by the
 * {TODO copy link}.
 * Using this component allows
 * ViewModels to define {@link javax.inject.Inject} constructors.
 */
@Subcomponent
public interface ViewModelSubComponent {
    @Subcomponent.Builder
    interface Builder {
        ViewModelSubComponent build();
    }

    BreedListViewModel breedListViewModel();
}
