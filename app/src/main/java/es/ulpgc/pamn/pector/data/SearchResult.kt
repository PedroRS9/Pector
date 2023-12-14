package es.ulpgc.pamn.pector.data

sealed class SearchResult{
    object Loading: SearchResult()
    data class ShowResults(val results: List<User>) : SearchResult()
    object NoResults: SearchResult()
    data class ShowError(val exception: Exception) : SearchResult()
}
