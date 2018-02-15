package kodeinViewModelInjector

import android.arch.lifecycle.ViewModel

open class MainViewModel(private val catService: CatService) : ViewModel() {
    private var lastCatPic: String? = null

    open fun fetchCatPicUrl(): String =
            lastCatPic ?: run {
                lastCatPic = catService.getCatPic()
                lastCatPic!!
            }
}