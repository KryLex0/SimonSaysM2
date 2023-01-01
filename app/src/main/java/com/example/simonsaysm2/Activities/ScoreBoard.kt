package com.example.simonsaysm2.Activities


import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.setPadding
import com.example.simonsaysm2.Database.AppDatabase
import com.example.simonsaysm2.Database.Player
import com.example.simonsaysm2.R
import kotlinx.android.synthetic.main.activity_score_board.*


class ScoreBoard : AppCompatActivity() {
    //private lateinit var btn_remove_all: Button
    private var difficultyHighscore: String = "All"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_score_board)

        btn_remove_all.setOnClickListener {
            removeAll()
        }

        spinner_difficulty_highscore.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, pos: Int, id: Long) {
                difficultyHighscore = spinner_difficulty_highscore.selectedItem.toString()
                dataScorePlayer(difficultyHighscore)
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    //remove all content of DB
    private fun removeAll(){
        AppDatabase.get(application).playerDao().deleteAll()
        finish()
        startActivity(intent)
    }


    //retrieve last 10 highscore based on difficulty
    private fun dataScorePlayer(difficulty: String) {
        tabContainer.removeAllViews()   //clear content of scoreBoard

        var listPlayerData: List<Player> = AppDatabase.get(application).playerDao().getTenLastAll()
        if(difficulty == "All"){
            listPlayerData = AppDatabase.get(application).playerDao().getTenLastAll()
        }else if(difficulty == "Facile"){
            listPlayerData = AppDatabase.get(application).playerDao().getTenLastFacile()
        }else if(difficulty == "Normal"){
            listPlayerData = AppDatabase.get(application).playerDao().getTenLastNormal()
        }else if(difficulty == "Difficile"){
            listPlayerData = AppDatabase.get(application).playerDao().getTenLastDifficile()
        }

        //if no data in database for selected difficulty, display a message to notify user
        if(listPlayerData.isEmpty()){
            var emptyV = TextView(this)
            emptyV.textSize = 20F
            emptyV.text = "Aucune donnée disponible"
            emptyV.layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            emptyV.textAlignment = View.TEXT_ALIGNMENT_CENTER
            tabContainer.addView(emptyV)
        }

        displayPlayerHighscore(listPlayerData)
    }



    //display last 10 highscores dynamically
    private fun displayPlayerHighscore(listPlayerData: List<Player>) {
        var rang = 1


        listPlayerData.forEach { it ->
            //create linearLayout to display each DB lines
            val parent = LinearLayout(this)
            parent.layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            parent.orientation = LinearLayout.HORIZONTAL
/*
            //textview for the ranking
            val tv0 = TextView(this)
            tv0.text = rang.toString()
            tv0.layoutParams =
                LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f)
            tv0.textAlignment = View.TEXT_ALIGNMENT_CENTER
            tv0.setBackgroundResource(R.drawable.row_borders)
            tv0.setPadding(15)
            parent.addView(tv0)

            //textview for name
            val tv1 = TextView(this)
            tv1.text = it.name
            tv1.layoutParams =
                LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f)
            tv1.textAlignment = View.TEXT_ALIGNMENT_CENTER
            tv1.setBackgroundResource(R.drawable.row_borders)
            tv1.setPadding(15)
            parent.addView(tv1)

            //textview for score
            val tv2 = TextView(this)
            tv2.text = it.score.toString()
            tv2.layoutParams =
                LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f)
            tv2.textAlignment = View.TEXT_ALIGNMENT_CENTER
            tv2.setBackgroundResource(R.drawable.row_borders)
            tv2.setPadding(15)
            parent.addView(tv2)

            //textview for duration
            val tv3 = TextView(this)
            tv3.text = it.time.toString()
            tv3.layoutParams =
                LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f)
            tv3.textAlignment = View.TEXT_ALIGNMENT_CENTER
            tv3.setBackgroundResource(R.drawable.row_borders)
            tv3.setPadding(15)
            parent.addView(tv3)

            //textview to delete the line in DB
            val tv4 = TextView(this)
            tv4.text = "X"
            tv4.layoutParams =
                LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f)
            tv4.textAlignment = View.TEXT_ALIGNMENT_CENTER
            tv4.setBackgroundResource(R.drawable.row_borders)
            tv4.setPadding(15)
*/
            //textview for the ranking
            val tv0 = TextView(this)
            //textview for name
            val tv1 = TextView(this)
            //textview for score
            val tv2 = TextView(this)
            //textview for duration
            val tv3 = TextView(this)
            //textview to delete the line in DB
            val tv4 = TextView(this)
            var tmpArrayTextView = arrayOf(tv0, tv1, tv2, tv3, tv4)
            var tmpText = ""
            var weight: Float

            for(i in 0..4) {
                weight = 1.0f
                when (i) {
                    0 -> {
                        tmpText = rang.toString()
                        weight = 0.5f
                    }
                    1 -> {
                        tmpText = it.name
                    }
                    2 -> {
                        tmpText = it.score.toString()
                    }
                    3 -> {
                        tmpText = it.time
                    }
                    4 -> {
                        tmpText = "X"
                    }
                }
                tmpArrayTextView[i].text = tmpText
                tmpArrayTextView[i].layoutParams =
                    LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, weight)
                tmpArrayTextView[i].textAlignment = View.TEXT_ALIGNMENT_CENTER
                tmpArrayTextView[i].setBackgroundResource(R.drawable.row_borders)
                tmpArrayTextView[i].setPadding(15)
                if(i != 4) {
                    parent.addView(tmpArrayTextView[i])
                }
            }



            val playerId = it.id
            tv4.setOnClickListener {
                //popup de confirmation de suppression de la ligne
                val builder = AlertDialog.Builder(this)

                builder.setTitle("Supprimer?")
                builder.setMessage("Rang: " + tv0.text + "\nNom: " + tv1.text + "\nScore: " + tv2.text + "\nTemps: " + tv3.text)
                builder.setCancelable(false)

                builder.setPositiveButton("Oui") { _, _ ->
                    AppDatabase.get(application).playerDao().deletePlayerFromId(playerId)
                    Toast.makeText(this, "Le score selectionné a bien été supprimé", Toast.LENGTH_SHORT).show()
                    finish()
                    startActivity(intent)
                }
                builder.setNegativeButton("Non") { _, _ -> }
                builder.show()
            }
            parent.addView(tv4)

            //add all textviews to a container already create in xml activity_score_board
            tabContainer.addView(parent)

            rang += 1
        }

    }



}