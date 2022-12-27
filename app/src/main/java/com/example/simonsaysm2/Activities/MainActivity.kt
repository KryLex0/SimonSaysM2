package com.example.simonsaysm2.Activities


import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
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
import kotlin.concurrent.timer
import kotlinx.coroutines.*



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
    private var timerParty = initTimer()
    private var timerRunning: Boolean = true


    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_party)

        this.listeBtnNextColor.addAll(arrayOf(btn_color1, btn_color2, btn_color3))

        this.nbBtnOpt = this.intent.getIntExtra("nb_btn_opt", 1)
        this.difficulty = this.intent.getStringExtra("difficulty").toString()
        this.animBtn = this.intent.getBooleanExtra("animSwitch", this.animBtn)

        startTimer()

        btnDataParty()  //display 1/2/3 btn in header based on the numbers of new colors foreach round set in the settings
        setBtnDifficulte()  //display 2/4/6 btn in the layout based on the difficulty
        nextLevel() //start game


    }
    //update timer for minutes and seconds
    private fun initTimer():  CountDownTimer{
        val timer = object: CountDownTimer(10000000000000000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                //cast to int
                var tmpSeconds = Integer.parseInt(timerSeconds.text as String)
                var tmpMinutes = Integer.parseInt(timerMinutes.text as String)

                var newTimerMinutes = ""
                var newTimerSeconds = ""

                if(timerRunning) {
                    if (tmpSeconds == 59) {
                        tmpMinutes += 1
                        tmpSeconds = 0
                    } else {
                        tmpSeconds += 1
                    }
                }

                newTimerMinutes = tmpMinutes.toString()
                newTimerSeconds = tmpSeconds.toString()

                //add "0" at the start if lower than 10
                if(tmpMinutes < 10){
                    newTimerMinutes = "0$newTimerMinutes"
                }
                if(tmpSeconds < 10){
                    newTimerSeconds = "0$newTimerSeconds"
                }
                timerMinutes.text = newTimerMinutes
                timerSeconds.text = newTimerSeconds
            }
            override fun onFinish() {
                // do something
            }

        }

        return timer
    }
    private fun startTimer(){
        timerParty.start()
        timerRunning = true
    }
    private fun pauseTimer(){
        timerParty.cancel()
        timerRunning = false
    }



    //function to display 2/4/6 buttons in the layout based on difficulty set in settings
    private fun setBtnDifficulte() {
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

        //add listener to each btn
        tab.forEach {
            it.visibility = View.VISIBLE
            val btn = it.id
            Log.d("123321", it.toString())
            it.setOnClickListener {
                //add user btn choice to array
                listColorPlayer.add(btn.toString())
                //compare number of btn clicked by user and total btn
                //to check when the user click on the max btn
                if(listColorParty.size == listColorPlayer.size) {
                    //verify the colors from user
                    this.verifCouleur()
                }
            }
        }

    }


    //animation of buttons
    private fun animButton(btn: MutableList<Button>, index: Int){
        val anim: Animation = AlphaAnimation(1.0f, 0.2f)
        anim.duration = 250
        anim.startOffset = 500
        anim.interpolator = LinearInterpolator()
        anim.repeatMode = Animation.REVERSE

        btn[index].startAnimation(anim)

        //make the animations running one by one
        anim.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationRepeat(animation: Animation?) {}
            override fun onAnimationStart(animation: Animation?) {
                pauseTimer()
            }

            //wait current animation to end before starting the next one
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
                startTimer()

            }
        })



    }



    //colored btn in layout to know next colors based on difficulty
    private fun btnDataParty(){
        this.listeBtnNextColor.forEach {
            it.visibility = View.GONE
        }
        for(i in 1..nbBtnOpt) {
            this.listeBtnNextColor[i-1].visibility = View.VISIBLE
        }
    }

    //function to add new random colors for next round
    private fun nextLevel() {
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

            //animate colored btn in header to notify user these are the new colors
            val anim: Animation = AlphaAnimation(1.0f, 0.2f)
            anim.duration = 100
            anim.startOffset = 200
            anim.interpolator = LinearInterpolator()
            anim.repeatMode = Animation.REVERSE
            this.listeBtnNextColor[j-1].startAnimation(anim)

            //change btn colors in header
            this.listeBtnNextColor[j-1].setBackgroundColor(Color.parseColor(tabColorHex[temp]))
        }

        //if last round was won and animation are enabled in settings, anim all btn to remember
        if(this.gagne && this.animBtn) {
            tab.forEach {
                it.isClickable = false
            }
            animButton(listColorParty, 0)
        }

    }




    @SuppressLint("SetTextI18n")
    //check if user choices are the same as the generated colors
    private fun verifCouleur(){
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
        //if the user choices are correct, update score and clear array of colors in header
        if(gagne){
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



    //create popup to display user score and let user choose a name
    private fun popupNamePlayer(){
        val builder = AlertDialog.Builder(this)

        builder.setTitle("Partie terminé")
        var timerTotalValue = timerMinutes.text.toString() + ":" + timerSeconds.text.toString()
        builder.setMessage("Votre score est de " + tv_val_score.text + "\nDurée de la partie: " + timerTotalValue)
        builder.setCancelable(false)

        val editTextNamePlayer = EditText(this)
        builder.setView(editTextNamePlayer)

        builder.setPositiveButton("Ok") { _, _ ->
            saveDataPlayer(editTextNamePlayer, tv_val_score, timerTotalValue)    //ajout a la bdd avec le score et le nom saisie dans l'edit text
            finish()
            val intent = Intent(this, ActivityStart::class.java)
            startActivity(intent)
        }
        builder.show()
    }


    //save date in DB
    private fun saveDataPlayer(playerDataName: EditText, playerDataScore: TextView, playerDataTime: String) {
        var nomJoueur: String
        if(playerDataName.text.isEmpty()){
            nomJoueur = "???"
        }else{
            nomJoueur = playerDataName.text.toString()
        }

        val scoreP = (playerDataScore.text.toString()).toLong()

        Log.d("test123", nomJoueur)

        AppDatabase.get(application).playerDao().insertPlayer(Player(nomJoueur, scoreP, this.difficulty, playerDataTime))
        Log.d("test123", "$nomJoueur a bien ete ajoute avec un score de $scoreP")
    }


}