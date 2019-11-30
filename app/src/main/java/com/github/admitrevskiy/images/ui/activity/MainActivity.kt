package com.github.admitrevskiy.images.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.admitrevskiy.images.R
import com.github.admitrevskiy.images.model.InitialLoadStatus
import com.github.admitrevskiy.images.ui.helpers.PhotosAdapter
import com.github.admitrevskiy.images.viewmodel.MainViewModel
import org.koin.android.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {

    /** DI */
    private val viewModel: MainViewModel by viewModel()

    /** Recycler and adapter */
    private lateinit var recycler: RecyclerView
    private lateinit var errorWrapper: ViewGroup
    private lateinit var retryButton: Button
    private lateinit var progressBar: ProgressBar
    private lateinit var adapter: PhotosAdapter

    /** Photos to be passed into recycler */

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        recycler = findViewById<RecyclerView>(R.id.images_recycler).apply {
            layoutManager = LinearLayoutManager(context)
        }

        adapter = PhotosAdapter(this, viewModel.photosLiveData, viewModel::loadPhotos)
        recycler.adapter = adapter
        progressBar = findViewById(R.id.progress)
        errorWrapper = findViewById(R.id.error_wrapper)
        retryButton = findViewById<Button>(R.id.retry).apply { setOnClickListener { viewModel.loadPhotos() } }

    }

    override fun onResume() {
        super.onResume()
        viewModel.initialLoadStatusLiveData.observe(this, Observer {
            when (it) {
                InitialLoadStatus.IN_PROGRESS -> {
                    progressBar.visibility = View.VISIBLE
                    errorWrapper.visibility = View.GONE
                    recycler.visibility = View.GONE
                }
                InitialLoadStatus.ERROR -> {
                    errorWrapper.visibility = View.VISIBLE
                    progressBar.visibility = View.GONE
                    recycler.visibility = View.GONE
                }
                InitialLoadStatus.LOADED -> {
                    recycler.visibility = View.VISIBLE
                    progressBar.visibility = View.GONE
                    errorWrapper.visibility = View.GONE
                }
                else -> {
                    Log.w("LiveDataObserver", "Bad value posted")}
            }
        })

        viewModel.onResume()
    }
}
