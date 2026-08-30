# BioLit battle plan

## Mission

Build a tiny Android app whose main job is to add **🐞 BioLit** to Android's selected-text context menu via `Intent.ACTION_PROCESS_TEXT`.

When the user selects text in Chrome, Firefox, a note app, etc. and chooses **🐞 BioLit**, BioLit should immediately open a BioLib.cz search for that selected text in the user's browser.

The launcher activity exists mainly for app settings. Avoid turning this into a browser, WebView wrapper, account system, or anything heroic. This app should remain boringly small.

## Confirmed BioLib search contract

BioLib homepage search submits a GET request to:

```text
https://www.biolib.cz/cz/formsearch/
```

The simple homepage search submits these parameters:

```text
searcharea=<mode>
string=<query>
```

Known `searcharea` values:

| Value | BioLib meaning |
| --- | --- |
| `1` | Taxony |
| `6` | Taxony (všechny jazyky) |
| `2` | Obrázky |
| `3` | Odkazy a literatura |
| `5` | Termíny |
| `9` | Termíny (všechny jazyky) |
| `7` | Biotopy |
| `8` | Lokality |
| `100` | Hledat všude |

The simple homepage `searcharea` URL only pre-fills the advanced search form
unless the hidden `action=execute` field is included. BioLit uses the verified
simple search contract:

```text
string=<query>
action=execute
searcharea=<mode>
```

**Default must be Czech Taxony.**

Do not hard-code the search mode inside `SearchActivity`; read it from app preferences. `SearchConfig` exposes supported presets and their exact GET parameters.

Supported simple executed-search presets:

```text
Taxony (čeština): searcharea=1
Taxony (všechny jazyky): searcharea=6
Obrázky: searcharea=2
Odkazy a literatura: searcharea=3
Termíny: searcharea=5
Termíny (všechny jazyky): searcharea=9
Biotopy: searcharea=7
Lokality: searcharea=8
Hledat všude: searcharea=100
```

The advanced form also exposes checkbox-level parameters such as
`searchrecords`, `searchvnames`, `searchsynonyms`, and `searchtype`, but those
should only become settings when the intended interaction with `searcharea`
presets is tested.

## UX target

### Selected-text flow

1. User selects text, e.g. `Bombus terrestris`.
2. Android context menu offers **Search BioLib**.
3. Tapping it launches no visible intermediate BioLit screen.
4. BioLit constructs the BioLib URL using the saved search mode.
5. The normal browser opens the search result.
6. BioLit finishes immediately.

Whitespace around selected text should be trimmed. Empty selections should quietly do nothing.

Use Android URI building / query-parameter APIs rather than manually concatenating encoded strings.

### App launcher / settings

Opening BioLit normally should show a minimal settings screen.

For v1, settings need only:

- **Search type** — one selectable mode from the table above.
- Default: **Taxony (čeština)** / `searcharea=1`.

Settings are local-only. No account, telemetry, analytics, cloud storage, permissions, or network API layer is needed.

A future settings screen may include optional switches such as browser handling, so keep search preferences in one obvious place rather than scattering constants around activities.

## Architecture

Keep it deliberately simple:

```text
MainActivity
  -> SettingsFragment
     -> SharedPreferences / AndroidX Preference

SearchActivity
  <- ACTION_PROCESS_TEXT
  <- ACTION_SEND
  -> read saved search mode
  -> build BioLib URI
  -> ACTION_VIEW
  -> finish()

SearchConfig
  -> BioLib endpoint
  -> searcharea values
  -> preference keys/defaults
```

No database, repository layer, dependency injection, networking library, Compose migration, or WebView is justified for v1.

## Android integration details

`SearchActivity` must remain exported and advertise:

```xml
<action android:name="android.intent.action.PROCESS_TEXT" />
<action android:name="android.intent.action.SEND" />
<category android:name="android.intent.category.DEFAULT" />
<data android:mimeType="text/plain" />
```

