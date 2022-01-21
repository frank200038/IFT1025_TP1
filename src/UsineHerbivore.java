import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 * Author: Yan Zhuang and Yu Deng
 * Date: 03/04/2021
 * This is a factory class designed to facilitate the creation of a Herbivore
 */
public final class UsineHerbivore extends UsineOrganisme {
    private double debrouillardise;
    private double voraciteMin;
    private double voraciteMax;
    private int tailleMaximum;
    private Set<String> aliments = new HashSet<>();

    // Create a HashMap containing only the extra variables for Herbivore
    // (Other variables are inherited, will be check later by using getCondition())
    private HashMap<String,Boolean> conditions = new HashMap<>(){{
        put("debrouillardise",false);
        put("voraciteMin",false);
        put("voraciteMax",false);
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

    public void setVoraciteMin(double voraciteMin) throws ConditionsInitialesInvalides {
        if (voraciteMin >= 0 && voraciteMin <= 1){
            this.voraciteMin = voraciteMin;
            conditions.replace("voraciteMin",true);
        } else {
            throw new ConditionsInitialesInvalides("voraciteMin doit être entre 0 et 1");
        }
    }

    public void setVoraciteMax(double voraciteMax) throws ConditionsInitialesInvalides {
        if (voraciteMax >= 0 && voraciteMax <= 1) {
            this.voraciteMax = voraciteMax;
            conditions.replace("voraciteMax",true);
        }else{
            throw new ConditionsInitialesInvalides("voraciteMax doit être entre 0 et 1");
        }
    }

    public void setTailleMaximum(int tailleMaximum) throws ConditionsInitialesInvalides {
        if (tailleMaximum > 0) {
            this.tailleMaximum = tailleMaximum;
        } else{
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


    //Override verifyCondition so that it will verify if voraciteMin is equal or smaller than voraciteMax
    @Override
    public void verifyCondition(HashMap<String,Boolean> condition) throws ConditionsInitialesInvalides {
        super.verifyCondition(condition);
        if (voraciteMin > voraciteMax){
            throw new ConditionsInitialesInvalides("voraciteMin doit être inférieur ou égale à voraciteMax");
        }
    }

    // Create a herbivore by creating an organisme (Contains common parameters). Then call the constructor that takes
    // organisms along with extra parameters
    public Herbivore creerHerbivore() throws ConditionsInitialesInvalides{
        conditions.putAll(getConditions());
        verifyCondition(conditions);
        var organisme = creerOrganisme();
        return new Herbivore(organisme,debrouillardise,voraciteMax,voraciteMin,tailleMaximum,aliments);
    }



}
