package kodeinViewModelInjector

class InMemCatService(private val urlOverride: String?) : CatService {
    companion object {
        const val aCatUrl = "http://24.media.tumblr.com/tumblr_m1mlrfFqvk1qze0hyo1_500.jpg"
    }

    override fun getCatPic(): String = urlOverride ?: aCatUrl
}