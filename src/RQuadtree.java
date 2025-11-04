package src;
import java.awt.Color;
import java.util.ArrayList;


public class RQuadtree {

    private static class Node {
        boolean estFeuille;
        int r, v, b;
        Node[] enfants;
        int x, y, side;
        Node parent;

        Node(int x, int y, int side, int r, int v, int b) {
            this.estFeuille = true;
            this.r = r; this.v = v; this.b = b;
            this.enfants = null;
            this.x = x; this.y = y; this.side = side;
            this.parent = null;
        }

        Node(int x, int y, int side, Node c1, Node c2, Node c3, Node c4) {
            this.estFeuille = false;
            this.enfants = new Node[]{c1, c2, c3, c4};
            for (Node c : enfants) if (c != null) c.parent = this;
            this.x = x; this.y = y; this.side = side;
        }

        boolean isSurFeuille() {
            if (estFeuille) return false;
            for (Node c : enfants) if (!c.estFeuille) return false;
            return true;
        }
    }

    private Node root;
    private int size;
    private ImagePNG templateImage;

    public RQuadtree(ImagePNG img) {
        this.templateImage = img.clone();
        this.size = img.width();
        this.root = buildFromImage(img, 0, 0, size);
    }

    private Node buildFromImage(ImagePNG img, int x, int y, int side) {
        if (side == 1) {
            Color c = img.getPixel(x, y);
            return new Node(x, y, 1, c.getRed(), c.getGreen(), c.getBlue());
        }
        int half = side / 2;
        Node c1 = buildFromImage(img, x, y, half);
        Node c2 = buildFromImage(img, x + half, y, half);
        Node c3 = buildFromImage(img, x + half, y + half, half);
        Node c4 = buildFromImage(img, x, y + half, half);

        if (c1.estFeuille && c2.estFeuille && c3.estFeuille && c4.estFeuille &&
            c1.r == c2.r && c1.r == c3.r && c1.r == c4.r &&
            c1.v == c2.v && c1.v == c3.v && c1.v == c4.v &&
            c1.b == c2.b && c1.b == c3.b && c1.b == c4.b) {
            return new Node(x, y, side, c1.r, c1.v, c1.b);
        }
        return new Node(x, y, side, c1, c2, c3, c4);
    }

    private static double luminance(int r, int v, int b) {
        return 0.2126 * r + 0.7152 * v + 0.0722 * b;
    }

    private static int[] moyenneCouleur(Node[] enfants) {
        int r = 0, v = 0, b = 0;
        for (Node c : enfants) { r += c.r; v += c.v; b += c.b; }
        return new int[]{r/4, v/4, b/4};
    }

    //  compressLambda
    public void compressLambda(int A) {
        compressLambdaRec(root, A);
    }

    private void compressLambdaRec(Node node, int A) {
        if (node == null || node.estFeuille) return;
        for (Node c : node.enfants) compressLambdaRec(c, A);

        if (node.isSurFeuille()) {
            double[] lum = new double[4];
            for (int i=0;i<4;i++) lum[i] = luminance(node.enfants[i].r, node.enfants[i].v, node.enfants[i].b);
            int[] colMoy = moyenneCouleur(node.enfants);
            double Lm = luminance(colMoy[0], colMoy[1], colMoy[2]);
            double maxDiff = 0;
            for (int i=0;i<4;i++) maxDiff = Math.max(maxDiff, Math.abs(Lm - lum[i]));
            if (maxDiff <= A) {
                node.estFeuille = true;
                node.r = colMoy[0]; node.v = colMoy[1]; node.b = colMoy[2];
                node.enfants = null;
            }
        }
    }



    // Collecte toutes les sur-feuilles dans la liste (parcours DFS)
    private void collectSurFeuilles(Node node, ArrayList<Node> out) {
        if (node == null || node.estFeuille) return;
        if (node.isSurFeuille()) out.add(node);
        else {
            for (Node c : node.enfants) collectSurFeuilles(c, out);
        }
    }

    // calcule la valeur A (dégradation de luminance) d'une sur-feuille
    private double calcA(Node s) {
        // Complexité O(1) (constante) : 4 enfants
        double[] lum = new double[4];
        for (int i=0;i<4;i++) lum[i] = luminance(s.enfants[i].r, s.enfants[i].v, s.enfants[i].b);
        int[] colMoy = moyenneCouleur(s.enfants);
        double Lm = luminance(colMoy[0], colMoy[1], colMoy[2]);
        double maxDiff = 0;
        for (int i=0;i<4;i++) maxDiff = Math.max(maxDiff, Math.abs(Lm - lum[i]));
        return maxDiff;
    }

    //toPNG / remplirImage
    public ImagePNG toPNG() {
        ImagePNG out = templateImage.clone();
        remplirImage(root, out);
        return out;
    }

    private void remplirImage(Node node, ImagePNG img) {
        if (node.estFeuille) {
            Color c = new Color(node.r, node.v, node.b);
            for (int i = node.x; i < node.x + node.side; i++)
                for (int j = node.y; j < node.y + node.side; j++)
                    img.setPixel(i, j, c);
        } else {
            for (Node c : node.enfants) remplirImage(c, img);
        }
    }

    // toString
    public String toString() {
        return nodeToString(root);
    }

    private String nodeToString(Node node) {
        if (node.estFeuille) {
            Color c = new Color(node.r, node.v, node.b);
            return ImagePNG.colorToHex(c);
        } else {
            return "(" +
                nodeToString(node.enfants[0]) + " " +
                nodeToString(node.enfants[1]) + " " +
                nodeToString(node.enfants[2]) + " " +
                nodeToString(node.enfants[3]) + ")";
        }
    }

    // Méthode utilitaire pour tests : compte les feuilles
    public int getLeafCount() {
        return countLeaves(root);
    }

    private int countLeaves(Node node) {
        if (node == null) return 0;
        if (node.estFeuille) return 1;
        int s = 0;
        for (Node c : node.enfants) s += countLeaves(c);
        return s;
    }
}

