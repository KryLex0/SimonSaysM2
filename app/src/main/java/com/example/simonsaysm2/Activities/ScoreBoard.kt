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


    private fun removeAll(){    //remove tout le contenue de la bdd
        AppDatabase.get(application).playerDao().deleteAll()
        finish()
        startActivity(intent)
    }


    //fonction qui recupere les 10 meilleurs score en fonction du niveau de difficulté (Facile, Normal, Difficile) OU tous sans distinction
    private fun dataScorePlayer(difficulty: String) {
        tabContainer.removeAllViews()   //clear le contenu du scoreBoard

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


        if(listPlayerData.isEmpty()){   //Affiche un message si aucune donnée présente dans la bdd suivant le niveau de difficulté
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



    private fun displayPlayerHighscore(listPlayerData: List<Player>) {   //affiche les 10 derniers meilleurs score (ajout dynamiquement)
        var rang = 1


        listPlayerData.forEach { it ->
            //creation d'un linear layout qui contiendra les infos
            val parent = LinearLayout(this)
            parent.layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            parent.orientation = LinearLayout.HORIZONTAL

            //textview qui contient le rang
            val tv1 = TextView(this)
            tv1.text = rang.toString()
            tv1.layoutParams =
                LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f)
            tv1.textAlignment = View.TEXT_ALIGNMENT_CENTER
            tv1.setBackgroundResource(R.drawable.row_borders)
            tv1.setPadding(15)
            parent.addView(tv1)

            //textview qui contient le nom
            val tv2 = TextView(this)
            tv2.text = it.name
            tv2.layoutParams =
                LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f)
            tv2.textAlignment = View.TEXT_ALIGNMENT_CENTER
            tv2.setBackgroundResource(R.drawable.row_borders)
            tv2.setPadding(15)
            parent.addView(tv2)

            //textview qui contient le score
            val tv3 = TextView(this)
            tv3.text = it.score.toString()
            tv3.layoutParams =
                LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f)
            tv3.textAlignment = View.TEXT_ALIGNMENT_CENTER
            tv3.setBackgroundResource(R.drawable.row_borders)
            tv3.setPadding(15)
            parent.addView(tv3)

            //textview qui permet de supprimer la ligne de la bdd
            val tv4 = TextView(this)
            tv4.text = "X"
            tv4.layoutParams =
                LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f)
            tv4.textAlignment = View.TEXT_ALIGNMENT_CENTER
            tv4.setBackgroundResource(R.drawable.row_borders)
            tv4.setPadding(15)

            val playerId = it.id
            tv4.setOnClickListener {
                //popup de confirmation de suppression de la ligne
                val builder = AlertDialog.Builder(this)

                builder.setTitle("Supprimer?")
                builder.setMessage("Rang: " + tv1.text + "\nNom: " + tv2.text + "\nScore: " + tv3.text)
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

            tabContainer.addView(parent)    //ajout dans un container (déja présent dans le xml activity_score_board) chaque ligne généré dynamiquement

            rang += 1
        }

    }



}