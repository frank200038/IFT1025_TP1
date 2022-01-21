
/**
 * Author: Yan Zhuang and Yu Deng
 * Date: 03/04/2021
 * Plante class that is responsible for the manipulation of all Plante
 */

public class Plante extends Organisme{

    public Plante(Organisme organisme){
        super(organisme);
    }

    // No need to verify when creating a baby
    // Create a Plante baby from an organisme baby. No need to retype all the parameters
    public Plante createPlanteBaby(){
        return new Plante(createOrganismeBaby());
    }


}