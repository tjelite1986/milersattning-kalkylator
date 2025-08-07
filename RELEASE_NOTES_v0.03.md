# 📱 Milersättning v0.03 - Release Notes

## 🎯 Översikt
Version 0.03 introducerar två mycket efterfrågade funktioner: **GPS-spårning för automatisk distansmätning** och **Export av resor till PDF/CSV**. Denna release gör appen betydligt mer kraftfull för både personlig användning och professionell kostnadsredovisning.

## ✨ Nya funktioner

### 🛰️ GPS-spårning (Automatisk distansmätning)
- **Nytt GPS-läge** bredvid adress- och manuellt läge
- **Automatisk distansberäkning** genom att spåra din faktiska resa
- **Real-time uppdateringar** av spårad distans under resan
- **Smart filtrering** - endast betydande rörelser (5+ meter) registreras
- **Batterisparande** - optimerade inställningar för location services

### 📤 Export av resor
- **PDF-export** - professionella rapporter med detaljerad information
- **CSV-export** - för import i Excel, Google Sheets eller andra system
- **Fullständig metadata** - alla resedetaljer inkluderas i exporten
- **Automatisk fildelning** - dela direkt via e-post, cloud-lagring, etc.
- **Sammanfattningssektion** - totala siffror och statistik i både PDF och CSV

### 📋 Hur du använder de nya funktionerna:

#### GPS-spårning:
1. **Aktivera GPS-läge** - välj "GPS-spårning (automatisk)" som metod
2. **Starta spårning** - klicka "Starta GPS" när du börjar din resa
3. **Övervaka progress** - se real-time distans och din position
4. **Stoppa spårning** - klicka "Stoppa GPS" när resan är klar
5. **Spara resa** - distansen fylls i automatiskt, lägg till beskrivning och spara

#### Export av resor:
1. **Samla resor** - se till att du har sparade resor i systemet
2. **Välj format** - klicka "Exportera PDF" eller "Exportera CSV"
3. **Dela filen** - välj hur du vill dela den genererade rapporten
4. **Använd i andra system** - importera CSV i bokföringssystem eller Excel

## 🔧 Tekniska förbättringar

### 🛰️ GPS-integration
- **Google Play Services** - använder pålitliga location services
- **Haversine-formel** - exakt distansberäkning mellan GPS-punkter
- **Smart filtrering** - undviker GPS-"jitter" och små rörelser
- **Behörighetshantering** - säker hantering av platstillstånd
- **Batterioptimiering** - effektiv användning av GPS för minimal batteridränering

### 📄 PDF-generering
- **iText 8.0** - professionell PDF-generation
- **Svensk layout** - optimerat för svenska användare och valutor
- **Tabellformat** - tydlig presentation av resedata
- **Automatisk formattering** - datum, belopp och distans visas korrekt

### 📊 CSV-kompatibilitet  
- **Standard CSV-format** - fungerar med alla stora program
- **UTF-8 encoding** - stöd för svenska tecken (å, ä, ö)
- **Escape-hantering** - säker hantering av specialtecken i beskrivningar
- **Metadata-headers** - kommentarsektion med sammanfattning

## 🚀 Förbättringar av befintliga funktioner

### 📱 Förbättrat användargränssnitt
- **Tre-läges-navigation** - tydlig separation mellan adress-, manuell och GPS-läge
- **Visuell feedback** - GPS-status och progress visas tydligt
- **Smart formhantering** - automatisk rensning när man byter mellan lägen
- **Exportsektion** - dedicerat område för exportfunktioner

### 🔒 Säkerhet och integritet
- **Lokala filer** - alla exporterade filer sparas endast på din enhet
- **Säker delning** - använder Android's inbyggda FileProvider
- **GPS-data** - ingen GPS-data skickas till externa servrar
- **Privat spårning** - all location-data förblir på din enhet

## 📱 Systemkrav (uppdaterat)

### 💾 Grundkrav
- Android 7.0 (API 24) eller senare  
- 75 MB ledigt utrymme (ökat från 50 MB för PDF-bibliotek)
- Internetanslutning för adresssökning

### 🛰️ GPS-funktioner
- **GPS/GNSS-mottagare** - för automatisk spårning
- **Platstillstånd** - appen begär tillgång till "exakt plats"
- **Bakgrundsbearbetning** - för kontinuerlig spårning under resan

### 📤 Export-funktioner  
- **Fillagring** - tillgång för att spara exporterade filer
- **Delning** - möjlighet att dela filer via andra appar

## 🛠️ Tekniska detaljer

