# BioLib mobile CSS plan

## Mission

Build a clean, production-credible responsive/mobile CSS retrofit for BioLib.cz.

The current Stylebot experiment proved that BioLib can be made much more usable on a phone, but the existing CSS is deliberately a rough test harness and is not the final deliverable.

The target is a stylesheet that could realistically be handed to the BioLib admin (Ziasystem) and integrated into BioLib itself with minimal HTML changes.

Important distinction:

- Our userscript is only laboratory equipment for fast testing in Kiwi/Android.
- The real deliverable is site-side responsive CSS plus one tiny required HTML change: a viewport meta tag.
- Do not design the solution around Stylebot, Kiwi quirks, or end users installing anything.

## Current BioLib frontend facts

BioLib currently uses an old fixed-width desktop layout.

Important existing dimensions/classes observed in the live CSS:

- `#displayarea { width: 1000px; }`
- `#screen { width: 787px; float: right; }`
- `#leftmenu { width: 200px; float: left; }`
- `#hp-main { width: 220px; float: left; }`
- `#hp-right { width: 545px; float: right; }`
- `.half { width: 48%; }`
- `.third { width: 33%; }`
- `.twothirds { width: 65%; }`
- several gallery/folder/image layouts also use fixed pixel widths and floats
- `#footbar` is absolutely positioned at the bottom of `#displayarea`

The homepage HTML is old-school but reasonably structured and can be made responsive without rewriting the whole site.

Key homepage blocks include:

- `#topbar`
- `#bllogo`
- `#searchmenu`
- `#topmenu`
- `#screen`
- `#hp-div`
- `#hp-news`
- `#hp-main`
- `#hp-right`
- `#hp-briefnews`
- `#leftmenu`
- `#footbar`

The current BioLib `<head>` has no viewport meta tag.

This is the single biggest blocker to sane mobile CSS because mobile browsers otherwise render the page using a desktop-style layout viewport and then scale it down.

The production HTML should gain:

```html
<meta name="viewport" content="width=device-width, initial-scale=1">
```

For our local test harness, the userscript should inject this tag dynamically if it is missing.

## Why the current Stylebot CSS is not good enough

Current root file:

- `biolib-stylebot-mobile.css`

It was useful for proving the concept, but it currently relies on broad overrides such as:

- forcing huge base font sizes to compensate for the missing viewport
- flattening large groups of elements with `position: static !important`
- applying width resets too broadly
- fixing only some outer widths while old inner desktop columns remain active
- hiding parts of the page before we have decided whether they should instead be responsibly reflowed

This produces several visible failures on phones:

1. Homepage `Novinky` still becomes two narrow desktop columns instead of sane mobile blocks.
2. `#hp-main` / `#hp-right` retain old fixed-width assumptions.
3. The old left sidebar becomes a very narrow tall strip with huge empty space beside it.
4. `#footbar` overlays or appears to follow content because of its absolute positioning.
5. Search controls and typography are oversized because the CSS is compensating for the missing viewport rather than working with a real mobile viewport.
6. Some descendants keep fixed dimensions even after the outer page is made `width:100%`.

Do not keep stacking more patches onto this file blindly.

## Tooling direction

Ditch Stylebot for the BioLib work.

Reuse the basic architecture/pattern from the `hanenashi/cssokoun` repository instead:

- tiny Tampermonkey/userscript seed
- remote asset loaded from GitHub
- CSS stored as a normal repository file
- fast refresh/edit/test loop

However, keep the BioLib implementation inside `biolit`; do not turn `cssokoun` into a multi-site project.

Suggested layout:

```text
web/
  biolit.user.js
  biolib-mobile.css
  README.md
```

The userscript should stay intentionally tiny.

Its jobs should be only:

1. Run on `https://www.biolib.cz/*` (and optionally bare `https://biolib.cz/*` if useful).
2. Inject `<meta name="viewport" content="width=device-width, initial-scale=1">` if missing.
3. Load/inject `web/biolib-mobile.css` from this GitHub repo.
4. Optionally provide a very small enable/disable toggle for testing.
5. Avoid site rewriting, content manipulation, navigation replacement, scraping, or any complex UI.

