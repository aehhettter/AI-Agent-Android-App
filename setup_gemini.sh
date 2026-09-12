#!/usr/bin/env bash
set -e

echo "==> [1/4] Aktualisiere app/build.gradle.kts..."
GRADLE_FILE="app/build.gradle.kts"
if [ -f "$GRADLE_FILE" ]; then
    if ! grep -q "generativeai" "$GRADLE_FILE"; then
        sed -i '/dependencies {/a \    implementation("com.google.ai.client.generativeai:generativeai:0.9.0")' "$GRADLE_FILE"
        echo "   -> Gemini SDK Dependency hinzugefügt."
    else
        echo "   -> Gemini SDK bereits vorhanden."
    fi
else
    echo "   -> WARNUNG: $GRADLE_FILE nicht gefunden!"
fi

echo "==> [2/4] Aktualisiere app/src/main/AndroidManifest.xml..."
MANIFEST_FILE="app/src/main/AndroidManifest.xml"
if [ -f "$MANIFEST_FILE" ]; then
    if ! grep -q "android.permission.INTERNET" "$MANIFEST_FILE"; then
        sed -i '/<manifest/a \    <uses-permission android:name="android.permission.INTERNET" />' "$MANIFEST_FILE"
        echo "   -> INTERNET-Berechtigung hinzugefügt."
    fi
    if ! grep -q "usesCleartextTraffic" "$MANIFEST_FILE"; then
        sed -i 's/<application/<application\n        android:usesCleartextTraffic="true"/' "$MANIFEST_FILE"
        echo "   -> usesCleartextTraffic=true hinzugefügt."
    fi
else
    echo "   -> WARNUNG: $MANIFEST_FILE nicht gefunden!"
fi

echo "==> [3/4] Schreibe ChatRepository.kt neu (Gemini SDK)..."
REPO_PATH="app/src/main/java/com/example/aiagent/ChatRepository.kt"
mkdir -p "$(dirname "$REPO_PATH")"
cat << 'EOF' > "$REPO_PATH"
package com.example.aiagent

import com.google.ai.client.generativeai.GenerativeModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ChatRepository(private val apiKey: String) {

    private val generativeModel = GenerativeModel(
        modelName = "gemini-1.5-flash",
        apiKey = apiKey
    )

    suspend fun sendMessage(prompt: String): String = withContext(Dispatchers.IO) {
        return@withContext try {
            val response = generativeModel.generateContent(prompt)
            response.text ?: "Keine Antwort erhalten."
        } catch (e: Exception) {
            "Fehler: ${e.localizedMessage}"
        }
    }

    companion object {
        fun create(apiKey: String): ChatRepository {
            return ChatRepository(apiKey)
        }
    }
}
EOF

echo "==> [4/4] Aktualisiere MainActivity.kt..."
MAIN_PATH="app/src/main/java/com/example/aiagent/MainActivity.kt"
if [ -f "$MAIN_PATH" ]; then
    # Ersetze N8N_BASE_URL durch GEMINI_API_KEY Placeholder
    sed -i 's/private val N8N_BASE_URL = .*/private val GEMINI_API_KEY = "DEIN_GEMINI_API_KEY_HIER"/' "$MAIN_PATH"
    sed -i 's/ChatRepository.create(N8N_BASE_URL)/ChatRepository.create(GEMINI_API_KEY)/' "$MAIN_PATH"
    echo "   -> MainActivity.kt auf GEMINI_API_KEY umgestellt."
fi

echo "==> Fertig! Alle Dateien wurden für das Gemini SDK angepasst."
