package com.example.simonsaysm2.Activities

import android.app.Activity
import android.content.Intent
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
    private var isSwitchCheckedAnimation = true
    private var isSwitchCheckedCompetitiveMode = false
    private var nbBtnOpt = 1



    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_options)

        this.difficulty = this.intent.getStringExtra("difficulty").toString()
        getIndex(spinner_difficulty, this.difficulty)   //restore difficulty previously set

        this.isSwitchCheckedAnimation = this.intent.getBooleanExtra("animSwitch", isSwitchCheckedAnimation)
        switch_anim.isChecked = this.isSwitchCheckedAnimation
        intent.putExtra("animSwitch", switch_anim.isChecked)

        this.isSwitchCheckedCompetitiveMode = this.intent.getBooleanExtra("competitiveMode", isSwitchCheckedCompetitiveMode)
        switch_competitive_mode.isChecked = this.isSwitchCheckedCompetitiveMode
        intent.putExtra("competitiveMode", switch_competitive_mode.isChecked)


        this.nbBtnOpt = this.intent.getIntExtra("nb_btn_opt", this.nbBtnOpt)
        nb_btn_option.text = this.nbBtnOpt.toString()
        sk_nb_btn_option.progress = this.nbBtnOpt

        intent.putExtra("world", "hello")



        val intent = Intent(applicationContext, ActivityStart::class.java)

        switch_anim.setOnCheckedChangeListener { _, isChecked ->
            Log.d("anim", "switched")
            intent.putExtra("animSwitch", isChecked)
        }

        switch_competitive_mode.setOnCheckedChangeListener { _, isChecked ->
            Log.d("compet", "switched$isChecked")
            intent.putExtra("competitiveMode", isChecked)
            Log.d("compet", intent.extras.toString())

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