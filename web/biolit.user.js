// ==UserScript==
// @name         BioLit BioLib Mobile CSS
// @namespace    https://github.com/hanenashi/biolit
// @version      0.1.0
// @description  Inject BioLib mobile viewport and development CSS.
// @match        https://www.biolib.cz/*
// @match        https://biolib.cz/*
// @grant        GM_addStyle
// @connect      raw.githubusercontent.com
// @run-at       document-start
// ==/UserScript==

(function () {
  "use strict";

  const cssUrl =
    "https://raw.githubusercontent.com/hanenashi/biolit/main/web/biolib-mobile.css";

  function ensureViewport() {
    const existing = document.querySelector("meta[name='viewport']");
    if (existing) {
      return;
    }

    const viewport = document.createElement("meta");
    viewport.name = "viewport";
    viewport.content = "width=device-width, initial-scale=1";
    const parent = document.head || document.documentElement;
    parent.appendChild(viewport);
  }

  function addCss(css) {
    if (typeof GM_addStyle === "function") {
      GM_addStyle(css);
      return;
    }

    const style = document.createElement("style");
    style.textContent = css;
    document.head.appendChild(style);
  }

  function loadCss() {
    fetch(cssUrl, { cache: "no-store" })
      .then((response) => {
        if (!response.ok) {
          throw new Error(`CSS load failed: ${response.status}`);
        }
        return response.text();
      })
      .then(addCss)
      .catch((error) => {
        console.warn("[BioLit] Could not load BioLib mobile CSS", error);
      });
  }

  ensureViewport();
  loadCss();
})();