The CSS must not depend on userscript-specific DOM additions.

The exact same CSS should remain suitable for eventual inclusion in BioLib's own stylesheet.

## CSS implementation strategy

### 1. Preserve desktop BioLib

Do not redesign the desktop site.

Responsive changes should be scoped to a mobile/tablet breakpoint, initially something like:

```css
@media screen and (max-width: 760px) {
    /* mobile rules */
}
```

The exact breakpoint may be adjusted after testing.

Desktop at normal widths should remain visually and functionally unchanged.

### 2. Use sane mobile typography

Once the viewport tag exists, remove the current `24px` base-font hack.

Start from roughly:

```css
html {
    font-size: 16px;
}

body {
    line-height: 1.45;
}
```

Use the native/system font stack only if it materially improves mobile readability without changing desktop identity.

Do not over-modernize the visual design. The goal is usable BioLib, not a redesign contest.

### 3. Flatten old fixed desktop columns on mobile

At mobile width, explicitly neutralize the desktop column system.

Likely targets include:

```css
#displayarea,
#screen,
#leftmenu,
#hp-main,
#hp-right,
.half,
.third,
.twothirds,
.folder-l,
.folder-r,
.image-l,
.image-c,
.image-r
```

For mobile these generally need some combination of:

```css
float: none;
width: 100%;
max-width: 100%;
min-width: 0;
margin-left: 0;
margin-right: 0;
box-sizing: border-box;
```

Do this deliberately per component rather than globally resetting every positioned element.

### 4. Homepage should become a real single-column page

`body.hp` is available and should be used to scope homepage-specific fixes.

Important homepage behavior:

- intro block full width
- mapping block full width
- the two `Novinky` entries stack vertically on mobile
- `Obrázek dne` and statistics should no longer occupy a tiny fixed sidebar column
- `Kde začít?` and tips should be full-width readable sections
- short change/news entries should stack normally
- no narrow 200px sidebar surrounded by empty blue space

Prefer normal document flow.

Avoid using absolute positioning to create a new mobile layout.

### 5. Decide what to do with `#leftmenu`

The current experiment demonstrates that merely changing its width is not enough.

Two acceptable strategies:

A. Stack it as a normal full-width block below the homepage content.

B. Hide it on `body.hp` if all important mobile navigation/search functionality remains available elsewhere.

Do not permanently hide it site-wide without checking pages that rely on it.

For first production-oriented CSS, prefer stacking over destructive hiding unless testing shows the sidebar is redundant on the homepage.

### 6. Fix the footer properly

Original BioLib has `#footbar` absolutely positioned.

On mobile it should return to normal document flow:

```css
#footbar {
    position: static;
    clear: both;
    width: 100%;
    box-sizing: border-box;
}
```

Check whether `#pagecleaner` remains necessary after this. If it is only a desktop spacer, hide/remove its mobile layout effect.

Footer must never overlay content.

### 7. Search header

The homepage/global search form is important and should stay prominent.

BioLib's current search form contains:

- hidden `action=execute`
- language flags
- search type select
- text input
- submit button

On phone:

- search controls should use available width
- target about 44-48px minimum tap height
- avoid giant fields
- preserve all existing form functionality
- do not rebuild search with JavaScript

The search form should remain functional with keyboard submission as well as the OK button.

### 8. Images

BioLib contains many images with explicit HTML width/height attributes.

Provide a safe mobile constraint such as:

```css
img {
    max-width: 100%;
    height: auto;
}
```

But never globally force `width:100%`, because icons, flags, avatars and tiny navigation graphics must remain small.

Later add component-specific gallery rules if necessary.

### 9. Forms and tables

BioLib contains old form/table layouts with explicit widths such as `.leftcol` and `.rightcol`.

Mobile pass must inspect at least:

- advanced search form
- login form
- settings/forms
- taxon/detail pages
- lists/tables

Do not assume homepage-only CSS is enough.

For forms, labels/inputs may need to stack vertically on narrow screens.

For real tabular data, preserve table meaning. Prefer horizontal scrolling over destroying table structure if a table cannot sensibly collapse.

### 10. Navigation

