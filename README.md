# SocialMesh

**SocialMesh** è una piattaforma mobile innovativa progettata per favorire nuove connessioni tra persone che partecipano agli stessi eventi, trasformando ogni occasione in un’opportunità sociale.

Attraverso un’interfaccia intuitiva, gli utenti possono visualizzare chi parteciperà a un determinato evento, esplorare la posizione delle persone sulla mappa, applicare filtri di ricerca e interagire tramite un sistema di like e match.

Grazie a funzionalità come la **Map Mode**, la **lista degli iscritti** e i **filtri personalizzati**, SocialMesh offre un’esperienza dinamica, ideale sia per chi desidera fare nuove amicizie, sia per chi cerca semplicemente compagnia per un evento.
---

## Indice

- [Descrizione](#descrizione)
- [Requisiti](#requisiti)
- [Installazione e Configurazione](#installazione-e-configurazione)
- [Utilizzo dell'Applicazione](#utilizzo-dellapplicazione)
- [Struttura del Repository](#struttura-del-repository)
- [Contributori](#contributori)
- [Licenza](#licenza)
- [Contatti e Supporto](#contatti-e-supporto)

---

## Descrizione

SocialMesh è un’app Android pensata per migliorare l’esperienza sociale legata agli eventi.  
Con SocialMesh puoi:

- **Matchare** con altri partecipanti agli eventi  
- Usare la **Map Mode** per esplorare chi ti circonda  
- Vedere la **lista degli iscritti** a un evento  
- Mettere **like** e ricevere match  
- Applicare **filtri** per trovare persone affini  

L'app è alimentata dalle API di Ticketmaster per il recupero degli eventi e utilizza Google Maps per la visualizzazione interattiva.  
> **Nota:** L’API di Ticketmaster funziona solo negli Stati Uniti, quindi è consigliato impostare la posizione fittizia dell’emulatore su **Indianapolis** (zona ricca di eventi).

---

## Requisiti

- Linguaggio: Java  
- Build System: Gradle  
- IDE Consigliata: Android Studio  
- Emulatore Android con posizione fittizia (consigliato: Indianapolis, USA)

---

## Installazione e Configurazione

### 1. Clona il repository

```bash
git clone https://github.com/MartinaKenna/SocialMesh.git
```

### 2. Configura le API Keys

Crea o modifica il file `local.properties` nella root del progetto con:

```properties
events_api_key=ymPPalpoNoG8lG5xyca0AQ6uhACG4y3j
MAPS_API_KEY=AIzaSyB3WFIwU3fCEYmeuX0APxNQibeo6G4Kuww
```

### 3. Imposta la posizione sull’emulatore Android

- Apri l’emulatore  
- Imposta una posizione fittizia su **Indianapolis, USA**

### 4. Avvia l’app

```bash
./gradlew build
./gradlew installDebug
```

---

## Utilizzo dell'applicazione

- Consenti i permessi di posizione  
- Naviga nella mappa interattiva per vedere eventi e persone  
- Apri un evento per vedere chi partecipa  
- Esplora i profili, metti like e matcha  
- Applica filtri per trovare persone con interessi simili

---

## Struttura del repository

```text
app/                  → Codice sorgente
Screenshot/           → Immagini dell’interfaccia
Documentazione/       → Risorse e spiegazioni
build.gradle.kts      → Configurazione del build system
local.properties      → File locale con le API keys (non incluso nel repo)
.gitignore            → File esclusi dal versionamento
```

---

## Contributori

- Martina Kenna – 879403  
- Giovanni Mensi – 886516  
- Francesco Barresi – 905027

---

## Licenza

Licenza non specificata.  
Contattare gli sviluppatori per utilizzo o collaborazione.

---

## Contatti e Supporto

Per dubbi, problemi o proposte:

- Apri un [issue](https://github.com/MartinaKenna/SocialMesh/issues)  
- Oppure contatta direttamente il team

---

Con **SocialMesh**, ogni evento diventa un’opportunità per conoscere qualcuno di speciale.  
**Matcha. Conosci. Vivi l’evento.**
