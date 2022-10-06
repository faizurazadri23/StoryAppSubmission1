package com.faizurazadri.storyappsubmission1.custom_view

import android.app.Activity
import android.content.Context
import android.graphics.Canvas
import android.text.InputType
import android.text.method.PasswordTransformationMethod
import android.util.AttributeSet
import android.view.View
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import com.faizurazadri.storyappsubmission1.R

class MyEditTextPassword : AppCompatEditText {

    private val messageError = MutableLiveData<String>()
    private val hideError = MutableLiveData<Boolean>()

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init()
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        hint = context.getString(R.string.input_password_description);

        textAlignment = View.TEXT_ALIGNMENT_VIEW_START

        textSize = 14f
    }

    private fun init() {
        inputType = InputType.TYPE_TEXT_VARIATION_PASSWORD
        transformationMethod = PasswordTransformationMethod()

        doAfterTextChanged { text ->

            if (text?.isEmpty() == true) {
                setErrorMessage(context.getString(R.string.input_password_description))
            } else {
                if (text?.isEmpty() == true) {
                    setErrorMessage(context.getString(R.string.must_not_empty))
                } else {
                    if ((text?.length ?: 0) < 6) {
                        setErrorMessage(context.getString(R.string.more_than_six_character))
                    } else {
                        hideErrorMessage()
                    }
                }
            }
        }
    }

    fun onValidateInput(
        activity: Activity,
        hideErrorMessage: () -> Unit,
        setErrorMessage: (message: String) -> Unit
    ) {
        hideError.observe(activity as LifecycleOwner) { hideErrorMessage }
        messageError.observe(activity as LifecycleOwner) { setErrorMessage }
    }

    private fun hideErrorMessage() {
        hideError.value = true
    }

    private fun setErrorMessage(message: String) {
        messageError.value = message
    }
}