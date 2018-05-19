package kodeinViewModelInjector

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.squareup.picasso.Picasso
import io.kodeinViewModelInjector.R
import kotlinx.android.synthetic.main.activity_main.*
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider

class MainActivity : AppCompatActivity() {
    private val viewModel by viewModelBinder<MainViewModel> {
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
