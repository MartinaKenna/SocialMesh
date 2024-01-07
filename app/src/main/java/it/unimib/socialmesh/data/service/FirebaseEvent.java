package it.unimib.socialmesh.data.service;
import java.util.ArrayList;
import java.util.List;

import it.unimib.socialmesh.model.jsonFields.Image;

public class FirebaseEvent {

    private String name;
    private String remoteId;
    private Long localId;
    private String url;
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

    public FirebaseEvent(String remoteId,String name, Long localId, List<String> partecipants, String url) {
        this.name = name;
        this.remoteId = remoteId;
        this.localId = localId;
        this.participants = partecipants;
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
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





}
