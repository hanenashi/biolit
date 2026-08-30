# BioLib Mobile CSS

This folder contains the mobile CSS experiment intended for eventual BioLib.cz
site-side integration.

Install `biolit.user.js` in Tampermonkey or another userscript manager while
testing:

```text
https://raw.githubusercontent.com/hanenashi/biolit/main/web/biolit.user.js
```

The userscript only:

- injects the missing viewport meta tag;
- loads `biolib-mobile.css` from this repository.

The loaded CSS URL is:

```text
https://raw.githubusercontent.com/hanenashi/biolit/main/web/biolib-mobile.css
```

The CSS should remain usable without the userscript once BioLib itself includes:

```html
<meta name="viewport" content="width=device-width, initial-scale=1">
```