### 📦 Versionsinformation
- **Versionskod**: 3
- **Versionsnamn**: 0.03  
- **APK-storlek**: ~12 MB (ökat från ~8 MB)
- **Målplattform**: Android SDK 34

### 📚 Nya biblioteksberoenden
- **iText 8.0** - PDF-generering
- **Google Play Services Location 21.0** - GPS-funktioner
- **FileProvider** - säker fildelning

### 🔧 Prestandaoptimering
- **Bakgrundstrådar** - export körs på bakgrundstrådar
- **Smart GPS-polling** - endast 5-sekunders intervaller
- **Minnesoptimering** - effektiv hantering av stora reselistor

## 🚀 Installation och uppgradering

### 📱 Ny installation
1. Ladda ner `milersattning-v0.03.apk`
2. Aktivera "Okända källor" i Android-inställningar  
3. Installera APK-filen
4. **Viktigt**: Bevilja platstillstånd för GPS-funktioner
5. Bevilja fillagringstillstånd för export

### 🔄 Uppgradering från v0.02
- **Automatisk datamigrering** - alla befintliga resor och favoriter bevaras
- **Nya funktioner aktiveras** - GPS och export blir tillgängliga direkt
- **Inga inställningsändringar** - befintliga konfigurationer behålls
- **Tillstånd krävs** - du kommer att tillfrågas om GPS- och fillåtelser

### 📊 Kompatibilitet med tidigare versioner
- **Resedata** - fullständigt kompatibel med v0.01 och v0.02
- **Favoriter** - alla sparade favoritplatser fungerar som tidigare
- **Inställningar** - milersättningssats och andra inställningar bevaras

## 🎉 Kommande funktioner (nästa release)

### 📊 v0.04 planeras att innehålla:
- **Månadsrapporter och statistik** - detaljerade analyser av dina resor
- **Automatisk backup** - säkerhetskopiera data till cloud
- **Kategorier för resor** - organisera resor efter typ (arbete, privat, etc.)
- **Ruttoptimering** - föreslå effektivare vägar för återkommande resor
- **Kilometerpåslag** - stöd för olika tariff för olika fordonstyper

### 🔮 Längre framtid:
- **Multi-fordonstöd** - hantera flera fordon med olika milersättningar
- **Integration med bokföringssystem** - direkt export till populära tjänster  
- **Offline-kartfunktion** - GPS-spårning utan internetuppkoppling
- **Företagsfunktioner** - stöd för team och administratörer

## 🐛 Kända problem och lösningar

### 🛰️ GPS-relaterade
- **Första GPS-fix** - kan ta 30-60 sekunder i inomhusmiljöer
  - *Lösning*: Starta GPS-spårning utomhus för bästa resultat
- **Batterianvändning** - GPS förbrukar mer batteri än andra lägen  
  - *Lösning*: Använd bara under aktiva resor, inte för parkering

### 📤 Export-relaterade
- **PDF-storlek** - stora reselistor kan skapa stora PDF-filer
  - *Lösning*: Exportera månadsvis istället för hela listan
- **Fildelning** - vissa e-postklienter begränsar bifogade filer
  - *Lösning*: Använd cloud-lagring för stora rapporter

## 📞 Support och feedback

### 🆘 Få hjälp
- **Rapportera buggar** via GitHub Issues  
- **Användarguide** finns på projektets wiki
- **FAQ** uppdateras kontinuerligt baserat på användarfrågor

### 💡 Föreslå funktioner  
- **GitHub Discussions** - för större funktionsförslag
- **Issues** - för specifika förbättringar  
- **Community** - diskutera med andra användare

### 🔍 Felsökning
- **GPS fungerar inte**: Kontrollera platstillstånd i Android-inställningar
- **Export misslyckas**: Kontrollera tillgängligt lagringsutrymme  
- **Appen kraschar**: Skicka kraschrapporter via Android's system

---

## 🙏 Tack till communityn

Stort tack till alla som testade beta-versionen och gav feedback på GPS-funktionaliteten och exportformaten. Era förslag har hjälpt till att göra v0.03 till den mest robusta versionen hittills!

**🤖 Utvecklad med hjälp av [Claude Code](https://claude.ai/code)**

**📅 Releasedatum**: 6 augusti 2025  
**👨‍💻 Utvecklare**: Thomas  
**🏷️ Version**: 0.03  
**📦 Filnamn**: milersattning-v0.03.apk
**⭐ Rekommendation**: GPS-spårning ger den mest exakta distansmätningen för professionell kostnadsredovisning!