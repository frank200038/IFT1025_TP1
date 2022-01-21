import java.util.Set;

/**
 * Author: Yan Zhuang and Yu Deng
 * Date: 03/04/2021
 * Herbivore class that is responsible for the manipulation of all herbivores
 */

public class Herbivore extends Organisme{

    private double debrouillardise;
    private double voraciteMin;
    private double voraciteMax;
    private int tailleMaximum;
    private Set<String> aliments;


    public Herbivore(Organisme organisme,double debrouillardise,double voraciteMax, double voraciteMin, Integer tailleMaximum
                     ,Set<String> aliments){
        super(organisme);
        this.debrouillardise = debrouillardise;
        this.voraciteMax = voraciteMax;
        this.voraciteMin = voraciteMin;

        if (tailleMaximum != null) {
            this.tailleMaximum = tailleMaximum;
        }else{
            this.tailleMaximum = 10*(int)organisme.getEnergieEnfant();
        }

        this.aliments = aliments;
    }

    public double getDebrouillardise() {
        return debrouillardise;
    }

    public double getVoraciteMin() {
        return voraciteMin;
    }

    public double getVoraciteMax() {
        return voraciteMax;
    }

    public int getTailleMaximum() {
        return tailleMaximum;
    }

    public Set<String> getAliments() {
        return aliments;
    }

    // No need to verify when creating a baby
    // Create a Herbivore baby from an organisme baby. No need to retype all the parameters
    public Herbivore createHerbivoreBaby(){
        var organisme = createOrganismeBaby();
        return new Herbivore(organisme, debrouillardise, voraciteMax, voraciteMin,
                tailleMaximum,aliments);
    }



}
