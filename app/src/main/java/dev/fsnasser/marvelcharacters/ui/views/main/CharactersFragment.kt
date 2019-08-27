package dev.fsnasser.marvelcharacters.ui.views.main


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dagger.android.support.DaggerFragment
import dev.fsnasser.marvelcharacters.R
import dev.fsnasser.marvelcharacters.ui.adapters.CharactersListAdapter
import dev.fsnasser.marvelcharacters.databinding.FragmentCharactersBinding
import dev.fsnasser.marvelcharacters.utils.views.SpacesItemDecoration
import javax.inject.Inject

class CharactersFragment : DaggerFragment() {

    private lateinit var mViewModel: MainViewModel

    private lateinit var mBinding: FragmentCharactersBinding

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var mLayoutManager: GridLayoutManager

    private var isLoadingMore = false

    private var isLastPage = false

    private var mStart = 0

    private var mNameStartsWith: String? = null

    private val mRecyclerViewOnScrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)

            val visibleItemCount = mLayoutManager.childCount
            val totalItemCount = mLayoutManager.itemCount
            val firstVisibleItemPosition = mLayoutManager.findFirstVisibleItemPosition()

            if(!isLoadingMore && !isLastPage) {
                if((visibleItemCount + firstVisibleItemPosition) >= totalItemCount
                    && firstVisibleItemPosition >= 0
                    && totalItemCount >= PAGE_LIMIT - 10) {
                    mStart += PAGE_LIMIT
                    isLoadingMore = true
                    mViewModel.getAll(mStart, PAGE_LIMIT, nameStartsWith = mNameStartsWith)
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activity?.let {
            mViewModel = ViewModelProviders.of(it, viewModelFactory).get(MainViewModel::class.java)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_characters, container,
            false)

        val adapter = CharactersListAdapter(ArrayList(), mViewModel)

        mBinding.apply {
            viewModel = mViewModel
            executePendingBindings()

            mLayoutManager = rvCharacters.layoutManager as GridLayoutManager
            rvCharacters.addOnScrollListener(mRecyclerViewOnScrollListener)

            rvCharacters.adapter = adapter

            rvCharacters.addItemDecoration(
                SpacesItemDecoration(
                    2,
                    8,
                    false
                )
            )

            srCharacters.setOnRefreshListener {
                mStart = 0
                isLastPage = false
                mViewModel.getAll(mStart, PAGE_LIMIT, nameStartsWith = mNameStartsWith,
                    refreshing = true)
            }
        }

        mViewModel.characters.observe(this, Observer { characters ->
            isLoadingMore = false

            if(characters.size < PAGE_LIMIT) isLastPage = true

            if(mStart == 0) adapter.updateCharactersList(characters)
            else adapter.addCharactersToList(characters)
        })

        mViewModel.search.observe(this, Observer { nameStartsWith ->
            mStart = 0
            isLastPage = false
            mNameStartsWith = nameStartsWith
            nameStartsWith?.let {
                mViewModel.getAll(mStart, PAGE_LIMIT, nameStartsWith = mNameStartsWith)
            } ?: run {
                mViewModel.getAll(mStart, PAGE_LIMIT)
            }
        })

        mViewModel.updateFavoriteItemId.observe(this, Observer { characterId ->
            characterId?.let {
                adapter.updateFavoriteItem(characterId)
            }
        })

        mViewModel.getAll(mStart, PAGE_LIMIT)

        return mBinding.root
    }

    companion object {
        private const val PAGE_LIMIT = 20

        @JvmStatic
        fun newInstance() = CharactersFragment()
    }

}