The current experiment hides `#topmenu`, which is acceptable for testing but not necessarily for a production patch.

Before finalizing:

- inspect whether mobile users can still reach system, articles, localities, biotopes, gallery, encyclopedia, references and discussion
- decide whether top navigation should wrap, collapse, become horizontal scrolling, or be selectively hidden
- avoid building a JavaScript hamburger menu unless clearly necessary

Try CSS-only responsive navigation first.

### 11. Avoid broad destructive resets

Do not use giant selector groups that force all of these indiscriminately:

```css
position: static !important;
float: none !important;
width: 100% !important;
```

Use targeted component rules.

Use `!important` only where required to override BioLib's existing CSS specificity or inline styles.

The eventual production patch should be readable enough that BioLib's admin can understand what it changes.

## Development order

### Phase 1 - test harness

1. Add `web/biolit.user.js`.
2. Add `web/biolib-mobile.css`.
3. Userscript injects viewport meta.
4. Userscript loads CSS from repo.
5. Confirm quick update/refresh workflow in Kiwi.
6. Keep existing `biolib-stylebot-mobile.css` temporarily as reference; do not delete it until new CSS reaches parity.

### Phase 2 - homepage foundation

Fix only the fundamentals first:

1. viewport
2. normal font scale
3. `#displayarea` / `#screen` responsive width
4. stack `.half`
5. stack `#hp-main` / `#hp-right`
6. deal with `#leftmenu`
7. fix footer flow
8. constrain images
9. make search form sane

Do not spend time on visual polish before these structural problems are solved.

### Phase 3 - representative BioLib pages

Test at least:

- homepage `/`
- taxon search results
- a taxon detail page
- gallery/image page
- advanced search form
- discussion/forum page if accessible
- login/form page
- taxonomy tree/system page

Add page/component-specific CSS only after observing a real failure.

### Phase 4 - production cleanup

Once behavior is good:

1. remove test-only hacks
2. reduce unnecessary `!important`
3. organize CSS into commented sections
4. confirm desktop unaffected
5. confirm common tablet width
6. confirm current Android portrait
7. confirm Android landscape
8. make the production requirement explicit in README: viewport meta + responsive CSS

## Acceptance criteria

Do not call the CSS complete until:

- BioLib loads at native mobile scale with a proper viewport.
- No horizontal page overflow on representative pages, except intentionally scrollable data tables.
- Homepage does not retain narrow desktop columns.
- `Novinky` stacks into readable blocks.
- `#hp-main`, `#hp-right` and sidebar content are usable at phone width.
- Footer never overlays content.
- Search controls are readable, tappable and functional.
- Text does not need artificial 24px scaling.
- Images never blow out the viewport.
- Czech text and links remain untouched.
- Existing BioLib behavior is preserved.
- Desktop layout remains unchanged outside the responsive breakpoint.
- CSS is understandable and production-credible, not a pile of browser-extension hacks.
- Userscript can be removed entirely once BioLib itself adds viewport meta and the CSS.

## Non-goals

Do not:

- rewrite BioLib as a SPA
- introduce React/Bootstrap/Tailwind/etc.
- put BioLib into a custom WebView
- make BioLit Android responsible for rendering BioLib
- scrape and reconstruct BioLib pages
- change BioLib backend/search behavior
- redesign the whole desktop site
- depend on Stylebot
- require end users to install our userscript

## Relationship to the Android BioLit app

Keep this web work conceptually separate from the Android app.

Android BioLit currently provides selected-text / Share -> BioLib search convenience.

The mobile CSS work is a parallel effort to make the destination BioLib pages usable on phones.

Sharing the same repository is convenient, but do not couple Android code to the userscript/CSS loader.

## Final deliverables envisioned

Development/testing assets in this repo:

```text
web/biolit.user.js
web/biolib-mobile.css
web/README.md
```

Production handoff for BioLib admin:

1. Add to BioLib HTML `<head>`:

```html
<meta name="viewport" content="width=device-width, initial-scale=1">
```

2. Integrate the responsive rules from `web/biolib-mobile.css` into BioLib's site CSS or load it as an additional stylesheet.

That is the real destination of this work.
