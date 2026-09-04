# 🤖 AI-Agent Android App mit n8n Backend

Eine vollständig funktionsfähige Android-App, die mit einem n8n-Workflow kommuniziert, um KI-gestützte Antworten zu liefern.

## 🚀 Features

✅ Chat-Interface mit Material Design
✅ Echtzeit-Kommunikation mit n8n Backend
✅ OpenAI/KI-Integration
✅ Kotlin mit Coroutines
✅ MVVM-Architektur
✅ Optimiert für Galaxy A15 und alle Android-Geräte (API 24+)

## 📋 Anforderungen

- Android Studio Giraffe (2022.3.1) oder neuer
- JDK 17 oder neuer
- Android SDK API 34 (Zielversion)
- MinAPI 24 (Android 7.0)

## 🛠️ Installation

### 1. Repository klonen
```bash
git clone https://github.com/aehhettter/AI-Agent-Android-App.git
cd AI-Agent-Android-App
```

### 2. Android Studio öffnen
```bash
# Mit Android Studio öffnen
studio .
```

### 3. n8n Backend aufsetzen

```bash
# Docker Compose starten
docker-compose up -d
```

### 4. IP-Adresse konfigurieren

In `MainActivity.kt` (Zeile ~22) die IP-Adresse ändern:

```kotlin
private val N8N_BASE_URL = "http://192.168.1.XXX:5678/"  // Deine IP hier!
```

**Deine IP finden:**
```bash
# Windows
ipconfig

# Mac/Linux
ifconfig
```

Suche nach `192.168.x.x` oder `10.0.x.x`

### 5. App builden

```bash
# Debug APK
./gradlew assembleDebug

# Release APK (für Play Store)
./gradlew assembleRelease
```

Die APK befindet sich dann in:
```
app/build/outputs/apk/debug/app-debug.apk
app/build/outputs/apk/release/app-release.apk
```

## 📱 App auf Galaxy A15 installieren

### Option 1: USB-Debugging

1. Galaxy A15 mit USB-Kabel verbinden
2. Entwickleroptionen aktivieren:
   - Einstellungen → Über das Gerät
   - "Buildnummer" 7x antippen
3. USB-Debugging aktivieren
4. In Android Studio: `Run > Run 'app'`

### Option 2: APK direkt installieren

1. `app-debug.apk` auf dein Handy kopieren
2. Datei-Manager öffnen → APK antippen → Installieren

## 🔧 n8n Workflow Setup

### Docker Compose starten

```bash
docker-compose up -d
```

### n8n öffnen

Browser: `http://localhost:5678`

### Workflow importieren

1. Klick auf "Workflows" → "New"
2. Webhook Node hinzufügen:
   - Path: `ai-agent`
   - Method: POST
3. OpenAI Node hinzufügen:
   - Model: `gpt-3.5-turbo`
   - System Message: "Du bist ein hilfreicher KI-Agent"
   - User Message: `{{ $json.message }}`
4. Response formatieren
5. Aktivieren & Testen

## 🐳 Docker Compose (n8n)

```yaml
version: '3.8'

services:
  n8n:
    image: n8nio/n8n:latest
    ports:
      - "5678:5678"
    environment:
      - N8N_HOST=0.0.0.0
      - N8N_PORT=5678
      - N8N_PROTOCOL=http
      - WEBHOOK_TUNNEL_URL=http://YOUR_IP:5678/
      - GENERIC_TIMEZONE=Europe/Berlin
    volumes:
      - n8n_data:/home/node/.n8n
    networks:
      - n8n-network

volumes:
  n8n_data:
    driver: local

networks:
  n8n-network:
    driver: bridge
```

## 📚 Projektstruktur

```
AI-Agent-Android-App/
├── app/
│   ├── src/main/
│   │   ├── java/com/example/aiagent/
│   │   │   ├── MainActivity.kt          # Haupt-Activity
│   │   │   ├── ChatViewModel.kt         # ViewModel für Chat-Logic
│   │   │   ├── ChatRepository.kt        # Repository für API-Calls
│   │   │   ├── ChatMessagesAdapter.kt   # RecyclerView Adapter
│   │   │   └── ChatViewModelFactory.kt  # Factory für ViewModel
│   │   ├── res/
│   │   │   ├── layout/
│   │   │   │   ├── activity_main.xml
│   │   │   │   └── item_chat_message.xml
│   │   │   ├── drawable/
│   │   │   │   └── input_background.xml
│   │   │   └── values/
│   │   │       ├── strings.xml
│   │   │       ├── colors.xml
│   │   │       └── themes.xml
│   │   └── AndroidManifest.xml
│   └── build.gradle.kts
├── build.gradle.kts
├── settings.gradle.kts
├── gradle.properties
└── README.md
```

## 🔑 OpenAI API-Key einbinden

1. https://platform.openai.com/api-keys besuchen
2. API-Key kopieren
3. In n8n → Credentials → OpenAI erstellen
4. API-Key einfügen

## ⚙️ Troubleshooting

### "Connection refused" Fehler
→ IP-Adresse in MainActivity.kt überprüfen

### "Port 5678 already in use"
→ `docker-compose down` → `docker-compose up -d`

### App crasht bei Start
→ Internet-Berechtigung in AndroidManifest.xml prüfen

### n8n Webhook antwortet nicht
→ Firewall-Einstellungen prüfen
→ Workflow muss aktiviert sein

## 🚀 Build & Release

### Debug APK
```bash
./gradlew assembleDebug
```

### Release APK
```bash
./gradlew assembleRelease
```

### App signieren für Play Store
```bash
./gradlew bundleRelease
```

## 📄 Lizenz

Apache License 2.0

## 👨‍💻 Autoren

Copilot AI Agent

## 🤝 Support

Braucht ihr Hilfe? Öffnet ein Issue im Repository! 🙌
