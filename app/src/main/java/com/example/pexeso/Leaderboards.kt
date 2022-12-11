package com.example.pexeso

import android.app.AlertDialog
import android.content.Context
import android.os.*
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button
import android.widget.TextView

lateinit var btnBackL: Button
lateinit var btnDelete: Button

lateinit var cislo1: TextView
lateinit var cislo2: TextView
lateinit var cislo3: TextView
lateinit var tv1n: TextView
lateinit var tv2n: TextView
lateinit var tv3n: TextView
lateinit var tv1s: TextView
lateinit var tv2s: TextView
lateinit var tv3s: TextView

var tempName = ""
var tempScore = 0.0f
var num1 = 0.0f
var num1n = ""
var num2 = 0.0f
var num2n = ""
var num3 = 0.0f
var num3n = ""

var saving = false

class Leaderboards : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_leaderboards)

        btnDelete = findViewById(R.id.btnDelete)
        btnBackL = findViewById(R.id.btnBackL)
        cislo1 = findViewById(R.id.cislo1)
        cislo2 = findViewById(R.id.cislo2)
        cislo3 = findViewById(R.id.cislo3)
        tv1n = findViewById(R.id.tv1n)
        tv2n = findViewById(R.id.tv2n)
        tv3n = findViewById(R.id.tv3n)
        tv1s = findViewById(R.id.tv1s)
        tv2s = findViewById(R.id.tv2s)
        tv3s = findViewById(R.id.tv3s)

        val sharedPreferences = getSharedPreferences("data", Context.MODE_PRIVATE)
        num1 = sharedPreferences.getFloat("top1", 0.0f)
        num1n = sharedPreferences.getString("top1n", "").toString()
        num2 = sharedPreferences.getFloat("top2", 0.0f)
        num2n = sharedPreferences.getString("top2n", "").toString()
        num3 = sharedPreferences.getFloat("top3", 0.0f)
        num3n = sharedPreferences.getString("top3n", "").toString()

        kontrolaDelete()
        btnDelete.setOnClickListener {
            if (num1 == 0.0f && num1n == "") {
            } else {
                vibrace()
                val builder = AlertDialog.Builder(this)
                builder.setMessage("Do you want to delete all saved data?")
                builder.setPositiveButton("Yes") { dialog, _ ->
                    dialog.dismiss()
                    tempName = ""
                    tempScore = 0.0f
                    num1 = 0.0f
                    num1n = ""
                    num2 = 0.0f
                    num2n = ""
                    num3 = 0.0f
                    num3n = ""

                    saving = true
                    deleteSharedPreferences("data")
                    zmenText()
                    kontrolaTop()
                    kontrolaDelete()
                }
                builder.setNegativeButton("No") { dialog, _ ->
                    dialog.cancel()
                }
                builder.show()
            }
        }
        btnBackL.setOnClickListener { finish() }

        kontrolaTop()

        if (saving == true) {
            val savedName = sharedPreferences.getString("NAME", "")
            val savedScore = sharedPreferences.getFloat("SCORE", 0.0f)

            if (savedScore > num3) {
                num3 = savedScore
                num3n = savedName.toString()

                val sharedPreferences = getSharedPreferences("data", Context.MODE_PRIVATE)
                val editor = sharedPreferences.edit()
                editor.putString("top3n", num3n)
                editor.putFloat("top3", num3)
                editor.apply()
            }

            if (savedScore > num2) {
                tempName = num2n
                tempScore = num2
                num2 = savedScore
                num2n = savedName.toString()
                num3 = tempScore
                num3n = tempName

                val sharedPreferences = getSharedPreferences("data", Context.MODE_PRIVATE)
                val editor = sharedPreferences.edit()
                editor.putString("top3n", num3n)
                editor.putFloat("top3", num3)
                editor.putString("top2n", num2n)
                editor.putFloat("top2", num2)
                editor.apply()
            }

            if (savedScore > num1) {
                tempName = num1n
                tempScore = num1
                num1 = savedScore
                num1n = savedName.toString()
                num2 = tempScore
                num2n = tempName

                val sharedPreferences = getSharedPreferences("data", Context.MODE_PRIVATE)
                val editor = sharedPreferences.edit()
                editor.putString("top2n", num2n)
                editor.putFloat("top2", num2)
                editor.putString("top1n", num1n)
                editor.putFloat("top1", num1)
                editor.apply()
            }
            saving = false
        }
        kontrolaDelete()
        udelejViditelne()
        kontrolaTop()
        zmenText()
    }

    fun zmenText() {
        tv1n.text = num1n
        tv1s.text = num1.toInt().toString()
        tv2n.text = num2n
        tv2s.text = num2.toInt().toString()
        tv3n.text = num3n
        tv3s.text = num3.toInt().toString()
    }

    fun vibrace() {
        val vib = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val vibratorManager =
                getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
            vibratorManager.defaultVibrator
        } else {
            @Suppress("DEPRECATION")
            getSystemService(VIBRATOR_SERVICE) as Vibrator
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vib.vibrate(VibrationEffect.createOneShot(300, VibrationEffect.DEFAULT_AMPLITUDE))
        } else {
            @Suppress("DEPRECATION")
            vib.vibrate(500)
        }
    }

    fun kontrolaTop() {
        if (num1 == 0.0f && num1n == "") {
            cislo1.visibility = View.INVISIBLE
            tv1n.visibility = View.INVISIBLE
            tv1s.visibility = View.INVISIBLE
        }
        if (num2 == 0.0f && num2n == "") {
            cislo2.visibility = View.INVISIBLE
            tv2n.visibility = View.INVISIBLE
            tv2s.visibility = View.INVISIBLE
        }
        if (num3 == 0.0f && num3n == "") {
            cislo3.visibility = View.INVISIBLE
            tv3n.visibility = View.INVISIBLE
            tv3s.visibility = View.INVISIBLE
        }
    }

    fun udelejViditelne() {
        cislo1.visibility = View.VISIBLE
        tv1n.visibility = View.VISIBLE
        tv1s.visibility = View.VISIBLE
        cislo2.visibility = View.VISIBLE
        tv2n.visibility = View.VISIBLE
        tv2s.visibility = View.VISIBLE
        cislo3.visibility = View.VISIBLE
        tv3n.visibility = View.VISIBLE
        tv3s.visibility = View.VISIBLE
    }

    fun kontrolaDelete() {
        if (num1 == 0.0f && num1n == "") {
            btnDelete.visibility = View.INVISIBLE
        } else {
            btnDelete.visibility = View.VISIBLE
        }
    }
}