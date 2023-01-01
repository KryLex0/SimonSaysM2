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
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.example.simonsaysm2.Database.AppDatabase
import com.example.simonsaysm2.Database.Player
import com.example.simonsaysm2.R
import kotlinx.android.synthetic.main.activity_main_party.*
import kotlinx.android.synthetic.main.data_party.*
import kotlinx.coroutines.*
import kotlin.random.Random
import kotlin.random.nextInt


class MainActivity : AppCompatActivity() {
    private var temp: Int = 0
    //private val tabColorName = arrayOf("RED", "BLUE", "GREEN", "YELLOW", "BROWN", "PURPLE")
    private val tabColorHex = arrayOf("#FF0000", "#4169E1", "#008000", "#FFFF00", "#A73A03", "#D006BE")
    //private val colorR = arrayOf(R.color.red, R.color.blue, R.color.green, R.color.yellow, R.color.brown, R.color.purple)
    private var listBtnNextColor: MutableList<Button> = mutableListOf()
    private var gagne = true
    //private var btnColor: MutableList<Button> = mutableListOf()

    private lateinit var difficulty: String
    private var nbBtnOpt: Int = 1
    private var animBtn = true
    private var competitiveMode = false

    private var tab: MutableList<Button> = mutableListOf()//tab = arrayOf<Button>()

    private var listColorParty: MutableList<Button> = mutableListOf() //= arrayOf<Button>()
    private var listColorPlayer: MutableList<String> = mutableListOf() //= arrayOf<String>()
    //global timer for the whole game
    private var timerParty = initTimerGlobal()
    private var timerRunning: Boolean = true

    private var timerRound = initTimerRound()
    private var timeRound = 0
    private var listTimeRound: MutableList<Int> = mutableListOf()
    private var listRapidityRound: MutableList<String> = mutableListOf()

