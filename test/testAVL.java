package test;


import java.io.IOException;

import src.AVL;
import src.ImagePNG;
//import src.RQuadtree;
 

public class testAVL {
    public static void main(String[] args) {
     
        try {
        // 1. Charger l'image
        ImagePNG image = new ImagePNG("pngs/2.png");

        // 2. Construire l'arbre AVL
        AVL avl = new AVL(image);

        // 3. Afficher les couleurs uniques
        System.out.println("Couleurs uniques dans l'image :");
        System.out.println(avl.toString());

        
        // 4. Tester la présence d'une couleur
        int r = 255, v = 255,  b = 255;
        boolean found = avl.search(r, v, b);
        System.out.println("La couleur (" + r + "," + v + "," + b + ") est-elle présente ? " + found);

        // 5. Supprimer cette couleur
        avl.remove(r, v, b);
        boolean stillThere = avl.search(r, v, b);
        System.out.println("Après suppression, est-elle encore là ? " + stillThere);

    } catch (IOException e) {
        System.out.println("Erreur lors du chargement de l'image : " + e.getMessage());
    }

 
        /*  // Test avec des couleurs manuelles
        AVL avl = new AVL();

        // Ajout de quelques couleurs
        avl.add(100, 100, 100); // 646464
        avl.add(150, 150, 150); // 969696
        avl.add(200, 200, 200); // c8c8c8
        avl.add(50, 50, 50);    // 323232
        avl.add(125, 125, 125); // 7d7d7d

        // Affichage de l’arbre
        System.out.println("Arbre AVL :");
        System.out.println(avl.toString());

        // Recherche de couleurs
        System.out.println("Recherche 969696 : " + avl.search(150, 150, 150)); // true
        System.out.println("Recherche ffffff : " + avl.search(255, 255, 255)); // false

        // Suppression d’une couleur
        avl.remove(100, 100, 100); // Supprime 646464
        System.out.println("Après suppression de 646464 :");
        System.out.println(avl.toString()); 
    } 
}*/
    }
}



    

