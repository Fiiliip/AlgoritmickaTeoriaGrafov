Vrcholy: 1, 2, 3, 4, 5, 6, 7

Hrany grafu: {1,2}, {1,5}, {2,3}, {2,5}, {3,4}, {3,5}, {4,5}, {6,7}

Hrany digrafu: (1,2), (2,3), (3,6), (1,4), (1,5), (4,2), (6,2), (5,6), (3,5), (4,5)

// Cvičenie 1

nie je

// Cvičenie 2

G1 = (V, H)

V = {1, 2, 3, 4, 5, 6, 7}

H = {{1, 2}, {1, 5}, {2, 3}, {2, 5}, {3, 4}, {3, 5}, {4, 5}, {6, 7}}

G2 = (V, H)

V = {1, 2, 3, 4, 5, 6}

H = { (1, 2), (2, 3), (3, 6), (1, 4), (1, 5), (4, 2),  (6, 2), (5, 6), (3, 5), (4, 5)}


Naprogramujte metódu, ktorá

1. vráti stupeň zadaného vrcholu grafu,
2. vráti vrchol grafu s najvyšším stupňom,
3. vráti zoznam všetkých vrcholov digrafu so vstupným stupňom 0.


// Cvičenie 3

Daný je graf G = (V, H, c)
V = {1, 2, 3, 4, 5, 6, 7, 8, 9}
H = {{1,2}, {2,3}, {2,5}, {3,6}, {4,5}, {4,8}, {5,6}, {5,8}, {6,9}, {7,8}, {7,9}}

|  h   | {1,2} | {2,3} | {2,5} | {3,6} | {4,5} | {4,8} | {5,6} | {5,8} | {6,9} | {7,8} | {7,9}
| c(h) |   5   |   4   |   4   |   5   |   2   |   1   |   7   |   3   |   6   |   1   |   2

Naprogramujte metódu 
1.	ktorá vráti maticu susednosti grafu G,
2.	ktorá vráti maticu incidencie grafu G,
3.	ktorá vráti maticu ohodnotení hrán grafu G.
4.	Základný algoritmus, ktorá v grafe G nájde a vráti najkratšiu cestu z vrcholu u do vrcholu v a jej dĺžku. 
