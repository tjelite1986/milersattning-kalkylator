# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview
Android app for calculating mileage reimbursement (milersättning) in Sweden.

## Development Environment Setup
- Android Studio with latest Android SDK
- Minimum SDK version: API 24 (Android 7.0)
- Target SDK version: Latest stable
- Kotlin as primary language

## Project Structure
Standard Android project structure with:
- `app/src/main/java/` - Main Kotlin source code
- `app/src/main/res/` - Resources (layouts, strings, etc.)
- `app/src/test/` - Unit tests
- `app/src/androidTest/` - Instrumentation tests

## Key Features 
- ✅ Distance calculation between addresses using OpenStreetMap (FREE!)
- ✅ Current Swedish mileage rates (2024: 1.85 SEK/km for car)
- ✅ Trip logging and history
- ✅ Manual distance entry option
- ❌ Export functionality for expense reports (TODO)
- ❌ GPS integration for automatic distance tracking (TODO)

## Build Commands
- `./gradlew build` - Build the project
- `./gradlew test` - Run unit tests
- `./gradlew connectedAndroidTest` - Run instrumentation tests
- `./gradlew assembleDebug` - Build debug APK
- `./gradlew assembleRelease` - Build release APK

## Dependencies
- OpenStreetMap (Nominatim + OSRM) for FREE address lookup and route calculation
- Room database for local data storage (prepared but not used yet)
- Material Design components
- Location services for future GPS functionality
- OkHttp for API calls
- Gson for JSON parsing

## Setup Instructions

### No API Keys Required! 🎉
The app now uses OpenStreetMap services which are completely free:
- **Nominatim**: For converting addresses to coordinates
- **OSRM**: For calculating driving routes and distances
- **No registration needed**: Just build and run!

### Current Features
- **Dual Mode Operation**: Toggle between manual distance entry and address-based calculation
- **Address-based Distance**: Enter from/to addresses and get FREE driving distance via OpenStreetMap
- **Smart Autocomplete**: Real-time address suggestions as you type (after 2 characters)
- **Trip Management**: Save trips with descriptions, view history, and clear all data
- **Configurable Rates**: Modify the mileage rate through settings
- **Local Storage**: All data is stored locally using SharedPreferences
- **Swedish Focus**: Address searches prioritized for Swedish locations

### How It Works
1. **Address Input**: Start typing Swedish addresses and get instant suggestions
   - Examples: "Stock..." → "Stockholm", "Göte..." → "Göteborg", "Malmö Central..."
2. **Autocomplete**: Real-time suggestions from OpenStreetMap database
3. **Geocoding**: Nominatim finds coordinates for selected addresses
4. **Route Calculation**: OSRM calculates the driving distance and time
5. **Cost Calculation**: Distance × your configured mileage rate

### Example Usage
- Type "Stock" → Select "Stockholm" from dropdown
- Type "Göte" → Select "Göteborg" from dropdown
- Press "Beräkna" → Get distance, travel time, and cost
- Save trip with automatic "Stockholm → Göteborg" description