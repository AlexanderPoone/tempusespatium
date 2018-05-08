SPARQL

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

```java
import com.bordercloud.sparql.Endpoint;
import java.util.ArrayList;
import java.util.HashMap;

public class Main {

    public static void main(String[] args) {
        try {
            Endpoint sp = new Endpoint("https://query.wikidata.org/sparql", false);

            String querySelect = "#defaultView:Timeline\n" +
                "SELECT DISTINCT ?eventLabel ?date ?dateLabel ?coord ?img\n" +
                "WHERE\n" +
                "{\n" +
                "  ?event wdt:P31+ wd:Q178561 .\n" +
                "\n" +
                "  OPTIONAL { ?event wdt:P585+ ?date }\n" +
                "  ?event wdt:P18+ ?img.\n" +
                "    ?event wdt:P625 ?coord .\n" +
                "\n" +
                "FILTER(YEAR(?date) > 1900).\n" +
                "\n" +
                "SERVICE wikibase:label { bd:serviceParam wikibase:language \"en\" }\n" +
                "   FILTER(EXISTS {\n" +
                "   ?event rdfs:label ?lang_label.\n" +
                "   FILTER(LANG(?lang_label) = \"en\")\n" +
                " })\n" +
                "}\n" +
                "ORDER BY DESC(?dateLabel)";

            HashMap rs = sp.query(querySelect);
            printResult(rs,30);

        }catch(EndpointException eex) {
            System.out.println(eex);
            eex.printStackTrace();
        }
    }

    public static void printResult(HashMap rs , int size) {

      for (String variable : (ArrayList) rs.get("result").get("variables")) {
        System.out.print(String.format("%-"+size+"."+size+"s", variable ) + " | ");
      }
      System.out.print("\n");
      for (HashMap value : (ArrayList>) rs.get("result").get("rows")) {
        //System.out.print(value);
        /* for (String key : value.keySet()) {
         System.out.println(value.get(key));
         }*/
        for (String variable : (ArrayList) rs.get("result").get("variables")) {
          //System.out.println(value.get(variable));
          System.out.print(String.format("%-"+size+"."+size+"s", value.get(variable)) + " | ");
        }
        System.out.print("\n");
      }
    }
}
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

```java
import com.bordercloud.sparql.Endpoint;
import java.util.ArrayList;
import java.util.HashMap;

public class Main {

    public static void main(String[] args) {
        try {
            Endpoint sp = new Endpoint("https://query.wikidata.org/sparql", false);

            String querySelect = "#defaultView:Graph\n" +
                "SELECT DISTINCT ?country ?countryLabel ?capital ?capitalLabel ?flagLabel ?armsLabel ?imgLabel\n" +
                "WHERE\n" +
                "{\n" +
                "  ?country wdt:P31 wd:Q3624078 .\n" +
                "  #not a former country\n" +
                "  FILTER NOT EXISTS {?country wdt:P31 wd:Q3024240}\n" +
                "  #and no an ancient civilisation (needed to exclude ancient Egypt)\n" +
                "  FILTER NOT EXISTS {?country wdt:P31 wd:Q28171280}\n" +
                "   ?country wdt:P36 ?capital.\n" +
                "     ?country wdt:P41 ?flag.\n" +
                "   ?country wdt:P94 ?arms.\n" +
                "   ?capital wdt:P18 ?img.\n" +
                "\n" +
                "  SERVICE wikibase:label { bd:serviceParam wikibase:language \"en\" }\n" +
                "}\n" +
                "ORDER BY ?countryLabel ";

            HashMap rs = sp.query(querySelect);
            printResult(rs,30);

        }catch(EndpointException eex) {
            System.out.println(eex);
            eex.printStackTrace();
        }
    }

    public static void printResult(HashMap rs , int size) {

      for (String variable : (ArrayList) rs.get("result").get("variables")) {
        System.out.print(String.format("%-"+size+"."+size+"s", variable ) + " | ");
      }
      System.out.print("\n");
      for (HashMap value : (ArrayList>) rs.get("result").get("rows")) {
        //System.out.print(value);
        /* for (String key : value.keySet()) {
         System.out.println(value.get(key));
         }*/
        for (String variable : (ArrayList) rs.get("result").get("variables")) {
          //System.out.println(value.get(variable));
          System.out.print(String.format("%-"+size+"."+size+"s", value.get(variable)) + " | ");
        }
        System.out.print("\n");
      }
    }
}
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
