package com.example.simonsaysm2.Activities


import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.view.animation.LinearInterpolator
import android.widget.Button
import android.widget.EditText
import android.widget.TableRow
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.simonsaysm2.Database.AppDatabase
import com.example.simonsaysm2.Database.Player
import com.example.simonsaysm2.R
import kotlinx.android.synthetic.main.activity_main_party.*
import kotlinx.android.synthetic.main.data_party.*


class MainActivity : AppCompatActivity() {
    private var temp: Int = 0
    private val tabColorName = arrayOf("RED", "BLUE", "GREEN", "YELLOW", "BROWN", "PURPLE")
    private val tabColorHex = arrayOf("#FF0000", "#4169E1", "#008000", "#FFFF00", "#A73A03", "#D006BE")
    private val colorR = arrayOf(R.color.red, R.color.blue, R.color.green, R.color.yellow, R.color.brown, R.color.purple)
    private var listeBtnNextColor: MutableList<Button> = mutableListOf()
    private var gagne = true
    private var btnColor: MutableList<Button> = mutableListOf()

    private lateinit var difficulty: String
    private var nbBtnOpt: Int = 1
    private var animBtn = true

    private var tab: MutableList<Button> = mutableListOf()//tab = arrayOf<Button>()

    private var listColorParty: MutableList<Button> = mutableListOf() //= arrayOf<Button>()
    private var listColorPlayer: MutableList<String> = mutableListOf() //= arrayOf<String>()


    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_party)

        //recupere le nombre de btn de couleur a retenir chaque tour (1,2,3) | le niveau de difficulté (Facile, Nomal, Difficile) | utilisation d'animation ou non pour les boutons

        this.listeBtnNextColor.addAll(arrayOf(btn_color1, btn_color2, btn_color3))

        this.nbBtnOpt = this.intent.getIntExtra("nb_btn_opt", 1)
        this.difficulty = this.intent.getStringExtra("difficulty").toString()
        this.animBtn = this.intent.getBooleanExtra("animSwitch", this.animBtn)


        btnDataParty()  //affiche 1/2/3 boutons dans l'en tête suivant le nombre de boutons par tours (options)
        setBtnDifficulte()  //affiche 2/4/6 boutons suivant la difficulté choisit (options)
        nextLevel() //lancement de la 1ere manche

    }



    private fun setBtnDifficulte() {    //fonction qui affiche 2/4/6 boutons en fonction de la difficulté (dans les parametres)
        if(this.difficulty == "Facile"){
            table_row_1.visibility = View.VISIBLE
            this.tab.addAll(listOf(btn_1_T, btn_2_T))
        }
        else if(this.difficulty == "Normal"){
            table_row_1.visibility = View.VISIBLE
            table_row_2.visibility = View.VISIBLE
            this.tab.addAll(listOf(btn_1_T, btn_2_T, btn_3_T, btn_4_T))
        }
        else if(this.difficulty == "Difficile"){
            table_row_1.visibility = View.VISIBLE
            table_row_2.visibility = View.VISIBLE
            table_row_3.visibility = View.VISIBLE
            this.tab.addAll(listOf(btn_1_T, btn_2_T, btn_3_T, btn_4_T, btn_5_T, btn_6_T))
        }

        tab.forEach {   //permet l'ajout d'un listener pour chaque boutons
            it.visibility = View.VISIBLE
            val btn = it.id
            Log.d("123321", it.toString())
            it.setOnClickListener {
                listColorPlayer.add(btn.toString())  //ajout du bouton clique par le joueur dans une liste
                if(listColorParty.size == listColorPlayer.size) {   //compare le nombre de boutons clique par le joueur au nombre de bouton a cliquer dans la partie actuelle
                    this.verifCouleur() //appel de la fonction qui compare les boutons du joueurs avec les boutons generes
                }
            }
        }

    }





    private fun animButton(btn: MutableList<Button>, index: Int){   //fonction d'animation des boutons
        val anim: Animation = AlphaAnimation(1.0f, 0.2f)
        anim.duration = 250
        anim.startOffset = 500
        anim.interpolator = LinearInterpolator()
        anim.repeatMode = Animation.REVERSE

        btn[index].startAnimation(anim)

        //evite de lancer plusieurs fois la meme animation d'un bouton en même temps
        anim.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationRepeat(animation: Animation?) {}
            override fun onAnimationStart(animation: Animation?) {}

            //permet de bien attendre la fin de chaque animations pour commencer la suivante
            override fun onAnimationEnd(animation: Animation?) {
                val index1 = index + 1
                if(index < btn.size - 1) {
                    animButton(btn, index1)
                }
                if(index == btn.size-1){
                    tab.forEach {
                        it.isClickable = true
                    }

                }
            }
        })

    }




    private fun btnDataParty(){ //bouton permettant de connaitre les prochaines couleurs (1,2 ou 3 boutons par tour suivant le choix dans les paramètres)
        this.listeBtnNextColor.forEach {
            it.visibility = View.GONE
        }
        for(i in 1..nbBtnOpt) {
            this.listeBtnNextColor[i-1].visibility = View.VISIBLE
        }
    }


    private fun nextLevel() {       //fonction pour generer les prochaines couleurs a retenir
        for(j in 1..this.nbBtnOpt){
            var i=0
            if (this.difficulty == "Facile") {
                i = 1
            } else if (this.difficulty == "Normal") {
                i = 3
            } else if (this.difficulty == "Difficile") {
                i = 5
            }

            temp = (0..i).random()
            listColorParty.add(tab[temp])

            //animation des boutons de l'entete pour permettre a l'utilisateur de comprendre que ce sont de nouvelles couleurs
            val anim: Animation = AlphaAnimation(1.0f, 0.2f)
            anim.duration = 100
            anim.startOffset = 200
            anim.interpolator = LinearInterpolator()
            anim.repeatMode = Animation.REVERSE
            this.listeBtnNextColor[j-1].startAnimation(anim)

            //change la couleur des boutons dans l'en tete pour connaitre les prochaines couleurs
            //this.listeBtnNextColor[j-1].text = tabColorName[temp]
            this.listeBtnNextColor[j-1].setBackgroundColor(Color.parseColor(tabColorHex[temp]))
        }

        if(this.gagne && this.animBtn) {    //si la derniere manche est gagné et l'animation des boutons est active (choix dans les parametres), anim les boutons des couleurs a retenir
            tab.forEach {
                it.isClickable = false
            }
            animButton(listColorParty, 0)
        }

    }




    @SuppressLint("SetTextI18n")
    private fun verifCouleur(){     //fonction qui verifie les couleurs generé aléatoirement avec celles saisie par le joueur
        var i = 0
        while(i<listColorParty.size){

            if(listColorParty[i].id.toString() == listColorPlayer[i]){
                Log.d("test", "gagné")
                gagne = true
            } else {
                Log.d("test", "perdu")
                gagne = false
                break
            }
            i += 1
        }
        if(gagne){
            //incremente la score suivant la difficulté et clear la liste des boutons saisies pour le prochain tour
            Toast.makeText(this, "Gagné", Toast.LENGTH_SHORT).show()
            var scoreTemp = 0
            if(this.nbBtnOpt == 1){scoreTemp = 1}else if(this.nbBtnOpt == 2){scoreTemp = 2} else if(this.nbBtnOpt == 3){scoreTemp = 3}
            tv_val_score.text = "${tv_val_score.text.toString().toInt() + scoreTemp}"
            listColorPlayer.clear()
        }else{
            Toast.makeText(this, "Perdu", Toast.LENGTH_SHORT).show()
            this.popupNamePlayer()
        }
        this.nextLevel()
    }




    private fun popupNamePlayer(){  //crée une popup qui contient le score ainsi qu'un champs pour saisir le nom
        val builder = AlertDialog.Builder(this)

        builder.setTitle("Partie terminé")
        builder.setMessage("Votre score est de " + tv_val_score.text)
        builder.setCancelable(false)

        val editTextNamePlayer = EditText(this)
        builder.setView(editTextNamePlayer)

        builder.setPositiveButton("Ok") { _, _ ->
            saveDataPlayer(editTextNamePlayer, tv_val_score)    //ajout a la bdd avec le score et le nom saisie dans l'edit text
            finish()
            val intent = Intent(this, ActivityStart::class.java)
            startActivity(intent)
        }
        builder.show()
    }



    private fun saveDataPlayer(playerDataName: EditText, playerDataScore: TextView) {
        var nomJoueur: String
        if(playerDataName.text.isEmpty()){
            nomJoueur = "???"
        }else{
            nomJoueur = playerDataName.text.toString()
        }

        val scoreP = (playerDataScore.text.toString()).toLong()

        Log.d("test123", nomJoueur)

        AppDatabase.get(application).playerDao().insertPlayer(Player(nomJoueur, scoreP, this.difficulty))
        Log.d("test123", "$nomJoueur a bien ete ajoute avec un score de $scoreP")
    }


}