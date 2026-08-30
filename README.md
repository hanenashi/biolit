# BioLit

Tiny Android helper that adds **🐞 BioLit** to Android's selected-text context menu and plain-text share sheet.

Select or share text in an app, choose **🐞 BioLit**, and BioLit opens a BioLib.cz search in the browser.

The launcher activity is intentionally just a settings screen. The selected search type is stored locally and used by the `PROCESS_TEXT` activity. Settings also show the installed app version for alpha testing.

## Current default

- Search type: **Taxony (čeština)**
- BioLib parameters: `action=execute&searcharea=1`
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

## BioLib Mobile CSS

`web/` contains a small userscript and responsive CSS draft for testing
BioLib.cz mobile readability with a real viewport meta tag.

`biolib-stylebot-mobile.css` is kept temporarily as the original Stylebot
proof-of-concept.
