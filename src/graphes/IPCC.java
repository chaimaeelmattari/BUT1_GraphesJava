package graphes;

import java.util.ArrayList;

public interface IPCC {
    int pc(IGraphe g, int source, int destination, ArrayList<Integer> chemin);
    boolean estOk(IGraphe g);
}
