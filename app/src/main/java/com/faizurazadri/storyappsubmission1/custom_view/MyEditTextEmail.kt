package com.faizurazadri.storyappsubmission1.custom_view

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.text.InputType
import android.util.AttributeSet
import android.util.Patterns
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.MutableLiveData
import com.faizurazadri.storyappsubmission1.R
import com.google.android.material.textfield.TextInputEditText

class MyEditTextEmail : AppCompatEditText, View.OnTouchListener {

    private lateinit var clearButtonImage: Drawable;
    private val messageError = MutableLiveData<String>()
    private val hideError = MutableLiveData<Boolean>()

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init()
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        hint = "Masukkan alamat email"

        textAlignment = View.TEXT_ALIGNMENT_VIEW_START

        textSize = 14f
    }

    private fun init() {
        clearButtonImage = ContextCompat.getDrawable(context, R.drawable.ic_close) as Drawable

        doAfterTextChanged { text ->

            if (text?.isEmpty() == true) {
                setErrorMessage(context.getString(R.string.must_not_empty))
            } else {
                if (!Patterns.EMAIL_ADDRESS.matcher(text).matches()) {
                    setErrorMessage(context.getString(R.string.type_email_not_valid))
                } else {
                    hideMessageError()
                }
            }
        }
    }

    private fun hideMessageError() {
        hideError.value = true
    }

    private fun setErrorMessage(message: String) {
        messageError.value = message
    }

    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        if (compoundDrawables[2] != null) {
            val clearButtonStart: Float
            val clearButtonEnd: Float
            var isClearButtonClicked = false

            if (layoutDirection == View.LAYOUT_DIRECTION_RTL) {
                clearButtonEnd = (clearButtonImage.intrinsicWidth + paddingStart).toFloat()

                if (event != null) {
                    when {
                        event.x < clearButtonEnd -> isClearButtonClicked = true
                    }
                }
            } else {
                clearButtonStart = (width - paddingEnd - clearButtonImage.intrinsicWidth).toFloat()

                if (event != null) {
                    when {
                        event.x > clearButtonStart -> isClearButtonClicked = true
                    }
                }
            }

            if (isClearButtonClicked) {
                if (event != null) {
                    when (event.action) {
                        MotionEvent.ACTION_DOWN -> {
                            clearButtonImage =
                                ContextCompat.getDrawable(context, R.drawable.ic_close) as Drawable
                            showClearButton()
                            return true
                        }

                        MotionEvent.ACTION_UP -> {
                            clearButtonImage =
                                ContextCompat.getDrawable(context, R.drawable.ic_close) as Drawable
                            when {
                                text != null -> text?.clear()
                            }

                            hideClearButton()
                            return true
                        }
                    }
                }
            } else return false
        }
        return false
    }

    private fun showClearButton() {
        setButtonDrawables(endOfTheText = clearButtonImage)
    }

    private fun hideClearButton() {
        setButtonDrawables()
    }

    private fun setButtonDrawables(
        startOfTheText: Drawable? = null,
        topOfTheText: Drawable? = null,
        endOfTheText: Drawable? = null,
        bottomOfTheText: Drawable? = null
    ) {
        setCompoundDrawablesWithIntrinsicBounds(
            startOfTheText,
            topOfTheText,
            endOfTheText,
            bottomOfTheText,
        )
    }

}

