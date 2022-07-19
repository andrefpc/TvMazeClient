package com.andrefpc.tvmazeclient.ui.person_details

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.andrefpc.tvmazeclient.R
import com.andrefpc.tvmazeclient.data.Person
import com.andrefpc.tvmazeclient.data.Show
import com.andrefpc.tvmazeclient.databinding.ActivityPersonDetailsBinding
import com.andrefpc.tvmazeclient.databinding.ActivityShowDetailsBinding
import com.andrefpc.tvmazeclient.ui.episode_details.EpisodeModal
import com.andrefpc.tvmazeclient.ui.show_details.ShowDetailsActivity
import com.andrefpc.tvmazeclient.ui.show_details.ShowDetailsViewModel
import com.andrefpc.tvmazeclient.ui.show_details.ShowHeaderAdapter
import com.andrefpc.tvmazeclient.ui.shows.ShowAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel

class PersonDetailsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPersonDetailsBinding
    private val viewModel: PersonDetailsViewModel by viewModel()
    private var headerAdapter: PersonHeaderAdapter? = null
    private val showAdapter by lazy { ShowAdapter() }
    private val concatAdapter by lazy { ConcatAdapter(headerAdapter, showAdapter) }

    private lateinit var person: Person

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPersonDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        person = intent.getSerializableExtra("person") as Person
        title = person.name

        initObservers()
        headerAdapter = PersonHeaderAdapter(person)
        initList()
    }

    private fun initList() {

        showAdapter.onClick {
            val intent = Intent(this, ShowDetailsActivity::class.java)
            intent.putExtra("show", it)
            startActivity(intent)
        }

        binding.personDetails.apply {
            layoutManager = LinearLayoutManager(this@PersonDetailsActivity)
            adapter = concatAdapter
        }
        viewModel.getShows(person.id)
    }

    /**
     * Init the ViewModel observers
     */
    private fun initObservers() {
        viewModel.listShows.observe(this) {
            showAdapter.submitList(it)
        }

        viewModel.error.observe(this) {
            Toast.makeText(this, "Error getting shows: ${it.message}", Toast.LENGTH_LONG).show()
        }
    }
}