import java.util.Set;

/**
 * Author: Yan Zhuang and Yu Deng
 * Date: 03/04/2021
 * Carnivore class that is responsible for the manipulation of all carnivores
 */

public class Carnivore extends Organisme{
    private double debrouillardise;
    private int tailleMaximum;
    private Set<String> aliments;


    // Constructor that would take organism, and other extra parameters
    // Facilite the creation of carnivores without the need to write multiple times the same parameters
    public Carnivore(Organisme organisme,double debrouillardise, Integer tailleMaximum,Set<String> aliments){
        super(organisme);

        if (tailleMaximum != null) {
            this.tailleMaximum = tailleMaximum;
        }else{
            this.tailleMaximum = 10*(int)organisme.getEnergieEnfant();
        }

        this.debrouillardise = debrouillardise;
        this.aliments = aliments;
    }

    public double getDebrouillardise() {
        return debrouillardise;
    }

    public int getTailleMaximum() {
        return tailleMaximum;
    }

    public Set<String> getAliments() {
        return aliments;
    }

    // No need to verify when creating a baby
    // Create a carnivore baby from an organisme baby. No need to retype all the parameters
    public Carnivore createCarnivoreBaby(){
        var organisme = createOrganismeBaby();
        return new Carnivore(organisme, debrouillardise,tailleMaximum,aliments);
    }
}
