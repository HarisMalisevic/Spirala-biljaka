package etf.rma.spirale.trefleAPI

sealed class Result<genericType> (
    val data : genericType? = null,
    val message : String? = null
){

    class Success<genericType>(data: genericType?): Result<genericType>(data)
    class Error<genericType>(data: genericType?, message: String): Result<genericType>(data, message)


}