# SimonSaysM2

![Kotlin](https://img.shields.io/badge/kotlin-%237F52FF.svg?style=for-the-badge&logo=kotlin&logoColor=white) ![Android Studio](https://img.shields.io/badge/Android%20Studio-3DDC84.svg?style=for-the-badge&logo=android-studio&logoColor=white)

> Projet réalisé dans le cadre du cours Hypermédia

Application de mémorisation de couleurs.

## Lancement d'une partie

L'utilisateur doit reproduire une suite de couleur. Chaque tour, une nouvelle couleur aléatoire est ajoutée à la suite de couleur.
Le but de cette application est à la fois de s'amuser, mais également d'entrainer sa mémoire à retenir une suite et la reproduire à l'identique.
Pendant la partie, il est possible de voir la durée de la partie, le score actuel ainsi que la nouvelle couleur aléatoire à retenir pour le tour. 

En fin de partie, une pop-up apparait indiquant à l'utilisateur le score obtenu, le temps total de la partie ainsi qu'un résumé sur sa réactivité suivant une moyenne(s'il a réalisé la suite de couleur de manière rapide, lente ou normal par tour).
Il est également proposé à l'utilisateur d'indiquer un nom (si aucun nom n'est saisie, "???" sera attribué).


## Page d'options

L'application propose différents éléments pouvant influencer la difficulté de la partie via une page d'option:
- Possibilité de changer la difficulté. Il existe 3 niveaux de difficultés:
  - Facile proposant seulement 2 couleurs à l'écran.

  - Normal proposant 4 couleurs à l'écran (mode de difficulté par défaut).

  - Difficile proposant 6 couleurs à l'écran.
- Changer le nombre de nouvelles couleurs à retenir par tour. Il est possible de paramétrer cet élément afin d'avoir 1, 2 ou 3 nouvelles couleurs à retenir par tour. Cet élément influe sur l'incrémentation du score.
- Activer ou désactiver l'animation de la suite de couleurs. En effet, lorsque ce paramètre est activé, une animation est réalisée chaque tour afin de permettre à l'utilisateur de se remémorer la suite de couleurs. Dans le cas ou cette option est désactivée, l'utilisateur devra se concentrer davantages afin de se souvenir lui-même des couleurs.
- Activer le mode contre la montre. Ce dernier élément de gameplay pousse l'utilisateur à réaliser, chaque tours, la suite de couleur dans un temps impartie. Si le temps est écoulé, l'utilisateur à perdu la partie.


## Tableau des scores

L'application sauvegarde les données de chaque parties afin de les afficher sous forme de tableau des scores. Ainsi, sur cette page, il est possible de consulter le 10 meilleurs scores par niveau de difficultés (Facile, Normal, Difficile, All).
Chaque ligne indique le rang, le nom du joueur, le temps total de la partie ainsi qu'un bouton permettant de supprimer la ligne en question. Il est également possible de supprimer l'entièreté des données via un unique bouton.

©KryLex0