    //timer for each round (used in the gamemode "against the watch")
    //private var timeRound: Long = 5000
    private lateinit var timerLeft: String// = tv_val_score.text.toString()
    private var isTimerLeftRoundRunning = false
    private lateinit var timerLeftRound: CountDownTimer// = initTimerLeftRound(timerLeft)


    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_party)

        this.listBtnNextColor.addAll(arrayOf(btn_color1, btn_color2, btn_color3))

        this.nbBtnOpt = this.intent.getIntExtra("nb_btn_opt", 1)
        this.difficulty = this.intent.getStringExtra("difficulty").toString()
        this.animBtn = this.intent.getBooleanExtra("animSwitch", this.animBtn)
        //this.competitiveMode = this.intent.getBooleanExtra("competitiveMode", this.competitiveMode)
        //if(!this.competitiveMode){
        //    timerLayoutLeft.visibility = View.VISIBLE
        //}
        //timerLeft = tv_val_score.text.toString()
        restartTimerLeft("0")
        startTimer(timerParty)

        btnDataParty()  //display 1/2/3 btn in header based on the numbers of new colors foreach round set in the settings
        setBtnDifficulty()  //display 2/4/6 btn in the layout based on the difficulty
        nextLevel() //start game




    }
    //update timer for minutes and seconds
    private fun initTimerGlobal():  CountDownTimer{
        val timer = object: CountDownTimer(10000000000000000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                //cast to int
                var tmpSeconds = Integer.parseInt(timerSeconds.text as String)
                var tmpMinutes = Integer.parseInt(timerMinutes.text as String)

                var newTimerMinutes: String
                var newTimerSeconds: String

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
    private fun startTimer(timer: CountDownTimer){
        timer.start()
        timerRunning = true
    }
    private fun pauseTimer(timer: CountDownTimer){
        timer.cancel()
        timerRunning = false
    }
    private fun startTimerLeft(timer: CountDownTimer){
        timer.start()
        isTimerLeftRoundRunning = true
    }
    private fun pauseTimerLeft(timer: CountDownTimer){
        timer.cancel()
        isTimerLeftRoundRunning = false
    }
    private fun restartTimerLeft(timerLeft: String){
        val newTimerLeft = timerLeft.toLong() + 3
        timerSecondsLeft.text = newTimerLeft.toString()

    }

    //init a timer for each round
    private fun initTimerRound():  CountDownTimer{
        val timer = object: CountDownTimer(1000000000000000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                if(timerRunning) {
                    timeRound += 1000
                }
            }
            override fun onFinish() {}
        }
        return timer
    }


    //init a timer that decrement for each round (gamemode against the watch)
    private fun initTimerLeftRound(timerLeft: String):  CountDownTimer{
        val newTimerLeft = timerLeft.toLong() + 3
        val timeInFuture = newTimerLeft * 1000
        val timer = object: CountDownTimer(timeInFuture, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                if(isTimerLeftRoundRunning) {
                    val newSecondsLeft = Integer.parseInt(timerSecondsLeft.text as String)
                    timerSecondsLeft.text = (newSecondsLeft - 1).toString()
                }

            }
            override fun onFinish() {
                Toast.makeText(this@MainActivity, "Temps écoulé", Toast.LENGTH_SHORT).show()
                popupNamePlayer()
            }
        }
        return timer
    }



    //function to display 2/4/6 buttons in the layout based on difficulty set in settings
    private fun setBtnDifficulty() {
        when (this.difficulty) {
            "Facile" -> {
                table_row_1.visibility = View.VISIBLE
                this.tab.addAll(listOf(btn_1_T, btn_2_T))
            }
            "Normal" -> {
                table_row_1.visibility = View.VISIBLE
                table_row_2.visibility = View.VISIBLE
                this.tab.addAll(listOf(btn_1_T, btn_2_T, btn_3_T, btn_4_T))
            }
            "Difficile" -> {
                table_row_1.visibility = View.VISIBLE
                table_row_2.visibility = View.VISIBLE
                table_row_3.visibility = View.VISIBLE
                this.tab.addAll(listOf(btn_1_T, btn_2_T, btn_3_T, btn_4_T, btn_5_T, btn_6_T))
            }
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
        pauseTimer(timerParty)
        if(isTimerLeftRoundRunning) {
            pauseTimerLeft(timerLeftRound)
        }
        //make the animations running one by one
        anim.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationRepeat(animation: Animation?) {}
            override fun onAnimationStart(animation: Animation?) {

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
                    //resume timerParty (timer for the whole game)
                    startTimer(timerParty)
                    if(!isTimerLeftRoundRunning) {
                        startTimerLeft(timerLeftRound)
                    }


                }



            }
        })


    }



    //colored btn in layout to know next colors based on difficulty
    private fun btnDataParty(){
        this.listBtnNextColor.forEach {
            it.visibility = View.GONE
        }
        for(i in 1..nbBtnOpt) {
            this.listBtnNextColor[i-1].visibility = View.VISIBLE
        }
    }

    //function to add new random colors for next round
    private fun nextLevel() {
        for(j in 1..this.nbBtnOpt){
            var i=0
            when (this.difficulty) {
                "Facile" -> {
                    i = 1
                }
                "Normal" -> {
                    i = 3
                }
                "Difficile" -> {
                    i = 5
                }
            }
            val currentTimestamp = System.currentTimeMillis()

            temp = Random(currentTimestamp).nextInt(0..i)//(0..i).random()
            listColorParty.add(tab[temp])

            //animate colored btn in header to notify user these are the new colors
            val anim: Animation = AlphaAnimation(1.0f, 0.2f)
            anim.duration = 100
            anim.startOffset = 200
            anim.interpolator = LinearInterpolator()
            anim.repeatMode = Animation.REVERSE
            this.listBtnNextColor[j-1].startAnimation(anim)

            //change btn colors in header
            this.listBtnNextColor[j-1].setBackgroundColor(Color.parseColor(tabColorHex[temp]))
        }

        //if last round was won and animation are enabled in settings, anim all btn to remember
        if(this.gagne && this.animBtn) {
            tab.forEach {
                it.isClickable = false
            }
            animButton(listColorParty, 0)
        }

        //append time of the past round to an array and restart the timer or the round
        if(tv_val_score.text.toString() != "0") {
            listTimeRound.add(timeRound)
            checkTimeRound(timeRound/1000)
        }
        Log.d("arrayTimeRound", listTimeRound.toString())
        timeRound = 0
        startTimer(timerRound)


        //recreate a new timer for the new round
        if(isTimerLeftRoundRunning){
            timerLeftRound.cancel()
            isTimerLeftRoundRunning = false
        }
        timerLeft = tv_val_score.text.toString()
        restartTimerLeft(timerLeft)
        timerLeftRound = initTimerLeftRound(timerLeft)
        startTimerLeft(timerLeftRound)
        isTimerLeftRoundRunning = true

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
            this.nextLevel()

        }else{
            Toast.makeText(this, "Perdu", Toast.LENGTH_SHORT).show()
            this.popupNamePlayer()
        }

    }



    //create popup to display user score and let user choose a name
    private fun popupNamePlayer(){
        val builder = AlertDialog.Builder(this)

        builder.setTitle("Partie terminé")
        val timerTotalValue = timerMinutes.text.toString() + ":" + timerSeconds.text.toString()
        builder.setMessage("Votre score est de " + tv_val_score.text + "\nDurée de la partie: " + timerTotalValue + "\n\n" + resumeRapidity())
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
        resumeRapidity()
        val nomJoueur: String = if(playerDataName.text.isEmpty()){
            "???"
        }else{
            playerDataName.text.toString()
        }

        val scoreP = (playerDataScore.text.toString()).toLong()

        Log.d("test123", nomJoueur)

        AppDatabase.get(application).playerDao().insertPlayer(Player(nomJoueur, scoreP, this.difficulty, playerDataTime))
        Log.d("test123", "$nomJoueur a bien ete ajoute avec un score de $scoreP")
    }

    //check the time for each round and append to an array if the user was fast, normal, or slow with the btn selection
    private fun checkTimeRound(timeForRound: Int){
        val score = (tv_val_score.text.toString()).toLong()
        Log.d("round123", score.toString())
        Log.d("round123", timeForRound.toString())
        var rapidity = ""
        if(timeForRound < (score-1)){
            rapidity = "rapide"
            Toast.makeText(this@MainActivity, rapidity, Toast.LENGTH_SHORT).show()
            //Log.d("round123", rapidity)
        }
        if(timeForRound in (score-1)..(score+1)){
            rapidity = "normal"
            Toast.makeText(this@MainActivity, rapidity, Toast.LENGTH_SHORT).show()
            //Log.d("round123", rapidity)
        }
        if(timeForRound > (score+1)){
            rapidity = "lent"
            Toast.makeText(this@MainActivity, rapidity, Toast.LENGTH_SHORT).show()
            //Log.d("round123", rapidity)
        }
        listRapidityRound.add(rapidity)

        //Log.e("round123", listRapidityRound.toString())
    }

    //return a short message to tell the user if he has good reactivity or not
    private fun resumeRapidity(): String {
        var messageRapidity = ""
        //retrieve frequency for each elements in array listRapidityRound
        val frequencies = listRapidityRound.groupingBy { it }.eachCount()

        val valFrequencyLent = frequencies["lent"] ?: 0
        val valFrequencyNormal = frequencies["normal"] ?: 0
        val valFrequencyRapide = frequencies["rapide"] ?: 0
        //Log.e("round123", valFrequencyLent.toString())
        if(valFrequencyLent > valFrequencyNormal && valFrequencyLent > valFrequencyRapide){
            messageRapidity = "Pendant cette partie, vous avez en moyenne joué lentement. Vous devriez travailler votre mémoire plus souvent!"
        }
        if(valFrequencyNormal > valFrequencyLent && valFrequencyNormal > valFrequencyRapide){
            messageRapidity = "Vous avez jouez de manière normale durant cette partie. Votre mémorisation des couleurs est bonne!"
        }
        if(valFrequencyRapide > valFrequencyLent && valFrequencyRapide > valFrequencyNormal){
            messageRapidity = "Vous avez été rapide pendant votre partie. Vous avez une mémoire visuelle assez poussée!"
        }
        return messageRapidity
    }
}