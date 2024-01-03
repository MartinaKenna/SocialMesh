package it.unimib.socialmesh.service;
import java.util.ArrayList;
import java.util.List;

public class FirebaseEvent {

    private String name;
    private String remoteId;
    private Long localId;

    private List<String> participants;

    public List<String> getParticipants() {
        return participants;
    }

    public void setParticipants(List<String> participants) {
        this.participants = participants;
    }
// Altri campi necessari

    public Long getLocalId() {
        return localId;
    }

    public void setLocalId(Long localId) {
        this.localId = localId;
    }

    public FirebaseEvent() {
        // Costruttore vuoto richiesto per Firebase
    }

    public FirebaseEvent(String remoteId,String name, Long localId, List<String> partecipants) {
        this.name = name;
        this.remoteId = remoteId;
        this.localId = localId;
        this.participants = partecipants;
    }

    public String getName() {
        return name;
    }

    public String getRemoteId() {
        return remoteId;
    }

    public void setRemoteId(String localId) {
        this.remoteId = localId;
    }

    public void setName(String name) {
        this.name = name;
    }


    // Aggiungi getter e setter per gli altri campi necessari

}
