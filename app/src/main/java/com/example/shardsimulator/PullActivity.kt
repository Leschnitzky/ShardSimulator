package com.example.shardsimulator

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.beust.klaxon.*
import com.example.shardsimulator.adapters.CustomAdapter
import com.example.shardsimulator.models.DrawCard
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.io.StringWriter
import java.util.*


class PullActivity : AppCompatActivity() {
    companion object{
        const val TAG ="PullActivity"
    }
    private lateinit var legendaryPool: List<String>
    private lateinit var legendaryPoolNormal: List<String>
    private lateinit var legendaryPoolVoid: List<String>
    private lateinit var epicPool:  List<String>
    private lateinit var epicPoolNormal: List<String>
    private lateinit var epicPoolVoid: List<String>
    private lateinit var rarePool:  List<String>
    private lateinit var rarePoolNormal: List<String>
    private lateinit var rarePoolVoid: List<String>
    private lateinit var baseChampionData: JsonObject
    private val mAncientRareChance = 91.5;
    private val mAncientEpicChance = 99.5;
    private val mVoidRareChance = 93.6;
    private val mVoidEpicChance = 99.6;
    private val mSacredEpicChance = 94;


    // Unusuable until I add common shards
    private lateinit var uncommonPool:  List<String>
    private lateinit var commonPool:  List<String>

    private val draws: MutableList<DrawCard> = mutableListOf<DrawCard>()


    @SuppressLint("WrongConstant")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pull)

        initData()

        val shardType = intent.getStringExtra(MainActivity.INTENT_TYPE_NAME)
        val numOfPulls = Integer.valueOf(intent.getStringExtra(MainActivity.INTENT_VALUE_NAME))
        var champion = ""
        var championImageName = ""
        var drawCard: DrawCard
        Log.d(TAG,"$numOfPulls")
        for(i in 1..numOfPulls){
            champion = drawShard(Integer.valueOf(shardType))
            championImageName = champion.replace(" ","_").replace("-","_").toLowerCase()
            drawCard = DrawCard(champion,
                baseChampionData.obj(champion)!!["rarity"] as String,
                "android.resource://$packageName/drawable/$championImageName"
            )
            draws.add(drawCard);

        }

        val recyclerView = findViewById<RecyclerView>(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this,LinearLayout.VERTICAL,false)
        recyclerView.adapter = CustomAdapter(draws,this)
        findViewById<FloatingActionButton>(R.id.fab).setOnClickListener {
            finish()
        }


    }

    private fun initData() {
        initChampionPools()
        initBaseChampionData()
    }

    private fun initBaseChampionData() {
        baseChampionData = Klaxon().parseJsonObject(resources.openRawResource(R.raw.champions_base_info).reader())
    }

    private fun initChampionPools() {
            val totalChampPool = Klaxon().parseJsonObject(resources.openRawResource(R.raw.champions_by_rarity).reader())
            val voidChampPool = Klaxon().parseJsonObject(resources.openRawResource(R.raw.champions_by_affinity).reader()).array<String>("void")!!.toList()
            Log.d(TAG,voidChampPool.toString())
            legendaryPool = totalChampPool.array<String>("legendary")!!.toList()
            legendaryPoolNormal = legendaryPool.filter { !voidChampPool.contains(it) }
            legendaryPoolVoid = legendaryPool.filter { voidChampPool.contains(it) }
            epicPool =  totalChampPool.array<String>("epic")!!.toList()
            epicPoolNormal = epicPool.filter { !voidChampPool.contains(it) }
            epicPoolVoid = epicPool.filter { voidChampPool.contains(it) }
            rarePool =  totalChampPool.array<String>("rare")!!.toList()
            rarePoolNormal = rarePool.filter { !voidChampPool.contains(it) }
            rarePoolVoid = rarePool.filter { voidChampPool.contains(it) }
    }


    private fun drawShard(type : Int): String {
        Log.d(TAG,"TYPE: $type")
        when(type){
            1 -> return drawAncientChamp()
            2 -> return drawVoidChamp()
            3 -> return drawSacredChamp()
        }
        return ""
    }

    fun drawAncientChamp(): String {
        val rand = Random()
        val result = 100*rand.nextDouble()
        Log.d(TAG,"result : $result")
        if(result <= mAncientRareChance){
            return rarePoolNormal.get(rand.nextInt(rarePoolNormal.size))
        } else if (result <= mAncientEpicChance) {
            return epicPoolNormal.get(rand.nextInt(epicPoolNormal.size))
        } else {
            return legendaryPoolNormal.get(rand.nextInt(legendaryPoolNormal.size))
        }
    }

    fun drawVoidChamp(): String {
        val rand = Random()
        val result = 100*rand.nextDouble()
        if(result <= mVoidRareChance){
            return rarePoolVoid.get(rand.nextInt(rarePoolVoid.size))
        } else if (result <= mVoidEpicChance) {
            return epicPoolVoid.get(rand.nextInt(epicPoolVoid.size))
        } else {
            return legendaryPoolVoid.get(rand.nextInt(legendaryPoolVoid.size))
        }
    }


    fun drawSacredChamp(): String {
        val rand = Random()
        val result = 100*rand.nextDouble()
        if (result <= mSacredEpicChance) {
            return epicPoolNormal.get(rand.nextInt(epicPoolNormal.size))
        } else {
            return legendaryPoolNormal.get(rand.nextInt(legendaryPoolNormal.size))
        }
    }

}
