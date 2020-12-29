package com.example.mbanking.ui.dialogs

import android.content.Context
import android.os.Bundle
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.Unbinder
import com.example.mbanking.R
import com.example.mbanking.ui.register.PinDialogInteractor
import com.example.mbanking.utils.AppConstants.Companion.IV_PREFERENCE
import com.example.mbanking.utils.AppConstants.Companion.MAX_PIN_LENGTH
import com.example.mbanking.utils.AppConstants.Companion.MIN_PIN_LENGTH
import com.example.mbanking.utils.AppConstants.Companion.PIN_PREFERENCE
import com.example.mbanking.utils.getPreferenceString
import com.example.mbanking.utils.isConnectedToInternet
import com.example.mbanking.utils.setPreference
import com.jakewharton.rxbinding2.widget.RxTextView
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.dialog_pin.*
import java.security.KeyStore
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.IvParameterSpec
import io.reactivex.android.schedulers.AndroidSchedulers

class PinDialog(
    private val dialogDismiss: PinDialogInteractor,
    private val isNewUser: Boolean
) : AppCompatDialogFragment(), View.OnClickListener {
    @BindView(R.id.btnFirst) lateinit var firstButton: Button
    @BindView(R.id.btnSecond) lateinit var secondButton: Button
    @BindView(R.id.btnThird) lateinit var thirdButton: Button
    @BindView(R.id.btnFourth) lateinit var fourthButton: Button
    @BindView(R.id.btnFifth) lateinit var fifthButton: Button
    @BindView(R.id.btnSixth) lateinit var sixthButton: Button
    @BindView(R.id.btnSeventh) lateinit var seventhButton: Button
    @BindView(R.id.btnEight) lateinit var eightButton: Button
    @BindView(R.id.btnNinth) lateinit var ninthButton: Button
    @BindView(R.id.btnTenth) lateinit var tenthButton: Button

    @BindView(R.id.btnDelete) lateinit var deleteButton: ImageButton
    @BindView(R.id.btnAccept) lateinit var acceptButton: Button

    @BindView(R.id.txtPin) lateinit var pinText: EditText
    @BindView(R.id.txtError) lateinit var errorText: TextView

    @BindView(R.id.layoutProgressBar) lateinit var progressBarLayout: FrameLayout

    private lateinit var appContext: Context

    private var unbinder: Unbinder? = null
    private var disposable: CompositeDisposable? = CompositeDisposable()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.dialog_pin, container, false)

        unbinder = ButterKnife.bind(this, view)

        setObservers()
        setOnClickListeners()

        return view
    }

    private fun setObservers() {
        disposable!!.add(RxTextView.afterTextChangeEvents(pinText)
            .subscribeOn(AndroidSchedulers.mainThread())
            .subscribe {
                val text = it.view().text.toString()

                //Enable accept button if pin is between MIN_PIN_LENGTH and MAX_PIN_LENGTH (4-6)
                acceptButton.isEnabled = text.length in MIN_PIN_LENGTH..MAX_PIN_LENGTH
                if (text.length in MIN_PIN_LENGTH..MAX_PIN_LENGTH)
                    acceptButton.setTextColor(
                        ContextCompat.getColor(
                            appContext,
                            R.color.colorGreen
                        )
                    )
                else
                    acceptButton.setTextColor(
                        ContextCompat.getColor(
                            appContext,
                            R.color.colorDisabled
                        )
                    )
            })
    }

    private fun setOnClickListeners() {
        firstButton.setOnClickListener(this)
        secondButton.setOnClickListener(this)
        thirdButton.setOnClickListener(this)
        fourthButton.setOnClickListener(this)
        fifthButton.setOnClickListener(this)
        sixthButton.setOnClickListener(this)
        seventhButton.setOnClickListener(this)
        eightButton.setOnClickListener(this)
        ninthButton.setOnClickListener(this)
        tenthButton.setOnClickListener(this)
        deleteButton.setOnClickListener(this)
        acceptButton.setOnClickListener(this)
    }

    override fun onPause() {
        super.onPause()
        if (disposable != null && !disposable!!.isDisposed) {
            disposable!!.dispose()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        if (unbinder != null) {
            unbinder!!.unbind()
            unbinder = null
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.DialogTheme)
    }

    override fun onAttach(context: Context) {
        appContext = context
        super.onAttach(context)
    }

    override fun onClick(v: View?) {
        when (v) {
            btnAccept -> handleAcceptClicked()
            btnDelete -> pinText.setText(pinText.text.dropLast(1))
            else -> handleNumberClicked(v as Button)
        }
    }

    private fun handleAcceptClicked() {
        //If there is no internet connection, inform user and do nothing
        if (!isConnectedToInternet(context)) {
            Toast.makeText(context, getString(R.string.no_internet), Toast.LENGTH_SHORT).show()
            return
        }

        progressBarLayout.visibility = View.VISIBLE
        //If user is trying to register, encrypt user's pin, and save it in SharedPreference,
        //along with ivBytes(Base64) that are used to decrypt pin
        if (isNewUser) {
            generateKey()

            val encryptedPin = encryptData(pinText.text.toString())
            setPreference(
                appContext, PIN_PREFERENCE,
                android.util.Base64.encodeToString(
                    encryptedPin.second,
                    android.util.Base64.NO_PADDING
                )
            )
            setPreference(
                appContext, IV_PREFERENCE,
                android.util.Base64.encodeToString(
                    encryptedPin.first,
                    android.util.Base64.NO_PADDING
                )
            )

            dialogDismiss.onAuthentication()
            dismiss()
        } else {
            //If user is trying to login, get pin and decrypt it. Then check if saved pin matches
            //pin that user gave as login auth
            val encryptedPinBase64 = getPreferenceString(appContext, PIN_PREFERENCE)
            val ivBytesBase64 = getPreferenceString(appContext, IV_PREFERENCE)

            //Decode saved pin and ivBytes with Base64
            val encryptedPin =
                android.util.Base64.decode(encryptedPinBase64, android.util.Base64.NO_PADDING)
            val ivBytes = android.util.Base64.decode(ivBytesBase64, android.util.Base64.NO_PADDING)

            //If user gave correct pin, handle login, if not show error text and reset pin TextView
            if (decryptData(ivBytes, encryptedPin) == pinText.text.toString()) {
                dialogDismiss.onAuthentication()
                dismiss()
            } else {
                errorText.visibility = View.VISIBLE
                pinText.setText("")
            }
            progressBarLayout.visibility = View.INVISIBLE
        }
    }

    private fun generateKey() {
        val keyGenerator =
            KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore")
        val keyGenParamSpec = KeyGenParameterSpec.Builder(
            "MyKeyAlias",
            KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
        )
            .setBlockModes(KeyProperties.BLOCK_MODE_CBC)
            .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
            .build()

        keyGenerator.init(keyGenParamSpec)
        keyGenerator.generateKey()
    }

    private fun handleNumberClicked(button: Button?) {
        if (button == null)
            return

        pinText.append(button.text)
    }

    private fun getKey(): SecretKey {
        val keyStore = KeyStore.getInstance("AndroidKeyStore")
        keyStore.load(null)

        val secretKeyEntry = keyStore.getEntry("MyKeyAlias", null) as KeyStore.SecretKeyEntry
        return secretKeyEntry.secretKey
    }

    private fun encryptData(data: String): Pair<ByteArray, ByteArray> {
        val cipher = Cipher.getInstance("AES/CBC/NoPadding")

        var temp = data
        while (temp.toByteArray().size % 16 != 0)
            temp += "\u0020"

        cipher.init(Cipher.ENCRYPT_MODE, getKey())

        val ivBytes = cipher.iv
        val encryptedBytes = cipher.doFinal(temp.toByteArray(Charsets.UTF_8))

        return Pair(ivBytes, encryptedBytes)
    }

    private fun decryptData(ivBytes: ByteArray, data: ByteArray): String {
        val cipher = Cipher.getInstance("AES/CBC/NoPadding")
        val spec = IvParameterSpec(ivBytes)

        cipher.init(Cipher.DECRYPT_MODE, getKey(), spec)
        return cipher.doFinal(data).toString(Charsets.UTF_8).trim()
    }
}