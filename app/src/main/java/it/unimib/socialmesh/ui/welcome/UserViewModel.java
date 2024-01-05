package it.unimib.socialmesh.ui.welcome;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import it.unimib.socialmesh.data.repository.user.IUserRepository;
import it.unimib.socialmesh.model.Result;
import it.unimib.socialmesh.model.User;

public class UserViewModel extends ViewModel {
    private static final String TAG = UserViewModel.class.getSimpleName();

    private final IUserRepository userRepository;
    private MutableLiveData<Result> userMutableLiveData;
    private MutableLiveData<String> profileFullName = new MutableLiveData<>();
    private MutableLiveData<String> profileBirthDate = new MutableLiveData<>();
    private boolean authenticationError;

    public UserViewModel(IUserRepository userRepository) {
        this.userRepository = userRepository;
        authenticationError = false;
    }

    public MutableLiveData<Result> getUserMutableLiveData(
            String email, String password, boolean isUserRegistered) {
        if (userMutableLiveData == null) {
            getUserData(email, password, isUserRegistered);
        }
        return userMutableLiveData;
    }

    public MutableLiveData<Result> getGoogleUserMutableLiveData(String token) {
        if (userMutableLiveData == null) {
            getUserData(token);
        }
        return userMutableLiveData;
    }

    public MutableLiveData<String> getProfileFullName() {
        return profileFullName;
    }

    // Getter per il LiveData della data di nascita
    public MutableLiveData<String> getProfileBirthDate() {
        return profileBirthDate;
    }

    // Metodo per salvare il nome e la data di nascita
    public void saveProfileDetails(String fullName, String birthDate) {
        // Effettua il salvataggio dei dati in Firebase
        // ...

        // Aggiorna i LiveData per l'aggiornamento dell'interfaccia utente
        profileFullName.setValue(fullName);
        profileBirthDate.setValue(birthDate);
    }

    public User getLoggedUser() {
        return userRepository.getLoggedUser();
    }

    public MutableLiveData<Result> logout() {
        if (userMutableLiveData == null) {
            userMutableLiveData = userRepository.logout();
        } else {
            userRepository.logout();
        }

        return userMutableLiveData;
    }

    public void getUser(String email, String password, boolean isUserRegistered) {
        userRepository.getUser(email, password, isUserRegistered);
    }

    public boolean isAuthenticationError() {
        return authenticationError;
    }

    public void setAuthenticationError(boolean authenticationError) {
        this.authenticationError = authenticationError;
    }

    private void getUserData(String email, String password, boolean isUserRegistered) {
        userMutableLiveData = userRepository.getUser(email, password, isUserRegistered);
    }

    private void getUserData(String token) {
        userMutableLiveData = userRepository.getGoogleUser(token);
    }
}
