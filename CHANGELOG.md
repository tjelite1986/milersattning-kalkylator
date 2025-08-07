# Changelog

Alla viktiga ändringar i detta projekt dokumenteras i denna fil.

## [0.03] - 2025-08-07

### ✨ Nya funktioner
- **GPS-spårning**: Automatisk distansmätning genom att spåra din faktiska resa
  - Real-time uppdateringar av spårad distans under resan
  - Smart filtrering - endast betydande rörelser (5+ meter) registreras
  - Batterisparande - optimerade inställningar för location services
  - Haversine-formel för exakt distansberäkning mellan GPS-punkter

- **Export av resor**: Professionella rapporter och dataexport
  - **PDF-export** - professionella rapporter med detaljerad information
  - **CSV-export** - för import i Excel, Google Sheets eller andra system
  - Fullständig metadata - alla resedetaljer inkluderas i exporten
  - Automatisk fildelning - dela direkt via e-post, cloud-lagring, etc.

- **Förbättrade resedetaljer**: Mer omfattande information för varje resa
  - Start- och sluttid för resor
  - Syfte (Privat, Arbete, Möte, Utbildning, Resa, Övrigt)
  - Fordonstyp (Bil, Motorcykel, Husbil, Lastbil, Övrigt)
  - Anteckningar för extra information
  - Status (Genomförd som standard)

### 🔧 Tekniska förbättringar
- **Google Play Services Location 21.0** - pålitliga location services
- **iText 8.0** - professionell PDF-generation med svensk layout
- **FileProvider** - säker hantering av platstillstånd och fildelning
- **Batterioptimiering** - effektiv användning av GPS för minimal batteridränering
- **UTF-8 encoding** - stöd för svenska tecken (å, ä, ö) i CSV-export

### 🎯 Användarupplevelse
- **Tre-läges-navigation** - tydlig separation mellan adress-, manuell och GPS-läge
- **Visuell feedback** - GPS-status och progress visas tydligt
- **Smart formhantering** - automatisk rensning när man byter mellan lägen
- **Exportsektion** - dedicerat område för exportfunktioner
- **Redigerbara resor** - uppdatera all information för sparade resor

---

## [0.02] - 2025-08-06

### ✨ Nya funktioner
- **Favoritplatser**: Nu kan du spara ofta använda adresser som favoriter
  - Stjärnknappar (★) bredvid adressfälten för snabb åtkomst
  - "Lägg till favorit"-knapp för att spara aktuell adress med eget namn
  - "Hantera favoriter"-knapp för att visa, använda eller ta bort sparade platser
  - Alla favoriter sparas lokalt på enheten

### 🐛 Buggfixar
- **Förbättrad autocomplete**: Fixat problem där bara gatunamn fylldes i istället för fullständig adress
  - Nu behålls husnummer från din ursprungliga sökning (t.ex. "Kastanjevägen 3")
  - Autocomplete visar nu fullständiga adresser när du väljer förslag
  - Smartare hantering av adressförslag från OpenStreetMap

### 🔧 Tekniska förbättringar
- Förbättrad datahantering för address-förslag
- Optimerad dubbletthantering i autocomplete-resultat
- Bättre felhantering för adressökning

### 🎯 Användarupplevelse
- Snabbare arbetsflöde med sparade favoriter
- Mer tillförlitlig adressinmatning
- Behåller användarens inmatade husnummer

---

## [0.01] - 2024 (Initial release)

### ✨ Initiala funktioner
- Beräkning av milersättning med aktuella svenska satser (1.85 SEK/km)
- Dual-läge: Manuell distans eller adressbaserad beräkning
- OpenStreetMap-integration för GRATIS distansberäkning
- Real-time adressförslag (autocomplete)
- Resa historik med spara/visa funktioner
- Konfigurerbar milersättning
- Svenskfokuserad adressökning
- Lokal datalagring (ingen cloud/server krävs)