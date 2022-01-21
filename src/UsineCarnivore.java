import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 * Author: Yan Zhuang and Yu Deng
 * Date: 03/04/2021
 * This is a factory class designed to facilitate the creation of a Carnivore
 */
public final class UsineCarnivore extends UsineOrganisme {

    private double debrouillardise;
    private int tailleMaximum;
    private Set<String> aliments = new HashSet<>();

    // Create a HashMap containing only the extra variables for Carnivore
    // (Other variables are inherited, will be check later by using getCondition())
    private HashMap<String,Boolean> conditions = new HashMap<>(){{
        put("debrouillardise",false);
        put("aliments",false);
    }};

    public void setDebrouillardise(double debrouillardise) throws ConditionsInitialesInvalides {
        if (debrouillardise >= 0 && debrouillardise <= 1) {
            this.debrouillardise = debrouillardise;
            conditions.replace("debrouillardise",true);
        } else{
            throw new ConditionsInitialesInvalides("debrouillardise doit être entre 0 et 1");
        }
    }

    public void setTailleMaximum(int tailleMaximum) throws ConditionsInitialesInvalides {
        if (tailleMaximum > 0) {
            this.tailleMaximum = tailleMaximum;
        }else{
            throw new ConditionsInitialesInvalides("tailleMaximum doit être positif");
        }
    }

    public void addAliment(String aliment) throws ConditionsInitialesInvalides {
        if (! aliment.equals("")) {
            this.aliments.add(aliment);
            conditions.replace("aliments",true);
        } else{
            throw new ConditionsInitialesInvalides("aliement doit être non-vide");
        }
    }

    // Create a herbivore by creating an organisme (Contains common parameters). Then call the constructor that takes
    // organisms along with extra parameters
    public Carnivore creerCarnivore() throws ConditionsInitialesInvalides{
        conditions.putAll(getConditions());
        verifyCondition(conditions);
        var organisme = creerOrganisme();
        return new Carnivore(organisme,debrouillardise,tailleMaximum,aliments);
    }

}
