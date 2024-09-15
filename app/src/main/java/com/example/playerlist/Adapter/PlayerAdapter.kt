package com.example.playerlist.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.AdapterView.OnItemClickListener
import androidx.recyclerview.widget.RecyclerView
import com.example.playerlist.Data.Player
import com.example.playerlist.databinding.ListItemBinding

class PlayerAdapter(private val playerList: MutableList<Player>,
    private val clickListener: TaskClickListener):RecyclerView.Adapter<PlayerAdapter.PlayerViewHolder>() {
    interface TaskClickListener {
        fun onEditClick(position: Int)
        fun onDeleteClick(position: Int)

    }

    class PlayerViewHolder(val binding :ListItemBinding) :RecyclerView.ViewHolder(binding.root){
        fun bind(player: Player){
            binding.playerName.text =player.playerName
            binding.playerAge.text=player.playerAge.toString()
            binding.playerStatus.text=player.playerStatus
            binding.playerRating.text=player.playerRating.toString()
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlayerViewHolder {
        val binding=ListItemBinding.inflate(LayoutInflater.from(parent.context),
            parent,false)
        return PlayerViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return playerList.size
    }

    override fun onBindViewHolder(holder: PlayerViewHolder, position: Int) {
       val player=playerList[position]
        holder.bind(player)
        holder.binding.editButton.setOnClickListener {
            clickListener.onEditClick(position)
        }
        holder.binding.deleteButton.setOnClickListener {
            clickListener.onDeleteClick(position)
        }
    }
}