Read selected text from:

```kotlin
Intent.EXTRA_PROCESS_TEXT
Intent.EXTRA_TEXT
```

Only treat `Intent.ACTION_PROCESS_TEXT` and `Intent.ACTION_SEND` as valid invocations. Ignore unexpected launches safely.

The visible context-menu label should be short. Current target: **🐞 BioLit**.

Do not request Internet permission merely to open the browser: BioLit itself does not make network requests.

## First Codex pass

1. Open the repository and inspect the skeleton before changing architecture.
2. Make the Gradle project build cleanly with the locally available Android/Gradle toolchain.
   - Add/refresh the Gradle wrapper if missing.
   - Adjust plugin/dependency versions only if required by the installed toolchain.
   - Do not upgrade things just because newer versions exist.
3. Verify the package/namespace remains `cz.hanenashi.biolit` unless there is a concrete reason to change it.
4. Verify `SearchActivity` appears as an Android `PROCESS_TEXT` action on a real device/emulator.
5. Verify the default search is exactly `searcharea=1`.
6. Verify changing the setting changes subsequent context-menu searches without restarting the app.
7. Make the settings screen presentable but tiny.
8. Add tests where useful, especially URL/query construction if logic is extracted into a testable helper.
9. Update this file and README if implementation details materially change.

## Recommended cleanup during first pass

The skeleton intentionally prioritizes clarity over polish. Codex may:

- extract URI creation to a small pure/testable helper;
- centralize search modes in an enum/data class rather than parallel string arrays if that reduces duplication;
- localize strings properly;
- add a one-line settings explanation such as "Used by Search BioLib in the text-selection menu";
- add launcher/adaptive icons when artwork exists;
- add a tiny About section with the BioLib.cz destination and app version.

Do not add features merely to make the code look architecturally impressive.

## Possible v1.1 features — not required now

### Multiple context-menu entries

Android can technically expose separate activities such as Taxa / Images / Everywhere, but this would clutter the selection menu. Our chosen design is **one Search BioLib action + search type in app settings**. Keep that unless testing reveals a strong reason otherwise.

### Search language / endpoint

The current endpoint is the Czech BioLib interface. Search mode controls what BioLib searches; for v1 we are not switching `/cz/` versus `/en/` UI language separately.

## Acceptance checklist

Before calling v1 done:

- [x] Project builds from a clean checkout.
- [ ] Debug APK installs on current Android.
- [ ] Selecting ordinary text exposes **Search BioLib** in the selection menu / overflow.
- [x] Default install uses the Czech taxa executed-search preset.
- [x] Query text is preserved correctly, including spaces and non-ASCII characters.
- [x] Leading/trailing whitespace is removed.
- [x] Browser opens BioLib results directly.
- [x] No blank/intermediate BioLit activity flashes visibly during context search if avoidable.
- [x] Settings activity launches normally from the app icon.
- [x] Search type preference contains all known modes listed above.
- [x] Changing search type is respected on the very next search.
- [x] Plain text shared through Android Share opens the same BioLib search.
- [x] No unnecessary permissions.
- [x] No WebView, analytics, ads, account, database, or background service.
- [x] README briefly explains installation/use.

Alpha note: device validation is still pending for APK install and whether each host app exposes Android's `PROCESS_TEXT` menu item.

## Test cases

At minimum manually test:

```text
Bombus terrestris
čmelák zemní
lišaj
Amanita muscaria
  Bombus terrestris  
```

Also test selection from at least:

- Chrome or another Chromium browser
- Firefox, if installed
- a native editable text field / note app

Android apps are free to customize text-selection actions, so record any host app where `PROCESS_TEXT` is not exposed; that is not automatically a BioLit bug.

## Definition of "small"

The ideal v1 is a handful of Kotlin/XML files and starts instantly. If the implementation begins needing diagrams to explain the architecture, something has gone wrong.
