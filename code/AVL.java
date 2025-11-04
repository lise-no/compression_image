// AVL.java
import java.awt.Color;

public class AVL {

    // === Classe interne pour les nœuds de l'arbre ===
    private class Node {
        int r, v, b; // composantes de couleur
        Node left, right;
        int height; 

        Node(int r, int v, int b) {
            this.r = r;
            this.v = v;
            this.b = b;
            this.height = 1;
        }      

        int compareTo (Node other) {
            if (this.r != other.r) return this.r - other.r;
            if (this.v != other.v) return this.v - other.v;
            return this.b - other.b;
        }

        Color getColor() {
            return new Color(r, v, b);
        }

    }

    private Node root;

    // === Constructeurs ===

    public AVL(ImagePNG image) {
        // Parcours de l’image et insertion des couleurs
        for (int x = 0; x < image.width(); x++) {
            for (int y = 0; y < image.height(); y++) {
                Color col = image.getPixel(x, y);
                int r = col.getRed();
                int v = col.getGreen();
                int b = col.getBlue();

            // Vérifie si la couleur est déjà présente avant de l’ajouter
            if (!search(r, v, b)) {
                add(r, v, b);
            }
            }
        }
   }



    public AVL(RQuadtree tree) {
        // Parcours du quadtree et insertion des couleurs
    }


    public AVL() {
    root = null;
    }
    

    // === Méthodes principales ===

    public void add(int r, int v, int b) {
        root = insert(root, new Node(r, v, b));
    }

    public boolean search(int r, int v, int b) {
        return searchRec(root, new Node(r, v, b));
    }

    public void remove(int r, int v, int b) {
        root = remove(root, new Node(r, v, b));
    }

    public String toString() {
        return toString(root);
    }

    // === Fonctions auxiliaires ===

    private Node insert(Node node, Node toInsert) {
        // Insertion + équilibrage AVL
        if (node == null) {
            return toInsert;
        }
        if (toInsert.compareTo(node) < 0) {
            node.left = insert(node.left, toInsert);
        } else if (toInsert.compareTo(node) > 0) {
            node.right = insert(node.right, toInsert);
        } else {
            return node; // Couleur déjà présente
        }
        updateHeight(node);
        return balance(node, toInsert);
    }

    private void updateHeight(AVL.Node node) {
        // Met à jour la hauteur du nœud
         node.height = 1 + Math.max(getHeight(node.left), getHeight(node.right));
        
    }



    private boolean searchRec(Node node, Node target) {
        return false;
        // Recherche récursive
    }

    private Node remove(Node node, Node toRemove) {
        return null;
        // Suppression + équilibrage AVL
    }

    private String toString(Node node) {
        if (node == null) return "";
        return "(" + toString(node.left) + ImagePNG.colorToHex(node.getColor()) + toString(node.right) + ")";
    }

    // === Fonctions d’équilibrage AVL ===

    private int getHeight(Node node) {
        return node == null ? 0 : node.height;
    }

    private int getBalance(Node node) {
        return node == null ? 0 : getHeight(node.left) - getHeight(node.right);
    }

    private Node rotateRight(Node y) {
        Node x = y.left;
        Node T2 = x.right;

        x.right = y;
        y.left = T2;

        y.height = Math.max(getHeight(y.left), getHeight(y.right)) + 1;
        x.height = Math.max(getHeight(x.left), getHeight(x.right)) + 1;

        return x;
    }

    private Node rotateLeft(Node x) {
        Node y = x.right;
        Node T2 = y.left;

        y.left = x;
        x.right = T2;

        x.height = Math.max(getHeight(x.left), getHeight(x.right)) + 1;
        y.height = Math.max(getHeight(y.left), getHeight(y.right)) + 1;

        return y;
    }
    // === Rotations doubles ===
    private Node rotateLeftRight(Node node) {
        node.left = rotateLeft(node.left);
        return rotateRight(node);
    }

    private Node rotateRightLeft(Node node) {
        node.right = rotateRight(node.right);
        return rotateLeft(node);
    }

    private Node balance(Node node, Node inserted) {
        int balance = getBalance(node);

        if (balance > 1 && inserted.compareTo(node.left) < 0)
            return rotateRight(node);
        if (balance < -1 && inserted.compareTo(node.right) > 0)
            return rotateLeft(node);
        if (balance > 1 && inserted.compareTo(node.left) > 0)
            return rotateLeftRight(node);
        if (balance < -1 && inserted.compareTo(node.right) < 0)
            return rotateRightLeft(node);

        return node;
    }
}
