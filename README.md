# This repo is being revamped
*Learn any topic (without losing inner peace)*
```html
/* PaxAcademica plc */
<style>
  <!-- fade in/out goes here -->
</style>
<p><i class="fa-scroll fa-xl fa-fw"></i>&nbsp;&nbsp;Learn anything about <tt id="topic"></tt></p>
<p><i class="fa-globe fa-xl fa-fw"></i>&nbsp;&nbsp;Learn in <tt id="lingo"></tt></p>
<script>
  const lingos = ['catala', 'francais', 'espanol', '日本語', '中文'];
  const topics = ['Phonology', 'Medieval History', 'Forestry', 'Robotics'];
  let cnt = 0;
  let fadeIn = function() {
    querySelector('#lingo').innerText = lingos[cnt % lingos.length];
    querySelector('#topic').innerText = topics[cnt % topics.length];
    cnt += 1;
  };
  fadeIn();
  setInterval(fadeIn, 3000);
</script>
```

[Podfile](ios/Tempus%20E%20Spatium/Podfile)
* Sounds (`correctDing.wav`, `incorrectBuzz.wav`, `scoreIncreaseCashierSound.wav`, `trumpetSolSolDo2Yay.wav`): OK, I made them on MuseScore (exported to mp3), thus no copyright materials needed https://pixabay.com/sound-effects/search/whoosh/
* Select relaxing music while learning `[pull down: none, Green Hills, Pagodes, Soirée dans Grenade] [Custom]`: OK, ditto [app/src/main/res/raw](app/src/main/res/raw) | [ios/Tempus E Spatium/Tempus E Spatium](ios/Tempus%20E%20Spatium/Tempus%20E%20Spatium)
<!-- bgmusic_green_hills 青山 -->
<!-- bgmusic_pagodes 塔樓 -->
<!-- bgmusic_soiree_dans_grenade 格拉納達黃昏 -->
* Website version (Vue3,Vuetify,[Flario](https://store.vuetifyjs.com/products/flairo-theme-pro) [essays = postings]: keep GitHub, X [change from bird to X https://fontawesome.com/icons/x-twitter], YouTube and WhatsApp [+44] [https://fontawesome.com/icons/whatsapp]): front page started (`LEARN ANY TOPIC (WITHOUT LOSING INNER PEACE)`) but not finished, merged with personal site & redirect to GooglePlay/AppStore using promo images
```html
<i class="fa-brands fa-x-twitter fa-xl fa-fw"></i> | <i class="fa-brands fa-whatsapp fa-xl fa-fw"></i>
```

TODOs:
1. **WikiProject sets** Vs. **custom sets of articles** (auto-complete just like Wikipedia)
```
[Refresh all articles]
Set name: [_2020-01-01 00:02:04______] <- cannot be empty !

// This is a RecyclerView:

  [_______________________________________________]
  [_(imgPicasso) Kalman filter__(smallDesc)_______] (-) <- read-only text field
  [_(imgPicasso) Hungarian algorithm__(smallDesc)_] (-)
// If time is allowed, disable shuffle, drag to move.

[R.string.ok] [R.string.cancel]
```
Also Quizlet.

Saved to SQLite/FMDB [ios/Tempus E Spatium/Tempus E Spatium/HighscoresDBManager.swift](ios/Tempus%20E%20Spatium/Tempus%20E%20Spatium/HighscoresDBManager.swift):

```swift
let TABLE_SETS = "sets"
let TABLE_SET_ARTICLES = "setArticles"
let TABLE_CONNECTING_WALL = "connectingWall"
```

Table `sets`: setname | historicHigh | playedTimes | lastPlayedDate | creationDate | modifyDate

Table `setArticles`: article | setName | lastRefreshedDate | creationDate

Table `connectingWall`:
```sql
INSERT INTO connectingWall ('clue1', 'clue2', 'clue3', 'clue4', 'clue5', 'clue6', 'clue7', 'clue8', 'clue9', 'clue10', 'clue11', 'clue12', 'clue13', 'clue14', 'clue15', 'clue16', 'conn1', 'conn2', 'conn3', 'conn4') VALUES ('clue1', 'clue2', 'clue3', 'clue4', 'clue5', 'clue6', 'clue7', 'clue8', 'clue9', 'clue10', 'clue11', 'clue12', 'clue13', 'clue14', 'clue15', 'clue16', 'conn1', 'conn2', 'conn3', 'conn4')
```

2. Themes:
  * Relaxed Bears (bg: #C78311, #FEF0CB; txt: #FDD000, #F19EB4) <!-- 懶熊 -->
  * Fire & Ice (#FD7A2D, #B1F3FC; bg/txt opposite) <!-- 冰火 -->
  * Zen (#282631 off-black, #FEF8DE off-white) <!-- Yin yang-->  <!-- 禪 -->
  * Spring (#FF44BB, #FFB847) <!-- 春 -->
  * Summer (#46A266, #BADC66) <!-- 夏 -->
  * Autumn (#FF4300, #FFB800) <!-- 秋 -->
  * Winter (#0041DE, #00B9A2) <!-- 冬 -->
3. Custom brackets...
  * Default: highlight all for you `[Reset to default]` <!-- 重設 -->
  * Highlight text you want to make brackets -> `[Make bracket|Cancel]`
4. Switch to `view binding`
5. Add IPA game in custom mode -- similar vowels, diphthongs, consonants (e.g., sibilants) [Japanese would be easier to make though]
6. 'Series' on WikiData? (under research; `GestureDetector.SimpleOnGestureListener` maybe? I can use free versions of Symphony/Piano Concerto No. X)
```java
Collections.shuffle(Arrays.asList(integerArray));

List<String> mSelectedBlks = new ArrayList<>();
List<String> mLstGrp1;
List<String> mLstGrp2;
List<String> mLstGrp3;
List<String> mLstGrp4;
AudioPlayer mAudioPlayer = new AudioPlayer();
int mLives = 5;
RecyclerView mHeartRecyclerView = this.findViewById(R.id.hearts);
Fragment mPlayer1View = this.findViewById(R.id.player1), mPlayer2View = this.findViewById(R.id.player2), mView = this.findViewById(R.id.view);

@onClick
void onClickListener(Block block) {
  mSelectedBlks.append(block);
  if (mSelectedBlks.length() == 4) {
    // check if correct
    boolean correct = false;
    if (mLstGrp1.containsAll(mSelectedBlks) || mLstGrp2.containsAll(mSelectedBlks) || mLstGrp3.containsAll(mSelectedBlks) || mLstGrp4.containsAll(mSelectedBlks)) {
      correct = true;
    }
    if (correct) {
      // play shift animation
      for (var selected: mSelectedBlks) {
        selected.clickable = false;
      }
      mStockpiled += 1;
      if (mStockpiled == 4) {
        mAudioPlayer.play(R.raw.shave_and_a_haircut_two_piece);

        // winning condition
        mPlayer1View.lock();
        mPlayer2View.lock();
      }
    } else {
      // incorrect
      mLives -= 1;
      mHeartRecyclerView.getElement(mLives).setDrawable(R.drawable.heart_empty);    // starts from 0
      if (mLives == 0) {
        mView.clickable = false;  // controller.mClickArea.isUserInteractionEnabled = false
      }
    }
  }
  // reset block colour
  for (var bk: mBlks) {
    if (bk.clickable) {
      bk.setColor(R.color.honeydew);
    }
  }
}
```
Wall Solved Sound Effect: `shavedAndAHaircutTwoPiece.mp3`
  * Five `heart_full.jpg` (Vs. `heart_empty.jpg`) at the beginning (horizontal `RecyclerView`, dock at top-right corner)
  * Three colours: ~~`selected` (red), `unselected` (maybe honeydew instead of soda blue?), and `solved` (maybe yellow?)~~
    * [all radial gradient]
    * #1A7D6E (teal, solved)
    * #032960 (navy, selected)
    * #ACD7FB (sky, unselected)
  * Style: Luminescent shading // Rounded corners // Dosis font
  * Translation animation + Set colour (tween animation?) and texts on the fly
  * ~~Question bank schema? <!-- Copy for goodness sake -->~~ // ~~[DB Browser for SQLite download](https://sqlitebrowser.org/)~~ (Done.)
7. `Translations (add more locales!) + SQLite tables initial values + XPath` should compile to both OSes
  * `compile_translations.py`
```py
import pandas as pd
from xml.dom.minidom import parseString

ANDROID_OUT_PATH = 'app/src/java/hk/edy/cuhk/cse/tempusespatium/res/values/strings.xml'
IOS_OUT_PATH = '"ios/Tempus E Spatium/Tempus E Spatium/Base.lproj/Localizable.strings"'

# Android strings
df = pd.read_excel('locale.xlsx')
android = '<?xml version="1.0" encoding="utf-8"?><resources>'
for i,r in df.iterrows():
  android += f'<string name="{r["key"]}">{r["en"]}</string>'
android += '</resources>'

dom = parseString(android)
android = dom.toprettyxml()
print(android)
with open(ANDROID_OUT_PATH, 'w', encoding='utf-8') as f:
  f.write(android)

# TODO: iOS strings
ios = ''
with open(IOS_OUT_PATH, 'w', encoding='utf-8') as f:
  f.write(ios)

print(ios)
```
  * `compile_sqlite.py`
```py3
import pandas as pd

# TODO
```
  * `compile_xpath.py`
```py3
import pandas as pd
from selenium import webdriver
from selenium.webdriver.common.action_chains import ActionChains
from selenium.webdriver.common.keys import Keys

# TODO
```
Ono:
https://github.com/AlexanderPoone/tempusespatium/blob/aac6f923cf0172410501aac7ab3080312bb6d8ca/ios/Tempus%20E%20Spatium/Tempus%20E%20Spatium/ExteriorViewController.swift#L235
------------------------

* Rebranding
* UI Overhaul
* Setting up for a more unified experience
* Algorithm Overhaul (add topic modelling)

------------------------------

### Unit Test: Are WikiProjects' XPaths Working?
Need a table for these XPaths. TODO.

lang | delimitedXpaths

```py
for s in delimitedXpath.split('|'):
    err = driver.execute_script(s)    # How deep?
```
------------------------------

Tempus E Spatium (pronounced _TEM-poo-se-SPA-ti-oom_) is _Latin for space and time_. It is an edutainment app for adult learners by exploiting the Semantic Web.

[README](https://github.com/SoftFeta/tempusespatium/raw/master/shitty_ppt.pptx)

[README](https://github.com/SoftFeta/tempusespatium/raw/master/shitty_ppt.pptx)

[README](https://github.com/SoftFeta/tempusespatium/raw/master/shitty_ppt.pptx)

[README](https://github.com/SoftFeta/tempusespatium/raw/master/shitty_ppt.pptx)

[README](https://github.com/SoftFeta/tempusespatium/raw/master/shitty_ppt.pptx)

[README](https://github.com/SoftFeta/tempusespatium/raw/master/shitty_ppt.pptx)

[README](https://github.com/SoftFeta/tempusespatium/raw/master/shitty_ppt.pptx)

[README](https://github.com/SoftFeta/tempusespatium/raw/master/shitty_ppt.pptx)

[README](https://github.com/SoftFeta/tempusespatium/raw/master/shitty_ppt.pptx)

[README](https://github.com/SoftFeta/tempusespatium/raw/master/shitty_ppt.pptx)











###### Wow, GitHub supports SPARQL syntax highlighting...
```sparql
#defaultView:Timeline
SELECT DISTINCT ?eventLabel ?date ?dateLabel ?coord ?img
WHERE
{
  { ?event wdt:P31* wd:Q178561. } UNION
  { ?event wdt:P31* wd:Q81672. }
  
  ?event wdt:P585* ?date .
  ?event wdt:P18+ ?img.
  OPTIONAL { ?event wdt:P625 ?coord }

FILTER(YEAR(?date) > 1900).

SERVICE wikibase:label { bd:serviceParam wikibase:language "en" }
   FILTER(EXISTS {
   ?event rdfs:label ?lang_label.
   FILTER(LANG(?lang_label) = "en")
 })
}
ORDER BY DESC(?dateLabel)
```

```sparql
#defaultView:Graph
SELECT DISTINCT ?country ?countryLabel ?country_EN ?country_DE ?country_FR ?capital ?capitalLabel ?flagLabel ?armsLabel ?imgLabel
WHERE
{
  ?country wdt:P31 wd:Q3624078 .
  #not a former country
  FILTER NOT EXISTS {?country wdt:P31 wd:Q3024240}
  #and no an ancient civilisation (needed to exclude ancient Egypt)
  FILTER NOT EXISTS {?country wdt:P31 wd:Q28171280}
   ?country wdt:P36 ?capital.
     ?country wdt:P41 ?flag.
   ?country wdt:P94 ?arms.
   ?capital wdt:P18 ?img.

     SERVICE wikibase:label { bd:serviceParam wikibase:language "en".
     }
     SERVICE wikibase:label { bd:serviceParam wikibase:language "en".
            ?country rdfs:label ?country_EN.
     } hint:Prior hint:runLast false.
     SERVICE wikibase:label { bd:serviceParam wikibase:language "de".
            ?country rdfs:label ?country_DE.
     } hint:Prior hint:runLast false.
     SERVICE wikibase:label { bd:serviceParam wikibase:language "fr".
            ?country rdfs:label ?country_FR.
     } hint:Prior hint:runLast false.}
ORDER BY ?countryLabel 
```

```sparql
#defaultView:Table
SELECT DISTINCT ?country ?countryLabel ?country_EN ?country_FR ?anthemLabel ?anthem_FR ?audioLabel
WHERE
{
  ?country wdt:P31 wd:Q3624078 .
  #not a former country
  FILTER NOT EXISTS {?country wdt:P31 wd:Q3024240}
  #and no an ancient civilisation (needed to exclude ancient Egypt)
  FILTER NOT EXISTS {?country wdt:P31 wd:Q28171280}
   ?country wdt:P85 ?anthem.
   ?anthem wdt:P51 ?audio.

     SERVICE wikibase:label { bd:serviceParam wikibase:language "en".
     }
     SERVICE wikibase:label { bd:serviceParam wikibase:language "en".
            ?country rdfs:label ?country_EN.
     } hint:Prior hint:runLast false.
     SERVICE wikibase:label { bd:serviceParam wikibase:language "fr".
            ?country rdfs:label ?country_FR.
     } hint:Prior hint:runLast false.
     SERVICE wikibase:label { bd:serviceParam wikibase:language "fr".
            ?anthem rdfs:label ?anthem_FR.
     } hint:Prior hint:runLast false.
}
ORDER BY ?countryLabel
```

<!--
# Rampant Sphinges
Papyrus font
Spectres/Sand dunes of the Sphinges

Hello professor, could you comment on my project idea? It is an educational, labyrinthine board game for adults and young adults. Let's call it 'Rampant Sphinges'. The main idea is scraping Wikipedia and minimal parsing to generate adult-level questions about various fields.

An adventurer/archaeologists on a camelback got lost in the Valley of the Dead in Egypt, which is maze-like with many divergent paths. Having five HP (four limbs and the main body), the adventurer must flee from the sand dunes (desert hills). The camel may walk one tile, or prance through two tiles. As such, the user has a slight freedom to pick the subject they liked, but they do not know the actual question. A sphinx pops up and blocks the way. Akin to legends, the sphinx ask the player to answer questions to let him go. Once the question is revealed, it cannot be cancelled. If the player answers incorrectly, the sphinx will eat one limb and he will lose one health.

At the start, the players choose three subjects which is available as a WikiProject. The scraper will download relevant articles parsed by XPath. This means, each question is generated on the fly. Titles will be coloured in three ways, each representing one of the three subjects. The order is randomised.
Question types include : 
1. Complete the sentence. Each question of this type is generated from one paragraph. They take advantage of the fact that hyperlinked words are more important than those that are not. They appear in two foms. In the first form, some letters will be given, or else it would be too difficult to guess: let's say the word is 'palimpsest', p________t. In the second form, an anagram will be provided instead.
2. Map pinning. The target could be a historic event, birthplace of a celebrity, or a heritage site. for instance Where did the Battle of a Hastings take place? (I will use WebKit to hold Google Maps API stuff and JavaScript)

Bridge tiles are more challenging, as the adventurer has to confront the crocodile god Sobek, who is mad at the protagonists for looting artefacts.

To introduce more RNG, quicksand (25%) and breaking bridges (50%) has a chance of instantly losing one life and has no questions. They may appear adjacently and hence unavoidable. The adventurer should choose the safest path. Some tiles the adventurer may pick up ankhs, to lower the chance of falling in quicksand and falling bridges, scarabs to skip a question, and dead bushes to make the camel prance three tiles. The ankh,  scarab, and dead bushes will be consumed (breaks, vanishes, digested) after use.

I wonder if I can add more question types to the game. Perhaps something fun like 'draw the border between England and Scotland' (the problem is these are not general questions but geography questions) and 'rearrange symbols to form IPAs of a difficult English word' (given a usage frequency database).

I am planning to implement it using Python and SDL. Is there anything that I should be aware of, like shortcuts / pitfalls? Thank you!
-->
