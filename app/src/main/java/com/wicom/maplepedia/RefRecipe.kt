package com.wicom.maplepedia

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.opencsv.CSVReader
import java.io.InputStreamReader

class RefRecipe : AppCompatActivity() {

    private var itemStrs: Array<String?> = arrayOfNulls<String>(4)

    private var recipeLout0 : LinearLayout? = null
    private var recipeLout1 : LinearLayout? = null
    private var recipeLout2 : LinearLayout? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.guide_recipe)

        recipeLout0 = findViewById(R.id.ref_recipeTypePos0)
        recipeLout1 = findViewById(R.id.ref_recipeTypePos1)
        recipeLout2 = findViewById(R.id.ref_recipeTypePos2)
        recipeLoad()


        val spinnerRecipe: Spinner = findViewById(R.id.ref_recipeTypeSpnr)
        // 문자열 배열 리소스에서 ArrayAdapter를 생성합니다.
        ArrayAdapter.createFromResource(
            this,
            R.array.ref_recipeType,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            // 드롭다운 메뉴의 레이아웃을 지정합니다.
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // 어댑터를 Spinner에 적용합니다.
            spinnerRecipe.adapter = adapter

        }

        spinnerRecipe.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                // 여기에서 선택된 항목에 따라 필요한 동작을 수행합니다.

                when(position) {
                    0 -> {
                        recipeLout0?.visibility = VISIBLE
                        recipeLout1?.visibility = GONE
                        recipeLout2?.visibility = GONE
                    }
                    1 -> {
                        recipeLout0?.visibility = GONE
                        recipeLout1?.visibility = VISIBLE
                        recipeLout2?.visibility = GONE
                    }
                    2 -> {
                        recipeLout0?.visibility = GONE
                        recipeLout1?.visibility = GONE
                        recipeLout2?.visibility = VISIBLE
                    }
                }


            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // 아무것도 선택되지 않았을 때의 동작을 처리합니다.
            }
        }

    }



    private fun recipeLoad() { //스킬 읽어오기
        val assetManager = this.assets
        val inputStream = assetManager.open("ref_recipe.csv") //5차 공용 scv
        val reader = CSVReader(InputStreamReader(inputStream))
        try {
            var nextRecord: Array<String>?
            // we are going to read data line by line
            while (reader.readNext().also { nextRecord = it } != null) {
                for(j in 0..3) {
                    itemStrs[j] = nextRecord?.get(j)//아이템 정보 1개 가져오기
                    //Log.i("skill", skillStrs[j].toString())
                }
                recipeAdd() //스킬 하나 레이아웃 생성하고, 거기에 가져온 스킬 정보 붙이기.
            } //해당 과정을 테이블 행의 수만큼 반복
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun recipeAdd() {

        var recipeLayout: LinearLayout? = null
        when(itemStrs[1]?.toInt()) {
            0 -> recipeLayout = recipeLout0
            1 -> recipeLayout = recipeLout1
            2 -> recipeLayout = recipeLout2
        }

        val inflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val rowView: View = inflater.inflate(R.layout.layout_guide_recipe, null) //스킬 레이아웃 생성
        recipeLayout!!.addView(rowView, recipeLayout!!.childCount)

        val recipeIcon: ImageView = rowView.findViewById(R.id.guide_info_RecipeIcon)
        val mDrawableName = "item_" + itemStrs[0]
        val resID = resources.getIdentifier(mDrawableName, "drawable", packageName)
        recipeIcon.setImageResource(resID)

        val textRecipeName: TextView = rowView.findViewById(R.id.guide_info_recipeName)
        val textRecipeDesc: TextView = rowView.findViewById(R.id.guide_info_recipeDesc)

        textRecipeName.text = itemStrs[2]
        textRecipeDesc.text = itemStrs[3]?.replace("\\n", "\n")
    }


}