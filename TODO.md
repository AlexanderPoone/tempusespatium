[Podfile](ios/Tempus%20E%20Spatium/Podfile)
* Sounds (`correctDing.wav`, `incorrectBuzz.wav`, `scoreIncreaseCashierSound.wav`, `trumpetSolSolDo2Yay.wav`): OK, I made them on MuseScore (exported to mp3), thus no copyright materials needed https://pixabay.com/sound-effects/search/whoosh/
* Select relaxing music while learning `[pull down: none, Green Hills, Derry Air, L'hiver] [Custom]`: OK, ditto<br>(Resources folders: [app/src/main/res/raw](app/src/main/res/raw) | [ios/Tempus E Spatium/Tempus E Spatium](ios/Tempus%20E%20Spatium/Tempus%20E%20Spatium))
```xml
<!-- bgmusic_green_hills 青山 -->
<!-- bgmusic_derry_air 倫敦德里小調 -->
<!-- bgmusic_lhiver 冬 -->
```

TODOs:
1. Support of other Wikimedia Projects + Quizlet
2. **Custom fine-grained syllabus** (one set can include multiple **WikiProjects**; and you can remove articles from a WikiProject)
```java
/* NewFineGrainedSetActivity.dart */

[Refresh all articles]
Set name: [_2020-01-01 00:02:04______] // ← cannot be empty !

// This is a `RecyclerView` of my `BeninView`

  [__Autocomplete-dropdown_______________________________________________________]
  [_R.id.squarePreview | Kalman filter__(R.id.smallDesc)_______] (-)  // ← read-only text field, instead of label
  [_R.id.squarePreview | Hungarian algorithm__(R.id.smallDesc)_] (-)
// If time is allowed, disable shuffle, drag to move article order.

[R.string.ok] [R.string.cancel]
```
```java
/* BeninView.dart */

BeninView {    // recycled by RecyclerView
  mSquarePreview = findViewById(R.id.squarePreview);
  Glide.with(this).load().into(mSquarePreview);
}
```

Saved to SQLite (FMDB [ios/Tempus E Spatium/Tempus E Spatium/HighscoresDBManager.swift](ios/Tempus%20E%20Spatium/Tempus%20E%20Spatium/HighscoresDBManager.swift) → sqflite):

```swift
let TABLE_SETS = "sets"
let TABLE_SET_ARTICLES = "setArticles"
let TABLE_CONNECTING_WALL = "connectingWall"
```

Table `sets`: 
setName | historicHigh | playedTimes | lastPlayedDate | creationDate | modifyDate
-------|--------------|-------------|----------------|---------------|------------
String(PK) | Int | Int | Timestamp | Timestamp | Timestamp

Table `setArticles`: 
setName | article | lastRefreshedDate | creationDate | source
-------|--------------|-------------|----------------|----------
String(PK) | String(PK) | Timestamp | Timestamp | URL (for Revision)

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
3. **Custom brackets** (this requires replacing `<a>` with some `<span class="">` and adding `<style>` for the highlight effect, and a bottom popup)
    * Default: highlight all for you `[Reset to default]` <!-- 重設 -->
    * Highlight text you want to make brackets -> `[Make bracket|Cancel]`
4. Switch to `view binding`
5. Add IPA game in custom mode -- similar vowels, diphthongs, consonants (e.g., sibilants) [Japanese would be easier to make though]
6. 'Series' on WikiData? (under research; `GestureDetector.SimpleOnGestureListener` maybe? I can use free versions of Symphony/Piano Concerto No. X)
```java
public class Fragment {
  Block[] mViews = { findViewById(R.id.block1),
      findViewById(R.id.block2),
      findViewById(R.id.block3),
      findViewById(R.id.block4),
      findViewById(R.id.block5),
      findViewById(R.id.block6),
      findViewById(R.id.block7),
      findViewById(R.id.block8),
      findViewById(R.id.block9),
      findViewById(R.id.block10),
      findViewById(R.id.block11),
      findViewById(R.id.block12),
      findViewById(R.id.block13),
      findViewById(R.id.block14),
      findViewById(R.id.block15),
      findViewById(R.id.block16) }; // fixed sized, use array instead of list

  int[] mRandOrder = Collections.shuffle(Arrays.asList(Integer.Range(16))); // fixed sized, array 1 to 16

  List<String> mSelectedBlks = new ArrayList<>();
  // await
  String[] mClues = database.rawQuery("SELECT * FROM connectingWall ORDER BY RANDOM() LIMIT 1;"); // get random row from
                                                                                                  // DB
  List<String> mLstGrp1 = Arrays.copyOfRange(mClues, 0, 4);
  List<String> mLstGrp2 = Arrays.copyOfRange(mClues, 4, 8);
  List<String> mLstGrp3 = Arrays.copyOfRange(mClues, 8, 12);
  List<String> mLstGrp4 = Arrays.copyOfRange(mClues, 12, 16);
  AudioPlayer mAudioPlayer = new AudioPlayer();
  int mLives = 5, mGrpsDone = 0;
  RecyclerView mHeartRecyclerView = this.findViewById(R.id.hearts);
  Fragment mPlayer1View = this.findViewById(R.id.player1), mPlayer2View = this.findViewById(R.id.player2),
      mView = this.findViewById(R.id.view);

  // TODO: Animation -- z-rotate the wall 180 degrees
  void newWall() {
    mLives = 5;
    mGrpsDone = 0;
    for (int x = 0; x < 16; x++) {
      mViews[mRandOrder[x]].setText(mClues[x]);
    }
  }

  @onClick
  void onClickListener(Block block) {
    mSelectedBlks.append(block);
    if (mSelectedBlks.length() == 4) {
      // check if correct
      boolean correct = false;
      if (mLstGrp1.containsAll(mSelectedBlks) || mLstGrp2.containsAll(mSelectedBlks)
          || mLstGrp3.containsAll(mSelectedBlks) || mLstGrp4.containsAll(mSelectedBlks)) {
        correct = true;
      }
      if (correct) {
        // play shift animation
        for (Block selected : mSelectedBlks) {
          selected.clickable = false;
        }
        mGrpsDone += 1;
        if (mGrpsDone == 4) {
          mAudioPlayer.play(R.raw.shave_and_a_haircut_two_piece);

          // winning condition
          mPlayer1View.lock();
          mPlayer2View.lock();
        }
      } else {
        // incorrect
        mLives -= 1;
        mHeartRecyclerView.getElement(mLives).setDrawable(R.drawable.heart_empty); // starts from 0
        if (mLives == 0) {
          mView.clickable = false; // controller.mClickArea.isUserInteractionEnabled = false
        }
      }
    }
    // reset block colour
    for (Block bk : mBlks) {
      if (bk.clickable) {
        bk.setColor(R.color.honeydew);
      }
    }
  }
}
```
Bottom navy blue bar (Bold Dosis):
* State1: [_TextEdit______] [_->_]
* State2: [_TickOrCross_] Real Theme

Wall Solved Sound Effect: `shavedAndAHaircutTwoPiece.mp3` (MuseScore)
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
  * `compile_sqlite.py`
```py3
import pandas as pd

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
-----|----------------
String(PK) | `[String, String, String...]`
  * `compile_xpath.py`
```py3
import pandas as pd
from selenium import webdriver
from selenium.webdriver.common.action_chains import ActionChains
from selenium.webdriver.common.keys import Keys

for s in delimitedXpath.split('|'):
    err = driver.execute_script(s)    # How deep?
```
