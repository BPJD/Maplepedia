package com.wicom.maplepedia

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.opencsv.CSVReader
import java.io.InputStreamReader

class SimulatorGenesisEnchant : AppCompatActivity() {

    private var selectedWpn : Int = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.simulate_weapon_genesis)

        val spinnerWpn: Spinner = findViewById(R.id.sim_select)
        // 문자열 배열 리소스에서 ArrayAdapter를 생성합니다.
        ArrayAdapter.createFromResource(
            this,
            R.array.sim_class,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            // 드롭다운 메뉴의 레이아웃을 지정합니다.
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // 어댑터를 Spinner에 적용합니다.
            spinnerWpn.adapter = adapter

        }




        spinnerWpn.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                // 여기에서 선택된 항목에 따라 필요한 동작을 수행합니다.

                selectedWpn = position
                Log.i("genesis_sim", position.toString())

            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // 아무것도 선택되지 않았을 때의 동작을 처리합니다.
            }
        }

    }


    fun classButtonClicked(v: View) {

        val mesoCur: Int = findViewById<EditText>(R.id.sim_meso).text.toString().toInt()
        val fireEternalCur: Int = findViewById<EditText>(R.id.sim_fire).text.toString().toInt()
        val fireBlackCur: Int = findViewById<EditText>(R.id.sim_black_fire).text.toString().toInt()

        val buttonClassNumber: Int = v.tag.toString().toInt()
        val mToast = Toast.makeText(applicationContext,buttonClassNumber.toString(), Toast.LENGTH_SHORT)

        val nextIntent = Intent(this, ClassInfoActivity::class.java)

        nextIntent.putExtra("fireE", fireEternalCur)
        nextIntent.putExtra("mesoCur", mesoCur)
        nextIntent.putExtra("fireB", fireBlackCur)

        startActivity(nextIntent);

        //mToast.show()
    }




}