package etf.rma.spirale

enum class Zemljiste(val naziv: String) {
    PJESKOVITO("Pjeskovito zemljište"),
    GLINENO("Glinеno zemljište"),
    ILOVACA("Ilovača"),
    CRNICA("Crnica"),
    SLJUNOVITO("Šljunovito zemljište"),
    KRECNJACKO("Krečnjačko zemljište");

    companion object{
         fun getOpisList(): List<String> {
            return MedicinskaKorist.entries.map {
                it.opis
            }
        }
    }

}