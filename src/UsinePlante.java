
/**
 * Author: Yan Zhuang and Yu Deng
 * Date: 03/04/2021
 * This is a factory class designed to facilitate the creation of a Plante
 */

public final class UsinePlante extends UsineOrganisme{

    public Plante creerPlante() throws ConditionsInitialesInvalides {
        verifyCondition(getConditions());
        var organisme = creerOrganisme();
        return new Plante(organisme);
    }

}
