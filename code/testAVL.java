//import java.io.File;
//import java.io.IOException;

public class testAVL {
    public static void main(String[] args) {
        /*try {
        ImagePNG image = new ImagePNG("exemple.png");
        AVL avl = new AVL(image);

        System.out.println("Couleurs de l'image :");
        System.out.println(avl.toString());
    } catch (IOException e) {
        e.printStackTrace();
    }*/

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
}



    

