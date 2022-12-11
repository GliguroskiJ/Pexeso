package com.example.pexeso

import android.content.Context
import android.os.*
import androidx.appcompat.app.AppCompatActivity
import com.example.pexeso.R.drawable.*
import android.app.AlertDialog
import android.content.Intent
import android.provider.Settings.Global.putString
import android.view.View
import android.widget.*

lateinit var score: TextView
lateinit var moves: TextView

lateinit var btnRestart: Button
lateinit var btnBack: Button

lateinit var karta1: ImageButton
lateinit var karta2: ImageButton
lateinit var karta3: ImageButton
lateinit var karta4: ImageButton
lateinit var karta5: ImageButton
lateinit var karta6: ImageButton
lateinit var karta7: ImageButton
lateinit var karta8: ImageButton
lateinit var karta9: ImageButton
lateinit var karta10: ImageButton
lateinit var karta11: ImageButton
lateinit var karta12: ImageButton
lateinit var karta13: ImageButton
lateinit var karta14: ImageButton
lateinit var karta15: ImageButton
lateinit var karta16: ImageButton

lateinit var Karty: List<ImageButton>
lateinit var Vlastnosti: List<vlastnostiKarty>

var hry = mutableListOf<Int>()

class startGame : AppCompatActivity() {

    var vybraneId: Int? = null
    var noveSkore = 0.0f
    var skore = 0
    var nSkore = 0.0f
    var kroky = 0
    var nKroky = 0.0f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start_game)

        score = findViewById(R.id.score)
        moves = findViewById(R.id.moves)

        btnRestart = findViewById(R.id.btnRestart)
        btnBack = findViewById(R.id.btnBack)

        karta1 = findViewById(R.id.karta1)
        karta2 = findViewById(R.id.karta2)
        karta3 = findViewById(R.id.karta3)
        karta4 = findViewById(R.id.karta4)
        karta5 = findViewById(R.id.karta5)
        karta6 = findViewById(R.id.karta6)
        karta7 = findViewById(R.id.karta7)
        karta8 = findViewById(R.id.karta8)
        karta9 = findViewById(R.id.karta9)
        karta10 = findViewById(R.id.karta10)
        karta11 = findViewById(R.id.karta11)
        karta12 = findViewById(R.id.karta12)
        karta13 = findViewById(R.id.karta13)
        karta14 = findViewById(R.id.karta14)
        karta15 = findViewById(R.id.karta15)
        karta16 = findViewById(R.id.karta16)

        zacniHru()

        btnRestart.setOnClickListener {
            if (skore == 0 && kroky == 0) {
            } else dialogRestart()
        }
        btnBack.setOnClickListener {
            if (skore == 0 && kroky == 0) {
                finish()
            } else dialogZpet()
        }
    }

    fun update() {
        Vlastnosti.forEachIndexed { id, karta ->
            val card = Karty[id]
            if (karta.match) {
                card.alpha = 0.4f
            }
            card.setImageResource(if (karta.fliped) karta.id else k000)
        }
    }

    fun restartujData() {
        btnRestart.visibility = View.INVISIBLE
        skore = 0
        kroky = 0
        moves.text = "Moves: $kroky"
        score.text = "Score: $skore/8"

        hry = mutableListOf(k001, k002, k003, k004, k005, k006, k007, k008)
        hry.addAll(hry)
        hry.shuffle()

        Vlastnosti = Karty.indices.map { id ->
            vlastnostiKarty(hry[id])
        }

        Karty.forEachIndexed { id, karta ->
            karta.setOnClickListener {
                if (vlastnostiKarty(hry[id]).match == false) {
                    karta.animate().apply {
                        duration = 1000
                        rotationYBy(360f)
                    }.start()
                } else {
                    karta.animate().apply { }
                }
                aktualizaceKaret(id)
                update()
            }
        }

        Vlastnosti.forEachIndexed { id, karta ->
            val card = Karty[id]
            card.alpha = 1f
            card.setImageResource(if (karta.fliped) karta.id else k000)
        }
    }

    fun zacniHru() {
        moves.text = "Moves: $kroky"
        score.text = "Score: $skore/8"

        Karty = listOf(
            karta1,
            karta2,
            karta3,
            karta4,
            karta5,
            karta6,
            karta7,
            karta8,
            karta9,
            karta10,
            karta11,
            karta12,
            karta13,
            karta14,
            karta15,
            karta16
        )
        hry = mutableListOf(k001, k002, k003, k004, k005, k006, k007, k008)
        hry.addAll(hry)
        hry.shuffle()

        Vlastnosti = Karty.indices.map { id ->
            vlastnostiKarty(hry[id])
        }

        Karty.forEachIndexed { id, karta ->
            karta.setOnClickListener {
                if (!vlastnostiKarty(hry[id]).match) {
                    karta.animate().apply {
                        duration = 1000
                        rotationYBy(360f)
                    }.start()
                }/*else if(vlastnostiKarty(hry[id]).match){
                    karta.animate().apply{
                        duration=500
                        rotationYBy(90f) }.withEndAction{
                        karta.animate().apply{
                            duration=500
                            rotationYBy(-180f) }.start() } } */
                aktualizaceKaret(id)
                update()
            }
        }
    }

    fun aktualizaceKaret(pozice: Int) {
        val aktKarta = Vlastnosti[pozice]
        if (aktKarta.fliped) {
            return
        }
        if (vybraneId == null) {
            obnovaKaret()
            vybraneId = pozice
        } else {
            kontrolaVyberu(vybraneId!!, pozice)
            vybraneId = null
            kroky++
            score.text = "Score: $skore/8"
            if (skore == 8) {
                noveSkore()
                dialogUloz()
            }
            btnRestart.visibility = View.VISIBLE
        }
        aktKarta.fliped = !aktKarta.fliped
        moves.text = "Moves: $kroky"
    }

    fun obnovaKaret() {
        for (card in Vlastnosti) {
            if (!card.match) card.fliped = false
        }
    }

    fun kontrolaVyberu(vyber1: Int, vyber2: Int) {
        if (Vlastnosti[vyber1].id == Vlastnosti[vyber2].id) {
            Vlastnosti[vyber1].match = true
            Vlastnosti[vyber2].match = true
            skore++
            vibrace()
            Karty[vyber1].isClickable = false
            Karty[vyber2].isClickable = false
        }
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
            vib.vibrate(200)
        }
    }

    fun dialogRestart() {
        vibrace()
        val builder = AlertDialog.Builder(this)
        builder.setMessage("Do you want to restart the game?")
        builder.setPositiveButton("Yes") { dialog, _ ->
            restartujData()
            dialog.dismiss()
        }
        builder.setNegativeButton("No") { dialog, _ ->
            dialog.cancel()
        }
        builder.show()
    }

    fun dialogZpet() {
        vibrace()
        val builder = AlertDialog.Builder(this)
        builder.setMessage("Do you want to go back to the main menu?")
        builder.setPositiveButton("Yes") { _, _ ->
            finish()
            hry.clear()
        }
        builder.setNegativeButton("No") { dialog, _ ->
            dialog.cancel()
        }
        builder.show()
    }

    fun dialogUloz() {
        saving = true
        val builder = AlertDialog.Builder(this)
        val inflater = layoutInflater
        builder.setMessage("Congratulations! Enter your name to save your result!")
        val dialogLayout = inflater.inflate(R.layout.dialog_uloz_layout, null)
        val editText = dialogLayout.findViewById<EditText>(R.id.editText)
        builder.setView(dialogLayout)
        builder.setPositiveButton("Save") { _, _ ->
            val sharedPreferences = getSharedPreferences("data", Context.MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            //editText.text.toString().trim().length < 3
            editor.putString("NAME", editText.text.toString())
            editor.putFloat("SCORE", noveSkore)
            editor.apply()
            val intent = Intent(this, Leaderboards::class.java)
            startActivity(intent)
            finish()
        }
        builder.setNegativeButton("Cancel") { dialog, _ ->
            dialog.cancel()
            finish()
        }
        builder.show()
    }

    fun noveSkore() {
        nSkore = skore.toFloat()
        nKroky = kroky.toFloat()
        noveSkore = (nSkore / nKroky) * 1000
    }
}