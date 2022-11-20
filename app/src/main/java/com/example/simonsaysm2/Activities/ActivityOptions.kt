package com.example.simonsaysm2.Activities

import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.SeekBar
import android.widget.Spinner
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.simonsaysm2.R
import kotlinx.android.synthetic.main.activity_options.*


//import kotlinx.android.synthetic.main.activity_options.*


class ActivityOptions : AppCompatActivity() {
    private lateinit var difficulty: String
    private var isSwitchChecked = true
    private var nbBtnOpt = 1



    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_options)

        this.difficulty = this.intent.getStringExtra("difficulty").toString()
        getIndex(spinner_difficulty, this.difficulty)   //remettre la difficulté comme selectionné avant et pas a la valeur initial (Facile)

        this.isSwitchChecked = this.intent.getBooleanExtra("animSwitch", isSwitchChecked)
        switch_anim.isChecked = this.isSwitchChecked

        this.nbBtnOpt = this.intent.getIntExtra("nb_btn_opt", this.nbBtnOpt)
        nb_btn_option.text = this.nbBtnOpt.toString()
        sk_nb_btn_option.progress = this.nbBtnOpt


        val intent = Intent(applicationContext, ActivityStart::class.java)

        switch_anim.setOnCheckedChangeListener { buttonView, isChecked ->
            intent.putExtra("animSwitch", isChecked)
        }



        sk_nb_btn_option.setOnSeekBarChangeListener(object :
            SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seek: SeekBar, progress: Int, fromUser: Boolean) {
                nb_btn_option.text = progress.toString()
                intent.putExtra("nb_btn_opt", progress)
            }

            override fun onStartTrackingTouch(seek: SeekBar) {}
            override fun onStopTrackingTouch(seek: SeekBar) {}
        })


        spinner_difficulty.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, pos: Int, id: Long) {
                val item = parent.getItemAtPosition(pos)
                Log.d("test123", item.toString())

                intent.putExtra("difficulty", item.toString())
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        setResult(Activity.RESULT_OK, intent)

    }


    private fun getIndex(spinner: Spinner, myString: String): Int {
        var index = 0
        for (i in 0 until spinner.count) {
            if (spinner.getItemAtPosition(i) == myString) {
                index = i
            }
        }

        spinner.setSelection(index)
        return index
    }




}