package com.example.simonsaysm2.Activities


import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.simonsaysm2.Database.AppDatabase
import com.example.simonsaysm2.R
import com.example.simonsaysm2.Activities.ScoreBoard
import kotlinx.android.synthetic.main.activity_start.*


class ActivityStart : AppCompatActivity() {
    private var nbBtnOpt = 1
    private var difficulty: String = "Normal"
    private var isSwitchChecked = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)

        val lastScore = AppDatabase.get(application).playerDao().getLastScore()

        if(lastScore == null){
            last_score.text = "0"
        }else{
            last_score.text = lastScore.score.toString()
        }

        val highScore = AppDatabase.get(application).playerDao().getHighScore()
        if(highScore == null){
            highest_score.text =  "0"
        }else{
            highest_score.text =  highScore.score.toString()
        }


        btn_start.setOnClickListener{
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("difficulty", this.difficulty)
            intent.putExtra("nb_btn_opt", this.nbBtnOpt)
            intent.putExtra("animSwitch", this.isSwitchChecked)
            startActivity(intent)
        }

        btn_high_score.setOnClickListener{
            val intent = Intent(this, ScoreBoard::class.java)
            startActivity(intent)
        }

        btn_options.setOnClickListener{
            val intent = Intent(this, ActivityOptions::class.java)
            intent.putExtra("difficulty", this.difficulty)
            intent.putExtra("nb_btn_opt", this.nbBtnOpt)
            intent.putExtra("animSwitch", this.isSwitchChecked)
            startActivityForResult(intent, 111)
        }



    }

    @Deprecated("Deprecated")
    @Override
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == 111 && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                this.difficulty = data.getStringExtra("difficulty").toString()
                this.nbBtnOpt = data.getIntExtra("nb_btn_opt", 1)
                this.isSwitchChecked = data.getBooleanExtra("animSwitch", this.isSwitchChecked)
            }
        }

        super.onActivityResult(requestCode, resultCode, data)
    }


}