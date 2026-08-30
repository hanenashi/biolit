# BioLit

Tiny Android helper that adds **Search BioLib** to Android's selected-text context menu.

Select text in an app, choose **Search BioLib**, and BioLit opens a BioLib.cz search in the browser.

The launcher activity is intentionally just a settings screen. The selected search type is stored locally and used by the `PROCESS_TEXT` activity.

## Current default

- Search type: **Taxony (čeština)**
- BioLib parameter: `searcharea=1`
- Endpoint: `https://www.biolib.cz/cz/formsearch/`

See [`battleplan.md`](battleplan.md) for the implementation plan and acceptance criteria.
