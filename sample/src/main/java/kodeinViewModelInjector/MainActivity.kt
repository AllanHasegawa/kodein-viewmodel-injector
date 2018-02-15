package kodeinViewModelInjector

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.github.salomonbrys.kodein.bind
import com.github.salomonbrys.kodein.instance
import com.github.salomonbrys.kodein.provider
import com.squareup.picasso.Picasso
import io.kodeinViewModelInjector.R
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private val viewModel by viewModelBinder<MainActivity, MainViewModel> {
        bind() from provider { MainViewModel(instance()) }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel.fetchCatPicUrl().let { url ->
            Log.i("MainActivity", "Loading $url")
            Picasso.with(this@MainActivity).load(url).into(catPicIv)
            catPicUrlTv.text = url
        }
    }
}
