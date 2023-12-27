package it.unimib.socialmesh.ui.main;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import it.unimib.socialmesh.repository.IEventsRepositoryWithLiveData;

public class EventViewModelFactory implements ViewModelProvider.Factory{
    private final IEventsRepositoryWithLiveData iEventsRepositoryWithLiveData;

    public EventViewModelFactory(IEventsRepositoryWithLiveData iEventsRepositoryWithLiveData) {
        this.iEventsRepositoryWithLiveData = iEventsRepositoryWithLiveData;
    }
    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class <T> modelClass){
        return (T) new EventViewModel(iEventsRepositoryWithLiveData);
    }
}
