package src;
// AVL.java
import java.awt.Color;

public class AVL {

    // === interne class for  the nodes of the trees ===
    private class Node {
        int r, v, b; // red, green, blue components
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

    // === Constructors ===

     public AVL() {
    root = null;
    }

    public AVL(ImagePNG image) {
        // 
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

    // update height of a node
    private void updateHeight(AVL.Node node) {
        // Met à jour la hauteur du nœud
         node.height = 1 + Math.max(getHeight(node.left), getHeight(node.right));
        
    }

    // recursive insertion
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
        return balance(node);    
        
    }


    // rcursive search
   private boolean searchRec(Node node, Node target) {
    if (node == null) return false;

    int cmp = target.compareTo(node);
    if (cmp == 0) return true;
    if (cmp < 0) return searchRec(node.left, target);
    return searchRec(node.right, target);
   }


    // suppression of a node
    private Node remove(Node node, Node toRemove) {
        // Suppression + équilibrage AVL
        if (node == null) return null;
        int cmp = toRemove.compareTo(node);
        if (cmp < 0) {
           node.left = remove(node.left, toRemove);
        } 
        else if (cmp > 0) {
        node.right = remove(node.right, toRemove);
        } 
        else {
            // Node found
            if (node.left == null) return node.right;
            if (node.right == null) return node.left;

            // Node with two children : replace by the smallest of the right subtree
            Node temp = minValueNode(node.right);
            node.r = temp.r;
            node.v = temp.v;
            node.b = temp.b;
            node.right = remove(node.right, temp);
        }
        updateHeight(node);
        return balance(node); 

   }


    private Node minValueNode(Node node) {
        while (node.left != null) node = node.left;
        return node;
    }

    private String toString(Node node) {
        if (node == null) return "";
        return "(" + toString(node.left) + ImagePNG.colorToHex(node.getColor()) + toString(node.right) + ")";
    }

    // === balancing of AVL ===

    // calculate height
    private int getHeight(Node node) {
        return node == null ? 0 : node.height;
    }

    // Calculate balance factor
    private int getBalance(Node node) {
        return node == null ? 0 : getHeight(node.left) - getHeight(node.right);
    }
    
    //
    private Node rotateRight(Node y) {
        if (y == null || y.left == null) return y;
        Node x = y.left;

        y.left = x.right;
        x.right = y;

        y.height = Math.max(getHeight(y.left), getHeight(y.right)) + 1;
        x.height = Math.max(getHeight(x.left), getHeight(x.right)) + 1;

        return x;
    }

    //
    private Node rotateLeft(Node x) {

        if (x == null || x.right == null) return x;
        Node y = x.right;
        
        x.right = y.left;
        y.left = x;

        x.height = Math.max(getHeight(x.left), getHeight(x.right)) + 1;
        y.height = Math.max(getHeight(y.left), getHeight(y.right)) + 1;

        return y;
    }
    
    // node balancing
    private Node balance(Node node) {
    
        if (node == null) return null;

        int bal = getBalance(node);

        if (bal == 2) {
           if (getBalance(node.left) >= 0) {
              return rotateRight(node); // Left-Left
            } else {
                return rotateLeftRight(node); // Left-Right
              }
         }
        else if (bal == -2) {
            if (getBalance(node.right) <= 0) {
                return rotateLeft(node); // Right-Right
            } 
            else {
                return rotateRightLeft(node); // Right-Left
             }
        }
        
            return node; // No balancing needed
     }

     // Double rotation Left-Right
    private Node rotateLeftRight(Node node) {
        node.left = rotateLeft(node.left);
        return rotateRight(node);
    }
    
    // Double rotation Right-Left
    private Node rotateRightLeft(Node node) {
        node.right = rotateRight(node.right);
        return rotateLeft(node);
    }

}
