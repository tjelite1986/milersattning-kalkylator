# 📱 Milersättning Kalkylator

En professionell Android-app för att beräkna milersättning enligt svenska satser. Appen använder GPS-spårning, adresssökning via OpenStreetMap och exporterar data till PDF/CSV - allt helt gratis utan API-nycklar!

![Version](https://img.shields.io/badge/version-0.03-blue.svg)
![Platform](https://img.shields.io/badge/platform-Android-green.svg)
![Min SDK](https://img.shields.io/badge/min%20SDK-24-orange.svg)
![License](https://img.shields.io/badge/license-MIT-brightgreen.svg)

## 🎯 **Funktioner**

### 📍 **Tre beräkningssätt**
- **🛰️ GPS-spårning** - Automatisk distansmätning med real-time uppdateringar
- **🏠 Adresssökning** - Ange från/till-adresser med smart autocomplete
- **✋ Manuell inmatning** - Ange distans direkt i kilometer

### 💰 **Aktuella svenska satser**
- **2024: 1.85 SEK/km** för personbil
- Konfigurerbar sats för andra fordon eller framtida ändringar

### ⭐ **Avancerade funktioner**
- **📍 Favoritplatser** - Spara ofta använda adresser
- **📊 Export till PDF/CSV** - Professionella rapporter för bokföring
- **🕐 Detaljerad reseinfo** - Tid, syfte, fordonstyp, anteckningar
- **📱 Modern UI** - Intuitivt gränssnitt med Material Design
- **🔒 Privat** - All data sparas lokalt, ingen cloud-synkning

## 🚀 **Installation**

### 📥 **Ladda ner senaste versionen**
1. Gå till [Releases](https://github.com/tjelite1986/milersattning-kalkylator/releases)
2. Ladda ner `milersattning-v0.03.apk`
3. Aktivera "Installera från okända källor" i Android-inställningar
4. Installera APK-filen
5. Bevilja GPS-tillstånd för automatisk spårning (valfritt)

### 📋 **Systemkrav**
- Android 7.0 (API 24) eller senare
- 75 MB ledigt lagringsutrymme
- GPS för automatisk distansspårning (valfritt)
- Internetanslutning för adresssökning (valfritt)

## 🛠️ **Användning**

### 🛰️ **GPS-spårning**
1. Välj "GPS-spårning (automatisk)" som metod
2. Klicka "Starta GPS" när du börjar din resa
3. Se real-time distans under resan
4. Klicka "Stoppa GPS" när resan är klar
5. Lägg till beskrivning och spara

### 🏠 **Adressberäkning**
1. Välj "Beräkna från adresser" som metod
2. Skriv start- och destinationsadress
3. Använd autocomplete-förslag eller favoriter
4. Klicka "Beräkna" för att få distans och belopp
5. Spara resan med automatisk beskrivning

### 📊 **Export av data**
1. Samla flera resor i historiken
2. Klicka "Exportera PDF" för professionell rapport
3. Eller "Exportera CSV" för Excel/Google Sheets
4. Dela filen direkt via e-post eller cloud-lagring

## 🔧 **Teknisk information**

### 📦 **Arkitektur**
- **Språk**: Kotlin
- **Platform**: Android Native
- **Min SDK**: 24 (Android 7.0)
- **Target SDK**: 34 (Android 14)
- **Databas**: SharedPreferences (lokal lagring)

### 📚 **Huvudbibliotek**
- **Google Play Services Location 21.0** - GPS-spårning
- **iText 8.0** - PDF-generering
- **OkHttp 4.12** - OpenStreetMap API-anrop
- **Gson 2.10** - JSON-parsing
- **Material Design Components** - Modern UI

### 🗺️ **Externa tjänster**
- **OpenStreetMap Nominatim** - Geocoding (adress → koordinater)
- **OpenStreetMap OSRM** - Ruttberäkning och distans
- **Helt gratis** - Inga API-nycklar eller registrering krävs!

## 🏗️ **Utveckling**

### 🔨 **Bygga från källkod**
```bash
# Klona repository
git clone https://github.com/tjelite1986/milersattning-kalkylator.git

# Öppna i Android Studio eller bygg med Gradle
cd milersattning-kalkylator
./gradlew assembleDebug

# APK skapas i: app/build/outputs/apk/debug/
```

### 📁 **Projektstruktur**
```
app/src/main/java/com/milersattning/app/
├── MainActivity.kt              # Huvud-activity
├── OpenStreetMapService.kt      # API-integration
├── GPSTrackingService.kt        # GPS-spårning  
├── ExportService.kt             # PDF/CSV export
├── AddressAutocompleteAdapter.kt # Adresssökning
├── FavoritesAdapter.kt          # Favorithantering
└── TripsAdapter.kt              # Resehistorik
```

## 📚 **Dokumentation**

- [📄 CHANGELOG.md](CHANGELOG.md) - Versionshistorik
- [📋 Release Notes v0.03](RELEASE_NOTES_v0.03.md) - Detaljerad funktionsbeskrivning
- [📋 Release Notes v0.02](RELEASE_NOTES_v0.02.md) - Favoritfunktioner

## 🎯 **Roadmap**

### 🔮 **v0.04 (Planerad)**
- 📊 Månadsrapporter och statistik
- 🔄 Automatisk backup till cloud
- 🏷️ Kategorier för resor (arbete, privat, etc.)
- 🛣️ Ruttoptimering för återkommande resor
- 🚗 Olika satser för olika fordonstyper

### 🚀 **Framtida versioner**
- 👥 Multi-fordonstöd
- 🔗 Integration med bokföringssystem
- 🗺️ Offline-kartfunktion
- 🏢 Företagsfunktioner

## 🤝 **Bidrag**

Bidrag välkomnas! Skapa en Issue eller Pull Request för:
- 🐛 Buggrapporter
- 💡 Funktionsförslag  
- 🔧 Kodförbättringar
- 📖 Dokumentation

## 🙏 **Tack till**

- **OpenStreetMap** - Gratis kartdata och routing
- **Material Design** - UI-komponenter
- **Android Community** - Inspiration och hjälp

## 📄 **Licens**

Detta projekt är licensierat under MIT License - se [LICENSE](LICENSE) fil för detaljer.

## 💬 **Support**

- 🐛 **Buggar**: Öppna en [Issue](https://github.com/tjelite1986/milersattning-kalkylator/issues)
- 💡 **Funktionsförslag**: Använd [Discussions](https://github.com/tjelite1986/milersattning-kalkylator/discussions)
- 📧 **Kontakt**: [tjelite1986@gmail.com](mailto:tjelite1986@gmail.com)

---

**🤖 Utvecklad med [Claude Code](https://claude.ai/code)**

**⭐ Om du gillar appen, ge den en stjärna på GitHub!**