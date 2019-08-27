package dev.fsnasser.marvelcharacters.ui.adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import dev.fsnasser.marvelcharacters.R
import dev.fsnasser.marvelcharacters.databinding.CharacterListItemBinding
import dev.fsnasser.marvelcharacters.ui.entities.Character
import dev.fsnasser.marvelcharacters.ui.views.detail.CharacterDetailActivity
import dev.fsnasser.marvelcharacters.ui.views.main.MainActivity
import dev.fsnasser.marvelcharacters.ui.views.main.MainViewModel

class CharactersListAdapter(private var characters: ArrayList<Character>,
                            private val mainViewModel: MainViewModel)
    : RecyclerView.Adapter<CharactersListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: CharacterListItemBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context), R.layout.character_list_item, parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = characters.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.init(characters[position])
    }

    fun updateCharactersList(charactersList: List<Character>) {
        characters = ArrayList(charactersList)
        notifyDataSetChanged()
    }

    fun addCharactersToList(charactersList: List<Character>) {
        characters.addAll(charactersList)
        notifyDataSetChanged()
    }

    fun updateFavoriteItem(characterId: Long) {
        val character = characters.firstOrNull{ it.id == characterId }
        character?.let {
            it.isFavorite = !it.isFavorite
            notifyDataSetChanged()
            mainViewModel.getAllFavorites()
        }
    }

    inner class ViewHolder(binding: CharacterListItemBinding) : RecyclerView.ViewHolder(binding.root) {

        private val mBinding = binding

        private val mContext = mBinding.root.context

        fun init(characterItem: Character) {
            mBinding.apply {
                tvCharacterItemName.text = characterItem.name

                if(!characterItem.thumbnail.isNullOrBlank()) {
                    Picasso.with(mContext)
                        .load("${characterItem.thumbnail}/standard_xlarge.${characterItem.thumbnailExt}")
                        .into(ivCharacterItem)
                }

                if(characterItem.isFavorite) {
                    ivCharacterItemFavorite.setOnClickListener {
                        mainViewModel.removeFromFavorites(characterItem)
                        Toast.makeText(mContext, mContext.getString(R.string.removed_from_favorites),
                            Toast.LENGTH_SHORT).show()
                        ivCharacterItemFavorite.setImageDrawable(
                            ContextCompat.getDrawable(mContext, R.drawable.ic_star_border))
                        mainViewModel.updateFavoriteItemId.value = characterItem.id
                    }

                    ivCharacterItemFavorite.setImageDrawable(
                        ContextCompat.getDrawable(mContext, R.drawable.ic_star))
                } else {
                    ivCharacterItemFavorite.setOnClickListener {
                        mainViewModel.addToFavorites(characterItem)
                        Toast.makeText(mContext, mContext.getString(R.string.added_to_favorites),
                            Toast.LENGTH_SHORT).show()
                        ivCharacterItemFavorite.setImageDrawable(
                            ContextCompat.getDrawable(mContext, R.drawable.ic_star))
                        mainViewModel.updateFavoriteItemId.value = characterItem.id
                    }

                    ivCharacterItemFavorite.setImageDrawable(
                        ContextCompat.getDrawable(mContext, R.drawable.ic_star_border))
                }

                clCharacterItemContainer.setOnClickListener {
                    val intent = Intent(mContext, CharacterDetailActivity::class.java)
                    intent.putExtra(CharacterDetailActivity.CHARACTER_OBJ, characterItem)
                    val activity = mContext as MainActivity
                    activity.startActivityForResult(intent, MainActivity.DETAIL_ACTIVITY_REQ)
                }
            }
        }

    }

}