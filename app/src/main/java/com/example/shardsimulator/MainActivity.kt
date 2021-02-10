package com.example.shardsimulator

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds


class  MainActivity : AppCompatActivity() {

    companion object{
        const val INTENT_TYPE_NAME = "type"
        const val INTENT_VALUE_NAME = "value"
    }
    lateinit var mAdView : AdView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        initMonatization()
        initUI();
    }

    private fun initMonatization() {
        MobileAds.initialize(
            this
        ) { }
        mAdView = findViewById(R.id.adView)
        val adRequest = AdRequest.Builder().build()
        mAdView.loadAd(adRequest)

    }


    private fun initUI() {
        initButtons();
    }

    private fun initButtons() {
        setButtonWithValues(R.id.ancient_roll_1, "1", 1.toString())
        setButtonWithValues(R.id.ancient_roll_10, "1", 10.toString())
        setButtonWithValues(R.id.void_roll_1, "2", 1.toString())
        setButtonWithValues(R.id.void_roll_10, "2", 10.toString())
        setButtonWithValues(R.id.sacred_roll_1, "3", 1.toString())
        setButtonWithValues(R.id.sacred_roll_10, "3", 10.toString())
    }

    private fun setButtonWithValues(id: Int, type: String, value: String){
        findViewById<Button>(id).setOnClickListener {
            val intent = Intent(this, PullActivity::class.java)
            intent.putExtra(INTENT_TYPE_NAME, type)
            intent.putExtra(INTENT_VALUE_NAME, value)
            startActivity(intent)
        }
    }
}