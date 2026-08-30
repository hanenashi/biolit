# BioLit

Tiny Android helper that adds **Search BioLib** to Android's selected-text context menu.

Select text in an app, choose **Search BioLib**, and BioLit opens a BioLib.cz search in the browser.

The launcher activity is intentionally just a settings screen. The selected search type is stored locally and used by the `PROCESS_TEXT` activity. Settings also show the installed app version for alpha testing.

## Current default

- Search type: **Taxony (čeština)**
- BioLib parameters: executed advanced-search parameters for scientific names, Czech common names, and synonyms
- Endpoint: `https://www.biolib.cz/cz/formsearch/`

See [`battleplan.md`](battleplan.md) for the implementation plan and acceptance criteria.

## Build

```sh
./gradlew test assembleDebug
```

The debug APK is written to:

```text
app/build/outputs/apk/debug/app-debug.apk
```
