package com.dilekozgul.calculator

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_main.*
import net.objecthunter.exp4j.ExpressionBuilder

class MainActivity : AppCompatActivity() {

    // Girdi ve çıktıyı görüntülemek için kullanılan TextView
    lateinit var txtInput: TextView

    // Son basılan tuşun sayı olup olmadığını kontrol ederiyorum
    var lastNumeric: Boolean = false

    // Mevcut durumun hatalı olup olmadığını kontrol ederiyorum
    var stateError: Boolean = false

    // Doğruysa, başka bir nokta eklemeye izin vermez
    var lastDot: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        window.statusBarColor = ContextCompat.getColor(this, R.color.statusBarColor)
        //supportActionBar!!.setBackgroundDrawable(ColorDrawable(Color.parseColor("#FF565152")))
        window.navigationBarColor = ContextCompat.getColor(this, R.color.navigationBarColor)

        txtInput = findViewById(R.id.tvInput)

    }
    fun onDigit(view: View) {//sayıların kullandığı metot
        if (stateError) {
            txtInput.text = (view as Button).text
            stateError = false
        } else {
            txtInput.append((view as Button).text)
        }
        lastNumeric = true//son bastığım sayı

    }

    fun onDecimalPoint(view: View) {//noktanın kullandığı metot
        if (lastNumeric && !stateError && !lastDot) {//sadece bir sayıya bastıktan sonra noktaya basılabilir. hesap makinası açılınca veya işlem butonlarından birine bastıktan sonra noktayı basmayı engelledik.
            txtInput.append(".")
            lastNumeric = false//son basılan sayı değil
            lastDot = true//son basılan nokta
        }

    }

    fun onOperator(view: View) {//işlemlerin kullandığı metot
        if (lastNumeric && !stateError) {//sadece son basılan sayı ise ve hata yoksa bu metot çalışır. noktadan sonra işlem yapılmasını engelledik.
            txtInput.append((view as Button).text)
            lastNumeric = false //son basılan sayı değil
            lastDot = false    // son basılan nokta değil
        }
    }

    fun onClear(view: View) {//her şeyi ilk haline çeviriyorum
        this.txtInput.text = ""
        lastNumeric = false
        stateError = false
        lastDot = false
    }

    fun onEqual(view: View) {
        // Son girilen sadece bir sayı ise işlem hesaplanabilir.
        if (lastNumeric && !stateError) {
            val txt = txtInput.text.toString()
            // ExpressionBuilder (exp4j kütüphanenin bir sınıf)
            // Exp4j kütüphanesi, bir ifadeyi String formatında işleyebilir ve sonucu döndürebilir.
            // Bu nedenle txt String değerini ExpressionBuilder'a atıyoruz
            val expression = ExpressionBuilder(txt).build()
            try {
                // Sonucu hesaplayor
                val result = expression.evaluate()
                txtInput.text = result.toString()
                lastDot = true // Sonuç bir nokta içerdiği için true yaptık
            } catch (ex: ArithmeticException) {
                // Hata varsa gösterecek
                txtInput.text = "Error"
                stateError = true
                lastNumeric = false
            }
        }
    }

}