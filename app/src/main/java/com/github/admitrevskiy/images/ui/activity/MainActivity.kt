package com.github.admitrevskiy.images.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.admitrevskiy.images.R
import com.github.admitrevskiy.images.model.Photo
import com.github.admitrevskiy.images.ui.helpers.ImagesAdapter
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
    private lateinit var adapter: ImagesAdapter

    /** Photos to be passed into recycler */
    private val photo: ArrayList<Photo> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        recycler = findViewById<RecyclerView>(R.id.images_recycler).apply {
            layoutManager = LinearLayoutManager(context)
        }

        adapter = ImagesAdapter(photo, viewModel::loadPhotos)
        recycler.adapter = adapter
        progressBar = findViewById(R.id.progress)
        errorWrapper = findViewById(R.id.error_wrapper)
        retryButton = findViewById<Button>(R.id.retry).apply { setOnClickListener { viewModel.loadPhotos() } }

    }

    override fun onResume() {
        super.onResume()
        viewModel.photosLiveData.observe(this, Observer {
            photo.addAll(it)
            adapter.notifyDataSetChanged()
        })

        viewModel.progressLiveData.observe(this, Observer {
            progressBar.visibility = if (it) View.VISIBLE else View.GONE
        })

        viewModel.errorLiveData.observe(this, Observer {
            errorWrapper.visibility = if (it) View.VISIBLE else View.GONE
        })

        // Let's check if we already have some photos loaded
        // There are no needs to trigger loading after screen rotation
        viewModel.photosLiveData.value?.apply {
            if (this.isNotEmpty()) {
                viewModel.loadPhotos()
            } else {
                photo.addAll(this)
            }
        } ?: viewModel.loadPhotos()
    }
}
