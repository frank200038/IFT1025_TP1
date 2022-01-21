import java.util.HashMap;
import java.util.Map;

/**
 * Author: Yan Zhuang and Yu Deng
 *
 * Date: 03/04/2021
 *
 * This is a factory class designed to facilitate the creation of a Organisme
 * This class is a superclass of other factory classes: {@code UsinePlante}, {@code UsineHerbivore},{@code UsineCarnivore}
 */

public class UsineOrganisme {
    private String nomEspece;
    private double besoinEnergie;
    private double efficaciteEnergie;
    private double resilience;
    private double fertilite;
    private int ageFertilite;
    private double energieEnfant;

    private HashMap<String,Boolean> conditions = new HashMap<>(){{
        put("nomEspece",false);
        put("besoinEnergie",false);
        put("efficaciteEnergie",false);
        put("resilience",false);
        put("fertilite",false);
        put("ageFertilite",false);
        put("energieEnfant",false);
    }};


    /**
     * Return a COPY of the conditions
     * @return COPY of the conditions
     */
    public HashMap<String, Boolean> getConditions() {
        var conditionsCopy = new HashMap<String,Boolean>();
        var conditionIter = conditions.entrySet().iterator();

        while (conditionIter.hasNext()){
            var element =  conditionIter.next();
            conditionsCopy.put(element.getKey(),element.getValue());
        }

        return conditionsCopy;
    }

    public void setNomEspece(String nomEspece) throws ConditionsInitialesInvalides {
        if (!nomEspece.equals("")) {
            this.nomEspece = nomEspece;
            conditions.replace("nomEspece",true);
        } else{
            throw new ConditionsInitialesInvalides("Le nom d'espèce doit être non-vide");
        }
    }

    public void setBesoinEnergie(double besoinEnergie) throws ConditionsInitialesInvalides {
        if (besoinEnergie > 0) {
            this.besoinEnergie = besoinEnergie;
            conditions.replace("besoinEnergie",true);
        } else{
            throw new ConditionsInitialesInvalides("besoinEnergie doit être positif");
        }
    }

    public void setEfficaciteEnergie(double efficaciteEnergie) throws ConditionsInitialesInvalides {
        if (efficaciteEnergie >=0 && efficaciteEnergie <= 1) {
            this.efficaciteEnergie = efficaciteEnergie;
            conditions.replace("efficaciteEnergie",true);
        } else{
            throw new ConditionsInitialesInvalides("efficaciteEnergie doit être entre 0 et 1");
        }
    }

    public void setResilience(double resilience) throws ConditionsInitialesInvalides {
        if (resilience >=0 && resilience <= 1) {
            this.resilience = resilience;
            conditions.replace("resilience",true);
        }else{
            throw new ConditionsInitialesInvalides("resilience doit être entre 0 et 1");
        }
    }

    public void setFertilite(double fertilite) throws ConditionsInitialesInvalides {
        if (fertilite >=0 && fertilite <= 1) {
            this.fertilite = fertilite;
            conditions.replace("fertilite",true);
        } else{
            throw new ConditionsInitialesInvalides("fertilite doit être entre 0 et 1");
        }
    }

    public void setAgeFertilite(int ageFertilite) throws ConditionsInitialesInvalides {
        if (ageFertilite >=0 ) {
            this.ageFertilite = ageFertilite;
            conditions.replace("ageFertilite",true);
        } else{
            throw new ConditionsInitialesInvalides("ageFertilite doit être égale ou supérieur que 0");
        }
    }

    public void setEnergieEnfant(double energieEnfant) throws ConditionsInitialesInvalides {
        if (energieEnfant > 0) {
            this.energieEnfant = energieEnfant;
            conditions.replace("energieEnfant",true);
        } else{
            throw new ConditionsInitialesInvalides("energieEnfant doit être positif");
        }
    }

    /**
     * Verify is variables are intialised. If not, list out the name of all non-intialised variables
     * @param conditions Conditions to be verified
     * @throws ConditionsInitialesInvalides
     */
    public void verifyCondition(HashMap<String,Boolean> conditions) throws ConditionsInitialesInvalides{
        var uninitialized = "";

        // Any variable associated with a value of False is not initialised. Add the name to a String for output later
        for (Map.Entry element : conditions.entrySet()){
            var key = (String) element.getKey();
            var value = (Boolean) element.getValue();
            if (value == false){
                uninitialized += key + " ";
            }
        }

        if (!uninitialized.equals("")){
            throw new ConditionsInitialesInvalides("Les variables suivantes ne sont pas initilisés: "+uninitialized);
        }
    }

    public final Organisme creerOrganisme() throws ConditionsInitialesInvalides {
        verifyCondition(conditions);
        return new Organisme(nomEspece, energieEnfant,0, besoinEnergie, efficaciteEnergie, resilience, fertilite, ageFertilite, energieEnfant);
    }

}
