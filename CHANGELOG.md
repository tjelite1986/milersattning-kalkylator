# Changelog

Alla viktiga ändringar i detta projekt dokumenteras i denna fil.

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