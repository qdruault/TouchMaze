# TouchMaze

Le Projet TouchMaze a été développé au sein de mon cursus d'ingénieur à l'UTC. Il s'agit d'un jeu Android de parcours d'un labyrinthe en coopération à retour haptique. 

## Technologie Tactos

La technologie [Tactos](http://www.intertact.net/?page_id=206) est développée par l'UTC. Il s'agit d'un boitier de retour haptique permettant aux personnes en situation d'handicap visuel de percevoir un environnement numérique. Une cellule braille comportant des picots pouvant se lever est alors en contact avec un doigt de l'utilisateur. Ce boitier est alors collé à l'arrière du téléphone pour permettre une utilisation intuitive.

## Présentation du projet

### But du jeu

Le but est d’avancer dans un labyrinthe pour trouver le trésor qui s’y trouve. Pour que le trésor soit accessible, trois pylônes, dispersés dans le labyrinthe doivent être activés. L'essentiel du jeu n'étant pas visible, nous allons présenter des modèles expliquant ce que peuvent ressentir les deux joueurs.

### Phase d'exploration

* Le guide : Le premier joueur a sur son écran un espace qu’il peut parcourir avec son pouce. Au niveau visuel cet espace est vide, mais lorsqu’il le parcourt, il peut sentir les murs du labyrinthe sur son index grâce aux picots. Il peut également sentir son coéquipier, les pylônes et le coffre (respectivement un cercle, des carrés et un triangle).

![Guide screen](https://github.com/qdruault/TouchMaze/blob/master/img/maze.PNG "Guide screen")

*Noir : les deux joueurs peuvent ressentir ce mur. Bleu : seulement le guide. Orange : seulement l'explorateur*

* L'explorateur : Le second joueur a sur son écran quatre boutons de direction pour se déplacer. Il a également quatre rectangles représentant les murs qui l'entourent. En passant son doigt dessus, il peut alors en ressentir certains grâce au retour haptique. A l'aide des informations qu'il peut déduire et des indications de son coéquipier, il peut donc parcourir le labyrinthe sans encombre. En cas de choc dans un mur, un retour haptique se fait sentir.

![Explorer screen](https://github.com/qdruault/TouchMaze/blob/master/img/explorer.PNG "Explorer screen")

*Ici, l'explorateur ne peut seulement sentir le mur du bas. Le guide doit alors lui indiquer la présence des deux autres murs.*

Lorsque le premier joueur l’a correctement guidé sur une case contenant un pylône, un message apparaît, faisant passer les deux joueurs dans la phase d'énigmes.

### Phase d'énigmes

![Enigma](https://github.com/qdruault/TouchMaze/blob/master/img/enigma.PNG "Enigma")

*Écrans du guide et de l'explorateur. Les symboles en pointillés ne sont pas visibles, mais ressentis grâce aux picots qui se lèvent les uns après les autres pour former une espèce de GIF tactile*

L'explorateur doit alors faire comprendre à son guide le tableau qu'il doit résoudre en lui indiquant les motifs présent et ceux manquants. Une fois que le guide a identifié le tableau à compléter, il lui décrit les formes qu'il ressent aux cases manquantes. L'explorateur doit alors compléter le tableau grâce à son coéquipier pour résoudre cette énigme.

## Notes

Ceci reste un projet scolaire répondant à un cadre et à des délais précis. Beaucoup d'améliorations peuvent être apportées à cette application (ajout d'un chronomètre, gestion de points de vie, torches pour éclairer certains murs, etc.). Merci à l'UTC pour le prêt des modules Tactos qui nous ont permis le développement de cette application innovante.

## Auteurs

Projet développé dans le cadre de l'UV NF28 à l'UTC par Baptiste DE FILIPPIS, Quentin DRUAULT-AUBIN, Théo HORDEQUIN et Thibault MERCIER.
