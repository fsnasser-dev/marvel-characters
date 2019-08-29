package dev.fsnasser.marvelcharacters.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import dev.fsnasser.marvelcharacters.R
import dev.fsnasser.marvelcharacters.databinding.ComicSeriesListItemBinding
import dev.fsnasser.marvelcharacters.ui.entities.Character

class ComicsSeriesAdapter(private var comicsSeries: List<Character.ComicSerie>)
    : RecyclerView.Adapter<ComicsSeriesAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: ComicSeriesListItemBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context), R.layout.comic_series_list_item, parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = comicsSeries.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.init(comicsSeries[position])
    }

    fun update(comicSeriesList: List<Character.ComicSerie>) {
        comicsSeries = comicSeriesList
        notifyDataSetChanged()
    }

    inner class ViewHolder(binding: ComicSeriesListItemBinding) : RecyclerView.ViewHolder(binding.root) {

        private val mBinding = binding

        private val mContext = mBinding.root.context

        fun init(comicSerieItem: Character.ComicSerie) {
            mBinding.apply {
                comicSerie = comicSerieItem
                executePendingBindings()

                if(!comicSerieItem.thumbnail.isNullOrBlank()) {
                    Picasso.get().load("${comicSerieItem.thumbnail}/portrait_xlarge.jpg")
                        .into(ivComicSeriesImg)
                }
            }
        }

    }

}