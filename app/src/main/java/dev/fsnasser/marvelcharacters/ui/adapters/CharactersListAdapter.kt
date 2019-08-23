package dev.fsnasser.marvelcharacters.ui.adapters

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

class CharactersListAdapter(var characters: List<Character>) : RecyclerView.Adapter<CharactersListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: CharacterListItemBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context), R.layout.character_list_item, parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = characters.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.init(characters[position])
    }


    inner class ViewHolder(binding: CharacterListItemBinding) : RecyclerView.ViewHolder(binding.root) {

        private val mBinding = binding

        fun init(characterItem: Character) {
            mBinding.apply {
                character = characterItem
                executePendingBindings()

                characterItem.image?.let { image ->
                    Picasso.with(mBinding.root.context).load(image).into(ivCharacterItem)
                }

                if(characterItem.isFavorite) {
                    ivCharacterItemFavorite.setOnClickListener {
                        Toast.makeText(mBinding.root.context, "Removido dos favoritos",
                            Toast.LENGTH_SHORT).show()
                    }
                    ivCharacterItemFavorite.setImageDrawable(
                        ContextCompat.getDrawable(mBinding.root.context, R.drawable.ic_star))
                } else {
                    ivCharacterItemFavorite.setOnClickListener {
                        Toast.makeText(mBinding.root.context, "Adicionado aos favoritos",
                            Toast.LENGTH_SHORT).show()
                    }
                    ivCharacterItemFavorite.setImageDrawable(
                        ContextCompat.getDrawable(mBinding.root.context, R.drawable.ic_star_border))
                }

                clCharacterItemContainer.setOnClickListener {
                    //TODO: Ir para a tela de detalhe do personagem
                }
            }
        }

    }

}