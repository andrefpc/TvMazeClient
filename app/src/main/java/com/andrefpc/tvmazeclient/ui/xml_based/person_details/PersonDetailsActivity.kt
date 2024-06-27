package com.andrefpc.tvmazeclient.ui.xml_based.person_details

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.andrefpc.tvmazeclient.R
import com.andrefpc.tvmazeclient.core.data.Person
import com.andrefpc.tvmazeclient.core.data.Show
import com.andrefpc.tvmazeclient.databinding.ActivityPersonDetailsBinding
import com.andrefpc.tvmazeclient.databinding.ActivityShowDetailsBinding
import com.andrefpc.tvmazeclient.ui.xml_based.episode_details.EpisodeModal
import com.andrefpc.tvmazeclient.ui.xml_based.show_details.ShowDetailsActivity
import com.andrefpc.tvmazeclient.ui.xml_based.show_details.ShowDetailsViewModel
import com.andrefpc.tvmazeclient.ui.xml_based.show_details.ShowHeaderAdapter
import com.andrefpc.tvmazeclient.ui.xml_based.shows.ShowAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * Person details screen of the application
 */
class PersonDetailsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPersonDetailsBinding
    private val viewModel: PersonDetailsViewModel by viewModel()
    private var headerAdapter: PersonHeaderAdapter? = null
    private val showAdapter by lazy { ShowAdapter() }
    private val concatAdapter by lazy { ConcatAdapter(headerAdapter, showAdapter) }

    private lateinit var person: Person

    /**
     * Lifecycle method that run when the activity is created
     */
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

    /**
     * Init the list of favorite shows
     */
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
        binding.shimmerDetails.startProgress()
    }

    /**
     * Init the ViewModel observers
     */
    private fun initObservers() {
        viewModel.listShows.observe(this) {
            if(it.isEmpty()){
                headerAdapter?.hideLabel()
                Toast.makeText(this, getString(R.string.person_without_shows, person.name), Toast.LENGTH_SHORT).show()
            }else{
                showAdapter.submitList(it)
            }
            binding.shimmerDetails.stopProgress()
        }

        viewModel.error.observe(this) {
            Toast.makeText(this, getString(R.string.error_getting_shows, it.message), Toast.LENGTH_LONG).show()
        }
    }
}