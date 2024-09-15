package com.example.playerlist

import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.playerlist.Adapter.PlayerAdapter
import com.example.playerlist.Data.Player


class MainActivity : AppCompatActivity() {
    private lateinit var playerList: MutableList<Player>
    private lateinit var recyclerView: RecyclerView
    private lateinit var playerAdapter: PlayerAdapter
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var nameText: EditText
    private lateinit var ageText: EditText
    private lateinit var statusText: EditText
    private lateinit var ratingText: EditText


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sharedPreferences = getSharedPreferences("Player", MODE_PRIVATE)

        recyclerView = findViewById(R.id.recyclerView)
        nameText = findViewById(R.id.nameEt)
        ageText = findViewById(R.id.ageEt)
        statusText = findViewById(R.id.statusEt)
        ratingText = findViewById(R.id.ratingEt)
        playerList = retrivePlayer()

        val addButton: Button = findViewById(R.id.addBtn)
        addButton.setOnClickListener {
            val NameText = nameText.text.toString()
            val AgeText = ageText.text.toString().toInt()
            val StatusText = statusText.text.toString()
            val RatingText =ratingText.text.toString().toInt()
            if (NameText.isNotEmpty()) {
                val player = Player(NameText, AgeText, StatusText,RatingText.toInt())
                playerList.add(player)
                savePlayer(playerList)
                playerAdapter.notifyItemInserted(playerList.size - 1)
                nameText.text.clear()
                ageText.text.clear()
                statusText.text.clear()
                ratingText.text.clear()
            } else {
                Toast.makeText(this, "Can't be Empty", Toast.LENGTH_SHORT).show()
            }

        }
        playerAdapter = PlayerAdapter(playerList, object : PlayerAdapter.TaskClickListener {
            override fun onEditClick(position: Int) {
                nameText.setText(playerList[position].playerName)
                ageText.setText(playerList[position].playerAge)
                statusText.setText(playerList[position].playerStatus)
                ratingText.setText(playerList[position].playerRating)
                playerList.removeAt(position)
                playerAdapter.notifyDataSetChanged()
            }

            override fun onDeleteClick(position: Int) {
                val alertDialog = AlertDialog.Builder(this@MainActivity)
                alertDialog.setTitle("Delete Task")
                alertDialog.setMessage("Are you sure you want to delete this task?")
                alertDialog.setPositiveButton("Yes") { _, _ ->
                    deletePlayer(position)
                }
                alertDialog.setNegativeButton("No") { _, _ -> }
                alertDialog.show()

            }

        })

        recyclerView.adapter = playerAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)

    }
    private fun savePlayer(playerList: MutableList<Player>) {
        val editor = sharedPreferences.edit()
        val playerset = HashSet<String>()

        playerList.forEach { playerset.add(it.playerName)}
        editor.putStringSet("Name", playerset)
        editor.apply()
        playerList.forEach { playerset.add(it.playerAge.toString())}
        editor.putStringSet("age", playerset)
        editor.apply()
        playerList.forEach { playerset.add(it.playerStatus)}
        editor.putStringSet("status", playerset)
        editor.apply()
        playerList.forEach { playerset.add(it.playerRating.toString())}
        editor.putStringSet("rating", playerset)
        editor.apply()

    }

    private fun retrivePlayer(): MutableList<Player> {
        val player = sharedPreferences.getStringSet("player", HashSet())?:HashSet()
        return player.map{Player(it,0,it ,0)}.toMutableList()

    }

    private fun deletePlayer(position: Int) {
        playerList.removeAt(position)
        playerAdapter.notifyItemRemoved(position)
        savePlayer(playerList)
    }
}





    

    

