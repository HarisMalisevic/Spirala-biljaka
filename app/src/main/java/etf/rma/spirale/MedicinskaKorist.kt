package etf.rma.spirale

enum class MedicinskaKorist(val opis: String) {
    SMIRENJE("Smirenje - za smirenje i relaksaciju"),
    PROTUUPALNO("Protuupalno - za smanjenje upale"),
    PROTIVBOLOVA("Protivbolova - za smanjenje bolova"),
    REGULACIJAPRITISKA("Regulacija pritiska - za regulaciju visokog/niskog pritiska"),
    REGULACIJAPROBAVE("Regulacija probave"),
    PODRSKAIMUNITETU("Podrška imunitetu");

    companion object{
         fun getOpisList(): List<String> {
            return entries.map {
                it.opis
            }
        }
    }
}

